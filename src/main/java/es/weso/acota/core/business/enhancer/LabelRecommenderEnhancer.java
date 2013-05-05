package es.weso.acota.core.business.enhancer;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.entity.ProviderTO;
import es.weso.acota.core.entity.TagTO;
import es.weso.acota.core.exceptions.AcotaConfigurationException;
import es.weso.acota.core.exceptions.AcotaPersistenceException;
import es.weso.acota.core.utils.AcotaUtil;
import es.weso.acota.core.utils.lang.LanguageDetector;
import es.weso.acota.core.utils.mahout.RecommenderUtil;
import es.weso.acota.persistence.LabelDAO;
import es.weso.acota.persistence.factory.FactoryDAO;

/**
 * LabelRecommenderEnhancer is an {@link Enhancer} specialized in suggesting 
 * Labels, depending in the feedback of the different users. This {@link Enhancer} 
 * increases, or adds it if there is not currently present, the weight of the
 * labels tagged in common documents
 * 
 * @author César Luis Alvargonzález
 *
 */
public class LabelRecommenderEnhancer extends EnhancerAdapter implements FeedbackConfigurable{

	protected LabelDAO labelDao;
	protected LanguageDetector languageDetector;
	protected DataModel dataModel;
	protected RecommenderUtil recommenderUtil;
	
	protected int numRecommendations;
	protected double relevance;
	
	public FeedbackConfiguration configuration;

	/**
	 * Zero-argument default constructor
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
	 * @throws AcotaPersistenceException 
	 */
	public LabelRecommenderEnhancer() throws AcotaConfigurationException, InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException, AcotaPersistenceException  {
		super();
		LabelRecommenderEnhancer.provider = new ProviderTO(
				"Label Recommender Enhancer");
		loadConfiguration(configuration);
		this.labelDao = FactoryDAO.createLabelDAO(this.configuration);
		this.recommenderUtil = new RecommenderUtil(configuration);
	}
	
	/**
	 * Zero-argument default constructor
	 * @param configuration Acota-feedback's configuration class
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
	 * @throws AcotaPersistenceException 
	 */
	public LabelRecommenderEnhancer(FeedbackConfiguration configuration) throws AcotaConfigurationException, InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException, AcotaPersistenceException {
		super();
		LabelRecommenderEnhancer.provider = new ProviderTO(
				"Label Recommender Enhancer");
		loadConfiguration(configuration);
		this.labelDao = FactoryDAO.createLabelDAO(this.configuration);
		this.recommenderUtil = new RecommenderUtil(configuration);
	}
	
	@Override
	public void loadConfiguration(FeedbackConfiguration configuration) throws AcotaConfigurationException{
		if(configuration==null)
			configuration = new FeedbackConfiguration();
		this.configuration = configuration;
		this.relevance = configuration.getLabelRecommenderRelevance();
		this.numRecommendations = configuration.getLabelRecomenderNumRecommendations();
		this.languageDetector = LanguageDetector.getInstance(configuration);
	}

	@Override
	protected void execute() throws Exception {
		
		ItemBasedRecommender recommender = recommenderUtil.loadRecommender();
		List<RecommendedItem> items = null;
		
		for (Entry<String, TagTO> label : AcotaUtil.backupTags(tags).entrySet()) {
			try {
	
				items = recommender.mostSimilarItems(recommenderUtil.fromIdToLong(label.getKey()),
						numRecommendations);
				handleRecommendLabels(items);
			} catch (TasteException e) {
				e.printStackTrace();
				//Drain, It sucks but is essentially necessary, Mahout 
				//throws an exception when it tries to recommend an 
				//item that does not exists
			}
		}
 	}

	@Override
	protected void preExecute() throws Exception {
		this.suggest = request.getSuggestions();
		this.tags = suggest.getTags();
		suggest.setResource(request.getResource());
	}

	@Override
	protected void postExecute() throws Exception {
		logger.debug("Add providers to request");
		this.request.getTargetProviders().add(getProvider());
		logger.debug("Add suggestons to request");
		this.request.setSuggestions(suggest);
	}

	/**
	 * Handles the recommendation of the recommended items by Mahout, increasing
	 * the weight associated to its labels
	 * @param items Ids (hashCodes) of the recommended labels
	 * @throws AcotaPersistenceException An exception that provides information on a
	 * database access error or other errors.
	 * @throws AcotaConfigurationException ConfigurationException Any exception that 
	 * occurs while initializing a Configuration object
	 */
	protected void handleRecommendLabels(List<RecommendedItem> items) 
			throws AcotaPersistenceException, AcotaConfigurationException
			 {
		Collection<Integer> hashes = getHashCollection(items);
		Set<String> recommendedLabel = labelDao.getLabelsByHashCodes(hashes);
		for (String label : recommendedLabel) {
			handleRecommendLabel(label);
		}
	}

	/**
	 * Transforms a Collection<RecommendedItem> to a Collection<Integer>
	 * @param items Collection of the recommended items by Mahout
	 * @return Collection<Integer> with the ids (hashCodes) of the items
	 */
	protected Collection<Integer> getHashCollection(
			Collection<RecommendedItem> items) {
		Set<Integer> hashes = new HashSet<Integer>();
		for (RecommendedItem item : items) {
			hashes.add(recommenderUtil.fromLongToId(item.getItemID()));
		}
		return hashes;
	}

	/**
	 * Increases the weight of the {@link TagTO} associated to the label
	 * @param label {@link TagTO}'s label
	 * @throws AcotaConfigurationException ConfigurationException Any exception that 
	 * occurs while initializing a Configuration object
	 */
	protected void handleRecommendLabel(String label) throws AcotaConfigurationException {
		TagTO tag = new TagTO(label, languageDetector.detect(label),
				provider,request.getResource());
		fillSuggestions(tag, relevance);
	}

}
