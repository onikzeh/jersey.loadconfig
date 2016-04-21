package au.com.ko.samples.jersey;

import au.com.ko.samples.prov.ServiceBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class ApplicationConfig extends ResourceConfig {

	public ApplicationConfig() {
		super(JacksonFeature.class, SampleService.class);

		// Binding interfaces or Classes to factory that return an instance of that interface or class
		register(new ServiceBinder());
	}

}
