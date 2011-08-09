JSON-DTO Module for Play!
=========================

Simple plugin and associated conventions for making JSON-DTO binding a breeze with the Play! Framework.

Installation
------------

Obtain a copy of the JSON-DTO module and drop it under the `modules/` path of your Play! application.
That should be it.

How it works
------------

When building a RESTful JSON API with Play! (or with any web framework for that matter), you are often doing two things:

1. Reading in requests that (may) contain a JSON representation of a model object
1. Responding to requests with another (possible) such representation

Play! offers [automatic HTTP-POJO-binding](http://www.playframework.org/documentation/1.2.2/controllers#pojo)
which is great when reading in regular form data, but
when building an API you often want to be very specific about what to expose.
You may want to change the public names of fields, or may want to add generated, read-only fields to your
objects.  One example of such a field could be a `url` field, which (for our purposes at least) is immutable, and must be generated whenever the
object is rendered out as JSON.
The default POJO-binding also only works with named request parameters, whereas one would like
to read in the object from the request body, just as you respond with the object in the response body.
Finally, as the Play! model objects are backed by a JPA Entity Manager and are thus part of a complex graph of objects,
rendering a representation of the object relationships may get very hairy pretty quick.

The JSON-DTO module offers to solve these issues by using a [Data Transfer Object](http://en.wikipedia.org/wiki/Data_transfer_object)
for defining the JSON representation of a model object.  This takes away a lot of the magic involved with
annotating your models for which fields to expose, which to allow editing for etc.  Instead, each model object that
can be represented as JSON (`JSONDTORepresentable`) explicitly defines that representation (`JSONDTO`)
and methods for converting to and from such an instance.

Please find some concrete examples below.

Examples
--------

Let's say we're building an API for maintaining a list of notes.  Our model object looks like:

	@Entity
	public class Note extends Model {

		public String title = "";

		@ManyToMany
		public List<Tag> tags = new ArrayList<Tag>();

	}

We want to give the Note's JSON representation the aforementioned immutable `url` field,
and also render out the tags list as just a list of the names of the tags,
not the complete objects.  Here we go:

	@Entity
	public class Note extends Model implements JSONDTORepresentable<Note.DTO> {

		public String title = "";

		@ManyToMany
		public List<Tag> tags = new ArrayList<Tag>();

		public class DTO implements JSONDTO {
			public String my_title;
			public List<String> tags;
			public String url;
		}

		@Override
		public void merge(DTO dto) {
			// TODO
		}

		@Override
		public DTO toDTO() {
			// TODO
		}

	}

By implementing `JSONDTORepresentable` the model object signals that it can represent itself as a DTO if need be.
We define that representation to be `Note.DTO`, which is just a POJO without any inheritance or annotations magic.
It contains the fields we want to expose within the JSON representation of the Note model.
We implement it as an inline class to keep these closely related classes together,
though you can just as well define the implementation of `JSONDTO` as a completely separate class.

Note that the DTO differs from the actual model object - only the DTO has the `url` field,
while the implicit `id` field of the model object is not exposed via the DTO.
The `title` field has also been renamed as `my_title`,
since that is how we want to expose it in our API.

The `JSONDTORepresentable` interface defines two methods, namely `merge(DTO)` and `toDTO()`.
The former should copy any properties from the DTO to the model object,
while the latter should do the opposite, that is, produce an instance of `DTO` based on the properties of the model object.
Let's look at an implementation of `toDTO()` first:

	@Override
	public DTO toDTO() {

		DTO dto = new DTO();
		dto.my_title = this.title;
		dto.url = Router.reverse(/* ... */).url;
		dto.tags = new ArrayList<String>();

		for (Tag tag : this.tags)
			dto.tags.add(tag.name);

		return dto;

	}

To do the opposite, we implement a `merge(DTO)` as well:

	@Override
	public void merge(DTO dto) {

		this.title = dto.my_title;

		for (String tagName : dto.tags) {
			Tag tag = Tag.find("byName", tagName).first();
			this.tags.add(tag);
		}

	}

Note that the `url` field is simply ignored since we want it to be read-only.

To make use of the model object and its DTO in a controller, you could do something like:

	public static void getNote(Long id) throws Exception {

		Note note = Note.findById(id);

		notFoundIfNull(note);

		JSONDTOUtil.renderDTO(note, response);

	}

The above controller method could match to a route `GET /notes/{id} Notes.getNote`.
Note that we can pass the model object directly to `JSONDTOUtil.renderDTO()`,
which will know to call `toDTO()` appropriately.
You can also pass in a list of model objects (that implement `JSONDTORepresentable`):

	public static void getAllNotes() throws Exception {

		List<Note> notes = Note.findAll();

		JSONDTOUtil.renderDTO(notes, response);

	}

To update a note, one could bind the following method to a route `PUT /notes/{id} Notes.updateNote`:

	public static void updateNote(Long id, Note.DTO noteDTO) throws Exception {

		Note note = Note.findById(id);

		notFoundIfNull(note);

		note.merge(noteDTO);
		note.save();

	}

Running the tests
-----------------

...

See also
--------

 1. XXX
