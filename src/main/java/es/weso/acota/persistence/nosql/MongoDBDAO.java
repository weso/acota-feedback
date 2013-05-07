package es.weso.acota.persistence.nosql;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.business.enhancer.FeedbackConfigurable;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.core.exceptions.AcotaPersistenceException;
import es.weso.acota.persistence.DBMS;

/**
 * MongoDB Connection keeper
 * @author César Luis Alvargonzález
 */
public class MongoDBDAO implements FeedbackConfigurable {
	protected FeedbackConfiguration configuration;
	private Mongo connection = null;
	private DB db = null;

	private static MongoDBDAO MONGO_DB_DAO_INSTANCE = null;

	/**
	 * Default Constructor
	 * @param configuration Acota-feedback's configuration class
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 * @throws AcotaConfigurationException Any exception that occurs while initializing 
	 * a Configuration object
	 */
	private MongoDBDAO(FeedbackConfiguration configuration) throws AcotaPersistenceException,
			AcotaConfigurationException {
		super();
		loadConfiguration(configuration);
		try {
			String port = configuration.getDatabasePort();
			port = port.isEmpty() ? DBMS.DB_MONGODB_PORT : port;
			
			connection = new Mongo(configuration.getDatabaseUrl(),
					Integer.valueOf(port));
			db = connection.getDB(configuration.getDatabaseName());
			
			if (!db.isAuthenticated()) {
				db.authenticate(configuration.getDatabaseUser(), configuration
						.getDatabasePassword().toCharArray());
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		} catch (MongoException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		}

	}

	/**
	 * Gets an instance of {@link MongoDBDAO}, 
	 * in the case it does not exists, it will create one,
	 * @param configuration Acota-feedback's configuration class
	 * @return The {@link MongoDBDAO} Instance
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 * @throws AcotaConfigurationException Any exception that occurs while initializing 
	 * a Configuration object
	 */
	public static MongoDBDAO getInstance(FeedbackConfiguration configuration)
			throws AcotaPersistenceException, AcotaConfigurationException {
		if (MONGO_DB_DAO_INSTANCE == null) {
			MongoDBDAO.MONGO_DB_DAO_INSTANCE = new MongoDBDAO(configuration);
		}
		return MONGO_DB_DAO_INSTANCE;
	}

	@Override
	public void loadConfiguration(FeedbackConfiguration configuration)
			throws AcotaConfigurationException {
		this.configuration = configuration != null 
				? configuration : new FeedbackConfiguration();
	}

	public Mongo getConnection() {
		return connection;
	}

	public void setConnection(Mongo connection) {
		this.connection = connection;
	}

	public DB getDb() {
		return db;
	}

	public void setDb(DB db) {
		this.db = db;
	}

}