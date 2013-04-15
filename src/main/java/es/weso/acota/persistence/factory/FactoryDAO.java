package es.weso.acota.persistence.factory;


import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.persistence.DocumentDAO;
import es.weso.acota.persistence.FeedbackDAO;
import es.weso.acota.persistence.LabelDAO;

/**
 * FactoryDAO is a class that creates DAO Objects, the implementation
 * of this objects is defined within the {@link FeedbackConfiguration} class 
 * @author César Luis Alvargonzález
 */
public class FactoryDAO {

	private static FactoryDAO FACTORY_DAO;
	private static FeedbackConfiguration configuration;

	/**
	 * Zero-Argument Constructor
	 * @throws AcotaConfigurationException ConfigurationException Any exception that 
	 * occurs while initializing a Configuration object
	 */
	private FactoryDAO() throws AcotaConfigurationException {
		FactoryDAO.configuration = new FeedbackConfiguration();
	}

	/**
	 * Instantiate FactoryDAO (Singleton Design Pattern)
	 * @throws AcotaConfigurationException ConfigurationException Any exception that 
	 * occurs while initializing a Configuration object
	 */
	private static void instanciateClass() throws AcotaConfigurationException {
		if (FACTORY_DAO == null) {
			FactoryDAO.FACTORY_DAO = new FactoryDAO();
		}
	}

	/**
	 * Creates a DocumentDAO with the implementation defined in the configuration
	 * @see DocumentDAO
	 * @return DocumentDAO with the implementation defined in the configuration
	 * @throws AcotaConfigurationException ConfigurationException Any exception that 
	 * occurs while initializing a Configuration object
	 * @throws InstantiationException Thrown when an application tries to instantiate
	 * an interface or an abstract class
	 * @throws IllegalAccessException An IllegalAccessException is thrown when an
	 * application tries to reflectively create an instance
	 * @throws ClassNotFoundException Thrown when an application tries to load in a
	 * class through its string name
	 */
	public static DocumentDAO createDocumentDAO()
			throws ClassNotFoundException, AcotaConfigurationException,
			InstantiationException, IllegalAccessException {
		instanciateClass();
		return (DocumentDAO) Class.forName(configuration.getDocumentDAOClass())
				.newInstance();
	}

	/**
	 * Creates a FeedbackDAO with the implementation defined in the configuration
	 * @see FeedbackDAO
	 * @return DocumentDAO with the implementation defined in the configuration
	 * @throws AcotaConfigurationException ConfigurationException Any exception that 
	 * occurs while initializing a Configuration object
	 * @throws InstantiationException Thrown when an application tries to instantiate
	 * an interface or an abstract class
	 * @throws IllegalAccessException An IllegalAccessException is thrown when an
	 * application tries to reflectively create an instance
	 * @throws ClassNotFoundException Thrown when an application tries to load in a
	 * class through its string name
	 */
	public static FeedbackDAO createFeedbackDAO()
			throws AcotaConfigurationException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		instanciateClass();
		return (FeedbackDAO) Class.forName(configuration.getFeedbackDAOClass())
				.newInstance();
	}

	/**
	 * Creates a LabelDAO with the implementation defined in the configuration
	 * @see LabelDAO LabelDAO with the implementation defined in the configuration
	 * @return LabelDAO with the implementation defined in the configuration
	 * @throws AcotaConfigurationException ConfigurationException Any exception that 
	 * occurs while initializing a Configuration object
	 * @throws InstantiationException Thrown when an application tries to instantiate
	 * an interface or an abstract class
	 * @throws IllegalAccessException An IllegalAccessException is thrown when an
	 * application tries to reflectively create an instance
	 * @throws ClassNotFoundException Thrown when an application tries to load in a
	 * class through its string name
	 */
	public static LabelDAO createLabelDAO() throws AcotaConfigurationException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		instanciateClass();
		return (LabelDAO) Class.forName(configuration.getLabelDAOClass())
				.newInstance();
	}
}
