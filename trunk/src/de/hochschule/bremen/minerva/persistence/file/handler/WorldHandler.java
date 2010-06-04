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
package de.hochschule.bremen.minerva.persistence.file.handler;

import java.util.Vector;

import de.hochschule.bremen.minerva.persistence.Handler;
import de.hochschule.bremen.minerva.persistence.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.vo.ValueObject;

// Not implemented. Only for demonstration purposes.
public class WorldHandler implements Handler {

	@Override
	public ValueObject read(int id) throws DataAccessException {
		System.out.println("WorldHandler.read() - Filebased storage is not implemented.");
		return null;
	}

	public ValueObject read(String name) throws DataAccessException {
		System.out.println("WorldHandler.read() - Filebased storage is not implemented.");		
		return null;
	}

	@Override
	public Vector<? extends ValueObject> readAll()
			throws DataAccessException {
		System.out.println("WorldHandler.readAll() - Filebased storage is not implemented.");
		return null;
	}

	@Override
	public Vector<? extends ValueObject> readAll(ValueObject reference)
			throws DataAccessException {
		System.out.println("WorldHandler.readAll() - Filebased storage is not implemented.");
		return null;
	}

	@Override
	public void remove(ValueObject candidate) throws DataAccessException {
		System.out.println("WorldHandler.remove() - Filebased storage is not implemented.");
	}

	@Override
	public void save(ValueObject registrable) throws DataAccessException {
		System.out.println("WorldHandler.save() - Filebased storage is not implemented.");
	}
}