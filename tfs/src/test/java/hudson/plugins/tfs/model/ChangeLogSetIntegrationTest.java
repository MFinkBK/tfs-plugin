package hudson.plugins.tfs.model;

import org.htmlunit.html.HtmlPage;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.recipes.LocalData;
import org.junit.Ignore;

public class ChangeLogSetIntegrationTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    /**
     * Asserts that the change log links the user who checked in the changeset.
     * @throws Exception thrown if problem
     */
    @Ignore("Jenkins fails to start with the old @LocalData; re-enable after updating the test data")
    @LocalData
    @Issue("JENKINS-4943")
    @Test
    public void testThatLogSetContainsCheckedInByUserReference() throws Exception {
        try (JenkinsRule.WebClient webClient = j.createWebClient()) {
            HtmlPage page = webClient.getPage(j.jenkins.getItem("4943"), "2/changes");
            // JenkinsRule serves Jenkins under the /jenkins context path
            j.assertXPath(page, "//a[@href=\"/jenkins/user/dude/\"]");
        }
    }
}
