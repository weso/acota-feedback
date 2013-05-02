package es.weso.acota.persistence.nosql;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.entity.persistence.tables.LabelTable;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.core.exceptions.AcotaPersistenceException;
import es.weso.acota.persistence.LabelDAO;

/**
 * Concrete implementation of LabelDAO for the nosql DBMS, MongoDB
 * @see GenericMongoDBDAO
 * @see LabelDAO
 * @author César Luis Alvargonzález
 */
public class LabelMongoDBDAO extends GenericMongoDBDAO implements LabelDAO {
	
	protected String tableName;
	protected String idAttribute;
	protected String nameAttribute;
	protected String timestampAttribute;
	
	protected DB db;
	
	/**
	 * Zero-argument default constructor
	 * @throws AcotaConfigurationException Any exception that occurs while initializing 
	 * a Configuration object
	 * @throws AcotaPersistenceException  An exception that provides information on a
	 * database access error or other errors.
	 */
	public LabelMongoDBDAO() throws AcotaConfigurationException, AcotaPersistenceException{
		super();
		loadConfiguration(configuration);
		this.db = MongoDBDAO.getInstance(configuration).getDb();
	}
	
	/** 
	 * One-argument constructor
	 * @param configuration Acota-feedback's configuration class
	 * @throws AcotaConfigurationException Any exception that occurs while initializing 
	 * a Configuration object
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public LabelMongoDBDAO(FeedbackConfiguration configuration) throws AcotaConfigurationException, AcotaPersistenceException{
		super();
		loadConfiguration(configuration);
		this.db = MongoDBDAO.getInstance(configuration).getDb();
	}
	
	@Override
	public void loadConfiguration(FeedbackConfiguration configuration)
			throws AcotaConfigurationException {
		super.loadConfiguration(configuration);
		
		LabelTable label = this.configuration.getLabelTable();
		this.tableName = this.configuration.getDatabasePrefix()+label.getName();
		this.idAttribute = label.getIdAttribute();
		this.nameAttribute = label.getNameAttribute();
		this.timestampAttribute = label.getTimestampAttribute();
	}
	
	@Override
	public void saveLabel(Integer id, String label)
			throws AcotaPersistenceException {
		DBCollection labels = db.getCollection(tableName);
		
		BasicDBObject query = new BasicDBObject(idAttribute, id);
		query.append(nameAttribute, label);
		query.append(timestampAttribute, new Date());
		
		labels.save(query);
		
	}

	@Override
	public String getLabelById(Integer id) throws AcotaPersistenceException {
		DBCollection labels = db.getCollection(tableName);
		BasicDBObject query = new BasicDBObject(idAttribute, id);
		
		DBCursor cursor = labels.find(query);
		DBObject label = cursor.next();
		
		String value = null;
		if(label!=null)
			value = (String) label.get(nameAttribute);
		return value;
	}

	@Override
	public String getLabelByHash(Integer hashCode)
			throws AcotaPersistenceException {
		return getLabelById(hashCode);
	}

	@Override
	public Set<String> getLabelsByIds(Collection<Integer> ids)
			throws AcotaPersistenceException {
		Set<String> labels = new HashSet<String>();
		List<Integer> hashesList = new LinkedList<Integer>(ids);
		if (!ids.isEmpty()) {
			DBCollection labelsC = db.getCollection(tableName);
				
			BasicDBList or = new BasicDBList();
			for(int i = 0; i < ids.size(); i++){
				or.add(new BasicDBObject(idAttribute, hashesList.get(i)));
			}
			BasicDBObject query = new BasicDBObject("$or", or);
				
			DBCursor cursor = labelsC.find(query);
				
			DBObject label = null;
			
			while(cursor.hasNext()){
				label = cursor.next();
				labels.add((String) label.get(nameAttribute));
			}
		}
		return labels;
	}

	@Override
	public Set<String> getLabelsByHashCodes(Collection<Integer> hashCodes)
			throws AcotaPersistenceException {
		return getLabelsByIds(hashCodes);
	}

}
