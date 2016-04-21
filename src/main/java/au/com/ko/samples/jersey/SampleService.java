package au.com.ko.samples.jersey;

import au.com.ko.samples.config.ConnectionConfig;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class SampleService {

    @Inject
    private ConnectionConfig config;

    @GET
    @Path("doSomething")
    public String callAnotherServer() {
        String data = config.getHost() + ":" + config.getPort();
        // Maybe call another server....
        return data;
    }

}

