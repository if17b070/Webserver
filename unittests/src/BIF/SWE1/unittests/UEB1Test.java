package BIF.SWE1.unittests;

import static org.junit.Assert.*;

import org.junit.*;

import BIF.SWE1.interfaces.Url;
import uebungen.UEB1;

public class UEB1Test extends AbstractTestFixture<UEB1> {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	protected UEB1 createInstance() {
		return new UEB1();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void helloWorld() throws Exception {
		UEB1 ueb = createInstance();
		ueb.helloWorld();
	}

	@Test
	public void url_should_create_empty() {
		Url obj = createInstance().getUrl(null);
		assertNotNull("UEB1.GetUrl returned null", obj);

		assertEmptyOrNull(obj.getPath());
	}

	@Test
	public void url_should_return_raw_url_0() {
		Url obj = createInstance().getUrl("/test.jpg");
		assertNotNull("UEB1.GetUrl returned null", obj);

		assertEquals("/test.jpg", obj.getRawUrl());
	}

	@Test
	public void url_should_return_raw_url_1() {
		Url obj = createInstance().getUrl("/test.jpg?x=y");
		assertNotNull("UEB1.GetUrl returned null", obj);

		assertEquals("/test.jpg?x=y", obj.getRawUrl());
	}

	@Test
	public void url_should_return_raw_url_2() {
		Url obj = createInstance().getUrl("/test.jpg?x=1&y=2");
		assertNotNull("UEB1.GetUrl returned null", obj);

		assertEquals("/test.jpg?x=1&y=2", obj.getRawUrl());
	}

	@Test
	public void url_should_create_with_path() {
		Url obj = createInstance().getUrl("/test.jpg");
		assertNotNull("UEB1.GetUrl returned null", obj);

		assertEquals("/test.jpg", obj.getPath());
	}

	@Test
	public void url_should_parse_parameter() {
		Url obj = createInstance().getUrl("/test.jpg?x=1");
		assertNotNull("UEB1.GetUrl returned null", obj);

		assertNotNull(obj.getParameter().get("x"));
		assertEquals("1", obj.getParameter().get("x"));
	}

	@Test
	public void url_should_parse_more_parameter() {
		Url obj = createInstance().getUrl("/test.jpg?x=1&y=2");
		assertNotNull("UEB1.GetUrl returned null", obj);

		assertNotNull(obj.getParameter().get("x"));
		assertEquals("1", obj.getParameter().get("x"));
		
		assertNotNull(obj.getParameter().get("y"));
		assertEquals("2", obj.getParameter().get("y"));
	}

	@Test
	public void url_should_parse_return_path_without_parameter() {
		Url obj = createInstance().getUrl("/test.jpg?x=1");
		assertNotNull("UEB1.GetUrl returned null", obj);

		assertEquals("/test.jpg", obj.getPath());
	}
	
	@Test
	public void url_should_count_parameter() {
		Url obj = createInstance().getUrl("/test.jpg?x=7");
		assertNotNull("UEB1.GetUrl returned null", obj);

		assertEquals(1, obj.getParameterCount());
	}
	
	@Test
	public void url_should_count_parameter_2() {
		Url obj = createInstance().getUrl("/test.jpg?x=7&y=foo");
		assertNotNull("UEB1.GetUrl returned null", obj);

		assertEquals(2, obj.getParameterCount());
	}
	
	@Test
	public void url_should_count_parameter_0() {
		Url obj = createInstance().getUrl("/test.jpg");
		assertNotNull("UEB1.GetUrl returned null", obj);

		assertEquals(0, obj.getParameterCount());
	}
	
	@Test
	public void url_should_count_parameter_empty() {
		Url obj = createInstance().getUrl(null);
		assertNotNull("UEB1.GetUrl returned null", obj);

		assertEquals(0, obj.getParameterCount());
	}
}
