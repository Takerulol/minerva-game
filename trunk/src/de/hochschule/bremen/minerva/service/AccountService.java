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
import de.hochschule.bremen.minerva.vo.Player;

public class AccountService {

	/**
	 * DOCME
	 * @param player
	 * @throws PlayerExistsException
	 * @throws PersistenceIOException
	 */
	public void create(Player player) throws PlayerExistsException, PersistenceIOException {
		
	}
	
	/**
	 * DOCME
	 * @return
	 * @throws PersistenceIOException
	 */
	public Vector<Player> getPlayerList() throws PersistenceIOException {
		return null;
	}
	
	/**
	 * DOCME
	 * @param loggedInPlayers
	 * @return
	 * @throws PersistenceIOException
	 */
	public Vector<Player> getPlayerList(boolean loggedInPlayers) throws PersistenceIOException {
		return null;
	}
	
	/**
	 * DOCME
	 * @param username
	 * @return
	 * @throws PersistenceIOException
	 */
	public Player getPlayer(String username) throws PersistenceIOException {
		return null;
	}
	
	/**
	 * DOCME
	 * @param id
	 * @return
	 * @throws PersistenceIOException
	 */
	public Player getPlayer(int id) throws PersistenceIOException {
		return null;
	}
	
	/**
	 * DOCME
	 * @param player
	 * @return
	 * @throws PersistenceIOException
	 */
	public Player getPlayer(Player player) throws PersistenceIOException {
		return null;
	}
	
	/**
	 * DOCME
	 * @param player
	 * @throws WrongPasswordException
	 * @throws PlayerDoesNotExistException
	 * @throws PersistenceIOException
	 */
	public void login(Player player) throws WrongPasswordException, PlayerDoesNotExistException, PersistenceIOException {
		
	}
	
	/**
	 * DOCME
	 * @throws PersistenceIOException
	 */
	public void logout() throws PersistenceIOException {
		
	}
	
}
