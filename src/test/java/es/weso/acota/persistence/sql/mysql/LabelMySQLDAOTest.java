package es.weso.acota.persistence.sql.mysql;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.weso.acota.core.FeedbackConfiguration;
import es.weso.acota.core.exceptions.AcotaPersistenceException;

import es.weso.acota.persistence.DBMS;
import es.weso.acota.persistence.LabelDAO;
import es.weso.acota.persistence.sql.LabelSQLDAO;

public class LabelMySQLDAOTest extends MySLQDAOTest{
	
	private LabelDAO labelDao;
	
	@Before
	public void init() throws Exception {
		super.init();
		FeedbackConfiguration feedback = new FeedbackConfiguration();
		feedback.setDatabaseType(DBMS.DB_MYSQL);
		this.labelDao = new LabelSQLDAO(feedback); 
	}

	@After
	public void after() throws Exception {
		super.after();
	}
	
	@Test
	public void saveLabelTest() throws Exception {
		labelDao.saveLabel("foo".hashCode(), "foo");

		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("labels");

		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(this
				.getClass().getResource("/resources/dbunitExpected.xml"));
		ITable expectedTable = expectedDataSet.getTable("labels");

		assertEquals(expectedTable.getRowCount(), actualTable.getRowCount());

	}
	
	@Test
	public void getLabelByIdThatNotExists() throws AcotaPersistenceException {
		assertEquals(null, labelDao.getLabelById(1000));
	}
	
	@Test
	public void getLabelByIdTest() throws AcotaPersistenceException {
		assertEquals("group", labelDao.getLabelById(98629247));
	}
	
	@Test
	public void getLabelByHashThatNotExists() throws AcotaPersistenceException {
		assertEquals(null, labelDao.getLabelByHash(1000));
	}
	
	@Test
	public void getLabelByHashTest() throws AcotaPersistenceException {
		assertEquals("group", labelDao.getLabelByHash(98629247));
	}
	
	@Test
	public void getLabelsByIdsEmpty() throws AcotaPersistenceException {
		assertEquals(Collections.EMPTY_SET, labelDao.getLabelsByIds(Collections.<Integer> emptySet()));
	}
	
	@Test
	public void getLabelsByIdsThatNotExist() throws AcotaPersistenceException {
		Set<Integer> ids = new HashSet<Integer>();
		ids.add(9238424);
		ids.add(93247);
		assertEquals(Collections.EMPTY_SET, labelDao.getLabelsByHashCodes(ids));
	}
	
	@Test
	public void getLabelsByIdsTest() throws AcotaPersistenceException {
		Set<String> labels = new HashSet<String>();
		labels.add("group");
		labels.add("research");
		
		Set<Integer> ids = new HashSet<Integer>();
		ids.add(98629247);
		ids.add(-350895717);
		assertEquals(labels, labelDao.getLabelsByIds(ids));
	}
	
	@Test
	public void getLabelsByHashesEmpty() throws AcotaPersistenceException {
		assertEquals(Collections.EMPTY_SET, labelDao.getLabelsByHashCodes(Collections.<Integer> emptySet()));
	}
	
	@Test
	public void getLabelsByHashesThatNotExist() throws AcotaPersistenceException {
		Set<Integer> hashes = new HashSet<Integer>();
		hashes.add(9238424);
		hashes.add(93247);
		assertEquals(Collections.EMPTY_SET, labelDao.getLabelsByHashCodes(hashes));
	}
	
	@Test
	public void getLabelsByHashesTest() throws AcotaPersistenceException {
		Set<String> labels = new HashSet<String>();
		labels.add("group");
		labels.add("research");
		
		Set<Integer> hashes = new HashSet<Integer>();
		hashes.add(98629247);
		hashes.add(-350895717);
		assertEquals(labels, labelDao.getLabelsByHashCodes(hashes));
	}
	
}
