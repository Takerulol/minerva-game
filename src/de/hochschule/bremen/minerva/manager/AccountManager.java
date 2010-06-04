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

package de.hochschule.bremen.minerva.manager;

import java.util.Vector;

import de.hochschule.bremen.minerva.exceptions.PlayerAlreadyLoggedInException;
import de.hochschule.bremen.minerva.exceptions.PlayerDoesNotExistException;
import de.hochschule.bremen.minerva.exceptions.PlayerExistsException;
import de.hochschule.bremen.minerva.exceptions.WrongPasswordException;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.persistence.exceptions.PlayerNotFoundException;
import de.hochschule.bremen.minerva.persistence.service.PlayerService;
import de.hochschule.bremen.minerva.util.HashTool;
import de.hochschule.bremen.minerva.vo.Player;

/**
 * The login and registration subsystem.
 *
 * <br />
 * Usage:
 * 
 * <pre>
 * AccountManager.getInstance().<theMethod>
 * </pre>
 * 
 * @since 1.0
 * @version $Id$
 * 
 */
public class AccountManager {

	private static AccountManager instance = null;	
	
	private PlayerService service = PlayerService.getInstance();

	/**
	 * Singleton pattern. It is not possible
	 * to create a CountryService in the common way.
	 * So this constructor is private.
	 * 
	 */
	private AccountManager() {}
	
	/**
	 * Singleton pattern.
	 * Static method that controls the object creation.
	 * 
	 * @return DOCME
	 */
	public static AccountManager getInstance() {
		if (AccountManager.instance == null) {
			AccountManager.instance = new AccountManager();
		}
		return AccountManager.instance;
	}
	
	/**
	 * Adds the desired player into the database.
	 * 
	 * @param player Player object to create an entry in the database with.
	 * @throws PlayerExistsException Thrown if the player or email already exists.
	 * @throws PersistenceIOException
	 */
	public void createPlayer(Player player) throws PlayerExistsException, PersistenceIOException {
		player.setPassword(HashTool.md5(player.getPassword()));

		try {
			service.save(player);
		} catch (de.hochschule.bremen.minerva.persistence.exceptions.PlayerExistsException e) {
			throw new PlayerExistsException(player);
		} 
	}
	
	/**
	 * Gets all players in database
	 * 
	 * @return Vector of all registered players in the database.
	 * @throws PersistenceIOException
	 */
	public Vector<Player> getPlayerList() throws PersistenceIOException {
		Vector<Player> players = (Vector<Player>)service.findAll();
		return players;
	}
	
	/**
	 * Gets all players in database and can return all logged in players.
	 * 
	 * @param loggedInPlayers True if you want to get all logged in players.
	 * @return Vector of all registered players in the database. If loggedInPlayers == true you will only get the logged in players.
	 * @throws PersistenceIOException
	 */
	@SuppressWarnings("unchecked")
	public Vector<Player> getPlayerList(boolean loggedInPlayers) throws PersistenceIOException {
		Vector<Player> players = (Vector<Player>)service.findAll();
		if (loggedInPlayers && (players != null)) {
			Vector<Player> temp = (Vector<Player>) players.clone();
			for (Player player : players) {
				if (!player.isLoggedIn()) {
					temp.remove(player);
				}
			}
			players = temp;
		}
		return players;
	}
	
	/**
	 * Gets a player by username out of the database.
	 * 
	 * @param username Username of the player you want to get.
	 * @return Player object of the desired player.
	 * @throws PlayerDoesNotExistException, PersistenceIOException
	 */
	public Player getPlayer(String username) throws PlayerDoesNotExistException, PersistenceIOException {
		try {
			return service.find(username);
		} catch (PlayerNotFoundException e) {
			throw new PlayerDoesNotExistException(username);
		}
	}
	
	/**
	 * Gets a player by id out of the database.
	 * 
	 * @param id Id of the player you want to get.
	 * @return Player object of the desired player.
	 * @throws PlayerDoesNotExistException, PersistenceIOException
	 */
	public Player getPlayer(int id) throws PlayerDoesNotExistException, PersistenceIOException {
		try {
			return service.find(id);
		} catch (PlayerNotFoundException e) {
			throw new PlayerDoesNotExistException(id);
		}
	}
	
	/**
	 * Gets a player by given username or id (if username not given).
	 * 
	 * @param player Player object you want to fully get out of database.
	 * @return Player object of the desired player.
	 * @throws PersistenceIOException
	 */
	public Player getPlayer(Player player) throws PersistenceIOException, PlayerDoesNotExistException {
		
		if (player.getUsername() != null) {
			player = this.getPlayer(player.getUsername());
		} else if (player.getId() > 0) {
			try {
				player = this.getPlayer(player.getId());
			} catch (PlayerDoesNotExistException e) {
				throw new PlayerDoesNotExistException("Username not given and a player with" +
														" that id does not exist.");
			}
		} else {
			throw new PlayerDoesNotExistException("The player doesn't contain an id or a" +
													" username.");
		}
		
		return player;
	}
	
	
	/**
	 * The desired player will be logged in.
	 * 
	 * @param player Player you want to login.
	 * @throws WrongPasswordException Password typed in didn't match.
	 * @throws PlayerDoesNotExistException Player you want to login doesn't exist.
	 * @throws PersistenceIOException
	 */
	public void login(Player player) throws PlayerAlreadyLoggedInException, WrongPasswordException, PlayerDoesNotExistException, PersistenceIOException {
		Player temp = null;
		try {
			temp = this.getPlayer(player);
			if (temp.isLoggedIn()) {
				throw new PlayerAlreadyLoggedInException(temp);
			}
		} catch (PlayerNotFoundException e) {
			throw new PlayerDoesNotExistException(player);
		}

		String hashedPassword = HashTool.md5(player.getPassword());

		if (hashedPassword.equals(temp.getPassword())) {
			temp.setLoggedIn(true);
			service.save(temp);
			
			// TODO: Refactor and move this to Player#copy(Player player)
			player.setId(temp.getId());
			player.setUsername(temp.getUsername());
			player.setFirstName(temp.getFirstName());
			player.setLastName(temp.getLastName());
			player.setEmail(temp.getEmail());
			player.setLoggedIn(temp.isLoggedIn());
			
		} else {
			throw new WrongPasswordException(player);
		}
		
		
	}
	
	/**
	 * This will logout the desired player, regardless if he is logged in or not.
	 * 
	 * @throws PlayerDoesNotExistException Player you want to logout doesn't exist.
	 * @throws PersistenceIOException
	 */
	public void logout(Player player) throws PlayerDoesNotExistException, PersistenceIOException {
		try {
			player = this.getPlayer(player);
		} catch (PlayerDoesNotExistException e) {
			throw new PlayerDoesNotExistException("You can't logout a player that does not exist.");
		}
		player.setLoggedIn(false);
		service.save(player);
	}
	
	/**
	 * All logged in players will be logged out.
	 * 
	 * @throws PersistenceIOException
	 */
	public void logout() throws PersistenceIOException {
		Vector<Player> loggedInPlayers = this.getPlayerList(true);
		for (Player player : loggedInPlayers) {
			player.setLoggedIn(false);
			service.save(player);
		}
	}
}
