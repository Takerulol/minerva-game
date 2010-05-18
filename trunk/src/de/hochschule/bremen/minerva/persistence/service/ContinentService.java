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
import de.hochschule.bremen.minerva.persistence.FilterParameter;
import de.hochschule.bremen.minerva.persistence.exceptions.ContinentNotFoundException;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.vo.Continent;
import de.hochschule.bremen.minerva.vo.ValueObject;

public class ContinentService extends PersistenceService {

	private static ContinentService instance = null;

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
	 * @return DOCME
	 */
	public static ContinentService getInstance() {
		if (ContinentService.instance == null) {
			ContinentService.instance = new ContinentService();
		}
		return ContinentService.instance;
	}

	/**
	 * DOCME
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Vector<Continent> loadAll() throws PersistenceIOException {
		Vector<Continent> continents = (Vector)handler.readAll();
		return continents;
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public Continent load(int id) throws PersistenceIOException {
		try {
			return (Continent)handler.read(new FilterParameter(id));
		} catch (Exception e) {
			throw new ContinentNotFoundException(e.getMessage());
		}
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public void save(ValueObject candidate) throws PersistenceIOException {
		handler.save((Continent)candidate);
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public void delete(ValueObject candidate) throws PersistenceIOException {
		Continent continent = (Continent)candidate;
		handler.remove(continent);
	}
}
