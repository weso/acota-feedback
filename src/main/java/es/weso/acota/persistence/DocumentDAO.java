package es.weso.acota.persistence;

import java.util.Collection;
import java.util.Set;

import es.weso.acota.core.business.enhancer.FeedbackConfigurable;
import es.weso.acota.core.exceptions.AcotaPersistenceException;

/**
 * DocumentDAO Interface, this interface allows access to a database and
 * to perform CR (Create, Read) operations about documents
 * @author César Luis Alvargonzález
 *
 */
public interface DocumentDAO extends GenericDAO, FeedbackConfigurable{
	
	/**
	 * Saves a document into the database
	 * @param id Document's id, id is the url's hashCode
	 * @param url Document's URL
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public void saveDocument(Integer id, String url) throws AcotaPersistenceException;
	
	/**
	 * Returns the URL of the document associated with the provided id
	 * @param id Id of the document
	 * @return The URL of the document associated with the provided id
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public String getDocumentById(Integer id) throws AcotaPersistenceException;

	/**
	 * Returns the URL of the document associated with the provided hashCode
	 * @param hash hashCode of the document
	 * @return The URL of the document associated with the provided hashCode
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public String getDocumentByHashCode(Integer hash) throws AcotaPersistenceException;

	/**
	 * Returns a set of URL of the documents associated with the provided ids
	 * @param ids Collection of ids
	 * @return Collection of URL of the documents associated with the provided ids
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public Set<String> getDocumentsByIds(Collection<Integer> ids) throws AcotaPersistenceException;
	
	/**
	 * Returns a set of URL of the documents associated with the provided hashCodes
	 * @param hashses Collection of hashCodes
	 * @return Collection of URL of the documents associated with the provided ids
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public Set<String> getDocumentsByHashCodes(Collection<Integer> hashses) throws AcotaPersistenceException;
}
