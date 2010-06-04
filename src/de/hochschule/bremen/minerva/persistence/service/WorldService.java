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
package de.hochschule.bremen.minerva.persistence.service;

import java.util.Vector;

import de.hochschule.bremen.minerva.persistence.Handler;
import de.hochschule.bremen.minerva.persistence.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.persistence.exceptions.WorldExistsException;
import de.hochschule.bremen.minerva.persistence.exceptions.WorldNotFoundException;
import de.hochschule.bremen.minerva.vo.ValueObject;
import de.hochschule.bremen.minerva.vo.World;

/**
 * DOCME
 * @author akoenig
 *
 */
public class WorldService extends PersistenceService {

	private static WorldService instance = null;

	private Handler handler = WorldService.storage.createHandler(World.class);

	/**
	 * Singleton pattern. It is not possible
	 * to create a WorldService in the common way.
	 * So this constructor is private.
	 * 
	 */
	private WorldService() {}
	
	/**
	 * Singleton pattern.
	 * Static method that controls the object creation.
	 * 
	 * @return DOCME
	 */
	public static WorldService getInstance() {
		if (WorldService.instance == null) {
			WorldService.instance = new WorldService();
		}
		return WorldService.instance;
	}

	/**
	 * DOCME
	 * 
	 * @return
	 * @throws DataAccessException 
	 */
	@SuppressWarnings("unchecked")
	public Vector<World> findAll() throws DataAccessException {
		return (Vector<World>)handler.readAll();
	}

	/**
	 * DOCME
	 * 
	 * @param id
	 * @return
	 */
	public World find(int id) throws WorldNotFoundException, DataAccessException {
		return (World)handler.read(id);
	}

	/**
	 * DOCME
	 * 
	 * @param name
	 * @return
	 * @throws WorldNotFoundException
	 * @throws DataAccessException
	 */
	public World find(String name) throws WorldNotFoundException, DataAccessException {
		return (World)handler.read(name);
	}
	
	/**
	 * DOCME
	 * 
	 */
	@Override
	public void save(ValueObject candidate) throws WorldExistsException, DataAccessException {
		handler.save((World)candidate);
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public void delete(ValueObject candidate) throws DataAccessException {
		World deletableWorld = (World)candidate;
		
		handler.remove(deletableWorld);
	}
}