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
 * are the four basic functions of persistent storage.[1] Sometimes
 * CRUD is expanded with the words retrieve instead of read or destroy
 * instead of delete. It is also sometimes used to describe user interface
 * conventions that facilitate viewing, searching, and changing information;
 * often using computer-based forms and reports."
 * 
 * @author akoenig
 *
 */
public interface Handler {

	/**
	 * Read a object by id.
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public ValueObject read(int id) throws DataAccessException;

	/**
	 * Read a object by name
	 * 
	 * @param name
	 * @return
	 * @throws DataAccessException
	 */
	public ValueObject read(String name) throws DataAccessException;

	/**
	 * DOCME
	 * 
	 * @return
	 * @throws DataAccessException
	 */
	public Vector<? extends ValueObject> readAll() throws DataAccessException;

	/**
	 * Note: The reference parameter is optional. We use it if
	 * we have to read data by a given referenced value object
	 * (for example: read all countries by the referenced world).
	 * This is because Java does not support optional parameters. In some
	 * cases, like this one, this restriction is not very dressy.
	 * 
	 * TODO: Issue 6: "Crudable" Interfacedesign anpassen (http://code.google.com/p/minerva-game/issues/detail?id=6)
	 * 
	 * @param reference
	 * @return
	 * @throws DataAccessException
	 */
	public Vector<? extends ValueObject> readAll(ValueObject reference) throws DataAccessException;

	/**
	 * DOCME
	 * 
	 * @param candidate
	 * @throws DataAccessException
	 */
	public void remove(ValueObject candidate) throws DataAccessException;

	/**
	 * Save combines the "CRUD" operations: create and update
	 * 
	 * @param registrable
	 * @throws DataAccessException
	 */
	public void save(ValueObject registrable) throws DataAccessException;
}
