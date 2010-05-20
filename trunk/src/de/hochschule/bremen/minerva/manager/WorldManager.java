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

import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.persistence.service.ContinentService;
import de.hochschule.bremen.minerva.persistence.service.CountryService;
import de.hochschule.bremen.minerva.persistence.service.NeighbourService;
import de.hochschule.bremen.minerva.persistence.service.WorldService;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.Neighbour;
import de.hochschule.bremen.minerva.vo.World;

public class WorldManager {

	private static WorldManager instance = null;
	
	private WorldManager() {}

	/**
	 * DOCME
	 * 
	 * @return
	 */
	public static WorldManager getInstance() {
		if (WorldManager.instance == null) {
			WorldManager.instance = new WorldManager();
		}
		
		return WorldManager.instance;
	}

	/**
	 * DOCME
	 * 
	 * @return
	 */
	public Vector<World> getWorldList() throws PersistenceIOException {
		Vector<World> worlds = WorldService.getInstance().loadAll();

		for (World world : worlds) {
			this.loadWorldDependencies(world);
		}

		return worlds;
	}

	/**
	 * 
	 * Loads a world list from the persistence layer. 
	 * 
	 * IMPORTANT: If the 'lite' parameter is 'true', the method
	 * will only load the common world data (like: name, author, ...).
	 * If it is 'false' is behaves like the 'getWorldList' method and loads
	 * all complex world data (like country dependencies and so on).
	 * 
	 * @param lite - Does not load all country dependencies (only the world data).
	 * @return
	 * @throws PersistenceIOException
	 */
	public Vector<World> getWorldList(boolean lite) throws PersistenceIOException {
		if (lite) {
			return WorldService.getInstance().loadAll();
		} else {
			return this.getWorldList();
		}
	}

	/**
	 * DOCME
	 * 
	 * @param id
	 * @return
	 * @throws PersistenceIOException
	 */
	public World getWorld(int id) throws PersistenceIOException {
		World world = new World();
		world.setId(id);
		
		return this.getWorld(world);
	}

	/**
	 * DOCME
	 * 
	 * @param world
	 * @return
	 * @throws PersistenceIOException
	 */
	public World getWorld(World world) throws PersistenceIOException {
		world = WorldService.getInstance().load(world.getId());
		this.loadWorldDependencies(world);

		return world;
	}

	/**
	 * DOCME
	 * 
	 * @param world
	 * @return
	 * @throws PersistenceIOException 
	 */
	private void loadWorldDependencies(World world) throws PersistenceIOException {
		Vector<Country> countries = CountryService.getInstance().loadAll(world);
		
		for (Country country : countries) {
			country.setContinent(ContinentService.getInstance().load(country.getContinent().getId()));
			
			for (Neighbour neighbour : NeighbourService.getInstance().loadAll(country)) {
				world.getCountryGraph().connect(country, neighbour);
			}
		}
		world.setCountries(countries);
	}
}
