package jenkins.security;

import java.io.Closeable;
import java.io.IOException;

/**
 * Historically replaced the {@link ConfidentialStore} for unit tests via the
 * {@code ConfidentialStore.TEST} thread-local, which no longer exists in Jenkins core.
 *
 * <p>Current Jenkins core automatically uses {@link ConfidentialStore.Mock#INSTANCE}
 * (an in-memory store with a fixed random seed) whenever no Jenkins instance is running,
 * which is the case in plain unit tests. This class therefore only resets that store
 * on {@link #close()} so that tests don't see each other's keys.
 */
public class ConfidentialStoreOverride implements Closeable {

    public ConfidentialStoreOverride() {
        ConfidentialStore.Mock.INSTANCE.clear();
    }

    @Override
    public void close() throws IOException {
        ConfidentialStore.Mock.INSTANCE.clear();
    }
}
