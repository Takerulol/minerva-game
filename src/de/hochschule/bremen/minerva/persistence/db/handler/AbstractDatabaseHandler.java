/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id$
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Christian Bollmann: cbollmann@stud.hs-bremen.de
 *     Carina Strempel: cstrempel@stud.hs-bremen.de
 *     André König: akoenig@stud.hs-bremen.de
 * 
 * Web:
 *     http://minerva.idira.de
 * 
 */
package de.hochschule.bremen.minerva.persistence.db.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;

import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseConnectionException;
import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseDuplicateRecordException;
import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseIOException;
import de.hochschule.bremen.minerva.vo.ValueObject;


public abstract class AbstractDatabaseHandler {
	
	private static Connection connection = null;

	// TODO: Move this data to the 'config.ini'
	private static String databaseName = "minervaDB";
	private static String databaseDriver = "org.apache.derby.jdbc.EmbeddedDriver";
	
	/**
	 * DOCME
	 */
	private void connect() throws DatabaseConnectionException {
		try {
			if (AbstractDatabaseHandler.connection == null) {
				Class.forName(AbstractDatabaseHandler.databaseDriver).newInstance();
				AbstractDatabaseHandler.connection = DriverManager.getConnection("jdbc:derby:"+AbstractDatabaseHandler.databaseName+";create=true");
			}
		} catch (InstantiationException e) {
			throw new DatabaseConnectionException("Problem occured while database driver init.");
		} catch (IllegalAccessException e) {
			throw new DatabaseConnectionException();
		} catch (ClassNotFoundException e) {
			throw new DatabaseConnectionException("The database driver wasn't found. What about the driver jar? Is it available?");
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Unable to connect to the specified database. Check the database configuration 'config.ini'");
		}
	}

	/**
	 * DOCME
	 * 
	 */
	public void disconnect() throws DatabaseConnectionException {
		try {
			if (AbstractDatabaseHandler.connection != null) {
				AbstractDatabaseHandler.connection.close();
			}
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Database disconnect failed.");
		}
	}

	/**
	 * DOCME
	 * @param sql
	 * @param params
	 */
	private PreparedStatement createPreparedStatement(String sql, Object[] params) throws SQLException {
		PreparedStatement statement = AbstractDatabaseHandler.connection.prepareStatement(sql);

		statement.clearParameters();
		for (int i = 0; i < params.length; i++) {
			Object param = params[i];
			Class<? extends Object> paramType = param.getClass();

			if (paramType == java.lang.String.class) {
				statement.setString(i+1, (String)param);
			} else if (paramType == java.lang.Integer.class) {
				statement.setInt(i+1, (Integer)param);
			} else if (paramType == java.lang.Short.class) {
				statement.setShort(i+1, (Short)param);
			}
		}
		
		return statement;
	}

	/**
	 * 
	 * @param sql
	 * @return
	 * @throws DatabaseIOException
	 */
	protected ResultSet select(String sql) throws DatabaseIOException {
		this.connect();

		try {
			Statement statement = AbstractDatabaseHandler.connection.createStatement();
			statement.execute(sql);
			return statement.getResultSet();
		} catch (SQLException e) {
			throw new DatabaseIOException("Error while selecting data from the database: "+e.getMessage() + " - "+e.getErrorCode());
		}
	}
	
	/**
	 * DOCME
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	protected ResultSet select(String sql, Object[] params) throws DatabaseIOException {
		this.connect();

		try {
			PreparedStatement statement = this.createPreparedStatement(sql, params);
			statement.execute();
			return statement.getResultSet();

		} catch (SQLException e) {
			throw new DatabaseIOException("Error while selecting data (with params) from the database: "+e.getMessage() + " - "+e.getErrorCode());
		}
	}

	/**
	 * DOCME
	 * 
	 * @param sql
	 * @throws Exception
	 */
	protected void update(String sql, Object[] params) throws DatabaseIOException {
		this.connect();

		try {
			PreparedStatement statement = this.createPreparedStatement(sql, params);
			statement.execute();
		} catch (SQLException e) {
			switch (e.getErrorCode()) {
				case 20000:
					throw new DatabaseDuplicateRecordException("Error while updating a record. There is already a similar entry in the database.");

				default:
					throw new DatabaseIOException("Error while inserting a new record into the database: "+e.getMessage());
			}
		}		
	}

	/**
	 * DOCME
	 * 
	 * @param sql
	 * @throws Exception
	 */
	protected void delete(String sql, Object[] params) throws DatabaseIOException {
		this.connect();

		try {
			PreparedStatement statement = this.createPreparedStatement(sql, params);
			statement.execute();
		} catch (SQLException e) {
			throw new DatabaseIOException("Error while deleting a record from the database: "+e.getMessage() + "("+e.getErrorCode()+")");
		}
	}

	/**
	 * DOCME
	 * 
	 * @param sql
	 * @throws Exception
	 */
	protected void insert(String sql, Object[] params) throws DatabaseIOException {
		this.connect();

		try {
			PreparedStatement statement = this.createPreparedStatement(sql, params);
			statement.execute();
		} catch (SQLException e) {
			switch (e.getErrorCode()) {
				case 20000:
					throw new DatabaseDuplicateRecordException("Error while inserting a new record. The record exists already.");
				
				default:
					throw new DatabaseIOException("Error while inserting a new record into the database: "+e.getMessage());
			}
		}
	}

	/**
	 * TODO: Move the statements to static array.
	 * Just for testing purposes.
	 */
	public void createDatabaseModel() {
		try {
			Statement statement = AbstractDatabaseHandler.connection.createStatement();

			// Create the "world" table
			statement.execute("CREATE TABLE world (id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY,token CHAR(5) UNIQUE NOT NULL,name CHAR(64) UNIQUE NOT NULL,description VARCHAR(1024) NOT NULL,author VARCHAR(256) NOT NULL,version CHAR(10) NOT NULL,PRIMARY KEY (id))");
			PreparedStatement preparedStatement = AbstractDatabaseHandler.connection.prepareStatement("insert into world (token, name, description, author, version) values (?, ?, ?, ?, ?)");
			preparedStatement.setString(1, "earth");
			preparedStatement.setString(2, "Die Erde");
			preparedStatement.setString(3, "Eine winzige Beschreibung");
			preparedStatement.setString(4, "Andre König");
			preparedStatement.setString(5, "1.0 beta");
			preparedStatement.execute();

			preparedStatement = AbstractDatabaseHandler.connection.prepareStatement("insert into world (token, name, description, author, version) values (?, ?, ?, ?, ?)");
			preparedStatement.setString(1, "jupit");
			preparedStatement.setString(2, "Der Jupiter");
			preparedStatement.setString(3, "Eine Beschreibung der Welt");
			preparedStatement.setString(4, "Christian Bollmann");
			preparedStatement.setString(5, "1.2");
			preparedStatement.execute();
			
			preparedStatement = AbstractDatabaseHandler.connection.prepareStatement("insert into world (token, name, description, author, version) values (?, ?, ?, ?, ?)");
			preparedStatement.setString(1, "mars");
			preparedStatement.setString(2, "Der Mars");
			preparedStatement.setString(3, "Eine weitere Beschreibung");
			preparedStatement.setString(4, "Carina Strempel");
			preparedStatement.setString(5, "1.1");
			preparedStatement.execute();

			statement.execute("CREATE TABLE country (id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY, token CHAR(5) UNIQUE NOT NULL, name CHAR(25) UNIQUE NOT NULL, color CHAR(8) NOT NULL, world INT NOT NULL, continent INT NOT NULL)");

			preparedStatement = AbstractDatabaseHandler.connection.prepareStatement("insert into country (token, name, color, continent, world) values (?, ?, ?, ?, ?)");
			//insert into country (token, name, color, continent, world) values (?, ?, ?, ?, ?)
			preparedStatement.setString(1, "de");
			preparedStatement.setString(2, "Deutschland");
			preparedStatement.setString(3, "00ffffff");
			preparedStatement.setInt(4, 1);
			preparedStatement.setInt(5, 1);
			preparedStatement.execute();

			preparedStatement.setString(1, "es");
			preparedStatement.setString(2, "Spanien");
			preparedStatement.setString(3, "00000000");
			preparedStatement.setInt(4, 1);
			preparedStatement.setInt(5, 1);
			preparedStatement.execute();
			
			preparedStatement.setString(1, "us");
			preparedStatement.setString(2, "United States");
			preparedStatement.setString(3, "00efefef");
			preparedStatement.setInt(4, 2);
			preparedStatement.setInt(5, 1);
			preparedStatement.execute();

			statement.execute("CREATE TABLE continent (id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY, name CHAR(25) UNIQUE NOT NULL)");
			preparedStatement = AbstractDatabaseHandler.connection.prepareStatement("insert into continent (name) values (?)");
			preparedStatement.setString(1, "Mittel-Europa");
			preparedStatement.execute();

			preparedStatement = AbstractDatabaseHandler.connection.prepareStatement("insert into continent (name) values (?)");
			preparedStatement.setString(1, "Nordamerika");
			preparedStatement.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// DOCME
	protected abstract ValueObject resultSetToObject(ResultSet current) throws SQLException;
}