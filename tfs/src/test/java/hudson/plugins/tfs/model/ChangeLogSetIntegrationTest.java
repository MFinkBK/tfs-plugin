package hudson.plugins.tfs.model;

import org.jvnet.hudson.test.Issue;
import org.jvnet.hudson.test.HudsonTestCase;
import org.jvnet.hudson.test.recipes.LocalData;

import org.htmlunit.html.HtmlPage;

public class ChangeLogSetIntegrationTest extends HudsonTestCase {

    /**
     * Asserts that polling now longer throws an exception.
     * @throws Exception thrown if problem
     */
    @LocalData
    @Issue("JENKINS-4943")
    public void testThatLogSetContainsCheckedInByUserReference() throws Exception {
    	HtmlPage page = new WebClient().getPage(hudson.getItem("4943"), "2/changes");
    	assertXPath(page, "//a[@href=\"/user/dude/\"]");
    }
}
