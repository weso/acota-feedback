package es.weso.acota.core.utils.mahout;


import java.net.UnknownHostException;

import org.apache.mahout.cf.taste.impl.model.jdbc.ConnectionPoolDataSource;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.PostgreSQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.mongodb.MongoDBDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.postgresql.ds.PGSimpleDataSource;

import com.mongodb.MongoException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.entity.persistence.tables.FeedbackTable;
import es.weso.acota.core.exceptions.AcotaPersistenceException;

/**
 * Loads a DataModel for the specific DBMS (MySQL, PostgreSQL or MongoDB=
 * @author César Luis Alvargonzález
 *
 */
public class DataModelUtil {
	
	public static final String DB_MYSQL = "mysql";
	public static final String DB_POSTGRESQL = "postgresql";
	public static final String DB_MONGODB = "mongodb";
	
	/**
	 * Returns a repository of information for the default DBMS
	 * @param configuration Acota-feedback's configuration class
	 * @return A repository of information 
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public static DataModel loadDataModel(FeedbackConfiguration configuration) throws AcotaPersistenceException{
		try {
			return selectDataModel(configuration);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		} catch (MongoException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		}
	}

	/**
	 * Returns a repository of information for the default DBMS
	 * @param configuration Acota-feedback's configuration class
	 * @return A repository of information 
	 * @throws NumberFormatException Thrown to indicate that the application has attempted to convert a string 
	 * to one of the numeric types, but that the string does not have the appropriate format.
	 * @throws UnknownHostException Thrown to indicate that the IP address of a host could not be determined.
	 * @throws MongoException A general exception raised in Mongo
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	protected static DataModel selectDataModel(FeedbackConfiguration configuration) throws NumberFormatException, UnknownHostException, MongoException, AcotaPersistenceException{
		String type = configuration.getDatabaseType();
		if(type.equals(DB_MYSQL)){
				return loadMySQLDataModel(configuration);
		}else if(type.equals(DB_POSTGRESQL)){
				return loadPostgreSQLModel(configuration);
		}else if(type.equals(DB_MONGODB)){
				return loadMongoDBDataModel(configuration);
		}
		throw new AcotaPersistenceException("Wrong Database Type");
	}
	
	/**
	 * Returns a MongoDB repository of information 
	 * @param configuration Acota-feedback's configuration class
	 * @return A MongoDB repository of information 
	 * @throws NumberFormatException Thrown to indicate that the application has attempted to convert a string 
	 * to one of the numeric types, but that the string does not have the appropriate format.
	 * @throws UnknownHostException Thrown to indicate that the IP address of a host could not be determined.
	 * @throws MongoException A general exception raised in Mongo
	 */
	protected static DataModel loadMongoDBDataModel(FeedbackConfiguration configuration) throws NumberFormatException, UnknownHostException, MongoException{
		FeedbackTable feedback = configuration.getFeedbackTable();
		return new MongoDBDataModel(configuration.getDatabaseUrl(),Integer.valueOf(configuration.getDatabasePort()),
				configuration.getDatabaseName(),configuration.getDatabasePrefix()+feedback.getName(),
				true, true, null);
	}
	
	/**
	 * Returns a MySQL repository of information
	 * @param configuration Acota-feedback's configuration class
	 * @return A MySQL repository of information 
	 */
	protected static DataModel loadMySQLDataModel(FeedbackConfiguration configuration){
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setServerName(configuration.getDatabaseUrl());
		dataSource.setPort(Integer.parseInt(configuration.getDatabasePort()));
		dataSource.setUser(configuration.getDatabaseUser());
		dataSource.setPassword(configuration.getDatabasePassword());
		dataSource.setDatabaseName(configuration.getDatabaseName());

		ConnectionPoolDataSource data = new ConnectionPoolDataSource(dataSource);
		
		FeedbackTable feedback = configuration.getFeedbackTable();
		return new MySQLJDBCDataModel(data,
				configuration.getDatabasePrefix()+feedback.getName(), feedback.getDocumentIdAttribute(),
				feedback.getLabelIdAttribute(), feedback.getPreferenceAttribute(),
				feedback.getTimestampAttribute());
	}
	
	/**
	 * Returns a PostgreSQL repository of information 
	 * @param configuration Acota-feedback's configuration class
	 * @return A PostgreSQL repository of information 
	 */
	protected static DataModel loadPostgreSQLModel(FeedbackConfiguration configuration){
		PGSimpleDataSource dataSource = new PGSimpleDataSource();
		dataSource.setServerName(configuration.getDatabaseUrl());
		dataSource.setPortNumber(Integer.parseInt(configuration.getDatabasePort()));
		dataSource.setUser(configuration.getDatabaseUser());
		dataSource.setPassword(configuration.getDatabasePassword());
		dataSource.setDatabaseName(configuration.getDatabaseName());
		
		ConnectionPoolDataSource data = new ConnectionPoolDataSource(dataSource);
		
		FeedbackTable feedback = configuration.getFeedbackTable();
		return new PostgreSQLJDBCDataModel(data,
				configuration.getDatabasePrefix()+feedback.getName(), feedback.getDocumentIdAttribute(),
				feedback.getLabelIdAttribute(), feedback.getPreferenceAttribute(),
				feedback.getTimestampAttribute());
	}
}