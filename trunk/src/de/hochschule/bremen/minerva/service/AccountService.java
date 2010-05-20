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

package de.hochschule.bremen.minerva.service;

import java.util.Vector;
import de.hochschule.bremen.minerva.exceptions.PlayerDoesNotExistException;
import de.hochschule.bremen.minerva.exceptions.PlayerExistsException;
import de.hochschule.bremen.minerva.exceptions.WrongPasswordException;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.persistence.service.PlayerService;
import de.hochschule.bremen.minerva.vo.Player;

public class AccountService {

	private static AccountService instance = null;

	private PlayerService service = PlayerService.getInstance();

	/**
	 * Singleton pattern. It is not possible
	 * to create a CountryService in the common way.
	 * So this constructor is private.
	 * 
	 */
	private AccountService() {}
	
	/**
	 * Singleton pattern.
	 * Static method that controls the object creation.
	 * 
	 * @return DOCME
	 */
	public static AccountService getInstance() {
		if (AccountService.instance == null) {
			AccountService.instance = new AccountService();
		}
		return AccountService.instance;
	}
	
	/*
	 * TODO:
	 * 	- implement methods
	 * 	- docs
	 */
	
	/**
	 * DOCME
	 * @param player
	 * @throws PlayerExistsException
	 * @throws PersistenceIOException
	 */
	public void createPlayer(Player player) throws PlayerExistsException, PersistenceIOException {
		// TODO: passwort, etc abfragen
		service.save(player);
	}
	
	/**
	 * DOCME
	 * @return
	 * @throws PersistenceIOException
	 */
	@SuppressWarnings("unchecked")
	public Vector<Player> getPlayerList() throws PersistenceIOException {
		Vector<Player> players = (Vector)service.loadAll();
		return players;
	}
	
	/**
	 * DOCME
	 * @param loggedInPlayers
	 * @return
	 * @throws PersistenceIOException
	 */
	public Vector<Player> getPlayerList(boolean loggedInPlayers) throws PersistenceIOException {
		Vector<Player> players = (Vector<Player>)service.loadAll();
		for (Player player : players) {
			if (!player.isLoggedIn()) {
				players.remove(player);
			}
		}
		
		return players;
	}
	
	/**
	 * DOCME
	 * @param username
	 * @return
	 * @throws PersistenceIOException
	 */
	public Player getPlayer(String username) throws PersistenceIOException {
		return service.load(username);
	}
	
	/**
	 * DOCME
	 * @param id
	 * @return
	 * @throws PersistenceIOException
	 */
	public Player getPlayer(int id) throws PersistenceIOException {
		return service.load(id);
	}
	
	/**
	 * DOCME
	 * @param player
	 * @return
	 * @throws PersistenceIOException
	 */
	public Player getPlayer(Player player) throws PersistenceIOException, PlayerDoesNotExistException {
		Player temp = null;
		
		if (player.getId() > -1) {
			temp = service.load(player.getId());
		} else if (player.getUsername() != null) {
			temp = service.load(player.getUsername());
		} else {
			throw new PlayerDoesNotExistException("The player doesn't contain an id or a" +
													" username.");
		}
		
		return temp;
	}
	
	/**
	 * DOCME
	 * @param player
	 * @throws WrongPasswordException
	 * @throws PlayerDoesNotExistException
	 * @throws PersistenceIOException
	 */
	public void login(Player player) throws WrongPasswordException, PlayerDoesNotExistException, PersistenceIOException {
		//TODO: password,  bla exceptions halt
		Player temp = this.getPlayer(player);
		temp.setLoggedIn(true);
		service.save(temp);
	}
	
	/**
	 * DOCME
	 * @throws PersistenceIOException
	 */
	public void logout(Player player) throws PersistenceIOException {
		
	}
	
}
