package jsondto;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import play.Logger;
import play.PlayPlugin;
import play.mvc.Http;

/**
 * This plugin implements most of the JSON-DTO module.
 * 
 * It reads in the body content of a matching request, and binds that content to
 * a DTO-object expected by a controller.
 * 
 * @author Jarno Rantanen <jarno@jrw.fi>
 */
public class JSONDTOPlugin extends PlayPlugin {

	/**
	 * Content type to which this plugin should react.
	 * 
	 */
	private static final String CONTENT_TYPE = "application/json";

	/**
	 * Contents of the request body of a matching request, if one has been
	 * recorded.
	 * 
	 */
	private String body;

	/**
	 * Returns a boolean value indicating if we should react to this request or
	 * leave it be.
	 * 
	 * @param req
	 * @return Whether the request matches conditions
	 */
	private static boolean isMatchingRequest(Http.Request req) {

		return req != null && req.contentType.equals(CONTENT_TYPE) && (req.method.equals("POST") || req.method.equals("PUT"));

	}

	/**
	 * Reads in the request body of a matching request.
	 * 
	 * TODO: Note about exhausting the input stream
	 * 
	 */
	@Override
	public void beforeInvocation() {

		Logger.debug("JSONDTOPlugin: beforeInvocation");

	}

	/**
	 * Instantiates a DTO object from the previously read request body, and
	 * injects that to the controller that is expecting it as an argument.
	 * 
	 */
	@Override
	public Object bind(String name, Class clazz, Type type, Annotation[] annotations, Map<String, String[]> params) {

		Logger.debug("JSONDTOPlugin: bind");

		// FIXME

	}

}