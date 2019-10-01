//
// Translated by CS2J (http://www.cs2j.com): 30.09.2014 16:41:50
//

package BIF.SWE1.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import BIF.SWE1.interfaces.Url;
import uebungen.UEB2;

public class UEB2Test extends AbstractTestFixture<UEB2> {
	@Test
	public void helloWorld() throws Exception {
		UEB2 ueb = createInstance();
		ueb.helloWorld();
	}

	/********************* More URL tests *********************/
	@Test
	public void url_should_create_with_path_fragment() throws Exception {
		Url obj = createInstance().getUrl("/test.jpg#foo");
		assertNotNull("UEB2.GetUrl returned null", obj);

		assertEquals("/test.jpg", obj.getPath());
	}
	@Test
	public void url_should_parse_fragment() throws Exception {
		Url obj = createInstance().getUrl("/test.jpg#foo");
		assertNotNull("UEB2.GetUrl returned null", obj);

		assertEquals("foo", obj.getFragment());
	}
	@Test
	public void url_should_split_segments() throws Exception {
		Url obj = createInstance().getUrl("/foo/bar/test.jpg");
		assertNotNull("UEB2.GetUrl returned null", obj);

		assertNotNull(obj.getSegments());
		assertArrayEquals(new String[] { "foo", "bar", "test.jpg" } , obj.getSegments());
	}

	/********************* Basic request tests *********************/
	@Test
	public void request_hello_world() throws Exception {
		Request obj = createInstance().getRequest(
				RequestHelper.getValidRequestStream("/"));
		assertNotNull("UEB2.GetRequest returned null", obj);
	}

	@Test
	public void request_isValid_on_valid_request() throws Exception {
		Request obj = createInstance().getRequest(
				RequestHelper.getValidRequestStream("/"));
		assertNotNull("UEB2.GetRequest returned null", obj);
		assertTrue(obj.isValid());
	}

	@Test
	public void request_isInValid_on_invalid_request() throws Exception {
		Request obj = createInstance().getRequest(
				RequestHelper.getInvalidRequestStream());
		assertNotNull("UEB2.GetRequest returned null", obj);

		assertFalse(obj.isValid());
	}

	@Test
	public void request_isInValid_on_empty_request() throws Exception {
		Request obj = createInstance().getRequest(
				RequestHelper.getEmptyRequestStream());
		assertNotNull("UEB2.GetRequest returned null", obj);

		assertFalse(obj.isValid());
	}

	@Test
	public void request_should_parse_method_get() throws Exception {
		Request obj = createInstance().getRequest(
				RequestHelper.getValidRequestStream("/"));
		assertNotNull("UEB2.GetRequest returned null", obj);
		assertTrue(obj.isValid());
		assertEquals("GET", obj.getMethod());
	}

	@Test
	public void request_should_parse_method_post() throws Exception {
		Request obj = createInstance().getRequest(
				RequestHelper.getValidRequestStream("/", "POST"));
		assertNotNull("UEB2.GetRequest returned null", obj);

		assertTrue(obj.isValid());
		assertEquals("POST", obj.getMethod());
	}
	
	@Test
	public void request_should_parse_method_post_lowercase() throws Exception {
		Request obj = createInstance().getRequest(
				RequestHelper.getValidRequestStream("/", "post"));
		assertNotNull("UEB2.GetRequest returned null", obj);

		assertTrue(obj.isValid());
		assertEquals("POST", obj.getMethod());
	}
	
	@Test
	public void request_should_be_invalid_on_method_foo() throws Exception {
		Request obj = createInstance().getRequest(
				RequestHelper.getValidRequestStream("/", "FOO"));
		assertNotNull("UEB2.GetRequest returned null", obj);

		assertFalse(obj.isValid());
	}

	@Test
	public void request_should_parse_url() throws Exception {
		Request obj = createInstance().getRequest(
				RequestHelper.getValidRequestStream("/"));
		assertNotNull("UEB2.GetRequest returned null", obj);

		assertTrue(obj.isValid());
		assertNotNull(obj.getUrl());
		assertEquals("/", obj.getUrl().getRawUrl());
	}

	@Test
	public void request_should_parse_url_2() throws Exception {
		Request obj = createInstance().getRequest(
				RequestHelper.getValidRequestStream("/foo.html?a=1&b=2"));
		assertNotNull("UEB2.GetRequest returned null", obj);
		assertTrue(obj.isValid());
		assertNotNull(obj.getUrl());
		assertEquals("/foo.html?a=1&b=2", obj.getUrl().getRawUrl());
	}

	/********************* Basic response tests *********************/
	@Test
	public void response_hello_world() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB2.GetResponse returned null", obj);
	}

	@SuppressWarnings("unused")
	@Test
	public void response_should_throw_error_when_no_statuscode_was_set() throws Exception {
		Response obj = createInstance().getResponse();

		assertNotNull("UEB2.GetResponse returned null", obj);
		assertThrows(() -> { int tmp = obj.getStatusCode(); });
	}

	@Test
	public void response_should_save_statuscode() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB2.GetResponse returned null", obj);
		obj.setStatusCode(404);
		assertEquals(404, obj.getStatusCode());
	}

	@Test
	public void response_should_return_status_200() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB2.GetResponse returned null", obj);
		obj.setStatusCode(200);
		assertEquals("200 OK", obj.getStatus().toUpperCase());
	}

	@Test
	public void response_should_return_status_404() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB2.GetResponse returned null", obj);

		obj.setStatusCode(404);
		assertEquals("404 NOT FOUND", obj.getStatus().toUpperCase());
	}

	@Test
	public void response_should_return_status_500() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB2.GetResponse returned null", obj);

		obj.setStatusCode(500);
		assertEquals("500 INTERNAL SERVER ERROR", obj.getStatus().toUpperCase());
	}

	@Test
	public void response_should_save_header() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB2.GetResponse returned null", obj);

		obj.addHeader("foo", "bar");
		assertNotNull(obj.getHeaders());
		assertTrue(obj.getHeaders().containsKey("foo"));
		assertEquals("bar", obj.getHeaders().get("foo"));
	}

	@Test
	public void response_should_replace_header() throws Exception {
		Response obj = createInstance().getResponse();
		assertNotNull("UEB2.GetResponse returned null", obj);

		obj.addHeader("foo", "bar");
		assertNotNull(obj.getHeaders());
		assertTrue(obj.getHeaders().containsKey("foo"));
		assertEquals("bar", obj.getHeaders().get("foo"));

		obj.addHeader("foo", "override");
		assertTrue(obj.getHeaders().containsKey("foo"));
		assertEquals("override", obj.getHeaders().get("foo"));
	}

	@Override
	protected UEB2 createInstance() {
		return new UEB2();
	}
}
