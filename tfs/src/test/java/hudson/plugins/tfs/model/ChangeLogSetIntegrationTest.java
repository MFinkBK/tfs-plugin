package hudson.plugins.tfs.model;

import org.htmlunit.html.HtmlPage;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.recipes.LocalData;

public class ChangeLogSetIntegrationTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    /**
     * Asserts that the change log links the user who checked in the changeset.
     * @throws Exception thrown if problem
     */
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
