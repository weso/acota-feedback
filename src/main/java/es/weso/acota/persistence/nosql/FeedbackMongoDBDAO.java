package es.weso.acota.persistence.nosql;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.entity.persistence.Feedback;
import es.weso.acota.core.entity.persistence.tables.DocumentTable;
import es.weso.acota.core.entity.persistence.tables.FeedbackTable;
import es.weso.acota.core.entity.persistence.tables.LabelTable;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.core.exceptions.AcotaPersistenceException;
import es.weso.acota.persistence.FeedbackDAO;

/**
 * Concrete implementation of FeedbackDAO for MongoDB DBMS
 * @see GenericMongoDBDAO
 * @see FeedbackDAO
 * @author César Luis Alvargonzález
 * @since 0.3.8
 */
public class FeedbackMongoDBDAO extends GenericMongoDBDAO implements
		FeedbackDAO {

	protected String documentTableName;
	protected String documentIdAttribute;
	protected String documentNameAttribute;

	protected String feedbackTableName;
	protected String feedbackIdAttribute;
	protected String feedbackUserIdAttribute;
	protected String feedbackDocumentIdAttribute;
	protected String feedbackLabelIdAttribute;
	protected String feedbackPreferenceAttribute;
	protected String feedbackTimestampAttribute;

	protected String labelTableName;
	protected String labelIdAttribute;
	protected String labelNameAttribute;

	protected DB db;
	protected LabelMongoDBDAO labelDAO;
	protected DocumentMongoDBDAO documentDAO;

	/**
	 * Zero-argument default constructor
	 * 
	 * @throws AcotaConfigurationException
	 *             Any exception that occurs while initializing a Configuration
	 *             object
	 * @throws AcotaPersistenceException 
	 */
	public FeedbackMongoDBDAO() throws AcotaConfigurationException, AcotaPersistenceException {
		super();
		loadConfiguration(configuration);
		this.db = MongoDBDAO.getInstance(configuration).getDb();
		
	}

	/**
	 * One-argument constructor
	 * 
	 * @param configuration
	 *            Acota-feedback's this.configuration class
	 * @throws AcotaConfigurationException
	 *             Any exception that occurs while initializing a Configuration
	 *             object
	 * @throws AcotaPersistenceException 
	 */
	public FeedbackMongoDBDAO(FeedbackConfiguration configuration)
			throws AcotaConfigurationException, AcotaPersistenceException {
		super();
		loadConfiguration(this.configuration);
		this.db = MongoDBDAO.getInstance(this.configuration).getDb();
	}

	@Override
	public void loadConfiguration(FeedbackConfiguration configuration)
			throws AcotaConfigurationException {
		super.loadConfiguration(this.configuration);

		DocumentTable document = this.configuration.getDocumentTable();
		this.documentTableName = this.configuration.getDatabasePrefix()
				+ document.getName();
		this.documentIdAttribute = document.getIdAttribute();
		this.documentNameAttribute = document.getNameAttribute();

		FeedbackTable feedback = this.configuration.getFeedbackTable();
		this.feedbackTableName = this.configuration.getDatabasePrefix()
				+ feedback.getName();
		this.feedbackIdAttribute = feedback.getIdAttribute();
		this.feedbackUserIdAttribute = feedback.getUserIdAttribute();
		this.feedbackDocumentIdAttribute = feedback.getDocumentIdAttribute();
		this.feedbackLabelIdAttribute = feedback.getLabelIdAttribute();
		this.feedbackPreferenceAttribute = feedback.getPreferenceAttribute();
		this.feedbackTimestampAttribute = feedback.getTimestampAttribute();

		LabelTable label = this.configuration.getLabelTable();
		this.labelTableName = this.configuration.getDatabasePrefix()
				+ label.getName();
		this.labelIdAttribute = label.getIdAttribute();
		this.labelNameAttribute = label.getNameAttribute();
	}

	@Override
	public void saveFeedback(Feedback feedback)
			throws AcotaPersistenceException {
		DBCollection feedbacks = db.getCollection(feedbackTableName);

		BasicDBObject query = new BasicDBObject(feedbackIdAttribute,
				feedback.getId());
		query.append(feedbackUserIdAttribute, feedback.getUserId());
		query.append(feedbackDocumentIdAttribute, new Long(feedback.getDocument().hashCode()));
		query.append(feedbackLabelIdAttribute, new Long(feedback.getLabel().hashCode()));
		query.append(feedbackPreferenceAttribute, feedback.getPreference());
		query.append(feedbackTimestampAttribute, new Date());

		feedbacks.save(query);
	}

	@Override
	public Set<Feedback> getAllFeedbacks() throws AcotaPersistenceException {
		Set<Feedback> feedbacks = new HashSet<Feedback>();
		DBCollection feedbacksC = db.getCollection(feedbackTableName);
		DBCursor cursor = feedbacksC.find();

		DBObject feedback = null;
		while (cursor.hasNext()) {
			feedback = cursor.next();

			String document = documentDAO.getDocumentById((Integer) feedback
					.get(feedbackDocumentIdAttribute));
			
			String label = labelDAO.getLabelById((Integer) feedback
					.get(feedbackDocumentIdAttribute));

			feedbacks.add(new Feedback(((Integer) feedback
					.get(feedbackIdAttribute)), ((Integer) feedback
					.get(feedbackUserIdAttribute)), document, label,
					((Date) feedback.get(feedbackTimestampAttribute)))

			);
		}
		return feedbacks;
	}

	@Override
	public Set<Feedback> getFeedbacksByUserId(int userId) throws AcotaPersistenceException{
		Set<Feedback> feedbacks = new HashSet<Feedback>();
		DBCollection feedbacksC = db.getCollection(feedbackTableName);
		BasicDBObject query = new BasicDBObject(feedbackUserIdAttribute, userId);
		DBCursor cursor = feedbacksC.find(query);
	
		DBObject feedback = null;
		while (cursor.hasNext()) {
			feedback = cursor.next();
	
			String document = documentDAO.getDocumentById((Integer) feedback
					.get(feedbackDocumentIdAttribute));
			
			String label = labelDAO.getLabelById((Integer) feedback
					.get(feedbackDocumentIdAttribute));
	
			feedbacks.add(new Feedback(((Integer) feedback
					.get(feedbackIdAttribute)), ((Integer) feedback
					.get(feedbackUserIdAttribute)), document, label,
					((Date) feedback.get(feedbackTimestampAttribute)))
	
			);
		}
		return feedbacks;
	}

	@Override
	public Set<Feedback> getFeedbacksByLabel(String label)
			throws AcotaPersistenceException {
		Set<Feedback> feedbacks = new HashSet<Feedback>();
		DBCollection feedbacksC = db.getCollection(feedbackTableName);
		BasicDBObject query = new BasicDBObject(feedbackLabelIdAttribute, label.hashCode());
		DBCursor cursor = feedbacksC.find(query);
	
		DBObject feedback = null;
		while (cursor.hasNext()) {
			feedback = cursor.next();
	
			String document = documentDAO.getDocumentById((Integer) feedback
					.get(feedbackDocumentIdAttribute));
	
			feedbacks.add(new Feedback(((Integer) feedback
					.get(feedbackIdAttribute)), ((Integer) feedback
					.get(feedbackUserIdAttribute)), document, label,
					((Date) feedback.get(feedbackTimestampAttribute)))
	
			);
		}
		return feedbacks;
	}

	@Override
	public Set<Feedback> getFeedbacksByDocument(String document)
			throws AcotaPersistenceException {
		Set<Feedback> feedbacks = new HashSet<Feedback>();
		DBCollection feedbacksC = db.getCollection(feedbackTableName);
		BasicDBObject query = new BasicDBObject(feedbackUserIdAttribute, document.hashCode());
		DBCursor cursor = feedbacksC.find(query);
	
		DBObject feedback = null;
		while (cursor.hasNext()) {
			feedback = cursor.next();
			
			String label = labelDAO.getLabelById((Integer) feedback
					.get(feedbackDocumentIdAttribute));
	
			feedbacks.add(new Feedback(((Integer) feedback
					.get(feedbackIdAttribute)), ((Integer) feedback
					.get(feedbackUserIdAttribute)), document, label,
					((Date) feedback.get(feedbackTimestampAttribute)))
	
			);
		}
		return feedbacks;
	}

}
