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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import de.hochschule.bremen.minerva.persistence.Handler;
import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseDuplicateRecordException;
import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseIOException;
import de.hochschule.bremen.minerva.persistence.exceptions.PlayerExistsException;
import de.hochschule.bremen.minerva.persistence.exceptions.PlayerNotFoundException;
import de.hochschule.bremen.minerva.persistence.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.ValueObject;

//TODO: DOCME of the whole class PlayerHandler and the last method 

/**
 * DOCME
 */
public class PlayerHandler extends AbstractDatabaseHandler implements Handler {

	private final static HashMap<String, String> sql = new HashMap<String, String>();

	static {
		sql.put("selectById", "select \"id\", \"username\", \"password\", \"last_name\", \"first_name\", \"email\", \"logged_in\" from player where \"id\" = ?");
		sql.put("selectByName", "select \"id\", \"username\", \"password\", \"last_name\", \"first_name\", \"email\", \"logged_in\" from player where \"username\" = ?");
		sql.put("selectAll", "select \"id\", \"username\", \"password\", \"last_name\", \"first_name\", \"email\", \"logged_in\" from player order by \"username\"");
		sql.put("insert", "insert into player (\"username\", \"password\", \"last_name\", \"first_name\", \"email\") values (?, ?, ?, ?, ?)");
		sql.put("update", "update player set \"username\" = ?, \"password\" = ?, \"last_name\" = ?, \"first_name\" = ?, \"email\" = ?, \"logged_in\" = ? where \"id\" = ?");
		sql.put("delete", "delete from player where \"id\" = ?");
	}


	/**
	 * Reads ONE player with the given id from the database.
	 * 
	 * @param id - The player id.
	 * @return player
	 * @throws PlayerNotFoundException, PersistenceIOException
	 * 
	 */
	@Override
	public Player read(int id) throws DataAccessException {
		Player player = null;
		Object[] params = {id};

		try {
			player = this.read(sql.get("selectById"), params);
		} catch (PlayerNotFoundException e) {
			throw new PlayerNotFoundException("The player with the id '"+id+"' wasn't found.");
		} catch (DatabaseIOException e) {
			throw new DataAccessException("Error occurred while reading "
					                       + "the player (id=" + id +") "
					                       + "from the database. Reason: "+e.getMessage());
		}

		return player;
	}

	/**
	 * Reads ONE player with the given name from the database.
	 * 
	 * @param name - The username.
	 * @return Player object from the database.
	 */
	public Player read(String name) throws DataAccessException {
		Player player = null;
		Object[] params = {name};

		try {
			player = this.read(sql.get("selectByName"), params);
		} catch (PlayerNotFoundException e) {
			throw new PlayerNotFoundException("The player with the username '"+name+"' wasn't found.");
		} catch (DatabaseIOException e) {
			throw new DataAccessException("Error occurred while reading "
					                       + "the player (username=" + name +") "
					                       + "from the database. Reason: "+e.getMessage());
		}
		
		return player;
	}

	/**
	 * DOCME
	 * 
	 * @param sql
	 * @param params - Object array with parameters for the prepared statement.
	 * 
	 */
	private Player read(String sql, Object[] params) throws PlayerNotFoundException, DatabaseIOException {
		Player player = null;

		try {
			ResultSet record = this.select(sql, params);

			if (record.next()) {
				player = this.resultSetToObject(record);
				record.close();
			} else {
				throw new PlayerNotFoundException();
			}
		} catch (SQLException e) {
			throw new DatabaseIOException("Error while reading from result set: " + e.getErrorCode());
		}

		return player;
	}
	
	/**
	 * Reads ALL player from the database.
	 * 
	 * @return player
	 * @throws DataAccessException
	 */
	@Override
	public Vector<Player> readAll() throws DataAccessException {
		Vector<Player> players = new Vector<Player>();

		try {
			ResultSet record = this.select(sql.get("selectAll"));

			while (record.next()) {
				players.add(this.resultSetToObject(record));
			}

			record.close();
		} catch (DatabaseIOException e) {
			throw new DataAccessException(e.getMessage());
		} catch (SQLException e) {
			throw new DataAccessException("Error occurred while receiving a player list from the database: "
											 +e.getMessage()+" - "+e.getErrorCode());
		}
		
		return players;
	}


	/**
	 * Method remove to delete attributes of the player in the database.
	 * 
	 * @param candidate
	 * @throws DataAccessException 
	 */
	@Override
	public void remove(ValueObject candidate) throws DataAccessException {
		Player deletablePlayer = (Player)candidate;
		Object[] params = {deletablePlayer.getId()};

		try {
			this.delete(sql.get("delete"), params);
		} catch (DatabaseIOException e) {
			throw new DataAccessException(e.getMessage());
		}
		
	}

	/**
	 * Save attributes of the player in the database. 
	 * If it is a new player, the player will be insert in the database.
	 * If it is an already existing player, the attributes of the player will be update in the database.
	 * 
	 * @param player
	 * @throws PlayerExistsException, PersistenceIOException
	 */
	@Override
	public void save(ValueObject player) throws DataAccessException {
		Player registrablePlayer = (Player)player;

		try {
			try {
				// We try to load the player by the given id.
				// When this is not possible (PlayerNotFoundException), we
				// will update the record else we will insert it.
				this.read(registrablePlayer.getId());
				
				Object[] params = {
					registrablePlayer.getUsername(),
					registrablePlayer.getPassword(),
					registrablePlayer.getLastName(),
					registrablePlayer.getFirstName(),
					registrablePlayer.getEmail(),
					((registrablePlayer.isLoggedIn()) ? 1 : 0),
					registrablePlayer.getId()
				};

				this.update(sql.get("update"), params);
			} catch (PlayerNotFoundException e) {
				Object[] params = {
					registrablePlayer.getUsername(),
					registrablePlayer.getPassword(),
					registrablePlayer.getLastName(),
					registrablePlayer.getFirstName(),
					registrablePlayer.getEmail(),
				};

				this.insert(sql.get("insert"), params);
			}
		} catch (DatabaseDuplicateRecordException ex) {
			throw new PlayerExistsException("Unable to serialize the "
					+"player: '"+registrablePlayer.getUsername()+"'. There is already "
					+"a similar one.");
		} catch (DatabaseIOException e) {
			throw new DataAccessException("Unable to serialize the player: '"+registrablePlayer.getUsername()+"'. Reason: "+e.getMessage());			
		}

		// The player does not have a player id.
		// So we read the player object by the given username
		// to fulfill the referenced player value object.
		registrablePlayer.setId(this.read(registrablePlayer.getUsername()).getId());
		player = registrablePlayer;
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
		player.setUsername(current.getString(2));
		player.setPassword(current.getString(3));
		player.setLastName(current.getString(4));
		player.setFirstName(current.getString(5));
		player.setEmail(current.getString(6));
		player.setLoggedIn((current.getShort(7)) == 1); 
		
		return player;
	}

	/**
	 * DOCME
	 *
	 * @param reference
	 * @throws DataAccessException
	 * @return null
	 */
	@Override
	public Vector<Player> readAll(ValueObject reference) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}
}