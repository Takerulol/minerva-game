/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: ContinentService.java 122 2010-04-24 20:59:14Z andre.koenig $
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
package de.hochschule.bremen.minerva.persistence.service;

import java.util.Vector;

import de.hochschule.bremen.minerva.persistence.Handler;
import de.hochschule.bremen.minerva.persistence.FilterParameter;
import de.hochschule.bremen.minerva.persistence.exceptions.PlayerNotFoundException;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.ValueObject;

//TODO: DOCME the class and two methods.

/**
 * DOCME
 */
public class PlayerService extends PersistenceService {

	private static PlayerService instance = null;

	private Handler handler = PlayerService.storage.createHandler(Player.class);

	/**
	 * Singleton pattern. It is not possible
	 * to create a PlayerService in the common way.
	 * So this constructor is private.
	 */
	private PlayerService() {}
	
	/**
	 * Singleton pattern.
	 * Static method that controls the object creation.
	 * 
	 * @return PlayerService.instance
	 */
	public static PlayerService getInstance() {
		if (PlayerService.instance == null) {
			PlayerService.instance = new PlayerService();
		}
		return PlayerService.instance;
	}
	
	/**
	 * Method that delete the player object.
	 * 
	 * @throws PersistenceIOException
	 */
	@Override
	public void delete(ValueObject candidate) throws PersistenceIOException {
		Player deletablePlayer = (Player)candidate;
		
		handler.remove(deletablePlayer);
	}

	/**
	 * DOCME
	 * 
	 * @throws PlayerNotFoundException, PersistenceIOException
	 */
	@Override
	public Player load(int id) throws PersistenceIOException {
		try {
			return (Player)handler.read(new FilterParameter(id));
		} catch (Exception e) {
			throw new PlayerNotFoundException(e.getMessage());
		}
	}

	/**
	 * DOCME
	 * 
	 * @param username
	 * @return
	 * @throws PersistenceIOException
	 */
	public Player load(String username) throws PersistenceIOException {
		try {
			return (Player)handler.read(new FilterParameter(username));
		} catch (Exception e) {
			throw new PlayerNotFoundException(e.getMessage());
		}
	}
	
	/**
	 * DOCME
	 * 
	 * @throws PersistenceIOException
	 * @return players
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Vector<Player> loadAll() throws PersistenceIOException {
		Vector<Player> players = (Vector)handler.readAll();
		return players;
	}

	/**
	 * Method that save the object.
	 * 
	 * @throws PersistenceIOException
	 */
	@Override
	public void save(ValueObject candidate) throws PersistenceIOException {
		handler.save((Player)candidate);
	}

}
