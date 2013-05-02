package es.weso.acota.persistence;

import java.util.Set;

import es.weso.acota.core.business.enhancer.FeedbackConfigurable;
import es.weso.acota.core.entity.persistence.Feedback;
import es.weso.acota.core.exceptions.AcotaPersistenceException;

/**
 * FeedbackDAO Interface, this interface allows access to a database and
 * to perform CR (Create, Read) operations about feedbacks
 * @author César Luis Alvargonzález
 *
 */
public interface FeedbackDAO extends GenericDAO, FeedbackConfigurable{
	
	/**
	 * Saves a feedback into the database
	 * @param feedback Feedback to save
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public void saveFeedback(Feedback feedback) throws AcotaPersistenceException;
	
	/**
	 * Returns all the feedbacks stored in the database
	 * @return All the feedbacks stored in the database
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public Set<Feedback> getAllFeedbacks() throws AcotaPersistenceException;
	
	/**
	 * Returns all the feedbacks where the user, associated to the userId, has taken part
	 * @param userId Id of the user
	 * @return All the feedbacks where the user, associated to the userId, has taken part
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public Set<Feedback> getFeedbacksByUserId(int userId) throws AcotaPersistenceException;
	
	/**
	 * Returns all the feedbacks tagged with the provided label
	 * @param label Feedback's label
	 * @return All the feedbacks tagged with the provided label
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public Set<Feedback> getFeedbacksByLabel(String label) throws AcotaPersistenceException;
	
	/**
	 * Returns all the feedbacks tagged to an specific document
	 * @param document Feedback's document
	 * @return All the feedbacks tagged to an specific document
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public Set<Feedback> getFeedbacksByDocument(String document) throws AcotaPersistenceException;
	
}
