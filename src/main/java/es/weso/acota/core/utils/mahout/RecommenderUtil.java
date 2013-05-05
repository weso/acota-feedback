package es.weso.acota.core.utils.mahout;

import java.io.IOException;
import java.util.Map;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.mongodb.MongoDBDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.business.enhancer.FeedbackConfigurable;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.core.exceptions.AcotaPersistenceException;
import es.weso.acota.persistence.nosql.MongoDBDAO;

/**
 * 
 * @author César Luis Alvargonzález
 * 
 */
public class RecommenderUtil implements FeedbackConfigurable {

	private static final String DATA_SUFFIX = "_data";
	private FeedbackConfiguration configuration;
	protected String feedbackTableName;
	protected DB db;
	protected DBCollection feedbacks;

	protected DataModel dataModel;
	protected MongoDBDataModel mongoDataModel;

	public RecommenderUtil(FeedbackConfiguration configuration)
			throws AcotaPersistenceException, AcotaConfigurationException {
		super();
		loadConfiguration(configuration);
		this.db = MongoDBDAO.getInstance(this.configuration).getDb();
		this.feedbacks = db.getCollection(feedbackTableName);
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
	 * 
	 * @return ItemBasedRecommender Interface implemented by "item-based"
	 *         recommenders.
	 * @throws IOException
	 *             Signals that an I/O exception of some sort has occurred.
	 * @throws AcotaPersistenceException
	 * @throws TasteException
	 */
	public ItemBasedRecommender loadRecommender() throws IOException,
			AcotaPersistenceException, TasteException {
		this.dataModel = DataModelUtil.loadDataModel(configuration);
		this.mongoDataModel = !MongoDBDataModel.class
				.isInstance(this.dataModel) ? null : (MongoDBDataModel) dataModel;
		ItemSimilarity itemSimilarity = new TanimotoCoefficientSimilarity(
				dataModel);
		return new GenericItemBasedRecommender(dataModel, itemSimilarity);
	}

	/**
	 * 
	 * @param label
	 * @return
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
	 * 
	 * @param id
	 * @return
	 */
	public int fromLongToId(long id) {
		if (mongoDataModel != null) {
			DBObject objectIdLong = feedbacks.findOne(new BasicDBObject(
					"long_value", Long.toString(id)));
			Map<String, Object> idLong = (Map<String, Object>) objectIdLong
					.toMap();
			Object value = idLong.get("element_id");
			return value == null ? null : Integer.valueOf(value.toString());
		}
		return (int) id;
	}

}
