package es.weso.acota.persistence.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.apache.commons.pool.impl.GenericObjectPoolFactory;

import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.core.exceptions.AcotaPersistenceException;
import es.weso.acota.persistence.DBMS;
import es.weso.acota.persistence.GenericDAO;

/**
 * Concrete implementation of LabelDAO for the Relational DBMSs
 * 
 * @see GenericDAO
 * @author César Luis Alvargonzález
 */
public abstract class GenericSQLDAO implements GenericDAO {

	protected FeedbackConfiguration configuration;
	protected String url;

	private static ObjectPool<Connection> connPool;

	/**
	 * Zero-argument default constructor
	 * 
	 * @param configuration
	 * 
	 * @throws AcotaConfigurationException
	 *             Any exception that occurs while initializing a Configuration
	 *             object
	 */
	public GenericSQLDAO(FeedbackConfiguration configuration)
			throws AcotaConfigurationException {
		loadConfiguration(configuration);
		if (connPool != null) {
				try {
					GenericSQLDAO.connPool.close();
				} catch (Exception e) {
					e.printStackTrace();
					throw new AcotaConfigurationException(e);
				}
		}
		GenericSQLDAO.connPool = initObjectPool(this.configuration);
	}

	@Override
	public void loadConfiguration(FeedbackConfiguration configuration)
			throws AcotaConfigurationException {
		if (configuration == null)
			configuration = new FeedbackConfiguration();
		this.configuration = configuration;

	}

	/**
	 * Opens a Databases's {@link Connection}
	 * 
	 * @return Databases's {@link Connection}
	 * @throws AcotaPersistenceException
	 *             An exception that provides information on a database access
	 *             error or other errors.
	 */
	public Connection openConnection() throws AcotaPersistenceException {
		try {
			return connPool.borrowObject();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		}
	}

	/**
	 * Closes a Databases's {@link Connection}
	 * 
	 * @param conn
	 *            Databases's {@link Connection} to close
	 * @throws AcotaPersistenceException
	 *             An exception that provides information on a database access
	 *             error or other errors.
	 */
	public void closeConnection(Connection conn)
			throws AcotaPersistenceException {
		try {
			if (conn != null)
				connPool.returnObject(conn);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		}
	}

	/**
	 * Closes a {@link Statement}
	 * 
	 * @param st
	 *            {@link Statement} to close
	 * @throws AcotaPersistenceException
	 *             An exception that provides information on a database access
	 *             error or other errors.
	 */
	public void closeStatement(Statement st) throws AcotaPersistenceException {
		try {
			if (st != null)
				st.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		}
	}

	/**
	 * Closes a {@link ResultSet}
	 * 
	 * @param rs
	 *            {@link ResultSet} to close
	 * @throws AcotaPersistenceException
	 *             An exception that provides information on a database access
	 *             error or other errors.
	 */
	public void closeResult(ResultSet rs) throws AcotaPersistenceException {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		}
	}

	/**
	 * Initializes a Connection Pool
	 * 
	 * @param configuration
	 *            Acota's Feedback Configuration
	 * @return Connection Pool
	 * @throws AcotaConfigurationException
	 */
	private ObjectPool<Connection> initObjectPool(
			FeedbackConfiguration configuration)
			throws AcotaConfigurationException {
		PoolableObjectFactory<Connection> polableObjectFactory = new AcotaPoolableFactory(
				configuration);
		Config config = new GenericObjectPool.Config();
		config.maxActive = 10;
		config.testOnBorrow = true;
		config.testWhileIdle = true;
		config.timeBetweenEvictionRunsMillis = 10000;
		config.minEvictableIdleTimeMillis = 60000;

		GenericObjectPoolFactory<Connection> genericObjectPoolFactory = new GenericObjectPoolFactory<Connection>(
				polableObjectFactory, config);
		return genericObjectPoolFactory.createPool();
	}

	/**
	 * Loads the proper port for the DBMSs
	 * 
	 * @param configuration
	 *            Acota's Feedback Configuration
	 * @return Port of the DBMSs
	 */
	protected static int getPort(FeedbackConfiguration configuration) {
		String port = configuration.getDatabasePort();
		if (port.isEmpty()) {
			String type = configuration.getDatabaseType();
			if (type.equals(DBMS.DB_MYSQL)) {
				port = DBMS.DB_MYSQL_PORT;
			} else if (type.equals(DBMS.DB_MARIADB)) {
				port = DBMS.DB_MARIADB_PORT;
			} else if (type.equals(DBMS.DB_POSTGRESQL)) {
				port = DBMS.DB_POSTGRESQL_PORT;
			}
		}
		return Integer.parseInt(port);
	}

}
