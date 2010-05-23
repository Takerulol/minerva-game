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
	 * // Country sweden = new Country()
	 * // sweden.setName("Sweden");
	 * // ...
	 * // Country norway = new Country();
	 * // norway.setName("Norway");
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
				Vector<Integer> neighbours = world.getCountryGraph().getNeighbours(country.getId());

				if (world.getCountryGraph().hasNeighbours(country.getId())) {
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
	 * DOCME
	 * 
	 * @param file
	 * @throws WorldFileExtensionException 
	 * @throws WorldFileNotFoundException 
	 */
	public void store(File worldFile) throws WorldFileExtensionException, WorldFileNotFoundException  {
		WorldFile importer = new WorldFile(worldFile);
		importer.exec();

		World world = new World();

		world.setToken(importer.getToken());
		world.setName(importer.getName());
		world.setDescription(importer.getDescription());
		world.setAuthor(importer.getAuthor());
		world.setVersion(importer.getVersion());

		world.setCountries(importer.getCountries());

		importer.close();
	}

	/**
	 * DOCME
	 * 
	 * @return
	 */
	public Vector<World> getList() throws PersistenceIOException {
		Vector<World> worlds = WorldService.getInstance().loadAll();

		for (World world : worlds) {
			this.loadDependencies(world);
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
	public Vector<World> getList(boolean lite) throws PersistenceIOException {
		if (lite) {
			return WorldService.getInstance().loadAll();
		} else {
			return this.getList();
		}
	}

	/**
	 * DOCME
	 * 
	 * @param id
	 * @return
	 * @throws PersistenceIOException
	 */
	public World get(int id) throws PersistenceIOException {
		World world = new World();
		world.setId(id);
		
		return this.get(world);
	}

	/**
	 * DOCME
	 * 
	 * @param world
	 * @return
	 * @throws PersistenceIOException
	 */
	public World get(World world) throws PersistenceIOException {
		world = WorldService.getInstance().load(world.getId());
		this.loadDependencies(world);

		return world;
	}

	/**
	 * DOCME
	 * 
	 * @param world
	 * @return
	 * @throws PersistenceIOException 
	 */
	private void loadDependencies(World world) throws PersistenceIOException {
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
