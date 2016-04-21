package au.com.ko.samples.prov;

import au.com.ko.samples.config.ConnectionConfig;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

/**
 * HK2 binder to configure the server side bindings.
 */
public class ServiceBinder extends AbstractBinder {
	@Override
	protected void configure() {
		bindFactory(ConfigFactory.class).to(ConnectionConfig.class).in(Singleton.class);
	}
}
