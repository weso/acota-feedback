package es.weso.acota.persistence;

public interface DBMS {
	public static final String DB_MYSQL = "mysql";
	public static final String DB_MARIADB = "mariadb";
	public static final String DB_POSTGRESQL = "postgresql";
	public static final String DB_MONGODB = "mongodb";
	
	public static final String DB_MYSQL_PORT = "3306";
	public static final String DB_MARIADB_PORT = "3306";
	public static final String DB_POSTGRESQL_PORT = "5432";
	public static final String DB_MONGODB_PORT = "27017";
}
