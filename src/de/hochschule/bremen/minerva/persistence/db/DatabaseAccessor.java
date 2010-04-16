/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: WorldHandler.java 44 2010-04-07 10:20:55Z andre.koenig $
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
package de.hochschule.bremen.minerva.persistence.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import de.hochschule.bremen.minerva.exceptions.NoDataChangedException;

/*
 * TODO:
 * 	- sinnlose test-texte entfernen
 *  - mainklasse entfernen
 *  - vll so verschachteln, dass nur ein objekt erzeugt werden kann
 *  - select, update, insert, delete verbessern
 */


public class DatabaseAccessor {
	
	protected static Connection connection = null;
	protected static Statement statement = null;
	protected static ResultSet resultSet = null;
	
	private static String ip = "localhost";
	private static String folder = "/test";
	private static String driver = "org.apache.derby.jdbc.ClientDriver";
	
	private DatabaseAccessor() {}
	
	/*
	public static void main (String[] args) {
		DatabaseAccessor test = new DatabaseAccessor();
		
		try {
			test.connect();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Konnte nicht connecten.");
		} finally {
			test.disconnect();
			System.out.println("fertig.");
		}
	}
	*/
	
	public static void main (String[] args) {
		DatabaseAccessor test = new DatabaseAccessor();
		try {
			test.connect();
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			test.disconnect();
		}

	}
	
	
	/**
	 * DOCME
	 * 
	 * @throws Exception
	 */
	private void connect() throws Exception {
		try {
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection("jdbc:derby://"+ip+folder+";create=true");
		} catch (Exception e) {
			throw e;
		}
		System.out.println("alles paletti1");
	}

	/**
	 * DOCME
	 * 
	 */
	private void disconnect() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("alles paletti2");
	}

	/**
	 * DOCME
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	protected ResultSet select(String sql) throws Exception {
		/*
		 * Handling with resultSets:
		 * while (resultSet.next()) {
		 *	String user = resultSet.getString("name");
		 *	String number = resultSet.getString("number");
		 *	System.out.println("User: " + user);
		 *	System.out.println("ID: " + number);
		 * }
		 */
		
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			return resultSet;
		} catch (Exception e) {
			throw e;
		}	
	}

	/**
	 * DOCME
	 * 
	 * @param sql
	 * @throws Exception
	 */
	protected void update(String sql) throws Exception {
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			//resultSet = statement.executeQuery();
			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		}	
	}

	/**
	 * DOCME
	 * 
	 * @param sql
	 * @throws Exception
	 */
	protected void delete(String sql) throws Exception {
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			//resultSet = statement.executeQuery();
			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		}	
	}

	/**
	 * DOCME
	 * 
	 * @param sql
	 * @throws Exception
	 */
	protected void insert(String sql) throws Exception {
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			//resultSet = statement.executeQuery();
			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		}	
	}

	public static void setIp(String ip) {
		DatabaseAccessor.ip = ip;
	}
	public static String getIp() {
		return ip;
	}
	public static void setFolder(String folder) {
		DatabaseAccessor.folder = "/"+folder;
	}
	public static String getFolder() {
		return folder;
	}	
}
