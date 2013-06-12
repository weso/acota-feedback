package es.weso.acota.persistence.nosql;

import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.persistence.GenericDAO;

/**
 * Concrete implementation of LabelDAO for the MongoDB DBMS
 * @see GenericDAO
 * @author César Luis Alvargonzález
 * @since 0.3.8
 */
public class GenericMongoDBDAO implements GenericDAO{

	protected FeedbackConfiguration configuration;
	protected String url;
	
	@Override
	public void loadConfiguration(FeedbackConfiguration configuration)
			throws AcotaConfigurationException {
		if(configuration == null)
			configuration = new FeedbackConfiguration();
		this.configuration = configuration;	
	}
	


}
