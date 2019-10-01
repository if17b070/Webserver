package BIF.SWE1.unittests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.stream.StreamSupport;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.PluginManager;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import uebungen.UEB4;

/* Placeholder */
public class UEB4Test extends AbstractTestFixture<UEB4> {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	protected UEB4 createInstance() {
		return new UEB4();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void HelloWorld() throws Exception {
		UEB4 ueb = createInstance();
		ueb.helloWorld();
	}

	/********************* request tests **********************/

	@Test
	public void request_should_handle_post() throws Exception {
		Request obj = createInstance().getRequest(RequestHelper.getValidRequestStream("/", "POST"));
		assertNotNull("UEB4.GetRequest returned null", obj);
		assertTrue(obj.isValid());
		assertEquals("POST", obj.getMethod());
	}

	@Test
	public void request_should_parse_post_content_length() throws Exception {
		Request obj = createInstance().getRequest(RequestHelper.getValidRequestStream("/", "POST", "x=a&y=b"));
		assertNotNull("UEB4.GetRequest returned null", obj);
		assertTrue(obj.isValid());
		assertEquals("POST", obj.getMethod());
		assertEquals(7, obj.getContentLength());
	}

	@Test
	public void request_should_parse_post_content_type() throws Exception {
		Request obj = createInstance().getRequest(RequestHelper.getValidRequestStream("/", "POST", "x=a&y=b"));
		assertNotNull("UEB4.GetRequest returned null", obj);
		assertTrue(obj.isValid());
		assertEquals("POST", obj.getMethod());
		assertEquals("application/x-www-form-urlencoded", obj.getContentType());
	}

	@Test
	public void request_should_return_post_content() throws Exception {
		Request obj = createInstance().getRequest(RequestHelper.getValidRequestStream("/", "POST", "x=a&y=b"));
		assertNotNull("UEB4.GetRequest returned null", obj);
		assertTrue(obj.isValid());
		assertEquals("POST", obj.getMethod());
		assertNotNull(obj.getContentStream());
		byte[] bodyBytes = new byte[10];
		assertEquals(7, obj.getContentStream().read(bodyBytes, 0, 10));
		String body = new String(bodyBytes, 0, 7, "UTF-8");
		assertEquals("x=a&y=b", body);
	}

	@Test
	public void request_should_return_post_content_as_string() throws Exception {
		Request obj = createInstance().getRequest(RequestHelper.getValidRequestStream("/", "POST", "x=a&y=b"));
		assertNotNull("UEB4.GetRequest returned null", obj);
		assertTrue(obj.isValid());
		assertEquals("POST", obj.getMethod());
		assertNotNull(obj.getContentStream());
		assertEquals("x=a&y=b", obj.getContentString());
	}

	@Test
	public void request_should_return_post_content_as_bytes() throws Exception {
		Request obj = createInstance().getRequest(RequestHelper.getValidRequestStream("/", "POST", "x=a&y=b"));
		assertNotNull("UEB4.GetRequest returned null", obj);
		assertTrue(obj.isValid());
		assertEquals("POST", obj.getMethod());
		assertNotNull(obj.getContentBytes());
		assertEquals(7, obj.getContentBytes().length);
		String body = new String(obj.getContentBytes(), 0, 7, "UTF-8");
		assertEquals("x=a&y=b", body);
	}

	/********************** Response *********************************/
	@Test
	public void response_should_send_byte_content() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB4.GetResponse returned null", obj);
		byte[] content = String.format("Hello World, my GUID is %s! Ignore UTF-8 chars!", java.util.UUID.randomUUID()).getBytes("UTF-8");
		obj.setContent(content);
		obj.setStatusCode(200);

		ByteArrayOutputStream ms = new ByteArrayOutputStream();
		try {
			obj.send(ms);
			assertTrue(ms.size() > 0);
			BufferedReader sr = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(ms.toByteArray()), "ASCII"));
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
			char[] buffer = new char[content.length];
			sr.read(buffer);
			for (int i = 0; i < content.length; i++) {
				assertEquals(content[i], (byte) buffer[i]);
			}
		} finally {
			ms.close();
		}
	}

	@Test
	public void response_should_send_stream_content() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB4.GetResponse returned null", obj);
		byte[] bytes = String.format("Hello World, my GUID is %s! Ignore UTF-8 chars!", java.util.UUID.randomUUID()).getBytes("UTF-8");
		ByteArrayInputStream content = new ByteArrayInputStream(bytes);
		obj.setContent(content);
		obj.setStatusCode(200);

		ByteArrayOutputStream ms = new ByteArrayOutputStream();
		try {
			obj.send(ms);
			assertTrue(ms.size() > 0);
			BufferedReader sr = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(ms.toByteArray()), "ASCII"));
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
			char[] buffer = new char[bytes.length];
			sr.read(buffer);
			for (int i = 0; i < bytes.length; i++) {
				assertEquals(bytes[i], (byte) buffer[i]);
			}
		} finally {
			ms.close();
		}
	}

	/********************** PluginManager *********************************/
	@Test
	public void pluginmanager_hello_world() {
		PluginManager obj = createInstance().getPluginManager();
		assertNotNull("UEB4.GetPluginManager returned null", obj);
	}

	@Test
	public void pluginmanager_returns_plugins() {
		PluginManager obj = createInstance().getPluginManager();
		assertNotNull("UEB4.GetPluginManager returned null", obj);
		assertNotNull(obj.getPlugins());
	}

	@Test
	public void pluginmanager_returns_1_plugin() {
		PluginManager obj = createInstance().getPluginManager();
		assertNotNull("UEB4.GetPluginManager returned null", obj);
		assertNotNull(obj.getPlugins());
		assertTrue(obj.getPlugins().iterator().hasNext());
	}

	@Test
	public void pluginmanager_plugins_are_not_null() {
		PluginManager obj = createInstance().getPluginManager();
		assertNotNull("UEB4.GetPluginManager returned null", obj);
		assertNotNull(obj.getPlugins());
		for (Plugin p : obj.getPlugins()) {
			assertNotNull(p);
		}
	}
	
	@Test
	public void pluginmanager_should_add_plugin() {
		PluginManager obj = createInstance().getPluginManager();
		assertNotNull("UEB4.GetPluginManager returned null", obj);
		assertNotNull(obj.getPlugins());
		long count = StreamSupport.stream(obj.getPlugins().spliterator(), false).count();			
        Plugin myPlugin = new Plugin() {

			@Override
			public float canHandle(Request req) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Response handle(Request req) {
				// TODO Auto-generated method stub
				return null;
			}
        	
        };
        obj.add(myPlugin);
        assertEquals(count + 1, StreamSupport.stream(obj.getPlugins().spliterator(), false).count());
        boolean found = false;
		for (Plugin p : obj.getPlugins()) {
			if(p == myPlugin) found = true;
		}
		
		assertTrue("New plugin was not found.", found);
	}
	
	@Test
	public void pluginmanager_should_clear_plugins() {
		PluginManager obj = createInstance().getPluginManager();
		assertNotNull("UEB4.GetPluginManager returned null", obj);
		assertNotNull(obj.getPlugins());
		
		obj.clear();
		
		assertNotNull(obj.getPlugins());
        assertEquals(0l, StreamSupport.stream(obj.getPlugins().spliterator(), false).count());

	}
}
