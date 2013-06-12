package es.weso.acota.core.utils.mahout;

import static es.weso.acota.persistence.DBMS.DB_MYSQL;
import static es.weso.acota.persistence.DBMS.DB_MARIADB;
import static es.weso.acota.persistence.DBMS.DB_POSTGRESQL;
import static es.weso.acota.persistence.DBMS.DB_MONGODB;

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
import es.weso.acota.persistence.DBMS;

/**
 * Loads a DataModel for a specific DBMS (MySQL, MariaDB, PostgreSQL or MongoDB)
 * @author César Luis Alvargonzález
 *
 */
public class DataModelUtil {
	
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
			throw new AcotaPersistenceException(e);
		} catch (UnknownHostException e) {
			throw new AcotaPersistenceException(e);
		} catch (MongoException e) {
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
		if(type.equals(DB_MYSQL) || type.equals(DB_MARIADB)){
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
		
		return new MongoDBDataModel(configuration.getDatabaseUrl(), 
				getPort(configuration.getDatabasePort(), DBMS.DB_MONGODB_PORT),
				configuration.getDatabaseName(),
				configuration.getDatabasePrefix()+feedback.getName(),
				true, 
				true, 
				null,
				configuration.getDatabaseUser(),
				configuration.getDatabasePassword(), 
				feedback.getDocumentIdAttribute(),
				feedback.getLabelIdAttribute(), 
				feedback.getPreferenceAttribute(),null);
	}
	
	/**
	 * Returns a MySQL/MariaDB repository of information
	 * @param configuration Acota-feedback's configuration class
	 * @return A MySQL/MariaDB repository of information 
	 */
	protected static DataModel loadMySQLDataModel(FeedbackConfiguration configuration){
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setServerName(configuration.getDatabaseUrl());
		dataSource.setPort(getPort(configuration.getDatabasePort(), DBMS.DB_MYSQL_PORT));
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
		dataSource.setPortNumber(getPort(configuration.getDatabasePort(), DBMS.DB_POSTGRESQL_PORT));
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
	
	/**
	 * Returns the proper PORT for the specific {@link DataModel}.
	 * @param port Port supplied by the configuration system.
	 * @param defaultPort Default port for the specific DBMS
	 * @return Proper port for the {@link DataModel}.
	 */
	protected static int getPort(String port, String defaultPort){
		return Integer.parseInt(port.isEmpty() ? defaultPort : port);
	}
}