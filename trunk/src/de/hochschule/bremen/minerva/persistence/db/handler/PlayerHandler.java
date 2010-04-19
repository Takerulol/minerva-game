/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: CountryHandler.java 85 2010-04-18 14:24:04Z andre.koenig $
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import de.hochschule.bremen.minerva.persistence.Crudable;
import de.hochschule.bremen.minerva.persistence.FilterParameter;
import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseDuplicateRecordException;
import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseIOException;
import de.hochschule.bremen.minerva.persistence.exceptions.PlayerExistsException;
import de.hochschule.bremen.minerva.persistence.exceptions.PlayerNotFoundException;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.ValueObject;

//TODO: DOCME of the whole class PlayerHandler and the last method 

/**
 * DOCME
 */
public class PlayerHandler extends AbstractDatabaseHandler implements Crudable {

	private final static HashMap<String, String> sql = new HashMap<String, String>();

	static {
		sql.put("selectByUsername", "select id, username, password, last_name, first_name, email, last_login from player where username = ?");
		sql.put("selectById", "select id, username, password, last_name, first_name, email, last_login from player where id = ?");
		sql.put("selectAll", "select id, username, password, last_name, first_name, email, last_login from player order by username");
		sql.put("insert", "insert into player (username, password, last_name, first_name, email, last_login) values (?, ?, ?, ?, ?, ?)");
		sql.put("update", "update player set username = ?, password = ?, last_name = ?, first_name = ?, email = ?, last_login = ? where username = ?");
		sql.put("delete", "delete from player where username = ?");
	}


	/**
	 * Reads ONE player with the given id from the database.
	 * 
	 * @param id - The player id.
	 * @return player
	 * @throws PlayerNotFoundException, PersistenceIOException
	 */
	@Override
	public Player read(FilterParameter id) throws PersistenceIOException {
		Player player = null;
		Object[] params = {id.getInt()};
		
		try {
			ResultSet record = this.select(sql.get("selectById"), params);
			if (record.next()) {
				player = this.resultSetToObject(record);
				record.close();
			} else {
				throw new PlayerNotFoundException("Found no player with username: '"
												   +id+"'.");
			}

		} catch (DatabaseIOException e) {
			throw new PersistenceIOException(e.getMessage());
		} catch (SQLException e) {
			throw new PersistenceIOException("Error occurred while reading "
					                       + "the player (username=" +id+") "
					                       + "from the database.");
		}
		
		return player;
	}

	/**
	 * Reads ALL player from the database.
	 * 
	 * @return player
	 * @throws PersistenceIOException
	 */
	@Override
	public Vector<Player> readAll() throws PersistenceIOException {
		Vector<Player> players = new Vector<Player>();

		try {
			ResultSet record = this.select(sql.get("selectAll"));

			while (record.next()) {
				players.add(this.resultSetToObject(record));
			}

			record.close();
		} catch (DatabaseIOException e) {
			throw new PersistenceIOException(e.getMessage());
		} catch (SQLException e) {
			throw new PersistenceIOException("Error occurred while receiving a player list from the database: "
											 +e.getMessage()+" - "+e.getErrorCode());
		}
		
		return players;
	}


	/**
	 * Method remove to delete attributes of the player in the database.
	 * 
	 * @param candidate
	 * @throws PersistenceIOException 
	 */
	@Override
	public void remove(ValueObject candidate) throws PersistenceIOException {
		Player deletablePlayer = (Player)candidate;
		Object[] params = {deletablePlayer.getId()};

		try {
			this.delete(sql.get("delete"), params);
		} catch (DatabaseIOException e) {
			throw new PersistenceIOException(e.getMessage());
		}
		
	}

	/**
	 * Save attributes of the player in the database. 
	 * If it is a new player, the player will be insert in the database.
	 * If it is an already existing player, the attributes of the player will be update in the database.
	 * 
	 * @param registrable
	 * @throws PlayerExistsException, PersistenceIOException
	 */
	@Override
	public void save(ValueObject registrable) throws PersistenceIOException {
		Player registrablePlayer = (Player)registrable;

		try {
			Object[] params = {
				registrablePlayer.getUsername(),
				registrablePlayer.getPassword(),
				registrablePlayer.getLastName(),
				registrablePlayer.getFirstName(),
				registrablePlayer.getEmail(),
				registrablePlayer.getLastLogin()
			};

			this.insert(sql.get("insert"), params);
		} catch (DatabaseIOException e) {
			try {
				Object[] params = {
						registrablePlayer.getUsername(),
						registrablePlayer.getPassword(),
						registrablePlayer.getLastName(),
						registrablePlayer.getFirstName(),
						registrablePlayer.getEmail(),
						registrablePlayer.getLastLogin()
				};

				this.update(sql.get("update"), params);
			} catch (DatabaseDuplicateRecordException exe) {
				throw new PlayerExistsException("Unable to serialize the "
												+"player. There is already "
												+"a similar one.");
			} catch (DatabaseIOException ex) {
				throw new PersistenceIOException("Unable to serialize the "
												+"player object: "
												+ex.getMessage());
			}
		}
	}
	
	/**
	 * Get all attributes of the player from the database.
	 * 
	 * @param current
	 * @throws SQLException
	 * @return player
	 */
	@Override
	protected Player resultSetToObject(ResultSet current) throws SQLException {
		Player player = new Player();
		
		player.setId(current.getInt(1));
		player.setUsername(current.getString(2).trim());
		player.setPassword(current.getString(3).trim());
		player.setLastName(current.getString(4).trim());
		player.setFirstName(current.getString(5).trim());
		player.setEmail(current.getString(6).trim());
		player.setLastLogin(current.getString(7).trim());
		
		return player;
	}

	/**
	 * DOCME
	 *
	 * @param reference
	 * @throws PersistenceIOException
	 * @return null
	 */
	@Override
	public Vector<Player> readAll(ValueObject reference) throws PersistenceIOException {
		// TODO Auto-generated method stub
		return null;
	}
}
