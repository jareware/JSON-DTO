import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jsondto.JSONDTO;
import jsondto.JSONDTORepresentable;
import jsondto.JSONDTOUtil;

import org.junit.Test;

import play.test.UnitTest;

/**
 * Unit tests for the utilities used by the JSON-DTO module.
 * 
 * @author Jarno Rantanen <jarno.rantanen@futurice.com>
 */
public class JSONDTOUnitTest extends UnitTest {

	/**
	 * This is a test class standing in for an actual Model object.
	 * 
	 */
	private class Note implements JSONDTORepresentable<Note.DTO> {

		private String title;

		public class DTO implements JSONDTO {
			public String note_title;
		}

		@Override
		public void merge(DTO dto) {
			this.title = dto.note_title;
		}

		@Override
		public DTO toDTO() {
			DTO dto = new DTO();
			dto.note_title = this.title;
			return dto;
		}

	}

	@Test
	public void simpleSerialization() {

		Note note = new Note();
		note.title = "foo";

		assertEquals("{\"note_title\":\"foo\"}", JSONDTOUtil.getDTOsAsString(note));

	}

	@Test
	public void listSerialization() {

		Note note1 = new Note();
		note1.title = "foo";

		Note note2 = new Note();
		note2.title = "bar";

		List notes = new ArrayList<Note>();
		notes.add(note1);
		notes.add(note2);

		assertEquals("[{\"note_title\":\"foo\"},{\"note_title\":\"bar\"}]", JSONDTOUtil.getDTOsAsString(notes));

	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalArgumentForSerialization() {

		JSONDTOUtil.getDTOsAsString(new Object());

	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalArgumentForListSerialization() {

		List list = new ArrayList();
		list.add(new Object());

		JSONDTOUtil.getDTOsAsString(list);

	}

	@Test
	public void nullSerialization() {

		assertEquals("null", JSONDTOUtil.getDTOsAsString(null));

	}

	@Test
	public void emptyListSerialization() {

		assertEquals("[]", JSONDTOUtil.getDTOsAsString(new ArrayList()));

	}

	@Test
	public void emptyObjectSerialization() {

		class EmptyModel implements JSONDTORepresentable<EmptyModel.DTO> {

			class DTO implements JSONDTO {
			}

			@Override
			public void merge(DTO dto) {
			}

			@Override
			public DTO toDTO() {
				return new DTO();
			}

		}

		assertEquals("{}", JSONDTOUtil.getDTOsAsString(new EmptyModel()));

	}

	@Test
	public void readingInStreams() throws IOException {

		InputStream is = new ByteArrayInputStream("ABC".getBytes(JSONDTOUtil.CHARSET));

		assertEquals("ABC", JSONDTOUtil.readStream(is));

	}

	@Test
	public void readingEmptyStream() throws IOException {

		InputStream is = new ByteArrayInputStream("".getBytes(JSONDTOUtil.CHARSET));

		assertEquals("", JSONDTOUtil.readStream(is));

	}

}