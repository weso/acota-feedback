package es.weso.acota.persistence;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

/**
 * DocumentDAO Interface, this interface allows access to a database and
 * make CRD (Create, Read, Delete) operations about documents
 * @author César Luis Alvargonzález
 *
 */
public interface DocumentDAO extends GenericDAO{
	
	/**
	 * Saves a document into the database
	 * @param id Document's id, id is the url's hashCode
	 * @param url Document's URL
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 * @throws ClassNotFoundException Exception that occurs when the database driver
	 * is not found in runtime
	 */
	public void saveDocument(Integer id, String url) throws SQLException,
	ClassNotFoundException;
	
	/**
	 * Returns the URL of the document associated with the provided id
	 * @param id Id of the document
	 * @return The URL of the document associated with the provided id
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 * @throws ClassNotFoundException Exception that occurs when the database driver
	 * is not found in runtime
	 */
	public String getDocumentById(Integer id) throws SQLException,
			ClassNotFoundException;

	/**
	 * Returns the URL of the document associated with the provided hashCode
	 * @param hash hashCode of the document
	 * @return The URL of the document associated with the provided hashCode
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 * @throws ClassNotFoundException Exception that occurs when the database driver
	 * is not found in runtime
	 */
	public String getDocumentByHashCode(Integer hash) throws SQLException,
			ClassNotFoundException;

	/**
	 * Returns a set of URL of the documents associated with the provided ids
	 * @param ids Collection of ids
	 * @return Collection of URL of the documents associated with the provided ids
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 * @throws ClassNotFoundException Exception that occurs when the database driver
	 * is not found in runtime
	 */
	public Set<String> getDocumentsByIds(Collection<Integer> ids)
			throws SQLException, ClassNotFoundException;
	
	/**
	 * Returns a set of URL of the documents associated with the provided hashCodes
	 * @param hashses Collection of hashCodes
	 * @return Collection of URL of the documents associated with the provided ids
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 * @throws ClassNotFoundException Exception that occurs when the database driver
	 * is not found in runtime
	 */
	public Set<String> getDocumentsByHashCodes(Collection<Integer> hashses)
			throws SQLException, ClassNotFoundException;
}
