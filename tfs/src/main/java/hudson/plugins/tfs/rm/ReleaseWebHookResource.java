package hudson.plugins.tfs.rm;

import hudson.util.Secret;

/**
 * Implements ReleaseWebHookResource. Model to represent webHook register event.
 * @author Kalyan
 */
public class ReleaseWebHookResource {
    private String operationType;
    private String webHookName;
    private String projectName;
    private String payloadUrl;
    private Secret secret;

    public String getOperationType() {
        return this.operationType;
    }

    public void setOperationType(final String operationType) {
        this.operationType = operationType;
    }

    public String getWebHookName() {
        return this.webHookName;
    }

    public void setWebHookName(final String webHookName) {
        this.webHookName = webHookName;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(final String jobName) {
        this.projectName = jobName;
    }

    public String getPayloadUrl() {
        return this.payloadUrl;
    }

    public void setPayloadUrl(final String payloadUrl) {
        this.payloadUrl = payloadUrl;
    }

    public Secret getSecret() {
        return this.secret;
    }

    public void setSecret(final Secret secret) {
        this.secret = secret;
    }
}
