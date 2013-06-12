package es.weso.acota.core.utils.mahout;

import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.mongodb.MongoDBDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.business.enhancer.FeedbackConfigurable;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.core.exceptions.AcotaPersistenceException;
import es.weso.acota.persistence.DBMS;
import es.weso.acota.persistence.nosql.MongoDBDAO;

/**
 * RecommenderUtils performs peripheral recommender tasks.
 * @author César Luis Alvargonzález
 * @since 0.3.8
 */
public class RecommenderUtil implements FeedbackConfigurable {

	private static final String DATA_SUFFIX = "_data";
	private FeedbackConfiguration configuration;
	protected String feedbackTableName;
	protected DB db;
	protected DBCollection feedbacks;

	protected DataModel dataModel;
	protected MongoDBDataModel mongoDataModel;

	/**
	 * One-argument default constructor
	 * @param configuration Acota-feedback's configuration object
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 * @throws AcotaConfigurationException Any exception that 
	 * occurs while initializing a Configuration object
	 */
	public RecommenderUtil(FeedbackConfiguration configuration)
			throws AcotaPersistenceException, AcotaConfigurationException {
		super();
		loadConfiguration(configuration);
		if(this.configuration.getDatabaseType().equals(DBMS.DB_MONGODB)){
			this.db = MongoDBDAO.getInstance(this.configuration).getDb();
			this.feedbacks = db.getCollection(feedbackTableName);
		}
	}

	@Override
	public void loadConfiguration(FeedbackConfiguration configuration)
			throws AcotaConfigurationException {
		if (configuration != null) {
			configuration = new FeedbackConfiguration();
		}
		this.configuration = configuration;
		this.feedbackTableName = this.configuration.getFeedbackTable()
				.getName() + DATA_SUFFIX;
	}

	/**
	 * Loads into memory the Mahout Recommender
	 * @return ItemBasedRecommender Interface implemented by "item-based"
	 *         recommenders.
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 * @throws AcotaPersistenceException An exception that occurs while performing persistence
	 * @throws TasteException An exception thrown when an error occurs inside the Taste engine.
	 */
	public GenericItemBasedRecommender loadRecommender() throws IOException,
			AcotaPersistenceException, TasteException {
		this.dataModel = DataModelUtil.loadDataModel(configuration);
		this.mongoDataModel = !MongoDBDataModel.class
				.isInstance(this.dataModel) ? null : (MongoDBDataModel) dataModel;
		ItemSimilarity itemSimilarity = new TanimotoCoefficientSimilarity(
				dataModel);
		return new GenericItemBasedRecommender(dataModel, itemSimilarity);
	}

	/**
	 * Translates the String to Mahout/MongoDBDataModel's internal
	 * identifier, if required.
	 * @param label String to get translated
	 * @return Mahout/MongoDBDataModel's internal identifier
	 */
	public long fromIdToLong(String label) {
		Long code = new Long(label.hashCode());
		if (mongoDataModel != null) {
			String value = mongoDataModel.fromIdToLong(code.toString(), false);
			code = new Long(value);
		}
		return code;
	}

	/**
	 * Translates the Mahout/MongoDBDataModel's internal identifier to MongoDB
	 * identifier, if required.
	 * @param id Mahout's internal identifier
	 * @return ID to of the external MongoDB ID (mapping).
	 */
	public int fromLongToId(long id) {
		if (mongoDataModel != null) {
			DBObject objectIdLong = feedbacks.findOne(new BasicDBObject(
					"long_value", Long.toString(id)));
			Object value = objectIdLong.toMap().get("element_id");
			return value == null ? null : Integer.valueOf(value.toString());
		}
		return (int) id;
	}

}
