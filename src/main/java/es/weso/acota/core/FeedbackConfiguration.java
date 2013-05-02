package es.weso.acota.core;

import static es.weso.acota.persistence.DBMS.DB_MYSQL;
import static es.weso.acota.persistence.DBMS.DB_MARIADB;
import static es.weso.acota.persistence.DBMS.DB_POSTGRESQL;
import static es.weso.acota.persistence.DBMS.DB_MONGODB;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import es.weso.acota.core.business.enhancer.SeedConfiguration;
import es.weso.acota.core.entity.persistence.tables.DocumentTable;
import es.weso.acota.core.entity.persistence.tables.FeedbackTable;
import es.weso.acota.core.entity.persistence.tables.LabelTable;
import es.weso.acota.core.exceptions.AcotaConfigurationException;

/**
 * The main task of this class is to load the feedback CONFIGuration properties
 * of ACOTA, this properties could be set programmatically or by a CONFIGuration
 * file called acota.properties
 * 
 * @author César Luis Alvargonzález
 * 
 */
public class FeedbackConfiguration extends SeedConfiguration implements Configuration {

	protected static final String INTERNAL_ACOTA_PERSISTENCE_PROPERTIES_PATH = "inner.acota.persistence.properties";

	protected static Logger LOGGER;
	protected static CompositeConfiguration CONFIG;
	
	protected String documentDAOClass;
	protected String feedbackDAOClass;
	protected String labelDAOClass;

	protected String databaseType;
	protected String databaseUrl;
	protected String databasePort;
	protected String databaseName;
	protected String databaseUser;
	protected String databasePassword;
	protected String databasePrefix;

	protected DocumentTable documentTable;
	protected FeedbackTable feedbackTable;
	protected LabelTable labelTable;

	protected double simpleRecommenderRelevance;

	protected double labelRecommenderRelevance;
	protected int labelRecomenderNumRecommendations;

	protected static Logger logger;

	/**
	 * Zero-argument default constructor.
	 * 
	 * @throws AcotaConfigurationException
	 *             Any exception that occurs while initializing a Configuration
	 *             object
	 */
	public FeedbackConfiguration() throws AcotaConfigurationException {
		FeedbackConfiguration.LOGGER = Logger.getLogger(FeedbackConfiguration.class);
		
		CompositeConfiguration configuration = foo(CONFIG);
		if(CONFIG==null)
			FeedbackConfiguration.CONFIG = configuration;
		
		loadDAOClasses();
		loadDatabaseConfig();
		loadDocumentConfig();
		loadFeedbackConfig();
		loadLabelConfig();

		loadLabelRecommenderConfig();
		loadSimpleRecommenderSimple();
	}

	public String getDocumentDAOClass() {
		return documentDAOClass;
	}

	public void setDocumentDAOClass(String documentDAOClass) {
		this.documentDAOClass = documentDAOClass;
	}

	public String getFeedbackDAOClass() {
		return feedbackDAOClass;
	}

	public void setFeedbackDAOClass(String feedbackDAOClass) {
		this.feedbackDAOClass = feedbackDAOClass;
	}

	public String getLabelDAOClass() {
		return labelDAOClass;
	}

	public void setLabelDAOClass(String labelDAOClass) {
		this.labelDAOClass = labelDAOClass;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		if(databaseType.equals(DB_MYSQL) || databaseType.equals(DB_MARIADB)){
			this.databaseType = DB_MYSQL;
		}else if(databaseType.equals(DB_POSTGRESQL)){
			this.databaseType = DB_POSTGRESQL;
		}else if(databaseType.equals(DB_MONGODB)){
			this.databaseType = DB_MONGODB;
		}
		this.databaseType = databaseType;
	}

	public String getDatabaseUrl() {
		return databaseUrl;
	}

	public void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}

	public String getDatabasePort() {
		return databasePort;
	}

	public void setDatabasePort(String databasePort) {
		this.databasePort = databasePort;
	}
	
	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getDatabaseUser() {
		return databaseUser;
	}

	public void setDatabaseUser(String databaseUser) {
		this.databaseUser = databaseUser;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}

	public String getDatabasePrefix() {
		return databasePrefix;
	}

	public void setDatabasePrefix(String databasePrefix) {
		this.databasePrefix = databasePrefix;
	}
	
	public DocumentTable getDocumentTable() {
		return documentTable;
	}

	public void setDocumentTable(DocumentTable documentTable) {
		this.documentTable = documentTable;
	}

	public FeedbackTable getFeedbackTable() {
		return feedbackTable;
	}

	public void setFeedbackTable(FeedbackTable feedbackTable) {
		this.feedbackTable = feedbackTable;
	}

	public LabelTable getLabelTable() {
		return labelTable;
	}

	public void setLabelTable(LabelTable labelTable) {
		this.labelTable = labelTable;
	}

	public double getSimpleRecommenderRelevance() {
		return simpleRecommenderRelevance;
	}

	public void setSimpleRecommenderRelevance(double simpleRecommenderRelevance) {
		this.simpleRecommenderRelevance = simpleRecommenderRelevance;
	}

	public double getLabelRecommenderRelevance() {
		return labelRecommenderRelevance;
	}

	public void setLabelRecommenderRelevance(double labelRecommenderRelevance) {
		this.labelRecommenderRelevance = labelRecommenderRelevance;
	}

	public int getLabelRecomenderNumRecommendations() {
		return labelRecomenderNumRecommendations;
	}

	public void setLabelRecomenderNumRecommendations(
			int labelRecomenderNumRecommendations) {
		this.labelRecomenderNumRecommendations = labelRecomenderNumRecommendations;
	}

	@Override
	protected void loadCustomConfiguration(CompositeConfiguration config) throws AcotaConfigurationException {
		try {
			config.addConfiguration(new PropertiesConfiguration(this.getClass().getClassLoader()
					.getResource(INTERNAL_ACOTA_PERSISTENCE_PROPERTIES_PATH)));
		} catch (ConfigurationException e) {
			throw new AcotaConfigurationException(e);
		}
	}
	
	/**
	 * Loads the DAO Classes properties
	 */
	private void loadDAOClasses() {
		this.documentDAOClass = CONFIG
				.getString("database.dao.impl.documentDAO");
		this.feedbackDAOClass = CONFIG
				.getString("database.dao.impl.feedbackDAO");
		this.labelDAOClass = CONFIG.getString("database.dao.impl.labelDAO");
	}

	/**
	 * Loads the Database CONFIGuration properties
	 */
	private void loadDatabaseConfig() {
		this.databaseType = CONFIG.getString("database.type");
		this.databaseUrl = CONFIG.getString("database.url");
		this.databasePort = CONFIG.getString("database.port");
		this.databaseName = CONFIG.getString("database.name");
		this.databaseUser = CONFIG.getString("database.user");
		this.databasePassword = CONFIG.getString("database.password");
		this.databasePrefix = CONFIG.getString("database.prefix");
	}

	/**
	 * Loads the Document Table properties
	 */
	private void loadDocumentConfig() {
		this.documentTable = new DocumentTable();
		documentTable.setName(CONFIG.getString("database.document"));
		documentTable.setIdAttribute(CONFIG.getString("database.document.id"));
		documentTable.setNameAttribute(CONFIG
				.getString("database.document.name"));
		documentTable.setTimestampAttribute(CONFIG.getString("database.document.timestamp"));
	}

	/**
	 * Loads the Feedback Table properties
	 */
	private void loadFeedbackConfig() {
		this.feedbackTable = new FeedbackTable();
		feedbackTable.setName(CONFIG.getString("database.feedback"));
		feedbackTable.setIdAttribute(CONFIG.getString("database.feedback.id"));
		feedbackTable.setUserIdAttribute(CONFIG
				.getString("database.feedback.userId"));
		feedbackTable.setDocumentIdAttribute(CONFIG
				.getString("database.feedback.document"));
		feedbackTable.setLabelIdAttribute(CONFIG
				.getString("database.feedback.label"));
		feedbackTable.setPreferenceAttribute(CONFIG
				.getString("database.feedback.preference"));
		feedbackTable.setTimestampAttribute(CONFIG
				.getString("database.feedback.timestamp"));
	}

	/**
	 * Loads the Label Table properties
	 */
	private void loadLabelConfig() {
		this.labelTable = new LabelTable();
		labelTable.setName(CONFIG.getString("database.label"));
		labelTable.setIdAttribute(CONFIG.getString("database.label.id"));
		labelTable.setNameAttribute(CONFIG.getString("database.label.name"));
		labelTable.setTimestampAttribute(CONFIG.getString("database.label.timestamp"));
	}

	/**
	 * Loads the LabelRecommenderEnhancer properties
	 */
	private void loadLabelRecommenderConfig() {
		this.labelRecommenderRelevance = CONFIG
				.getDouble("enhancer.recommender.label.relevance");
		this.labelRecomenderNumRecommendations = CONFIG
				.getInt("enhancer.recommender.label.recommendations");
	}

	/**
	 * Loads the SimpleRecommenderEnhancer properties
	 */
	private void loadSimpleRecommenderSimple() {
		this.simpleRecommenderRelevance = CONFIG
				.getDouble("enhancer.recommender.simple.relevance");
	}

}
