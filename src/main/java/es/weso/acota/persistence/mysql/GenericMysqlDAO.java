package es.weso.acota.persistence.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.persistence.GenericDAO;

/**
 * Concrete implementation of LabelDAO for the DBMS MySQL 5.x
 * @see GenericDAO
 * @author César Luis Alvargonzález
 *
 */
public abstract class GenericMysqlDAO implements GenericDAO {

	protected FeedbackConfiguration configuration;
	protected String url;

	/**
	 * Zero-argument default constructor
	 * @throws AcotaConfigurationException Any exception that occurs while initializing 
	 * a Configuration object
	 */
	public GenericMysqlDAO() throws AcotaConfigurationException {
		super();
		this.configuration = new FeedbackConfiguration();
		this.url = new StringBuilder("jdbc:mysql://")
				.append(configuration.getDatabaseUrl()).append("/")
				.append(configuration.getDatabaseName()).toString();
	}

	@Override
	public Connection openConnection() throws ClassNotFoundException,
			SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection(url,
				configuration.getDatabaseUser(),
				configuration.getDatabasePassword());
	}

	@Override
	public void closeConnection(Connection con) throws SQLException {
		if (con != null)
			con.close();
	}

	@Override
	public void closeStatement(PreparedStatement ps) throws SQLException {
		if (ps != null)
			ps.close();
	}

	@Override
	public void closeResult(ResultSet rs) throws SQLException {
		if (rs != null)
			rs.close();
	}
}
