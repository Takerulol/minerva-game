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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
import de.hochschule.bremen.minerva.exceptions.PlayerDoesNotExistException;
import de.hochschule.bremen.minerva.exceptions.PlayerExistsException;
import de.hochschule.bremen.minerva.exceptions.WrongPasswordException;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.persistence.exceptions.PlayerNotFoundException;
import de.hochschule.bremen.minerva.persistence.service.PlayerService;
import de.hochschule.bremen.minerva.vo.Player;

public class AccountManager {

	private static AccountManager instance = null;
	private static String HASH_ALGORITHM = "MD5";
	
	
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
	 * @param player
	 * @throws PlayerExistsException
	 * @throws PersistenceIOException
	 */
	public void createPlayer(Player player) throws PlayerExistsException, PersistenceIOException {
		
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance(HASH_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			
		}
		m.update(player.getPassword().getBytes(), 0, player.getPassword().length() );
		player.setPassword(new BigInteger(1,m.digest()).toString(16));
		
		
		try {
			service.save(player);
		} catch (de.hochschule.bremen.minerva.persistence.exceptions.PlayerExistsException e) {
			throw new PlayerExistsException("There is already a player with that username" +
												" and/or email.");
		} 
	}
	
	/**
	 * Gets all players in database
	 * 
	 * @return
	 * @throws PersistenceIOException
	 */
	public Vector<Player> getPlayerList() throws PersistenceIOException {
		Vector<Player> players = (Vector<Player>)service.loadAll();
		return players;
	}
	
	/**
	 * Gets all players in database and can return all logged in players.
	 * 
	 * @param loggedInPlayers
	 * @return
	 * @throws PersistenceIOException
	 */
	@SuppressWarnings("unchecked")
	public Vector<Player> getPlayerList(boolean loggedInPlayers) throws PersistenceIOException {
		Vector<Player> players = (Vector<Player>)service.loadAll();
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
	 * Gets a player by username.
	 * 
	 * @param username
	 * @return
	 * @throws PlayerDoesNotExistException, PersistenceIOException
	 */
	public Player getPlayer(String username) throws PlayerDoesNotExistException, PersistenceIOException {
		try {
			return service.load(username);
		} catch (PlayerNotFoundException e) {
			throw new PlayerDoesNotExistException("A player with that username does not" +
													" exist.");
		}
	}
	
	/**
	 * Gets a player by id.
	 * 
	 * @param id
	 * @return
	 * @throws PlayerDoesNotExistException, PersistenceIOException
	 */
	public Player getPlayer(int id) throws PlayerDoesNotExistException, PersistenceIOException {
		try {
			return service.load(id);
		} catch (PlayerNotFoundException e) {
			throw new PlayerDoesNotExistException("A player with that id does not" +
													" exist.");
		}
	}
	
	/**
	 * Gets a player by given username or id (if username not given).
	 * 
	 * @param player
	 * @return
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
	 * @param player
	 * @throws WrongPasswordException
	 * @throws PlayerDoesNotExistException
	 * @throws PersistenceIOException
	 */
	public void login(Player player) throws WrongPasswordException, PlayerDoesNotExistException, PersistenceIOException {
		
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance(HASH_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			
		}
		
		Player temp = null;
		try {
			temp = this.getPlayer(player);
		} catch (PlayerNotFoundException e) {
			throw new PlayerDoesNotExistException("A player with that username does not exist.");
		}
		
		m.update(player.getPassword().getBytes(),0,player.getPassword().length());
		String pwTemp = new BigInteger(1,m.digest()).toString(16);
		
		if (pwTemp.equals(temp.getPassword())) {
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
			throw new WrongPasswordException("The password you typed in is wrong.");
		}
		
		
	}
	
	/**
	 * This will logout the desired player, regardless if he is logged in or not.
	 * 
	 * @throws PlayerDoesNotExistException, PersistenceIOException
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
