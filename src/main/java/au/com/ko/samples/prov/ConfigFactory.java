package au.com.ko.samples.prov;

import au.com.ko.samples.config.ConnectionConfig;
import org.glassfish.hk2.api.Factory;

import javax.inject.Singleton;

/**
 * Support for loading the configuration from a resource.
 */
@Singleton
public class ConfigFactory implements Factory<ConnectionConfig> {

    @Override
    public ConnectionConfig provide() {
        return ConfigManager.getConfig();
    }

    @Override
    public void dispose(ConnectionConfig config) {
    }
}