package es.weso.acota.persistence.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


import es.weso.acota.core.entity.persistence.tables.LabelTable;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.persistence.LabelDAO;

/**
 * Concrete implementation of LabelDAO for the DBMS MySQL 5.x
 * @see LabelDAO
 * @author César Luis Alvargonzález
 *
 */
public class LabelMysqlDAO extends GenericMysqlDAO implements LabelDAO {

	protected String tableName;
	protected String idAttribute;
	protected String nameAttribute;

	/**
	 * Zero-argument default constructor
	 * @throws AcotaConfigurationException Any exception that occurs while initializing 
	 * a Configuration object
	 */
	public LabelMysqlDAO() throws AcotaConfigurationException  {
		super();
		LabelTable label = configuration.getLabelTuple();
		this.tableName = label.getName();
		this.idAttribute = label.getIdAttribute();
		this.nameAttribute = label.getNameAttribute();
	}

	@Override
	public void saveLabel(Integer id, String label) throws SQLException,
			ClassNotFoundException {
		PreparedStatement ps = null;
		Connection con = null;

		try {
			con = openConnection();

			StringBuilder query = new StringBuilder("insert into ")
					.append(tableName).append(" (").append(idAttribute)
					.append(", ").append(nameAttribute).append(") values(?,?)");

			ps = con.prepareStatement(query.toString());
			ps.setInt(1, id);
			ps.setString(2, label);

			ps.executeUpdate();

		} catch (ClassNotFoundException e) {
			throw e;
		} finally {
			closeStatement(ps);
			closeConnection(con);

		}
	}

	@Override
	public String getLabelById(Integer id) throws SQLException,
			ClassNotFoundException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;

		String labels = null;

		try {
			con = openConnection();

			StringBuilder query = new StringBuilder("select * from ")
					.append(tableName).append(" where ").append(idAttribute)
					.append("=?");

			ps = con.prepareStatement(query.toString());
			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next()) {
				labels = rs.getString(nameAttribute);
			}

		} catch (ClassNotFoundException e) {
			throw e;
		} finally {
			closeResult(rs);
			closeStatement(ps);
			closeConnection(con);

		}
		return labels;
	}

	@Override
	public String getLabelByHash(Integer hash) throws SQLException,
			ClassNotFoundException {
		return getLabelById(hash);
	}

	@Override
	public Set<String> getLabelsByIds(Collection<Integer> ids)
			throws SQLException, ClassNotFoundException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;

		Set<String> labels = new HashSet<String>();
		List<Integer> hashesList = new LinkedList<Integer>(ids);
		if (!ids.isEmpty()) {
			try {
				con = openConnection();

				StringBuilder query = new StringBuilder("select * from ")
						.append(tableName).append(" where ");

				for (int i = 0; i < ids.size(); i++) {
					query.append(idAttribute).append("=?");
					if (i + 1 < ids.size()) {
						query.append(" or ");
					}
				}

				ps = con.prepareStatement(query.toString());

				for (int i = 0; i < hashesList.size(); i++) {
					ps.setInt(i + 1, hashesList.get(i));
				}

				rs = ps.executeQuery();

				while (rs.next()) {
					labels.add(rs.getString(nameAttribute));
				}

			} catch (ClassNotFoundException e) {
				throw e;
			} finally {
				closeResult(rs);
				closeStatement(ps);
				closeConnection(con);
			}
		}
		return labels;
	}

	@Override
	public Set<String> getLabelsByHashCodes(Collection<Integer> hashes)
			throws SQLException, ClassNotFoundException {
		return getLabelsByIds(hashes);
	}

}