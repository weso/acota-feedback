package es.weso.acota.persistence.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;

import es.weso.acota.core.entity.persistence.Feedback;
import es.weso.acota.core.entity.persistence.tables.DocumentTable;
import es.weso.acota.core.entity.persistence.tables.FeedbackTable;
import es.weso.acota.core.entity.persistence.tables.LabelTable;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.persistence.FeedbackDAO;

/**
 * Concrete implementation of FeedbackDAO for the DBMS MySQL 5.x
 * @see FeedbackDAO
 * @author César Luis Alvargonzález
 *
 */
public class FeedbackMysqlDAO extends GenericMysqlDAO implements FeedbackDAO {
	
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
	 * @throws ConfigurationException Any exception that occurs while initializing 
	 * a Configuration object
	 */
	public FeedbackMysqlDAO() throws AcotaConfigurationException {
		super();
		DocumentTable document = configuration.getDocumentTuple();
		this.documentTableName = document.getName();
		this.documentIdAttribute = document.getIdAttribute();
		this.documentNameAttribute = document.getNameAttribute();

		FeedbackTable feedback = configuration.getFeedbackTuple();
		this.feedbackTableName = feedback.getName();
		this.feedbackIdAttribute = feedback.getIdAttribute();
		this.feedbackUserIdAttribute = feedback.getUserIdAttribute();
		this.feedbackDocumentIdAttribute = feedback.getDocumentIdAttribute();
		this.feedbackLabelIdAttribute = feedback.getLabelIdAttribute();
		this.feedbackPreferenceAttribute = feedback.getPreferenceAttribute();
		this.feedbackTimestampAttribute = feedback.getTimestampAttribute();

		LabelTable label = configuration.getLabelTuple();
		this.labelTableName = label.getName();
		this.labelIdAttribute = label.getIdAttribute();
		this.labelNameAttribute = label.getNameAttribute();
	}

	@Override
	public void saveFeedback(Feedback feedback) throws ClassNotFoundException,
			SQLException {
		PreparedStatement ps = null;
		Connection con = null;

		try {
			con = openConnection();

			StringBuilder query = new StringBuilder("insert into ")
					.append(feedbackTableName).append(" (")
					.append(feedbackUserIdAttribute).append(", ")
					.append(feedbackDocumentIdAttribute).append(", ")
					.append(feedbackLabelIdAttribute).append(") values(?,?,?)");

			ps = con.prepareStatement(query.toString());
			ps.setInt(1, feedback.getUserId());
			ps.setInt(2, feedback.getDocument().hashCode());
			ps.setInt(3, feedback.getLabel().hashCode());

			ps.executeUpdate();

		} catch (ClassNotFoundException e) {
			throw e;
		} finally {
			closeStatement(ps);
			closeConnection(con);

		}
	}

	@Override
	public Set<Feedback> getAllFeedbacks() throws SQLException,
			ClassNotFoundException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		Feedback feedback = null;

		Set<Feedback> feedbacks = new HashSet<Feedback>();
		try {
			con = openConnection();

			StringBuilder query = new StringBuilder("select f.")
					.append(feedbackIdAttribute).append(", f.")
					.append(feedbackUserIdAttribute).append(", d.")
					.append(documentNameAttribute).append(", l.")
					.append(labelNameAttribute).append(",f.")
					.append(feedbackTimestampAttribute).append(" from ")
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
				feedback.setId(rs.getInt(feedbackIdAttribute));
				feedback.setUserId(rs.getInt(feedbackUserIdAttribute));
				feedback.setDocument(rs.getString("d." + documentNameAttribute));
				feedback.setLabel(rs.getString("l." + labelNameAttribute));
				feedback.setDate(new java.util.Date(rs.getTimestamp(feedbackTimestampAttribute).getTime()));
				feedbacks.add(feedback);
			}

		} catch (ClassNotFoundException e) {
			throw e;
		} finally {
			closeResult(rs);
			closeStatement(ps);
			closeConnection(con);

		}
		return feedbacks;
	}

	@Override
	public Set<Feedback> getFeedbacksByUserId(int userId) throws SQLException,
			ClassNotFoundException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		Feedback feedback = null;

		Set<Feedback> feedbacks = new HashSet<Feedback>();
		try {
			con = openConnection();

			StringBuilder query = new StringBuilder("select f.")
					.append(feedbackIdAttribute).append(", f.")
					.append(feedbackUserIdAttribute).append(", d.")
					.append(documentNameAttribute).append(", l.")
					.append(labelNameAttribute).append(",f.")
					.append(feedbackTimestampAttribute).append(" from ")
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
				feedback.setId(rs.getInt(feedbackIdAttribute));
				feedback.setUserId(rs.getInt(feedbackUserIdAttribute));
				feedback.setDocument(rs.getString("d." + documentNameAttribute));
				feedback.setLabel(rs.getString("l." + labelNameAttribute));
				feedback.setDate(new java.util.Date(rs.getTimestamp(feedbackTimestampAttribute).getTime()));
				feedbacks.add(feedback);
			}

		} catch (ClassNotFoundException e) {
			throw e;
		} finally {
			closeResult(rs);
			closeStatement(ps);
			closeConnection(con);

		}
		return feedbacks;
	}

	@Override
	public Set<Feedback> getFeedbacksByLabel(String label) throws SQLException,
			ClassNotFoundException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		Feedback feedback = null;

		Set<Feedback> feedbacks = new HashSet<Feedback>();
		try {
			con = openConnection();

			StringBuilder query = new StringBuilder("select f.")
					.append(feedbackIdAttribute).append(", f.")
					.append(feedbackUserIdAttribute).append(", d.")
					.append(documentNameAttribute).append(", l.")
					.append(labelNameAttribute).append(",f.")
					.append(feedbackTimestampAttribute).append(" from ")
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
				feedback.setId(rs.getInt(feedbackIdAttribute));
				feedback.setUserId(rs.getInt(feedbackUserIdAttribute));
				feedback.setDocument(rs.getString("d." + documentNameAttribute));
				feedback.setLabel(rs.getString("l." + labelNameAttribute));
				feedback.setDate(new java.util.Date(rs.getTimestamp(feedbackTimestampAttribute).getTime()));
				feedbacks.add(feedback);
			}

		} catch (ClassNotFoundException e) {
			throw e;
		} finally {
			closeResult(rs);
			closeStatement(ps);
			closeConnection(con);

		}
		return feedbacks;
	}

	@Override
	public Set<Feedback> getFeedbacksByDocument(String document)
			throws SQLException, ClassNotFoundException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		Feedback feedback = null;

		Set<Feedback> feedbacks = new HashSet<Feedback>();
		try {
			con = openConnection();

			StringBuilder query = new StringBuilder("select f.")
					.append(feedbackIdAttribute).append(", f.")
					.append(feedbackUserIdAttribute).append(", d.")
					.append(documentNameAttribute).append(", l.")
					.append(labelNameAttribute).append(",f.")
					.append(feedbackTimestampAttribute).append(" from ")
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
				feedback.setId(rs.getInt(feedbackIdAttribute));
				feedback.setUserId(rs.getInt(feedbackUserIdAttribute));
				feedback.setDocument(rs.getString("d." + documentNameAttribute));
				feedback.setLabel(rs.getString("l." + labelNameAttribute));
				feedback.setDate(new java.util.Date(rs.getTimestamp(feedbackTimestampAttribute).getTime()));
				feedbacks.add(feedback);
			}

		} catch (ClassNotFoundException e) {
			throw e;
		} finally {
			closeResult(rs);
			closeStatement(ps);
			closeConnection(con);

		}
		return feedbacks;
	}

}
