package es.weso.acota.persistence.factory;


import static es.weso.acota.persistence.DBMS.DB_MARIADB;
import static es.weso.acota.persistence.DBMS.DB_MONGODB;
import static es.weso.acota.persistence.DBMS.DB_MYSQL;
import static es.weso.acota.persistence.DBMS.DB_POSTGRESQL;

import java.lang.reflect.InvocationTargetException;

import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.persistence.DocumentDAO;
import es.weso.acota.persistence.FeedbackDAO;
import es.weso.acota.persistence.LabelDAO;
import es.weso.acota.persistence.nosql.DocumentMongoDBDAO;
import es.weso.acota.persistence.nosql.FeedbackMongoDBDAO;
import es.weso.acota.persistence.nosql.LabelMongoDBDAO;
import es.weso.acota.persistence.sql.DocumentSQLDAO;
import es.weso.acota.persistence.sql.FeedbackSQLDAO;
import es.weso.acota.persistence.sql.LabelSQLDAO;

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
		return (DocumentDAO) Class.forName(getDocumentDAO(configuration))
				.newInstance();
	}

	/**
	 * Creates a DocumentDAO with the implementation defined in the configuration
	 * @param configuration 
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
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public static DocumentDAO createDocumentDAO(FeedbackConfiguration configuration)
			throws ClassNotFoundException, AcotaConfigurationException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		instanciateClass();
		return (DocumentDAO) Class.forName(getDocumentDAO(configuration))
				.getDeclaredConstructor(FeedbackConfiguration.class)
				.newInstance(configuration);
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
		return (FeedbackDAO) Class.forName(getFeedbackDAO(configuration))
				.newInstance();
	}
	
	/**
	 * Creates a FeedbackDAO with the implementation defined in the configuration
	 * @param configuration 
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
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public static FeedbackDAO createFeedbackDAO(FeedbackConfiguration configuration)
			throws AcotaConfigurationException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		instanciateClass();
		return (FeedbackDAO) Class.forName(getFeedbackDAO(configuration))
				.getDeclaredConstructor(FeedbackConfiguration.class)
				.newInstance(configuration);
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
		return (LabelDAO) Class.forName(getLabelDAO(configuration))
				.newInstance();
	}
	
	/**
	 * Creates a LabelDAO with the implementation defined in the configuration
	 * @param configuration 
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
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public static LabelDAO createLabelDAO(FeedbackConfiguration configuration) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, AcotaConfigurationException  {
		instanciateClass();
		return (LabelDAO) Class.forName(getLabelDAO(configuration))
				.getDeclaredConstructor(FeedbackConfiguration.class)
				.newInstance(configuration);
	}
	
	protected static String getLabelDAO(FeedbackConfiguration configuration){
		String dao = configuration.getLabelDAOClass();
		String type = configuration.getDatabaseType();
		if(dao.isEmpty()){
			if(type.equals(DB_MYSQL) || type.equals(DB_MARIADB)
					|| type.equals(DB_POSTGRESQL)){
				dao = LabelSQLDAO.class.getName();
			}else if(type.equals(DB_MONGODB)){
				dao = LabelMongoDBDAO.class.getName();
			}
		}
		return dao;
	}
	
	protected static String getDocumentDAO(FeedbackConfiguration configuration){
		String dao = configuration.getDocumentDAOClass();
		String type = configuration.getDatabaseType();
		if(dao.isEmpty()){
			if(type.equals(DB_MYSQL) || type.equals(DB_MARIADB)
					|| type.equals(DB_POSTGRESQL)){
				dao = DocumentSQLDAO.class.getName();
			}else if(type.equals(DB_MONGODB)){
				dao = DocumentMongoDBDAO.class.getName();
			}
		}
		return dao;
	}
	
	protected static String getFeedbackDAO(FeedbackConfiguration configuration){
		String dao = configuration.getLabelDAOClass();
		String type = configuration.getDatabaseType();
		if(dao.isEmpty()){
			if(type.equals(DB_MYSQL) || type.equals(DB_MARIADB)
					|| type.equals(DB_POSTGRESQL)){
				dao = FeedbackSQLDAO.class.getName();
			}else if(type.equals(DB_MONGODB)){
				dao = FeedbackMongoDBDAO.class.getName();
			}
		}
		return dao;
	}
}
