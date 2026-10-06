//CHECKSTYLE:OFF
package hudson.plugins.tfs.rm;

import com.google.gson.Gson;
import hudson.ProxyConfiguration;
import hudson.util.Secret;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Ankit Goyal
 */

public class ReleaseManagementHttpClient
{
    private final HttpClient httpClient;
    private final String username;
    private final Secret password;
    private final String accountUrl;
    private final String basicAuth;

    ReleaseManagementHttpClient(String accountUrl, String username, Secret password)
    {
        this.accountUrl = accountUrl;
        this.username = username;
        this.password = password;
        // Uses the proxy configured in Jenkins (Manage Jenkins > System > HTTP Proxy)
        this.httpClient = ProxyConfiguration.newHttpClient();
        final String credentials = this.username + ":" + Secret.toString(this.password);
        this.basicAuth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

    public List<ReleaseDefinition> GetReleaseDefinitions(String project) throws ReleaseManagementException
    {
        String url = this.accountUrl + project + "/_apis/release/definitions?$expand=artifacts";
        String response = this.ExecuteGetMethod(url);
        DefinitionResponse definitionResponse = new Gson().fromJson(response, DefinitionResponse.class);
        return definitionResponse.getValue();
    }

    public String CreateRelease(String project, String body) throws ReleaseManagementException
    {
        String url = this.accountUrl + project + "/_apis/release/releases?api-version=3.0-preview.2";
        return this.ExecutePostmethod(url, body);
    }

    public ReleaseArtifactVersionsResponse GetVersions(String project, List<Artifact> artifacts) throws ReleaseManagementException
    {
        String url = this.accountUrl + project + "/_apis/release/artifacts/versions?api-version=3.0-preview.1";
        final String body = new Gson().toJson(artifacts);
        String response = this.ExecutePostmethod(url, body);
        return new Gson().fromJson(response, ReleaseArtifactVersionsResponse.class);
    }

    public List<Project> GetProjectItems() throws ReleaseManagementException
    {
        String url = this.accountUrl + "/_apis/projects?api-version=1.0";
        String response = this.ExecuteGetMethod(url);
        try {
            String values = new JSONObject(response).getString("value");
            return Arrays.asList(new Gson().fromJson(values, Project[].class));
        } catch (JSONException ex) {
            throw new ReleaseManagementException(ex);
        }
    }

    private String ExecutePostmethod(String url, String body) throws ReleaseManagementException
    {
        final HttpRequest.Builder builder = newRequestBuilder(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8));
        return execute(builder.build());
    }

    private String ExecuteGetMethod(String url) throws ReleaseManagementException
    {
        final HttpRequest.Builder builder = newRequestBuilder(url).GET();
        return execute(builder.build());
    }

    private HttpRequest.Builder newRequestBuilder(String url) throws ReleaseManagementException
    {
        try
        {
            return HttpRequest.newBuilder(URI.create(url))
                    .header("Authorization", this.basicAuth);
        }
        catch (IllegalArgumentException ex)
        {
            // invalid URL, e.g. unescaped characters in the project name
            throw new ReleaseManagementException(ex);
        }
    }

    private String execute(HttpRequest request) throws ReleaseManagementException
    {
        final HttpResponse<String> httpResponse;
        try
        {
            httpResponse = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (IOException ex)
        {
            throw new ReleaseManagementException(ex);
        }
        catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
            throw new ReleaseManagementException(ex);
        }

        final int status = httpResponse.statusCode();
        final String response = httpResponse.body();
        if (status >= 300)
        {
            throw new ReleaseManagementException("Error occurred.%nStatus: " + status + "%nResponse: " + response + "%n");
        }
        return response;
    }

    private class DefinitionResponse
    {

        private Integer count;
        private List<ReleaseDefinition> value = new ArrayList<ReleaseDefinition>();
        private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

        /**
        *
        * @return
        * The count
        */
        public Integer getCount()
        {
            return count;
        }

        /**
        *
        * @param count
        * The count
        */
        public void setCount(Integer count)
        {
            this.count = count;
        }

        /**
        *
        * @return
        * The value
        */
        public List<ReleaseDefinition> getValue()
        {
            return value;
        }

        /**
        *
        * @param value
        * The value
        */
        public void setValue(List<ReleaseDefinition> value)
        {
            this.value = value;
        }

        public Map<String, Object> getAdditionalProperties()
        {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value)
        {
            this.additionalProperties.put(name, value);
        }
    }
}
