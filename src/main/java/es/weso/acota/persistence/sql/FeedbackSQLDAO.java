package es.weso.acota.persistence.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.entity.persistence.Feedback;
import es.weso.acota.core.entity.persistence.tables.DocumentTable;
import es.weso.acota.core.entity.persistence.tables.FeedbackTable;
import es.weso.acota.core.entity.persistence.tables.LabelTable;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.core.exceptions.AcotaPersistenceException;
import es.weso.acota.persistence.FeedbackDAO;

/**
 * Concrete implementation of FeedbackDAO for relational DBMSs
 * @see GenericSQLDAO
 * @see FeedbackDAO
 * @author César Luis Alvargonzález
 */
public class FeedbackSQLDAO extends GenericSQLDAO implements FeedbackDAO {
	
	protected String documentTableName;
	protected String documentIdAttribute;
	protected String documentNameAttribute;

	protected String feedbackTableName;
	protected String feedbackIdAttribute;
	protected String feedbackUserIdAttribute;
	protected String feedbackDocumentIdAttribute;
	protected String feedbackLabelIdAttribute;
	protected String feedbackPreferenceAttribute;
	protected String feedbackTimestampAttribute;

	protected String labelTableName;
	protected String labelIdAttribute;
	protected String labelNameAttribute;

	/**
	 * Zero-argument default constructor
	 * @throws AcotaConfigurationException Any exception that occurs while initializing 
	 * a Configuration object
	 */
	public FeedbackSQLDAO() throws AcotaConfigurationException {
		super(null);
		loadConfiguration(configuration);
	}

	/** 
	 * One-argument constructor
	 * @param configuration Acota-feedback's this.configuration class
	 * @throws AcotaConfigurationException Any exception that occurs while initializing 
	 * a Configuration object
	 */
	public FeedbackSQLDAO(FeedbackConfiguration configuration) throws AcotaConfigurationException {
		super(configuration);
		loadConfiguration(configuration);
	}
	
	@Override
	public void loadConfiguration(FeedbackConfiguration configuration)
			throws AcotaConfigurationException {
		super.loadConfiguration(configuration);
		DocumentTable document = this.configuration.getDocumentTable();
		this.documentTableName = this.configuration.getDatabasePrefix()+document.getName();
		this.documentIdAttribute = document.getIdAttribute();
		this.documentNameAttribute = document.getNameAttribute();

		FeedbackTable feedback = this.configuration.getFeedbackTable();
		this.feedbackTableName = this.configuration.getDatabasePrefix()+feedback.getName();
		this.feedbackIdAttribute = feedback.getIdAttribute();
		this.feedbackUserIdAttribute = feedback.getUserIdAttribute();
		this.feedbackDocumentIdAttribute = feedback.getDocumentIdAttribute();
		this.feedbackLabelIdAttribute = feedback.getLabelIdAttribute();
		this.feedbackPreferenceAttribute = feedback.getPreferenceAttribute();
		this.feedbackTimestampAttribute = feedback.getTimestampAttribute();

		LabelTable label = this.configuration.getLabelTable();
		this.labelTableName = this.configuration.getDatabasePrefix()+label.getName();
		this.labelIdAttribute = label.getIdAttribute();
		this.labelNameAttribute = label.getNameAttribute();
	}
	
	@Override
	public void saveFeedback(Feedback feedback) throws AcotaPersistenceException {
		PreparedStatement ps = null;
		Connection con = null;

		try {
			con = openConnection();

			StringBuilder query = new StringBuilder("insert into ")
					.append(feedbackTableName).append(" (")
					.append(feedbackUserIdAttribute).append(", ")
					.append(feedbackDocumentIdAttribute).append(", ")
					.append(feedbackPreferenceAttribute).append(", ")
					.append(feedbackLabelIdAttribute).append(") values(?,?,?,?)");

			ps = con.prepareStatement(query.toString());
			ps.setInt(1, feedback.getUserId());
			ps.setInt(2, feedback.getDocument().hashCode());
			ps.setInt(3, feedback.getPreference());
			ps.setInt(4, feedback.getLabel().hashCode());

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
	public Set<Feedback> getAllFeedbacks() throws AcotaPersistenceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		Feedback feedback = null;

		Set<Feedback> feedbacks = new HashSet<Feedback>();
		try {
			con = openConnection();

			StringBuilder query = new StringBuilder("select f.")
					.append(feedbackIdAttribute).append(" as f1, f.")
					.append(feedbackUserIdAttribute).append(" as f2, d.")
					.append(documentNameAttribute).append(" as f3, l.")
					.append(labelNameAttribute).append(" as f4, f.")
					.append(feedbackTimestampAttribute).append(" as f5 from ")
					.append(documentTableName).append(" as d,")
					.append(feedbackTableName).append(" as f,")
					.append(labelTableName).append(" as l where f.")
					.append(feedbackDocumentIdAttribute).append(" = d.")
					.append(documentIdAttribute).append(" and f.")
					.append(feedbackLabelIdAttribute).append(" = l.")
					.append(labelIdAttribute);

			ps = con.prepareStatement(query.toString());
			rs = ps.executeQuery();

			while (rs.next()) {
				feedback = new Feedback();
				feedback.setId(rs.getInt("f1"));
				feedback.setUserId(rs.getInt("f2"));
				feedback.setDocument(rs.getString("f3"));
				feedback.setLabel(rs.getString("f4"));
				feedback.setDate(new java.util.Date(rs.getTimestamp("f5").getTime()));
				feedbacks.add(feedback);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		} finally {
			closeResult(rs);
			closeStatement(ps);
			closeConnection(con);

		}
		return feedbacks;
	}

	@Override
	public Set<Feedback> getFeedbacksByUserId(int userId) throws AcotaPersistenceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		Feedback feedback = null;

		Set<Feedback> feedbacks = new HashSet<Feedback>();
		try {
			con = openConnection();

			StringBuilder query = new StringBuilder("select f.")
					.append(feedbackIdAttribute).append(" as f1, f.")
					.append(feedbackUserIdAttribute).append(" as f2, d.")
					.append(documentNameAttribute).append(" as f3, l.")
					.append(labelNameAttribute).append(" as f4, f.")
					.append(feedbackTimestampAttribute).append(" as f5 from ")
					.append(documentTableName).append(" as d,")
					.append(feedbackTableName).append(" as f,")
					.append(labelTableName).append(" as l where f.")
					.append(feedbackUserIdAttribute).append(" = ? and f.")
					.append(feedbackDocumentIdAttribute).append(" = d.")
					.append(documentIdAttribute).append(" and f.")
					.append(feedbackLabelIdAttribute).append(" = l.")
					.append(labelIdAttribute);

			ps = con.prepareStatement(query.toString());
			ps.setInt(1, userId);
			rs = ps.executeQuery();

			while (rs.next()) {
				feedback = new Feedback();
				feedback.setId(rs.getInt("f1"));
				feedback.setUserId(rs.getInt("f2"));
				feedback.setDocument(rs.getString("f3"));
				feedback.setLabel(rs.getString("f4"));
				feedback.setDate(new java.util.Date(rs.getTimestamp("f5").getTime()));
				feedbacks.add(feedback);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		} finally {
			closeResult(rs);
			closeStatement(ps);
			closeConnection(con);

		}
		return feedbacks;
	}

	@Override
	public Set<Feedback> getFeedbacksByLabel(String label) throws AcotaPersistenceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		Feedback feedback = null;

		Set<Feedback> feedbacks = new HashSet<Feedback>();
		try {
			con = openConnection();

			StringBuilder query = new StringBuilder("select f.")
					.append(feedbackIdAttribute).append(" as f1, f.")
					.append(feedbackUserIdAttribute).append(" as f2, d.")
					.append(documentNameAttribute).append(" as f3, l.")
					.append(labelNameAttribute).append(" as f4, f.")
					.append(feedbackTimestampAttribute).append(" as f5 from ")
					.append(documentTableName).append(" as d,")
					.append(feedbackTableName).append(" as f,")
					.append(labelTableName).append(" as l where l.")
					.append(labelNameAttribute).append(" = ? and f.")
					.append(feedbackDocumentIdAttribute).append(" = d.")
					.append(documentIdAttribute).append(" and f.")
					.append(feedbackLabelIdAttribute).append(" = l.")
					.append(labelIdAttribute);

			ps = con.prepareStatement(query.toString());
			ps.setString(1, label);
			rs = ps.executeQuery();

			while (rs.next()) {
				feedback = new Feedback();
				feedback.setId(rs.getInt("f1"));
				feedback.setUserId(rs.getInt("f2"));
				feedback.setDocument(rs.getString("f3"));
				feedback.setLabel(rs.getString("f4"));
				feedback.setDate(new java.util.Date(rs.getTimestamp("f5").getTime()));
				feedbacks.add(feedback);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		} finally {
			closeResult(rs);
			closeStatement(ps);
			closeConnection(con);

		}
		return feedbacks;
	}

	@Override
	public Set<Feedback> getFeedbacksByDocument(String document)
			throws AcotaPersistenceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		Feedback feedback = null;

		Set<Feedback> feedbacks = new HashSet<Feedback>();
		try {
			con = openConnection();

			StringBuilder query = new StringBuilder("select f.")
					.append(feedbackIdAttribute).append(" as f1, f.")
					.append(feedbackUserIdAttribute).append(" as f2, d.")
					.append(documentNameAttribute).append(" as f3, l.")
					.append(labelNameAttribute).append(" as f4, f.")
					.append(feedbackTimestampAttribute).append(" as f5 from ")
					.append(documentTableName).append(" as d,")
					.append(feedbackTableName).append(" as f,")
					.append(labelTableName).append(" as l where d.")
					.append(documentNameAttribute).append(" = ? and f.")
					.append(feedbackDocumentIdAttribute).append(" = d.")
					.append(documentIdAttribute).append(" and f.")
					.append(feedbackLabelIdAttribute).append(" = l.")
					.append(labelIdAttribute);

			ps = con.prepareStatement(query.toString());
			ps.setString(1, document);
			rs = ps.executeQuery();

			while (rs.next()) {
				feedback = new Feedback();
				feedback.setId(rs.getInt("f1"));
				feedback.setUserId(rs.getInt("f2"));
				feedback.setDocument(rs.getString("f3"));
				feedback.setLabel(rs.getString("f4"));
				feedback.setDate(new java.util.Date(rs.getTimestamp("f5").getTime()));
				feedbacks.add(feedback);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AcotaPersistenceException(e);
		} finally {
			closeResult(rs);
			closeStatement(ps);
			closeConnection(con);

		}
		return feedbacks;
	}

}
