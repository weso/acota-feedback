package es.weso.acota.persistence;

import java.sql.SQLException;
import java.util.Set;

import es.weso.acota.core.entity.persistence.Feedback;

/**
 * FeedbackDAO Interface, this interface allows access to a database and
 * make CRD (Create, Read, Delete) operations about feedbacks
 * @author César Luis Alvargonzález
 *
 */
public interface FeedbackDAO extends GenericDAO{
	
	/**
	 * Saves a feedback into the database
	 * @param feedback Feedback to save
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 * @throws ClassNotFoundException Exception that occurs when the database driver
	 * is not found in runtime
	 */
	public void saveFeedback(Feedback feedback) throws SQLException, ClassNotFoundException;
	
	/**
	 * Returns all the feedbacks stored in the database
	 * @return All the feedbacks stored in the database
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 * @throws ClassNotFoundException Exception that occurs when the database driver
	 * is not found in runtime
	 */
	public Set<Feedback> getAllFeedbacks() throws SQLException, ClassNotFoundException;
	
	/**
	 * Returns all the feedbacks where the user, associated to the userId, has taken part
	 * @param userId Id of the user
	 * @return All the feedbacks where the user, associated to the userId, has taken part
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 * @throws ClassNotFoundException Exception that occurs when the database driver
	 * is not found in runtime
	 */
	public Set<Feedback> getFeedbacksByUserId(int userId) throws SQLException, ClassNotFoundException;
	
	/**
	 * Returns all the feedbacks tagged with the provided label
	 * @param label Feedback's label
	 * @return All the feedbacks tagged with the provided label
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 * @throws ClassNotFoundException Exception that occurs when the database driver
	 * is not found in runtime
	 */
	public Set<Feedback> getFeedbacksByLabel(String label) throws SQLException, ClassNotFoundException;
	
	/**
	 * Returns all the feedbacks tagged to an specific document
	 * @param document Feedback's document
	 * @return All the feedbacks tagged to an specific document
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 * @throws ClassNotFoundException Exception that occurs when the database driver
	 * is not found in runtime
	 */
	public Set<Feedback> getFeedbacksByDocument(String document) throws SQLException, ClassNotFoundException;
	
}
