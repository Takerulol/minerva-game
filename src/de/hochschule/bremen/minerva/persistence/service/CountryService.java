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

import de.hochschule.bremen.minerva.persistence.Crudable;
import de.hochschule.bremen.minerva.persistence.exceptions.CountryNotFoundException;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.World;

/**
 * DOCME
 * @author akoenig
 *
 */
public class CountryService extends PersistenceService {

	private static CountryService instance = null;

	private Crudable storageHandler = CountryService.storage.createHandler(Country.class);

	/**
	 * Singleton pattern. It is not possible
	 * to create a CountryService in the common way.
	 * So this constructor is private.
	 * 
	 */
	private CountryService() {}
	
	/**
	 * Singleton pattern.
	 * Static method that controls the object creation.
	 * 
	 * @return DOCME
	 */
	public static CountryService getInstance() {
		if (CountryService.instance == null) {
			CountryService.instance = new CountryService();
		}
		return CountryService.instance;
	}

	/**
	 * DOCME
	 * 
	 * @return
	 * @throws PersistenceIOException 
	 */
	@SuppressWarnings("unchecked")
	public Vector<Country> loadAll() throws PersistenceIOException {
		Vector<Country> countries = (Vector)storageHandler.readAll();
		return countries;
	}

	/**
	 * DOCME
	 * 
	 * @param byWorldId
	 * @return
	 * @throws PersistenceIOException
	 */
	@SuppressWarnings("unchecked")
	public Vector<Country> loadAll(World byWorld) throws PersistenceIOException {
		Vector<Country> countries = (Vector)storageHandler.readAll(byWorld);
		return countries;
	}

	/**
	 * DOCME
	 * 
	 * @param id
	 * @return
	 */
	public Country load(int id) throws CountryNotFoundException {
		try {
			return (Country)storageHandler.read(id);
		} catch (Exception e) {
			throw new CountryNotFoundException(e.getMessage());
		}
	}

	@Override
	public void save(Object candidate) throws PersistenceIOException {
		Country registrableCountry = (Country)candidate;
		storageHandler.save(registrableCountry);
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public void delete(Object candidate) throws PersistenceIOException {
		Country deletableCountry = (Country)candidate;
		
		storageHandler.remove(deletableCountry);
	}
}