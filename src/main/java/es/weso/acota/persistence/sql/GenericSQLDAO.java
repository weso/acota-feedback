package es.weso.acota.persistence.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.core.exceptions.AcotaPersistenceException;
import es.weso.acota.persistence.DBMS;
import es.weso.acota.persistence.GenericDAO;

/**
 * Concrete implementation of LabelDAO for the Relational DBMSs
 * @see GenericDAO
 * @author César Luis Alvargonzález
 */
public abstract class GenericSQLDAO implements GenericDAO {

	protected FeedbackConfiguration configuration;
	protected String url;

	/**
	 * Zero-argument default constructor
	 * @throws AcotaConfigurationException Any exception that occurs while initializing 
	 * a Configuration object
	 */
	public GenericSQLDAO() throws AcotaConfigurationException {
		super();
	}

	@Override
	public void loadConfiguration(FeedbackConfiguration configuration)
			throws AcotaConfigurationException {
		if(configuration==null)
			configuration = new FeedbackConfiguration();
		this.configuration = configuration;
		this.url = new StringBuilder("jdbc:").append(configuration.getDatabaseType().trim()).append("://")
		.append(configuration.getDatabaseUrl()).append(":").append(getPort(configuration)).append("/")
		.append(configuration.getDatabaseName()).toString();
	}
	
	/**
	 * Opens a Databases's {@link Connection}
	 * @return Databases's {@link Connection}
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public Connection openConnection() throws AcotaPersistenceException {
		try{
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

	/**
	 * Closes a Databases's {@link Connection}
	 * @param con Databases's {@link Connection} to close
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public void closeConnection(Connection con) throws AcotaPersistenceException {
		try{
		if (con != null)
			con.close();
		}catch(SQLException e){
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		}
	}

	/**
	 * Closes a {@link PreparedStatement}
	 * @param ps {@link PreparedStatement} to close
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public void closeStatement(PreparedStatement ps) throws AcotaPersistenceException{
		try{
		if (ps != null)
			ps.close();
		}catch(SQLException e){
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		}
	}

	/**
	 * Closes a {@link ResultSet}
	 * @param rs {@link ResultSet} to close
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 */
	public void closeResult(ResultSet rs) throws AcotaPersistenceException {
		try{
			if (rs != null)
				rs.close();
		}catch(SQLException e){
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
