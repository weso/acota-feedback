package es.weso.acota.persistence.sql;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.pool.BasePoolableObjectFactory;

import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.business.enhancer.FeedbackConfigurable;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.core.exceptions.AcotaPersistenceException;
import es.weso.acota.persistence.DBMS;
import java.sql.Connection;

public class AcotaPoolableFactory extends BasePoolableObjectFactory<Connection> implements FeedbackConfigurable{
	
	protected FeedbackConfiguration configuration;
	protected String url;
	
	public AcotaPoolableFactory(FeedbackConfiguration configuration) throws AcotaConfigurationException {
		super();
		loadConfiguration(configuration);
	}

	@Override
	public void loadConfiguration(FeedbackConfiguration configuration)
			throws AcotaConfigurationException {
		if(configuration==null)
			configuration = new FeedbackConfiguration();
		this.configuration = configuration;
		this.url = new StringBuilder("jdbc:").append(configuration.getDatabaseType().trim()).append("://")
		.append(configuration.getDatabaseUrl()).append(":").append(getPort(configuration)).append("/")
		.append(configuration.getDatabaseName()).append("?autoReconnectForPools=true").toString();
	}
	
	@Override
    public Connection makeObject() throws Exception {
		try {
		Class.forName(configuration.getDatabaseType().equals("mysql") 
				? "com.mysql.jdbc.Driver":"org.postgresql.Driver");
		return DriverManager.getConnection(url,
				configuration.getDatabaseUser(),
				configuration.getDatabasePassword());
		}catch(SQLException e){
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		}
     }
	
	protected static int getPort(FeedbackConfiguration configuration){
		String port = configuration.getDatabasePort();
		if(port.isEmpty()){
			String type = configuration.getDatabaseType();
			if(type.equals(DBMS.DB_MYSQL)){
				port = DBMS.DB_MYSQL_PORT;
			}else if(type.equals(DBMS.DB_MARIADB))
			{
				port = DBMS.DB_MARIADB_PORT;
			}else if(type.equals(DBMS.DB_POSTGRESQL))
			{
				port = DBMS.DB_POSTGRESQL_PORT;
			}
		}
		return Integer.parseInt(port);
	}
}
