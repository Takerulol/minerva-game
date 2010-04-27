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
package de.hochschule.bremen.minerva.persistence.db.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import de.hochschule.bremen.minerva.persistence.FilterParameter;
import de.hochschule.bremen.minerva.persistence.Handler;
import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseIOException;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.Neighbour;
import de.hochschule.bremen.minerva.vo.ValueObject;

public class NeighbourHandler extends AbstractDatabaseHandler implements Handler {

	private final static HashMap<String, String> sql = new HashMap<String, String>();

	static {
		sql.put("selectById", "select a.\"id\" as id from country a, neighbour b where b.\"neighbour_country\" = a.\"id\" and b.\"country\" = ?");
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public Vector<Neighbour> readAll(ValueObject reference) throws PersistenceIOException {
		Country referenceCountry = (Country)reference;
		Vector<Neighbour> countries = new Vector<Neighbour>();

		try {
			Object[] params = {referenceCountry.getId()};
			ResultSet record = this.select(sql.get("selectById"), params);

			while (record.next()) {
				countries.add(this.resultSetToObject(record));
			}

			record.close();
		} catch (DatabaseIOException e) {
			throw new PersistenceIOException(e.getMessage());
		} catch (SQLException e) {
			throw new PersistenceIOException("Error occurred while "
											+"receiving a neighbours "
											+"from the database (reference "
											+"country = "+referenceCountry.getId()+"): "
											+e.getMessage()
											+" - "+e.getErrorCode());
		}

		return countries;
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	protected Neighbour resultSetToObject(ResultSet current) throws SQLException {
		Neighbour neighbour = new Neighbour();
		neighbour.setId(current.getInt(1));
		return neighbour;
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public ValueObject read(FilterParameter param) throws PersistenceIOException {
		// TODO Auto-generated method stub
		return null;		
	}



	@Override
	public Vector<? extends ValueObject> readAll() throws PersistenceIOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(ValueObject candidate) throws PersistenceIOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(ValueObject registrable) throws PersistenceIOException {
		// TODO Auto-generated method stub
		
	}
}