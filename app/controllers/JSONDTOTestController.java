package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jsondto.JSONDTO;
import jsondto.JSONDTORepresentable;
import jsondto.JSONDTOUtil;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

/**
 * Test controller for exercising the actual DTO-binding of the JSONDTOPlugin.
 * 
 * @author Jarno Rantanen <jarno.rantanen@futurice.com>
 */
public class JSONDTOTestController extends Controller {

	private static final String ID = "test";

	@Before
	static void setUp() throws Exception {

		if (!Play.id.equals(ID))
			throw new Exception("This controller is only accessible within the '" + ID + "' framework ID");

	}

	/**
	 * This is a test class standing in for an actual Model object.
	 * 
	 */
	public static class Note implements JSONDTORepresentable<Note.DTO> {

		private String title;

		public class DTO implements JSONDTO {
			public String tit;
		}

		public Note(String title) {
			this.title = title;
		}

		@Override
		public void merge(DTO dto) {
			this.title = dto.tit;
		}

		@Override
		public DTO toDTO() {
			DTO dto = new DTO();
			dto.tit = this.title;
			return dto;
		}

	}

	public static void getNote() throws Exception {

		JSONDTOUtil.renderDTO(new Note("My note"), response);

	}

	public static void getNotes() throws Exception {

		List<Note> list = new ArrayList<Note>();
		list.add(new Note("foo"));
		list.add(new Note("bar"));

		JSONDTOUtil.renderDTO(list, response);

	}

	public static void getWithParams(String param) {

		renderText("param=" + param);

	}

	public static void postWithBody() throws IOException {

		renderText("body=" + JSONDTOUtil.readStream(request.body));

	}

	public static void postNote(Note.DTO note) throws IOException {

		renderText("title=" + note.tit + ";body=" + JSONDTOUtil.readStream(request.body));

	}

	public static void postNoteModel(Note note) throws IOException {

		renderText("note=" + note + ";body=" + JSONDTOUtil.readStream(request.body));

	}

}