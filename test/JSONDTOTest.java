import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import jsondto.JSONDTO;
import jsondto.JSONDTORepresentable;
import jsondto.Util;

import org.junit.Test;

/**
 * Test cases for the JSON-DTO module.
 * 
 * @author Jarno Rantanen <jarno@jrw.fi>
 */
public class JSONDTOTest {

	public class Note implements JSONDTORepresentable<Note.DTO> {

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
	public void testSimpleSerialization() {

		Note note = new Note();
		note.title = "foo";

		assertEquals("{\"note_title\":\"foo\"}", Util.getDTOsAsString(note));

	}

	@Test
	public void testListSerialization() {

		Note note1 = new Note();
		note1.title = "foo";

		Note note2 = new Note();
		note2.title = "bar";

		List notes = new ArrayList<Note>();
		notes.add(note1);
		notes.add(note2);

		assertEquals("[{\"note_title\":\"foo\"},{\"note_title\":\"bar\"}]", Util.getDTOsAsString(notes));

	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentForSerialization() {

		Util.getDTOsAsString(new Object());

	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentForListSerialization() {

		List list = new ArrayList();
		list.add(new Object());

		Util.getDTOsAsString(list);

	}

}