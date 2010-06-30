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
import de.hochschule.bremen.minerva.persistence.exceptions.ContinentExistsException;
import de.hochschule.bremen.minerva.persistence.exceptions.ContinentNotFoundException;
import de.hochschule.bremen.minerva.persistence.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.vo.Continent;
import de.hochschule.bremen.minerva.vo.ValueObject;

/**
 * Provides methods for continent I/O operations, like:
 * selecting, inserting, updating and deleting them via
 * the persistence handlers.
 * 
 * @since 1.0
 * @version $Id$
 * 
 */
public class ContinentService extends PersistenceService {

	// The ContinentService instance (singleton pattern)
	private static ContinentService instance = null;

	// The persistence handler
	private Handler handler = CountryService.storage.createHandler(Continent.class);

	/**
	 * Singleton pattern. It is not possible
	 * to create a ContinentService in the common way.
	 * So this constructor is private.
	 * 
	 */
	private ContinentService() {}
	
	/**
	 * Singleton pattern.
	 * Static method that controls the object creation.
	 * 
	 * @return A ContinentService instance.
	 * 
	 */
	public static ContinentService getInstance() {
		if (ContinentService.instance == null) {
			ContinentService.instance = new ContinentService();
		}
		return ContinentService.instance;
	}

	/**
	 * Returns all continents.
	 * 
	 * @return All available continent value objects.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Vector<Continent> findAll() throws DataAccessException {
		Vector<Continent> continents = (Vector<Continent>)handler.readAll();
		return continents;
	}

	/**
	 * Find a continent by an given id.
	 * 
	 * @param id The unique identifier.
	 * @return The continent (if found).
	 * 
	 * @throws ContinentNotFoundException
	 * @throws DataAccessException Common persistence exception.
	 * 
	 */
	@Override
	public Continent find(int id) throws ContinentNotFoundException, DataAccessException {
		return (Continent)handler.read(id);
	}

	/**
	 * Find a continent by an given name.
	 * 
	 * @param name The unique name.
	 * @return The continent (if found)
	 * 
	 * @throws ContinentNotFoundException
	 * @throws DataAccessException Common persistence exception.
	 * 
	 */
	public Continent find(String name) throws ContinentNotFoundException, DataAccessException {
		return (Continent)handler.read(name);
	}
	
	
	/**
	 * Saves a continent.
	 * 
	 * @param candidate The saveable continent value object.
	 * 
	 */
	@Override
	public void save(ValueObject candidate) throws ContinentExistsException, DataAccessException {
		handler.save((Continent)candidate);
	}

	/**
	 * Deletes a continent.
	 * 
	 * @param candidate The removable continent value object.
	 * 
	 */
	@Override
	public void delete(ValueObject candidate) throws DataAccessException {
		Continent continent = (Continent)candidate;
		handler.remove(continent);
	}
}
