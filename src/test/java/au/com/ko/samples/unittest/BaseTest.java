package au.com.ko.samples.unittest;

import au.com.ko.samples.unittest.support.MockInitialContextFactory;
import au.com.ko.samples.unittest.support.TestServer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Contains most common functionality for tests.
 */
public class BaseTest {

    private static TestServer testServer;
    static WebTarget target;

    protected Logger logger;

    @BeforeClass
    public static void initialize() throws IOException {
        // Mocking JNDI provider instead of web.xml
        // Used in ConfigManager#getConfigSrc
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                MockInitialContextFactory.class.getName());

        Properties config = new Properties();
        config.load(ServiceTest.class.getResourceAsStream("/config.properties"));

        // Configure Java log manager for tests (NOT SERVER!)
        LogManager.getLogManager().readConfiguration(BaseTest.class.getResourceAsStream("/config.properties"));

        // binds the location of config file to 'java:comp/env/ConnectionConfig' in JNDI
        MockInitialContextFactory.bind("java:comp/env/ConnectionConfig", config.getProperty("test.config.location"));

        // initialize and start the HTTP Server
        String contextPath = config.getProperty("test.server.contextPath");
        URI baseURI = UriBuilder.fromUri("http://localhost/" + contextPath)
                .port(Integer.parseInt(config.getProperty("test.server.port"))).build();

        testServer = new TestServer(baseURI);
        testServer.start();

        Client client = ClientBuilder.newClient();
        target = client.target(baseURI);
    }

    @AfterClass
    public static void stopServer() {
        if (testServer != null) {
            testServer.stop();
        }

        System.clearProperty(Context.INITIAL_CONTEXT_FACTORY);
    }

    @Before
    public void setup(){
        logger = Logger.getLogger(this.getClass().getSimpleName());
    }
}
