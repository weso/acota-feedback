package es.weso.acota.core;

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

	protected String databaseUrl;
	protected String databasePort;
	protected String databaseName;
	protected String databaseUser;
	protected String databasePassword;
	protected String databasePrefix;

	protected DocumentTable documentTuple;
	protected FeedbackTable feedbackTuple;
	protected LabelTable labelTuple;

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
	
	public DocumentTable getDocumentTuple() {
		return documentTuple;
	}

	public void setDocumentTuple(DocumentTable documentTuple) {
		this.documentTuple = documentTuple;
	}

	public FeedbackTable getFeedbackTuple() {
		return feedbackTuple;
	}

	public void setFeedbackTuple(FeedbackTable feedbackTuple) {
		this.feedbackTuple = feedbackTuple;
	}

	public LabelTable getLabelTuple() {
		return labelTuple;
	}

	public void setLabelTuple(LabelTable labelTuple) {
		this.labelTuple = labelTuple;
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
		this.documentTuple = new DocumentTable();
		documentTuple.setName(CONFIG.getString("database.document"));
		documentTuple.setIdAttribute(CONFIG.getString("database.document.id"));
		documentTuple.setNameAttribute(CONFIG
				.getString("database.document.name"));
	}

	/**
	 * Loads the Feedback Table properties
	 */
	private void loadFeedbackConfig() {
		this.feedbackTuple = new FeedbackTable();
		feedbackTuple.setName(CONFIG.getString("database.feedback"));
		feedbackTuple.setIdAttribute(CONFIG.getString("database.feedback.id"));
		feedbackTuple.setUserIdAttribute(CONFIG
				.getString("database.feedback.userId"));
		feedbackTuple.setDocumentIdAttribute(CONFIG
				.getString("database.feedback.document"));
		feedbackTuple.setLabelIdAttribute(CONFIG
				.getString("database.feedback.label"));
		feedbackTuple.setPreferenceAttribute(CONFIG
				.getString("database.feedback.preference"));
		feedbackTuple.setTimestampAttribute(CONFIG
				.getString("database.feedback.timestamp"));
	}

	/**
	 * Loads the Label Table properties
	 */
	private void loadLabelConfig() {
		this.labelTuple = new LabelTable();
		labelTuple.setName(CONFIG.getString("database.label"));
		labelTuple.setIdAttribute(CONFIG.getString("database.label.id"));
		labelTuple.setNameAttribute(CONFIG.getString("database.label.name"));
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
