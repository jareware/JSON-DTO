package jsondto;

/**
 * This is a marker interface for noting POJO's the JSON-DTO should react to.
 * That is, classes marked with this interface can be used as parameters to
 * controllers and they are automatically bound to the data in the request body.
 * 
 * @author Jarno Rantanen <jarno.rantanen@futurice.com>
 */
public interface JSONDTO {}