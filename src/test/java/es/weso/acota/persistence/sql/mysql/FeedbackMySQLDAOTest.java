package es.weso.acota.persistence.sql.mysql;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.entity.persistence.Feedback;
import es.weso.acota.persistence.DBMS;
import es.weso.acota.persistence.FeedbackDAO;
import es.weso.acota.persistence.sql.FeedbackSQLDAO;

public class FeedbackMySQLDAOTest extends MySLQDAOTest{

	protected FeedbackDAO feedbackDao;
	
	@Before
	public void init() throws Exception {
		super.init();
		FeedbackConfiguration feedback = new FeedbackConfiguration();
		feedback.setDatabaseType(DBMS.DB_MYSQL);
		this.feedbackDao = new FeedbackSQLDAO(feedback);
	}
	
	@After
	public void after() throws Exception {
		super.after();
	}
	
	@Test
	public void saveFeedbackTest() throws Exception {
		Feedback feedback = new Feedback(4, 4, "demo", "http://www.example.es",
				new Date(1351106647000L));
		feedbackDao.saveFeedback(feedback);
		
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("feedbacks");

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(this.getClass().getResource(
				"/resources/dbunitExpected.xml"));
        ITable expectedTable = expectedDataSet.getTable("feedbacks");

        assertEquals(expectedTable.getRowCount(), actualTable.getRowCount());
	
	}

	@Test
	public void getAllFeedbacksTest() throws Exception {
		Set<Feedback> feedbacks = new HashSet<Feedback>();
		feedbacks.add(new Feedback(3, 2, "group", "http://www.example.es",
				new Date(1351106647000L)));
		feedbacks.add(new Feedback(1, 1, "research", "http://www.weso.es",
				new Date(1351106647000L)));
		feedbacks.add(new Feedback(2, 2, "research", "http://www.weso.es",
				new Date(1351106647000L)));
		assertEquals(feedbacks, feedbackDao.getAllFeedbacks());
	}

	@Test
	public void getAllFeedbacksByUserIdEmpty() throws Exception {
		assertEquals(Collections.EMPTY_SET, feedbackDao.getFeedbacksByUserId(0));
	}

	@Test
	public void getAllFeedbacksByUserIdTest() throws Exception {
		Set<Feedback> feedbacks = new HashSet<Feedback>();
		feedbacks.add(new Feedback(1, 1, "research", "http://www.weso.es",
				new Date(1351106647000L)));
		assertEquals(feedbacks, feedbackDao.getFeedbacksByUserId(1));
	}

	@Test
	public void getAllFeedbacksByLabelEmpty() throws Exception {
		assertEquals(Collections.EMPTY_SET,
				feedbackDao.getFeedbacksByLabel("nolabel"));
	}

	@Test
	public void getAllFeedbacksByLabelTest() throws Exception {
		Set<Feedback> feedbacks = new HashSet<Feedback>();
		feedbacks.add(new Feedback(1, 1, "research", "http://www.weso.es",
				new Date(1351106647000L)));
		feedbacks.add(new Feedback(2, 2, "research", "http://www.weso.es",
				new Date(1351106647000L)));

		assertEquals(feedbacks, feedbackDao.getFeedbacksByLabel("research"));
	}

	@Test
	public void getAllFeedbacksByDocumentEmpty() throws Exception {
		assertEquals(Collections.EMPTY_SET,
				feedbackDao.getFeedbacksByDocument("http://www.nodocument.es"));
	}

	@Test
	public void getAllFeedbacksByDocumentTest() throws Exception {
		Set<Feedback> feedbacks = new HashSet<Feedback>();
		feedbacks.add(new Feedback(3, 2, "group", "http://www.example.es",
				new Date(1351106647000L)));

		assertEquals(feedbacks,
				feedbackDao.getFeedbacksByDocument("http://www.example.es"));
	}
	
}