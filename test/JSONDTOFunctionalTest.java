import org.junit.Test;

import play.mvc.Http.Response;
import play.test.FunctionalTest;

/**
 * Functional tests for the JSON-DTO module.
 * 
 * @author Jarno Rantanen <jarno.rantanen@futurice.com>
 */
public class JSONDTOFunctionalTest extends FunctionalTest {

	private static final String RP = "/jsondtoTestController/";

	/**
	 * Tests that we can render out a DTO.
	 * 
	 */
	@Test
	public void getNote() {

		Response response = GET(RP + "getNote");

		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset("utf-8", response);
		assertContentEquals("{\"tit\":\"My note\"}", response);

	}

	/**
	 * Tests that we can render out a list of DTOs.
	 * 
	 */
	@Test
	public void getNotes() {

		Response response = GET(RP + "getNotes");

		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset("utf-8", response);
		assertContentEquals("[{\"tit\":\"foo\"},{\"tit\":\"bar\"}]", response);

	}

	/**
	 * Tests that the plugin won't do anything silly with regular GET params.
	 * 
	 */
	@Test
	public void getWithParams() {

		Response response = GET(RP + "getWithParams?param=dihdah");

		assertIsOk(response);
		assertContentType("text/plain", response);
		assertCharset("utf-8", response);
		assertContentEquals("param=dihdah", response);

	}

	/**
	 * Tests that the plugin won't bind POST params that aren't JSON.
	 * 
	 */
	@Test
	public void postWithBody() {

		Response response = POST(RP + "postWithBody", "application/x-www-form-urlencoded", "wellhellothere");

		assertIsOk(response);
		assertContentType("text/plain", response);
		assertCharset("utf-8", response);
		assertContentEquals("body=wellhellothere", response);

	}

	/**
	 * Tests that we can automatically bind DTO's as controller parameters.
	 * 
	 * Also makes sure the request.body stream is actually exhausted (this may
	 * or may not be something we want but let's document it as a test case
	 * anyway).
	 * 
	 */
	@Test
	public void postNote() {

		Response response = POST(RP + "postNote", "application/json", "{\"tit\":\"My note\"}");

		assertIsOk(response);
		assertContentType("text/plain", response);
		assertCharset("utf-8", response);
		assertContentEquals("title=My note;body=", response);

	}

	/**
	 * Tests that the plugin won't react to JSONDTORepresentable parameters.
	 * 
	 * Also tests body exhaustion (see postNote() for rationale).
	 * 
	 */
	@Test
	public void postNoteModel() {

		Response response = POST(RP + "postNoteModel", "application/json", "{\"tit\":\"My note\"}");

		assertIsOk(response);
		assertContentType("text/plain", response);
		assertCharset("utf-8", response);
		assertContentEquals("note=null;body=", response);

	}

	/**
	 * Tests that if the request body doesn't contain an expected field, it'll just show up as being null.
	 * 
	 */
	@Test
	public void expectedFieldMissing() {

		Response response = POST(RP + "postNote", "application/json", "{\"something\":\"unexpected\"}");

		assertIsOk(response);
		assertContentType("text/plain", response);
		assertCharset("utf-8", response);
		assertContentEquals("title=null;body=", response);

	}

	// TODO: A test with missing required fields..?

}