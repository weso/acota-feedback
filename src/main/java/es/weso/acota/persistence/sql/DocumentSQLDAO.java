package es.weso.acota.persistence.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.entity.persistence.tables.DocumentTable;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.core.exceptions.AcotaPersistenceException;
import es.weso.acota.persistence.DocumentDAO;

/**
 * Concrete implementation of DocumentDAO for relational DBMSs
 * @see GenericSQLDAO
 * @see DocumentDAO
 * @author César Luis Alvargonzález
 */
public class DocumentSQLDAO extends GenericSQLDAO implements DocumentDAO {
	protected String tableName;
	protected String idAttribute;
	protected String nameAttribute;

	/**
	 * Zero-argument default constructor
	 * @throws AcotaConfigurationException Any exception that occurs while initializing 
	 * a Configuration object
	 */
	public DocumentSQLDAO() throws AcotaConfigurationException {
		super(null);
		loadConfiguration(configuration);
	}
	
	/** 
	 * One-argument constructor
	 * @param configuration Acota-feedback's configuration class
	 * @throws AcotaConfigurationException Any exception that occurs while initializing 
	 * a Configuration object
	 */
	public DocumentSQLDAO(FeedbackConfiguration configuration) throws AcotaConfigurationException {
		super(configuration);
		loadConfiguration(configuration);
	}
	
	@Override
	public void loadConfiguration(FeedbackConfiguration configuration)
			throws AcotaConfigurationException {
		super.loadConfiguration(configuration);
		
		DocumentTable label = this.configuration.getDocumentTable();
		this.tableName = this.configuration.getDatabasePrefix()+label.getName();
		this.idAttribute = label.getIdAttribute();
		this.nameAttribute = label.getNameAttribute();
	}

	@Override
	public void saveDocument(Integer id, String document) throws AcotaPersistenceException {
		PreparedStatement ps = null;
		Connection con = null;

		try {
			con = openConnection();

			StringBuilder query = new StringBuilder("insert into ")
					.append(tableName).append(" (").append(idAttribute)
					.append(", ").append(nameAttribute).append(") values(?,?)");

			ps = con.prepareStatement(query.toString());
			ps.setInt(1, id);
			ps.setString(2, document);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		} finally {
			closeStatement(ps);
			closeConnection(con);

		}
	}

	@Override
	public String getDocumentById(Integer id) throws AcotaPersistenceException {
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

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		} finally {
			closeResult(rs);
			closeStatement(ps);
			closeConnection(con);

		}
		return labels;
	}

	@Override
	public String getDocumentByHashCode(Integer hash) throws AcotaPersistenceException {
		return getDocumentById(hash);
	}

	@Override
	public Set<String> getDocumentsByIds(Collection<Integer> ids)
			throws AcotaPersistenceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;

		Set<String> documents = new HashSet<String>();
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
					documents.add(rs.getString(nameAttribute));
				}

			} catch (SQLException e) {
				e.printStackTrace();
				throw new AcotaPersistenceException(e);
			} finally {
				closeResult(rs);
				closeStatement(ps);
				closeConnection(con);
			}
		}
		return documents;
	}

	@Override
	public Set<String> getDocumentsByHashCodes(Collection<Integer> hashes)
			throws AcotaPersistenceException {
		return getDocumentsByIds(hashes);
	}
}
