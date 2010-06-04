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
import de.hochschule.bremen.minerva.persistence.exceptions.NeighbourNotFoundException;
import de.hochschule.bremen.minerva.persistence.exceptions.NotFoundException;
import de.hochschule.bremen.minerva.persistence.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.Neighbour;
import de.hochschule.bremen.minerva.vo.ValueObject;

public class NeighbourService extends PersistenceService {

	private static NeighbourService instance = null;

	private Handler handler = NeighbourService.storage.createHandler(Neighbour.class);

	/**
	 * Singleton pattern. It is not possible
	 * to create a NeighbourService in the common way.
	 * So this constructor is private.
	 */
	private NeighbourService() {}
	
	/**
	 * Singleton pattern.
	 * Static method that controls the object creation.
	 * 
	 * @return NeighbourService
	 */
	public static NeighbourService getInstance() {
		if (NeighbourService.instance == null) {
			NeighbourService.instance = new NeighbourService();
		}
		return NeighbourService.instance;
	}

	/**
	 * DOCME
	 * 
	 * @param byCountry
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public Vector<Neighbour> loadAll(Country byCountry) throws DataAccessException {
		return (Vector)handler.readAll(byCountry);
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public void save(ValueObject candidate) throws DataAccessException {
		handler.save((Neighbour)candidate);
	}

	@Override
	public void delete(ValueObject candidate) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Neighbour find(int id) throws NeighbourNotFoundException, DataAccessException {
		return (Neighbour)handler.read(id);
	}
	
	@Override
	public Vector<?> findAll() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueObject find(String name) throws NotFoundException,
			DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}
}
