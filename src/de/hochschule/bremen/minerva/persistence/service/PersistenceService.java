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

import de.hochschule.bremen.minerva.persistence.Persistence;
import de.hochschule.bremen.minerva.persistence.db.DatabasePersistence;
import de.hochschule.bremen.minerva.persistence.exceptions.ExistsException;
import de.hochschule.bremen.minerva.persistence.exceptions.NotFoundException;
import de.hochschule.bremen.minerva.persistence.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.persistence.file.FilebasedPersistence;
import de.hochschule.bremen.minerva.vo.ValueObject;

public abstract class PersistenceService {

	// PersistenceHandler - We use a database for persistence.
	// If we want to switch to a file based storage, here is yout switcher, buddy.
	protected static Persistence storage = new DatabasePersistence(); 

	abstract public Vector<?> findAll() throws DataAccessException;

	abstract public ValueObject find(int id) throws NotFoundException, DataAccessException;

	abstract public ValueObject find(String name) throws NotFoundException, DataAccessException;

	abstract public void save(ValueObject candidate) throws ExistsException, DataAccessException;

	abstract public void delete(ValueObject candidate) throws DataAccessException;

	/**
	 * Possibility to switch the persistence engine.
	 * We determine the current persistence engine and
	 * switch to file based or database storage.
	 * 
	 * CAUTION: To avoid data loss use this method chary!
	 *
	 */
	public static void switchPersistenceEngine() {
		if (PersistenceService.storage instanceof DatabasePersistence) {
			PersistenceService.storage = new FilebasedPersistence();
		} else {
			PersistenceService.storage = new DatabasePersistence();
		}
	}
}
