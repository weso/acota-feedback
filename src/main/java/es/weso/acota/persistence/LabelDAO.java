package es.weso.acota.persistence;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

/**
 * LabelDAO Interface, this interface allows access to a database and
 * make CRD (Create, Read, Delete) operations about labels
 * @author César Luis Alvargonzález
 *
 */
public interface LabelDAO extends GenericDAO {

	/**
	 * Saves a label into the database
	 * @param id Label's id, id is the label's hashCode
	 * @param label Label's name
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 * @throws ClassNotFoundException Exception that occurs when the database driver
	 * is not found in runtime
	 */
	public void saveLabel(Integer id, String label) throws SQLException,
	ClassNotFoundException;
	
	/**
	 * Returns the name of the label associated with the provided id
	 * @param id Id of the label
	 * @return Label's name of the label associated with the provided id
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 * @throws ClassNotFoundException Exception that occurs when the database driver
	 * is not found in runtime
	 */
	public String getLabelById(Integer id) throws SQLException,
			ClassNotFoundException;

	/**
	 * Returns the name of the label associated with the provided hashCode
	 * @param hashCode
	 * @return Label's name of the label associated with the provided hashCode
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 * @throws ClassNotFoundException Exception that occurs when the database driver
	 * is not found in runtime
	 */
	public String getLabelByHash(Integer hashCode) throws SQLException,
			ClassNotFoundException;

	/**
	 * Returns a set of name of the labels associated with the provided ids
	 * @param ids Collection of ids
	 * @return Collection of names, of the labels associated with the provided ids
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 * @throws ClassNotFoundException Exception that occurs when the database driver
	 * is not found in runtime
	 */
	public Set<String> getLabelsByIds(Collection<Integer> ids)
			throws SQLException, ClassNotFoundException;
	
	/**
	 * Returns a set of name of the labels associated with the provided ids
	 * @param hashCodes Collection of hashCodes
	 * @return Collection of names, of the labels associated with the provided hashCodes
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 * @throws ClassNotFoundException Exception that occurs when the database driver
	 * is not found in runtime
	 */
	public Set<String> getLabelsByHashCodes(Collection<Integer> hashCodes)
			throws SQLException, ClassNotFoundException;

}
