package es.weso.acota.persistence;

import java.util.Collection;
import java.util.Set;

import es.weso.acota.core.business.enhancer.FeedbackConfigurable;
import es.weso.acota.core.exceptions.AcotaPersistenceException;

/**
 * LabelDAO Interface, this interface allows access to a database and
 * to perform CR (Create, Read) operations about labels
 * @author César Luis Alvargonzález
 *
 */
public interface LabelDAO extends GenericDAO, FeedbackConfigurable{

	/**
	 * Saves a label into the database
	 * @param id Label's id, id is the label's hashCode
	 * @param label Label's name
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public void saveLabel(Integer id, String label) throws AcotaPersistenceException;
	
	/**
	 * Returns the name of the label associated with the provided id
	 * @param id Id of the label
	 * @return Label's name of the label associated with the provided id
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public String getLabelById(Integer id) throws AcotaPersistenceException;

	/**
	 * Returns the name of the label associated with the provided hashCode
	 * @param hashCode
	 * @return Label's name of the label associated with the provided hashCode
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public String getLabelByHash(Integer hashCode) throws AcotaPersistenceException;

	/**
	 * Returns a set of name of the labels associated with the provided ids
	 * @param ids Collection of ids
	 * @return Collection of names, of the labels associated with the provided ids
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public Set<String> getLabelsByIds(Collection<Integer> ids)
			 throws AcotaPersistenceException;
	
	/**
	 * Returns a set of name of the labels associated with the provided ids
	 * @param hashCodes Collection of hashCodes
	 * @return Collection of names, of the labels associated with the provided hashCodes
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public Set<String> getLabelsByHashCodes(Collection<Integer> hashCodes)
			 throws AcotaPersistenceException;

}
