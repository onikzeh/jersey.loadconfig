package au.com.ko.samples.unittest.support;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

import javax.naming.NamingException;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

import au.com.ko.samples.jersey.ApplicationConfig;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class TestServer {

    private HttpServer server;
    private URI baseURL;

    public TestServer(URI baseURL) {
        this.baseURL = baseURL;
    }

    public void start() throws IOException {

        server = HttpServer.create(new InetSocketAddress(baseURL.getPort()), 0);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                server.stop(0);
            }
        }));

        // create a handler wrapping the JAX-RS application
        HttpHandler handler = RuntimeDelegate.getInstance().createEndpoint(new ApplicationConfig(), HttpHandler.class);

        // map JAX-RS handler to the server root
        server.createContext(baseURL.getPath(), handler);
        server.start();
    }

    public void stop(){
        server.stop(0);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        URI baseURI = UriBuilder.fromUri("http://localhost/test").port(9080).build();
	    TestServer server = new TestServer(baseURI);
	    server.start();

        System.out.println("Application started.\n"
                + "Access via " + baseURI + " in the browser.\n"
                + "Hit enter to stop the application...");
    }
}

