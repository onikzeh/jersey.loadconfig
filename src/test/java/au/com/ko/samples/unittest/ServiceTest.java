package au.com.ko.samples.unittest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.ws.rs.core.Response;
import java.io.IOException;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(JUnit4.class)
public class ServiceTest extends BaseTest{

	@Test
	public void testSignData() throws IOException {

		Response response = target
				.path("doSomething")
				.request(APPLICATION_JSON)
				.get();

		assertEquals("Status code returned should be 200 ",Response.Status.OK.getStatusCode(),response.getStatus());
		assertNotNull("Response: ", response);

		logger.info(response.readEntity(String.class));
	}

}
