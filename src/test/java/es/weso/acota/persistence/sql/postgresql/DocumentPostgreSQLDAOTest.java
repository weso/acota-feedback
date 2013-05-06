package es.weso.acota.persistence.sql.postgresql;

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
import es.weso.acota.persistence.DocumentDAO;
import es.weso.acota.persistence.sql.DocumentSQLDAO;

public class DocumentPostgreSQLDAOTest extends PostgreSQLDAOTest{
	
	private DocumentDAO documentDao;
	
	@Before
	public void init() throws Exception {
		super.init();
		FeedbackConfiguration feedback = new FeedbackConfiguration();
		feedback.setDatabaseType(DBMS.DB_POSTGRESQL);
		this.documentDao = new DocumentSQLDAO(feedback); 
	}

	@After
	public void after() throws Exception {
		super.init();
	}

	@Test
	public void saveDocumentTest() throws Exception {
		documentDao.saveDocument("foo".hashCode(), "foo");

		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("documents");

		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(this
				.getClass().getResource("/resources/dbunitExpected.xml"));
		ITable expectedTable = expectedDataSet.getTable("documents");

		assertEquals(expectedTable.getRowCount(), actualTable.getRowCount());

	}
	
	@Test
	public void getDocumentByIdThatNotExists() throws AcotaPersistenceException {
		assertEquals(null, documentDao.getDocumentById(1000));
	}
	
	@Test
	public void getDocumentByIdTest() throws AcotaPersistenceException {
		assertEquals("http://www.weso.es", documentDao.getDocumentById(-220345143));
	}
	
	@Test
	public void getDocumentByHashThatNotExists() throws AcotaPersistenceException {
		assertEquals(null, documentDao.getDocumentByHashCode(1000));
	}
	
	@Test
	public void getDocumentByHashTest() throws AcotaPersistenceException {
		assertEquals("http://www.weso.es", documentDao.getDocumentByHashCode(-220345143));
	}
	
	@Test
	public void getDocumentsByIdsEmpty() throws AcotaPersistenceException {
		assertEquals(Collections.EMPTY_SET, documentDao.getDocumentsByIds(Collections.<Integer> emptySet()));
	}
	
	@Test
	public void getDocumentsByIdsThatNotExist() throws AcotaPersistenceException {
		Set<Integer> ids = new HashSet<Integer>();
		ids.add(9238424);
		ids.add(93247);
		assertEquals(Collections.EMPTY_SET, documentDao.getDocumentsByHashCodes(ids));
	}
	
	@Test
	public void getDocumentsByIdsTest()  throws AcotaPersistenceException {
		Set<String> Documents = new HashSet<String>();
		Documents.add("http://www.weso.es");
		Documents.add("http://www.example.es");
		
		Set<Integer> ids = new HashSet<Integer>();
		ids.add(-220345143);
		ids.add(778016507);
		assertEquals(Documents, documentDao.getDocumentsByIds(ids));
	}
	
	@Test
	public void getDocumentsByHashesEmpty() throws AcotaPersistenceException {
		assertEquals(Collections.EMPTY_SET, documentDao.getDocumentsByHashCodes(Collections.<Integer> emptySet()));
	}
	
	@Test
	public void getDocumentsByHashesThatNotExist() throws AcotaPersistenceException {
		Set<Integer> hashes = new HashSet<Integer>();
		hashes.add(9238424);
		hashes.add(93247);
		assertEquals(Collections.EMPTY_SET, documentDao.getDocumentsByHashCodes(hashes));
	}
	
	@Test
	public void getDocumentsByHashesTest() throws AcotaPersistenceException {
		Set<String> Documents = new HashSet<String>();
		Documents.add("http://www.weso.es");
		Documents.add("http://www.example.es");
		
		Set<Integer> hashes = new HashSet<Integer>();
		hashes.add(-220345143);
		hashes.add(778016507);
		assertEquals(Documents, documentDao.getDocumentsByHashCodes(hashes));
	}
	
}
