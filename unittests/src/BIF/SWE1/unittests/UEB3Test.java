package BIF.SWE1.unittests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import uebungen.UEB3;

/* Placeholder */
public class UEB3Test extends AbstractTestFixture<UEB3> {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	protected UEB3 createInstance() {
		return new UEB3();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void HelloWorld() throws Exception {
		UEB3 ueb = createInstance();
		ueb.helloWorld();
	}

	/**
	 * Meilenstein 1: Der WebServer ist in einer ersten Version implementiert
	 * und Multi-User fähig. Der Request, die URL sowie der Response sind in
	 * Objekte gekapselt. Die Funktionalität wird mittels eines Mock/Test/ersten
	 * Plugins getestet
	 */
	@Test
	public void milestone1_return_main_page() throws Exception {
		UEB3 ueb = createInstance();
		Plugin obj = ueb.getTestPlugin();
		assertNotNull("UEB3.GetTestPlugin returned null", obj);
		Request req = ueb.getRequest(RequestHelper.getValidRequestStream("/"));
		assertNotNull("UEB3.GetRequest returned null", req);
		float canHandle = obj.canHandle(req);
		assertTrue(canHandle > 0 && canHandle <= 1);
		Response resp = obj.handle(req);
		assertNotNull(resp);
		assertEquals(200, resp.getStatusCode());
		assertTrue(resp.getContentLength() > 0);
		ByteArrayOutputStream ms = new ByteArrayOutputStream();
		try {
			resp.send(ms);
			assertTrue(ms.size() > 0);
		} finally {
			ms.close();
		}
	}

	/********************* request tests *********************/

	@Test
	public void request_should_parse_header() throws Exception {
		Request obj = createInstance().getRequest(RequestHelper.getValidRequestStream("/"));
		assertNotNull("UEB3.GetRequest returned null", obj);
		assertTrue(obj.isValid());
		assertNotNull(obj.getHeaders());
		assertFalse(obj.getHeaders().isEmpty());
		assertTrue(obj.getHeaderCount() > 0);
	}

	@Test
	public void request_should_return_header() throws Exception {
		Request obj = createInstance().getRequest(RequestHelper.getValidRequestStream("/"));
		assertNotNull("UEB3.GetRequest returned null", obj);
		assertTrue(obj.isValid());
		assertNotNull(obj.getHeaders());
		assertTrue(obj.getHeaders().containsKey("user-agent"));
		assertEquals("Unit-Test-Agent/1.0 (The OS)", obj.getHeaders().get("user-agent"));
	}

	@Test
	public void request_should_return_random_header() throws Exception {
		String header = "random_" + java.util.UUID.randomUUID();
		String header_value = "value_" + java.util.UUID.randomUUID();
		Request obj = createInstance().getRequest(RequestHelper.getValidRequestStream("/", new String[][] { new String[] { header, header_value } }));
		assertNotNull("UEB3.GetRequest returned null", obj);
		assertTrue(obj.isValid());
		assertNotNull(obj.getHeaders());
		assertTrue(obj.getHeaders().containsKey(header));
		assertEquals(header_value, obj.getHeaders().get(header));
	}

	@Test
	public void request_should_return_useragent() throws Exception {
		Request obj = createInstance().getRequest(RequestHelper.getValidRequestStream("/"));
		assertNotNull("UEB3.GetRequest returned null", obj);
		assertEquals("Unit-Test-Agent/1.0 (The OS)", obj.getUserAgent());
	}

	/********************** Response *********************************/
	@Test
	public void response_should_save_contenttype() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB3.GetResponse returned null", obj);
		obj.setContentType("text/plain");
		assertEquals("text/plain", obj.getContentType());
	}

	@Test
	public void response_should_save_serverheader() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB3.GetResponse returned null", obj);
		obj.setServerHeader("foo");
		assertEquals("foo", obj.getServerHeader());
	}

	@Test
	public void response_should_return_default_serverheader() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB3.GetResponse returned null", obj);
		assertEquals("BIF-SWE1-Server", obj.getServerHeader());
	}

	@Test
	public void response_should_save_string_content() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB3.GetResponse returned null", obj);
		String content = "Hello World, my GUID is " + java.util.UUID.randomUUID() + "!";
		obj.setContent(content);
	}

	@Test
	public void response_should_set_content_length() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB3.GetResponse returned null", obj);
		String content = "Hello World, my GUID is " + java.util.UUID.randomUUID() + "!";
		obj.setContent(content);
		assertEquals(content.getBytes("UTF-8").length, obj.getContentLength());
	}

	@Test
	public void response_should_set_content_length_with_utf8() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB3.GetResponse returned null", obj);
		String content = "Test: äöüÄÖÜß";
		obj.setContent(content);
		assertEquals(content.getBytes("UTF-8").length, obj.getContentLength());
	}

	@Test
	public void response_should_send_200() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB3.GetResponse returned null", obj);
		String content = "Hello World, my GUID is " + java.util.UUID.randomUUID() + "!";
		obj.setContent(content);
		obj.setStatusCode(200);

		ByteArrayOutputStream ms = new ByteArrayOutputStream();

		try {
			obj.send(ms);
			assertTrue(ms.size() > 0);
			BufferedReader sr = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(ms.toByteArray()), "UTF-8"));
			String firstLine = sr.readLine();
			assertNotNull(firstLine);
			assertTrue(firstLine.startsWith("HTTP/1."));
			assertTrue(firstLine.endsWith("200 OK"));
		} finally {
			ms.close();
		}
	}

	@Test
	public void response_should_send_404() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB3.GetResponse returned null", obj);
		obj.setStatusCode(404);
		ByteArrayOutputStream ms = new ByteArrayOutputStream();
		try {
			obj.send(ms);
			assertTrue(ms.size() > 0);
			BufferedReader sr = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(ms.toByteArray()), "UTF-8"));
			String firstLine = sr.readLine();
			assertNotNull(firstLine);
			assertTrue(firstLine.startsWith("HTTP/1."));
			assertTrue(firstLine.endsWith("404 Not Found"));
		} finally {
			ms.close();
		}
	}

	@Test
	public void response_should_send_header() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB3.GetResponse returned null", obj);
		obj.setStatusCode(404);
		String header = "X-Test-Header-" + java.util.UUID.randomUUID();
		String header_value = "val_" + java.util.UUID.randomUUID();
		obj.addHeader(header, header_value);
		ByteArrayOutputStream ms = new ByteArrayOutputStream();
		try {
			obj.send(ms);
			assertTrue(ms.size() > 0);
			BufferedReader sr = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(ms.toByteArray()), "UTF-8"));
			String expected = String.format("%s: %s", header, header_value);
			String line;
			while ((line = sr.readLine()) != null) {
				if (expected.equals(line))
					return;
			}
		} finally {
			ms.close();
		}
		fail("Header not found.");
	}

	@Test
	public void response_should_send_server_header() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB3.GetResponse returned null", obj);
		obj.setStatusCode(200);
		String header = "Server";
		String header_value = "server_" + java.util.UUID.randomUUID();
		obj.setServerHeader(header_value);
		ByteArrayOutputStream ms = new ByteArrayOutputStream();
		try {
			obj.send(ms);
			assertTrue(ms.size() > 0);
			BufferedReader sr = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(ms.toByteArray()), "UTF-8"));
			String expected = String.format("%s: %s", header, header_value);
			String line;
			while ((line = sr.readLine()) != null) {
				if (expected.equals(line))
					return;
			}
		} finally {
			ms.close();
		}
		fail("Header not found.");
	}

	@Test
	public void response_should_send_content() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB3.GetResponse returned null", obj);
		String content = "Hello World, my GUID is " + java.util.UUID.randomUUID() + "!";
		obj.setContent(content);
		obj.setStatusCode(200);

		ByteArrayOutputStream ms = new ByteArrayOutputStream();
		try {
			obj.send(ms);
			assertTrue(ms.size() > 0);
			BufferedReader sr = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(ms.toByteArray()), "UTF-8"));
			boolean header_end_found = false;
			for (int i = 0; i < 1000; i++) {
				String line = sr.readLine();
				if (line == null)
					break;
				if (line.trim().equals("")) {
					header_end_found = true;
					break;
				}

			}
			assertTrue(header_end_found);
			assertEquals(content, sr.readLine());
		} finally {
			ms.close();
		}
	}

	@Test
	public void response_should_fail_sending_no_content() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB3.GetResponse returned null", obj);
		obj.setStatusCode(200);
        // Setting a content type but no content is not allowed
		obj.setContentType("text/html");

		assertThrows(() -> {
			ByteArrayOutputStream ms = new ByteArrayOutputStream();
			obj.send(ms);
		});
	}

	@Test
	public void response_should_send_content_utf8() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB3.GetResponse returned null", obj);
		String content = String.format("Hello World, my GUID is %s! And I'll add UTF-8 chars: öäüÖÄÜß!", java.util.UUID.randomUUID());
		obj.setContent(content);
		obj.setStatusCode(200);
		ByteArrayOutputStream ms = new ByteArrayOutputStream();
		try {
			obj.send(ms);
			assertTrue(ms.size() > 0);
			BufferedReader sr = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(ms.toByteArray()), "UTF-8"));
			boolean header_end_found = false;
			for (int i = 0; i < 1000; i++) {
				String line = sr.readLine();
				if (line == null)
					break;
				if (line.trim().equals("")) {
					header_end_found = true;
					break;
				}
			}
			assertTrue(header_end_found);
			assertEquals(content, sr.readLine());
		} finally {
			ms.close();
		}
	}

	/**************************** TestPlugin *************************************/
	@Test
	public void testplugin_hello_world() throws Exception {
		Plugin obj = createInstance().getTestPlugin();
		assertNotNull("UEB3.GetTestPlugin returned null", obj);
	}

	@Test
	public void testplugin_cannot_handle_url() throws Exception {
		UEB3 ueb = createInstance();
		Plugin obj = ueb.getTestPlugin();
		assertNotNull("UEB3.GetTestPlugin returned null", obj);
		Request req = ueb.getRequest(RequestHelper.getValidRequestStream("/foo.html"));
		assertNotNull("UEB3.GetTestPlugin returned null", req);
		assertEquals(0.0f, obj.canHandle(req), 0f);
	}

	@Test
	public void testplugin_can_handle_test_url() throws Exception {
		UEB3 ueb = createInstance();
		Plugin obj = ueb.getTestPlugin();
		assertNotNull("UEB3.GetTestPlugin returned null", obj);
		Request req = ueb.getRequest(RequestHelper.getValidRequestStream("/test/foo.html"));
		assertNotNull("UEB3.GetTestPlugin returned null", req);
		assertTrue(obj.canHandle(req) > 0);
	}
	
	@Test
	public void testplugin_can_handle_test_url_with_parameter() throws Exception {
		UEB3 ueb = createInstance();
		Plugin obj = ueb.getTestPlugin();
		assertNotNull("UEB3.GetTestPlugin returned null", obj);
		Request req = ueb.getRequest(RequestHelper.getValidRequestStream("/foo.html?test_plugin=true"));
		assertNotNull("UEB3.GetTestPlugin returned null", req);
		assertTrue(obj.canHandle(req) > 0);
	}

	@Test
	public void testplugin_can_handle_request() throws Exception {
		UEB3 ueb = createInstance();
		Plugin obj = ueb.getTestPlugin();
		assertNotNull("UEB3.GetTestPlugin returned null", obj);
		Request req = ueb.getRequest(RequestHelper.getValidRequestStream("/test/foo.html"));
		assertNotNull("UEB3.GetTestPlugin returned null", req);
		Response resp = obj.handle(req);
		assertNotNull(resp);
	}

	@Test
	public void testplugin_return_valid_response() throws Exception {
		UEB3 ueb = createInstance();
		Plugin obj = ueb.getTestPlugin();
		assertNotNull("UEB3.GetTestPlugin returned null", obj);
		Request req = ueb.getRequest(RequestHelper.getValidRequestStream("/test/foo.html"));
		assertNotNull("UEB3.GetTestPlugin returned null", req);
		Response resp = obj.handle(req);
		assertNotNull(resp);
		assertEquals(200, resp.getStatusCode());
		assertTrue(resp.getContentLength() > 0);
	}

	@Test
	public void testplugin_response_send_content() throws Exception {
		UEB3 ueb = createInstance();
		Plugin obj = ueb.getTestPlugin();
		assertNotNull("UEB3.GetTestPlugin returned null", obj);
		Request req = ueb.getRequest(RequestHelper.getValidRequestStream("/test/foo.html"));
		assertNotNull("UEB3.GetTestPlugin returned null", req);
		Response resp = obj.handle(req);
		assertNotNull(resp);
		ByteArrayOutputStream ms = new ByteArrayOutputStream();
		try {
			resp.send(ms);
			assertTrue(ms.size() > 0);
		} finally {
			ms.close();
		}
	}
}
