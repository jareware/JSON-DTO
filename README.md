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
			public String title;
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

Running the tests
-----------------

...

See also
--------

 1. XXX
