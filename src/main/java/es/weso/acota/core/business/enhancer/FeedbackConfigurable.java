package es.weso.acota.core.business.enhancer;


import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.exceptions.AcotaConfigurationException;

/**
 * Configurable Interface, this interface allows configure the classes that
 * implements this interface.
 * 
 * @author César Luis Alvargonzález
 * 
 */
public interface FeedbackConfigurable {
	/**
	 * Loads the configuration into the class that implements this interface
	 * 
	 * @param configuration
	 *            Acota-core's feedback class
	 * @throws AcotaConfigurationException 
	 *             Any exception that occurs while initializing a Configuration
	 *             object
	 */
	public void loadConfiguration(FeedbackConfiguration configuration)
			throws AcotaConfigurationException ;
}
