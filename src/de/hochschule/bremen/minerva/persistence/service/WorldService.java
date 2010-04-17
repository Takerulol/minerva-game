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

import java.util.List;

import de.hochschule.bremen.minerva.persistence.Crudable;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.persistence.exceptions.WorldNotFoundException;
import de.hochschule.bremen.minerva.vo.World;

/**
 * DOCME
 * @author akoenig
 *
 */
public class WorldService extends AbstractService {

	private static WorldService instance = null;

	private Crudable storageHandler = WorldService.storage.createHandler(World.class);

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
	 * @throws PersistenceIOException 
	 */
	public List<World> loadAll() throws PersistenceIOException {
		List<World> worlds = (List<World>)storageHandler.readAll();
		return worlds;
	}

	/**
	 * DOCME
	 * 
	 * @param id
	 * @return
	 */
	public World load(int id) throws WorldNotFoundException {
		try {
			return (World)storageHandler.read(id);
		} catch (Exception e) {
			throw new WorldNotFoundException(e.getMessage());
		}
	}

	@Override
	public void save(Object candidate) throws PersistenceIOException {
		World registrableWorld = (World)candidate;
		storageHandler.save(registrableWorld);
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public void delete(Object candidate) throws PersistenceIOException {
		World deletableWorld = (World)candidate;
		
		storageHandler.remove(deletableWorld);
	}
}