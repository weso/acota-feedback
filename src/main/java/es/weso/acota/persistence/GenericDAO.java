package es.weso.acota.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * GenericDAO Interface, this interface allows to make auxiliary operations to a 
 * relational database
 * @author César Luis Alvargonzález
 *
 */
public interface GenericDAO {
	
	/**
	 * Opens a Databases's {@link Connection}
	 * @return Databases's {@link Connection}
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 * @throws ClassNotFoundException Exception that occurs when the database driver
	 * is not found in runtime
	 */
	public Connection openConnection() throws ClassNotFoundException, SQLException;
	
	/**
	 * Closes a Databases's {@link Connection}
	 * @param con Databases's {@link Connection} to close
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 */
	public void closeConnection(Connection con) throws SQLException;

	/**
	 * Closes a {@link PreparedStatment}
	 * @param ps {@link PreparedStatment} to close
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 */
	public void closeStatement(PreparedStatement ps) throws SQLException;

	/**
	 * Closes a {@link ResultSet}
	 * @param rs {@link ResultSet} to close
	 * @throws SQLException An exception that provides information on a relational
	 * database access error or other errors.
	 */
	public void closeResult(ResultSet rs) throws SQLException;
}
