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
package de.hochschule.bremen.minerva.persistence;

import java.util.Vector;

import de.hochschule.bremen.minerva.persistence.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.vo.ValueObject;

/**
 * Each persistence handler needs the basic "CRUD" operations.
 * CRUD is:
 * 
 * "In computer programming, Create, Read, Update and Delete (CRUD)
 * are the four basic functions of persistent storage. Sometimes
 * CRUD is expanded with the words retrieve instead of read or destroy
 * instead of delete. It is also sometimes used to describe user interface
 * conventions that facilitate viewing, searching, and changing information;
 * often using computer-based forms and reports."
 * 
 * from: http://en.wikipedia.org/wiki/Create,_read,_update_and_delete
 * 
 * @since 1.0
 * @version $Id$
 *
 */
public interface Handler {

	/**
	 * Read a object by id.
	 * 
	 * @param id The identifier.
	 * @return The value object
	 * @throws DataAccessException Common persistence exception
	 * 
	 */
	public ValueObject read(int id) throws DataAccessException;

	/**
	 * Read a object by name
	 * 
	 * @param name The name identifier.
	 * @return
	 * @throws DataAccessException Common persistence exception
	 * 
	 */
	public ValueObject read(String name) throws DataAccessException;

	/**
	 * Reads all objects.
	 * 
	 * @return A vector with value objects.
	 * @throws DataAccessException Common persistence exception
	 * 
	 */
	public Vector<? extends ValueObject> readAll() throws DataAccessException;

	/**
	 * Reads all by an given reference value object.
	 * 
	 * Note: The reference parameter is optional. We use it if
	 * we have to read data by a given referenced value object
	 * (for example: read all countries by the referenced world).
	 * This is because Java does not support optional parameters. In some
	 * cases, like this one, this restriction is not very dressy.
	 * 
	 * @param reference The referenced value object.
	 * @return A vector with value objects.
	 * @throws DataAccessException Common persistence exception
	 * 
	 */
	public Vector<? extends ValueObject> readAll(ValueObject reference) throws DataAccessException;

	/**
	 * Deletes the given value object.
	 * 
	 * @param candidate The deletable value object.
	 * @throws DataAccessException Common persistence exception
	 * 
	 */
	public void remove(ValueObject candidate) throws DataAccessException;

	/**
	 * Save combines the "CRUD" operations: create and update
	 * 
	 * @param registrable The saveable value object.
	 * @throws DataAccessException Common persistence exception
	 * 
	 */
	public void save(ValueObject registrable) throws DataAccessException;
}
