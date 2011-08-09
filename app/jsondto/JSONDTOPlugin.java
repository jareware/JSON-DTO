package jsondto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import play.Logger;
import play.PlayPlugin;
import play.mvc.Http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * This plugin implements most of the JSON-DTO module.
 * 
 * It reads in the body content of a matching request, and binds that content to
 * a DTO-object expected by a controller.
 * 
 * @author Jarno Rantanen <jarno.rantanen@futurice.com>
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
	 * In short, we are only interested in requests that may contain a JSON-body
	 * (for our purist purposes, this doesn't include GET's and DELETE's).
	 * 
	 */
	private static boolean isMatchingRequest(Http.Request req) {

		return req != null && req.contentType.equals(CONTENT_TYPE) && (req.method.equals("POST") || req.method.equals("PUT"));

	}

	/**
	 * Reads in the request body of a matching request.
	 * 
	 * It should be noted that this operation will in fact exhaust the the input
	 * stream, since it doesn't implement a reset(). This means that any other
	 * plugins/controllers that try to read the stream after this method will
	 * see it being empty. This is generally not a problem, however, since if a
	 * Request isMatchingRequest(), it shouldn't be needed for anything else
	 * than the JSON-DTO binding that we're doing.
	 * 
	 * TODO: Replace the stream with identical content..?
	 * 
	 */
	@Override
	public void beforeInvocation() {

		Logger.debug("JSON-DTO: beforeInvocation()");

		Http.Request request = Http.Request.current();

		if (!isMatchingRequest(request))
			return;

		Logger.info("JSON-DTO: Reading in request body");

		try {

			String tempBody = JSONDTOUtil.readStream(request.body);

			request.body = new ByteArrayInputStream("".getBytes(JSONDTOUtil.CHARSET));

			if (tempBody.length() > 0) {

				Logger.debug("JSON-DTO: Request body stored");

				body = tempBody;

			}

		} catch (IOException e) {

			Logger.error("JSON-DTO: Could not read request body");

			e.printStackTrace();

		}

	}

	/**
	 * Instantiates a DTO object from the previously read request body, and
	 * injects that to the controller that is expecting it as an argument.
	 * 
	 * TODO: Allow binding arrays of JSONDTO's as well..?
	 * 
	 */
	@Override
	public Object bind(String name, Class clazz, Type type, Annotation[] annotations, Map<String, String[]> params) {

		Logger.debug("JSON-DTO: bind()");

		if (!isMatchingRequest(Http.Request.current()))
			return null; // only react to interesting requests

		if (!JSONDTO.class.isAssignableFrom(clazz))
			return null; // only react to classes marked as a DTO

		try {

			Logger.info("JSON-DTO: Binding JSON request body to controller parameter '" + name + "'");

			return new Gson().fromJson(body, clazz);

		} catch (JsonSyntaxException e) {

			Logger.error("JSON-DTO: Invalid JSON provided for controller parameter '" + name + "'");

			e.printStackTrace();

			return null;

		}

	}

}