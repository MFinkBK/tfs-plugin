package hudson.plugins.tfs.model;

import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.Secret;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Manual credentials configurer.
 */
@SuppressFBWarnings(value = "SE_TRANSIENT_FIELD_NOT_RESTORED", justification = "Maintain compatibility")
public class ManualCredentialsConfigurer extends CredentialsConfigurer {
    private static final long serialVersionUID = 1L;

    private final transient String userName;
    private final transient Secret password;

    /**
     * Constructor for data binding.
     */
    @DataBoundConstructor
    public ManualCredentialsConfigurer(final String userName, final Secret password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public Secret getPassword() {
        return password;
    }

    @Override
    public StandardUsernamePasswordCredentials getCredentials(final String collectionUri) {
        try {
            return new UsernamePasswordCredentialsImpl(
                    CredentialsScope.GLOBAL,
                    null,
                    null,
                    this.userName,
                    this.password.getPlainText()
            );
        } catch (final Descriptor.FormException e) {
            // thrown e.g. in FIPS mode when the password does not meet the requirements
            throw new IllegalArgumentException("Could not create credentials: " + e.getMessage(), e);
        }
    }

    /**
     * Class descriptor.
     */
    @Extension
    public static final class DescriptorImpl extends CredentialsConfigurerDescriptor {

        @Override
        public String getDisplayName() {
            return "Manual";
        }
    }
}
