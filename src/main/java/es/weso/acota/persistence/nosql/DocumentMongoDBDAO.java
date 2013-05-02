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
import es.weso.acota.core.entity.persistence.tables.DocumentTable;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.core.exceptions.AcotaPersistenceException;
import es.weso.acota.persistence.DocumentDAO;
import es.weso.acota.persistence.sql.GenericSQLDAO;

/**
 * Concrete implementation of DocumentDAO for MongoDB DBMS
 * @see GenericSQLDAO
 * @see DocumentDAO
 * @author César Luis Alvargonzález
 *
 */
public class DocumentMongoDBDAO extends GenericMongoDBDAO implements DocumentDAO {
	
	protected String tableName;
	protected String idAttribute;
	protected String nameAttribute;
	protected String timestampAttribute;
	
	protected DB db;
	
	/**
	 * Zero-argument default constructor
	 * @throws AcotaConfigurationException Any exception that occurs while initializing 
	 * a Configuration object
	 */
	public DocumentMongoDBDAO() throws AcotaConfigurationException{
		super();
		loadConfiguration(configuration);
	}
	
	/** 
	 * One-argument constructor
	 * @param configuration Acota-feedback's configuration class
	 * @throws AcotaConfigurationException Any exception that occurs while initializing 
	 * a Configuration object
	 * @throws AcotaPersistenceException 
	 */
	public DocumentMongoDBDAO(FeedbackConfiguration configuration) throws AcotaConfigurationException, AcotaPersistenceException{
		super();
		loadConfiguration(configuration);
		this.db = MongoDBDAO.getInstance(configuration).getDb();
	}
	@Override
	public void loadConfiguration(FeedbackConfiguration configuration)
			throws AcotaConfigurationException {
		super.loadConfiguration(configuration);
		
		DocumentTable label = this.configuration.getDocumentTable();
		this.tableName = this.configuration.getDatabasePrefix()+label.getName();
		this.idAttribute = label.getIdAttribute();
		this.nameAttribute = label.getNameAttribute();
		this.timestampAttribute = label.getTimestampAttribute();
	}
	
	@Override
	public void saveDocument(Integer id, String uri)
			throws AcotaPersistenceException {
		DBCollection documents = db.getCollection(tableName);
		
		BasicDBObject query = new BasicDBObject(idAttribute, id);
		query.append(nameAttribute, uri);
		query.append(timestampAttribute, new Date());
		
		documents.save(query);
	}
	
	@Override
	public String getDocumentById(Integer id) throws AcotaPersistenceException {
		DBCollection documents = db.getCollection(tableName);
		BasicDBObject query = new BasicDBObject(idAttribute, id);
		
		DBCursor cursor = documents.find(query);
		DBObject document = cursor.next();
		
		String value = null;
		if(document!=null)
			value = (String) document.get(nameAttribute);
		return value;
	}

	@Override
	public String getDocumentByHashCode(Integer hashCode)
			throws AcotaPersistenceException {
		return getDocumentById(hashCode);
	}

	@Override
	public Set<String> getDocumentsByIds(Collection<Integer> ids)
			throws AcotaPersistenceException {
		Set<String> documents = new HashSet<String>();
		List<Integer> hashesList = new LinkedList<Integer>(ids);
		if (!ids.isEmpty()) {
			DBCollection documentsC = db.getCollection(tableName);
				
			BasicDBList or = new BasicDBList();
			for(int i = 0; i < ids.size(); i++){
				or.add(new BasicDBObject(idAttribute, hashesList.get(i)));
			}
			BasicDBObject query = new BasicDBObject("$or", or);
				
			DBCursor cursor = documentsC.find(query);
				
			DBObject document = null;
			
			while(cursor.hasNext()){
				document = cursor.next();
				documents.add((String) document.get(nameAttribute));
			}
		}
		return documents;
	}

	@Override
	public Set<String> getDocumentsByHashCodes(Collection<Integer> hashCodes)
			throws AcotaPersistenceException {
		return getDocumentsByIds(hashCodes);
	}

}
