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
			throw new DatabaseConnectionException("Unable to connect to the specified database. Is it locked? Is the 'sqlexplorer' currently running? ;)");
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
	protected void insert(String sql, Object[] params) throws DatabaseIOException {
		this.connect();

		try {
			PreparedStatement statement = this.createPreparedStatement(sql, params);
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			switch (e.getErrorCode()) {
				case 30000:
					throw new DatabaseDuplicateRecordException("Error while inserting a new record. The record exists already.");

				case 20000:
					throw new DatabaseDuplicateRecordException("Error while inserting a new record. The record exists already.");
				
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
	protected void update(String sql, Object[] params) throws DatabaseIOException {
		this.connect();

		try {
			PreparedStatement statement = this.createPreparedStatement(sql, params);
			statement.executeUpdate();

			statement.close();
		} catch (SQLException e) {
			switch (e.getErrorCode()) {
				case 30000:	
					throw new DatabaseDuplicateRecordException("Error while updating a record. There is already a similar entry in the database.");

				case 20000:
					throw new DatabaseDuplicateRecordException("Error while updating a record. There is already a similar entry in the database.");

				default:
					throw new DatabaseIOException("Error while updating a record: "+e.getMessage());
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
	
	// DOCME
	protected abstract ValueObject resultSetToObject(ResultSet current) throws SQLException;
}