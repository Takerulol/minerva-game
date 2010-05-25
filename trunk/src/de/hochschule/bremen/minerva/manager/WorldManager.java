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

import de.hochschule.bremen.minerva.exceptions.WorldFileExtensionException;
import de.hochschule.bremen.minerva.exceptions.WorldFileNotFoundException;
import de.hochschule.bremen.minerva.exceptions.WorldFileParseException;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.persistence.exceptions.WorldNotFoundException;
import de.hochschule.bremen.minerva.persistence.service.ContinentService;
import de.hochschule.bremen.minerva.persistence.service.CountryService;
import de.hochschule.bremen.minerva.persistence.service.NeighbourService;
import de.hochschule.bremen.minerva.persistence.service.WorldService;
import de.hochschule.bremen.minerva.util.WorldFile;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.Neighbour;
import de.hochschule.bremen.minerva.vo.World;

public class WorldManager {

	private static WorldManager instance = null;
	
	private WorldManager() {}

	/**
	 * The world manager is a singleton class. It is not
	 * possible to create more than one instance.
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
	 * Adds a new world to the minerva system.
	 * 
	 * All world information will be stored, except
	 * of the country relation data. The country id's for
	 * example, will be generated in the persistence layer.
	 * So with this method it is NOT possible to store the
	 * neighbouring countries. You have to call this method
	 * to register the world initially. After that, you are
	 * able to create the connections between the countries
	 * and call this method again for country dependency storage.
	 * 
	 * example:
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
	 * 
	 * 
	 * @param world
	 * 
	 */
	public void store(World world) throws PersistenceIOException {
		boolean dependencyStorage = true; // Store county dependencies?

		// We try to load the world by the given name to verify that
		// it was already send through the persistence layer and have all
		// generated ids. If it is possible to load the world, with the
		// storage engine, we are able to store also the country dependencies.
		try {
			World dummy = WorldService.getInstance().load(world.getName());
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
				// TODO: Replace the following code with the new world.getNeighbours(country) method.
				Vector<Integer> neighbours = world.getNeighbours(country.getId());

				if (world.hasNeighbours(country)) {
					for (Integer id : neighbours) {
						Neighbour neighbour = new Neighbour();
						neighbour.setId(id);
						neighbour.getReference().setId(country.getId());
						NeighbourService.getInstance().save(neighbour);
					}
				}
			}
		}
	}

	/**
	 * Imports a world from a given world import file.
	 * This file contains the world data and was generated by
	 * an external tool which is able to create playable minerva
	 * world.
	 * 
	 * @param file - The world import file (*.world)
	 * @throws WorldFileExtensionException - Wrong file extension. 
	 * @throws WorldFileNotFoundException - The given world import file wasn't found.
	 * @throws WorldFileParseException - The given world import file is not well-formed. 
	 * @throws PersistenceIOException - Common exception from the persistence layer.
	 * 
	 */
	public void store(File worldFile) throws WorldFileExtensionException, WorldFileNotFoundException, WorldFileParseException, PersistenceIOException  {
		WorldFile world = new WorldFile(worldFile);
		world.parse();

		this.store(world);

		world.createCountryDependencies();

		this.store(world);
	}

	/**
	 * Loads a world list from the persistence layer.
	 * 
	 * @return A vector with all worlds from the persistence layer.
	 * @throws PersistenceIOException - Common exception from the persistence layer.
	 * 
	 */
	public Vector<World> getList() throws PersistenceIOException {
		Vector<World> worlds = WorldService.getInstance().loadAll();

		for (World world : worlds) {
			this.loadDependencies(world);
		}

		return worlds;
	}

	/**
	 * Loads a world list from the persistence layer. 
	 * 
	 * IMPORTANT: If the 'lite' parameter is 'true', the method
	 * will only load the common world data (like: name, author, ...).
	 * If it is 'false' is behaves like the 'getWorldList' method and loads
	 * all complex world data (like country dependencies and so on).
	 * 
	 * @param lite - Does not load all country dependencies (only the world data).
	 * @return A vector with all worlds from the persistence layer.
	 * @throws PersistenceIOException - Common exception from the persistence layer.
	 * 
	 */
	public Vector<World> getList(boolean lite) throws PersistenceIOException {
		if (lite) {
			return WorldService.getInstance().loadAll();
		} else {
			return this.getList();
		}
	}

	/**
	 * Loads a world from the persistence layer by an given id. 
	 * 
	 * @param id - The world id.
	 * @return - The world object.
	 * @throws PersistenceIOException - Common exception from the persistence layer.
	 * @throws WorldNotFoundException - If the world wasn't found.
	 * 
	 */
	public World get(int id) throws WorldNotFoundException, PersistenceIOException {
		World world = new World();
		world.setId(id);
		
		return this.get(world);
	}

	/**
	 * Loads a world from the persistence layer
	 * by an given "incomplete" world. Incomplete means,
	 * that the world object must have a valid id.
	 * 
	 * @param world
	 * @return - The "complete" world object.
	 * @throws PersistenceIOException - Common exception from the persistence layer.
	 * @throws WorldNotFoundException - If the world wasn't found.
	 * 
	 */
	public World get(World world) throws WorldNotFoundException, PersistenceIOException {
		world = WorldService.getInstance().load(world.getId());
		this.loadDependencies(world);

		return world;
	}

	/**
	 * Load country dependencies for a given world
	 * from the persistence layer.
	 * 
	 * @param world - The world for which we should load the country dependencies.
	 * @throws PersistenceIOException 
	 */
	private void loadDependencies(World world) throws PersistenceIOException {
		Vector<Country> countries = CountryService.getInstance().loadAll(world);
		
		for (Country country : countries) {
			country.setContinent(ContinentService.getInstance().load(country.getContinent().getId()));
			
			for (Neighbour neighbour : NeighbourService.getInstance().loadAll(country)) {
				world.connectCountries(country, neighbour);
			}
		}
		world.setCountries(countries);
	}
}