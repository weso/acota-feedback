package es.weso.acota.persistence.sql.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;

import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.persistence.DBMS;

/**
 * 
 * SQLDAO (MySQL) Unit Test Helper
 * @author César Luis Alvargonzález
 *
 */
public class MySLQDAOTest {
	
	@Before
	public void init() throws DatabaseUnitException, SQLException, Exception{
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getDataSet());
	}
	
	@After
	public void after() throws Exception {
		DatabaseOperation.DELETE_ALL.execute(getConnection(), getDataSet());
	}

	protected IDatabaseConnection getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		FeedbackConfiguration configuration = new FeedbackConfiguration();
		String port = configuration.getDatabasePort();
		port = port.isEmpty() ? DBMS.DB_MYSQL_PORT : port;
		Connection jdbcConnection = DriverManager.getConnection(
				"jdbc:"+DBMS.DB_MYSQL+"://"+ configuration.getDatabaseUrl() + ":"+ port+"/"+configuration.getDatabaseName(), configuration.getDatabaseUser(), configuration.getDatabasePassword());
		
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
		
		DatabaseConfig dbConfig = connection.getConfig();
		dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
		
		return connection;
	}

	protected DatabaseOperation getSetUpOperation() throws Exception {
		return DatabaseOperation.REFRESH;
	}

	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(this.getClass().getResource(
				"/dbunit.xml"));
	}
}
