package jsondto;

/**
 * Model objects marked with this interface are able to represent themselves as
 * a JSON-DTO, and vice-versa (that is, able to set their state according to a
 * given DTO).
 * 
 * @author Jarno Rantanen <jarno.rantanen@futurice.com>
 */
public interface JSONDTORepresentable<DTO extends JSONDTO> {

	/**
	 * Copies any state in the given JSON-DTO to this model object.
	 * 
	 */
	public void merge(DTO dto);
	
	/**
	 * Returns a JSON-DTO-representation of this model object.
	 * 
	 */
	public DTO toDTO();
	
}