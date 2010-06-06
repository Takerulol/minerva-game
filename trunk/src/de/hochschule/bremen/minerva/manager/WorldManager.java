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

import java.io.File;
import java.util.Vector;

import de.hochschule.bremen.minerva.exceptions.WorldDoesNotExistException;
import de.hochschule.bremen.minerva.exceptions.WorldFileExtensionException;
import de.hochschule.bremen.minerva.exceptions.WorldFileNotFoundException;
import de.hochschule.bremen.minerva.exceptions.WorldFileParseException;
import de.hochschule.bremen.minerva.persistence.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.persistence.exceptions.WorldNotFoundException;
import de.hochschule.bremen.minerva.persistence.service.ContinentService;
import de.hochschule.bremen.minerva.persistence.service.CountryService;
import de.hochschule.bremen.minerva.persistence.service.NeighbourService;
import de.hochschule.bremen.minerva.persistence.service.WorldService;
import de.hochschule.bremen.minerva.util.WorldFile;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.Neighbour;
import de.hochschule.bremen.minerva.vo.World;

/**
 * The world manager is a singleton, which summarizes the functionality
 * that the underlying layers provide. It collects all loosely filaments and
 * provides a lightweight access to this underlying layer.
 * So for example it is possible to register new world objects or get a complete
 * world list. To name just two use cases.<br />
 * 
 * <br />
 * Usage:
 * 
 * <pre>
 * WorldManager.getInstance().<theMethod>
 * </pre>
 *
 * @since 1.0
 * @version $Id$
 *
 */
public class WorldManager {

	private static WorldManager instance = null;

	/**
	 * Private singleton. See: WorldManager#getInstance()
	 * 
	 */
	private WorldManager() {}

	/**
	 * The world manager is a singleton class. It is not
	 * possible to create more than one instance.
	 * 
	 * @return The WorldManager instance.
	 * 
	 */
	public static WorldManager getInstance() {
		if (WorldManager.instance == null) {
			WorldManager.instance = new WorldManager();
		}
		
		return WorldManager.instance;
	}

	/**
	 * Adds a new world to the minerva system.
	 * 
	 * All world information will be stored, except
	 * of the country relation data. The country id's for
	 * example, will be generated in the persistence layer.
	 * So with this method it is NOT possible to store the
	 * neighboring countries. You have to call this method
	 * to register the world initially. After that, you are
	 * able to create the connections between the countries
	 * and call this method again for country dependency storage.
	 * 
	 * example:
	 * <pre>
	 * World world = new World()
	 * 
	 * // ...
	 * // Set the world information
	 * // world.setName("my World");
	 * // ... 
	 * Country sweden = new Country()
	 * sweden.setName("Sweden");
	 * // ...
	 * Country norway = new Country();
	 * norway.setName("Norway");
	 * // ...
	 * 
	 * // Add the new world to minerva system
	 * WorldManager.getInstance().store(world);
	 * 
	 * // Create the country relations
	 * world.connectCountries(sweden, norway);
	 *
	 * // Store the world.
	 * WorldManager.getInstance().store(world);
	 * </pre>
	 * 
	 * @param world The world object, which should be stored.
	 * @see 		World
	 * 
	 */
	public void store(World world) throws DataAccessException {
		boolean dependencyStorage = true; // Store county dependencies?

		// We try to load the world by the given name to verify that
		// it was already send through the persistence layer and have all
		// generated id's. If it is possible to load the world, with the
		// storage engine, we are able to store also the country dependencies.
		try {
			World dummy = WorldService.getInstance().find(world.getName());
			world.setId(dummy.getId());
		} catch (WorldNotFoundException e) {
			dependencyStorage = false;
		}

		WorldService.getInstance().save(world);

		for (Country country : world.getCountries()) {
			ContinentService.getInstance().save(country.getContinent());
			
			country.setWorldId(world.getId());
			CountryService.getInstance().save(country);
		}

		if (dependencyStorage) {
			for (Country country : world.getCountries()) {				
				if (world.hasNeighbours(country)) {
					Vector<Country> neighbours = world.getNeighbours(country);

					for (Country neighbour : neighbours) {
						Neighbour neighbourMapping = new Neighbour();
						neighbourMapping.setId(neighbour.getId());
						neighbourMapping.getReference().setId(country.getId());
						NeighbourService.getInstance().save(neighbourMapping);
					}
				}
			}
		}
	}

	/**
	 * Imports a world from a given world import file.
	 * This file contains the world data and was generated by
	 * an external tool which is able to create playable minerva world.
	 * 
	 * @param file The world import file (*.world)
	 * @throws WorldFileExtensionException Wrong file extension. 
	 * @throws WorldFileNotFoundException The given world import file wasn't found.
	 * @throws WorldFileParseException The given world import file is not well-formed. 
	 * @throws DataAccessException Common exception from the persistence layer.
	 * 
	 * @see WorldFile
	 * 
	 */
	public void store(File worldFile) throws WorldFileExtensionException, WorldFileNotFoundException, WorldFileParseException, DataAccessException  {
		WorldFile world = new WorldFile(worldFile);
		world.parse();

		this.store(world);

		world.createCountryDependencies();

		this.store(world);
		
		world.moveAssets();
		world.clean();
	}

	/**
	 * Loads a world list from the persistence layer.
	 * 
	 * @return A vector with all worlds from the persistence layer.
	 * @throws DataAccessException Common exception from the persistence layer.
	 * 
	 */
	public Vector<World> getList() throws DataAccessException {
		Vector<World> worlds = WorldService.getInstance().findAll();

		for (World world : worlds) {
			this.loadDependencies(world);
		}

		return worlds;
	}

	/**
	 * Loads a world list from the persistence layer. 
	 * <br />
	 * <b>IMPORTANT:</b> If the 'lite' parameter is 'true', the method
	 * will only load the common world data (like: name, author, ...).
	 * If it is 'false' is behaves like the 'getWorldList' method and loads
	 * all complex world data (like country dependencies and so on).
	 * 
	 * @param lite Does not load all country dependencies (only the world data).
	 * @return A vector with all worlds from the persistence layer.
	 * @throws DataAccessException Common exception from the persistence layer.
	 * 
	 * @see WorldManager#getList()
	 * 
	 */
	public Vector<World> getList(boolean lite) throws DataAccessException {
		if (lite) {
			return WorldService.getInstance().findAll();
		} else {
			return this.getList();
		}
	}

	/**
	 * Loads a world from the persistence layer by an given id. 
	 * 
	 * @param id The world id.
	 * @return The filled world object.
	 * @throws DataAccessException Common exception from the persistence layer.
	 * @throws WorldDoesNotExistException If the world wasn't found.
	 * 
	 */
	public World get(int id) throws WorldDoesNotExistException, DataAccessException {
		try {
			World world = new World();
			world.setId(id);
		
			return this.get(world);
		} catch (WorldNotFoundException e) {
			throw new WorldDoesNotExistException(id);
		}
	}

	/**
	 * Loads a world from the persistence layer
	 * by an given "incomplete" world. Incomplete means,
	 * that the world object must have a valid id. The method
	 * will fill the world object and returns it.
	 * 
	 * @param world
	 * @return The "complete" world object.
	 * @throws WorldDoesNotExistException If the world wasn't found.
	 * @throws DataAccessException Common exception from the persistence layer.
	 * 
	 */
	public World get(World world) throws WorldDoesNotExistException, DataAccessException {
		
		try {
			world = WorldService.getInstance().find(world.getId());
			this.loadDependencies(world);

			return world;
		} catch (WorldNotFoundException e) {
			throw new WorldDoesNotExistException(world);
		}
	}

	/**
	 * Load country dependencies for a given world
	 * from the persistence layer.
	 * 
	 * @param world The world for which we should load the country dependencies.
	 * @throws DataAccessException Common exception from the persistence layer.
	 *  
	 */
	private void loadDependencies(World world) throws DataAccessException {
		Vector<Country> countries = CountryService.getInstance().loadAll(world);
		
		for (Country country : countries) {
			country.setContinent(ContinentService.getInstance().find(country.getContinent().getId()));
			
			for (Neighbour neighbour : NeighbourService.getInstance().loadAll(country)) {
				world.connectCountries(country, neighbour);
			}
		}
		world.setCountries(countries);
	}
}