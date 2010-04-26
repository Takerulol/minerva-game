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

import de.hochschule.bremen.minerva.persistence.Handler;
import de.hochschule.bremen.minerva.persistence.FilterParameter;
import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseDuplicateRecordException;
import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseIOException;
import de.hochschule.bremen.minerva.persistence.exceptions.ContinentNotFoundException;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.persistence.exceptions.WorldExistsException;
import de.hochschule.bremen.minerva.vo.Continent;
import de.hochschule.bremen.minerva.vo.ValueObject;

/**
 * DOCME
 * 
 */
public class ContinentHandler extends AbstractDatabaseHandler implements Handler {

	private final static HashMap<String, String> sql = new HashMap<String, String>();

	static {
		sql.put("selectAll", "select \"id\", \"name\" from continent order by name");
		sql.put("selectById", "select \"id\", \"name\" from continent where \"id\" = ?");
		sql.put("insert", "insert into continent (name) values (?)");
		sql.put("update", "update continent set name = ? where id = ?");
		sql.put("delete", "delete from continent where id = ?");
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public Continent read(FilterParameter id) throws PersistenceIOException {
		Continent continent = null;
		Object[] params = {id.getInt()};
		
		try {
			ResultSet record = this.select(sql.get("selectById"), params);
			if (record.next()) {
				continent = this.resultSetToObject(record);

				record.close();
			} else {
				throw new ContinentNotFoundException("Found no continent"
													+"with id: '"+id+"'.");
			}

		} catch (DatabaseIOException e) {
			throw new PersistenceIOException(e.getMessage());
		} catch (SQLException e) {
			throw new PersistenceIOException("Error occurred while reading "
					                       + "the continent (id=" +id+") "
					                       + "from the database.");
		}

		return continent;
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public Vector<Continent> readAll() throws PersistenceIOException {
		Vector<Continent> continents = new Vector<Continent>();

		try {
			ResultSet record = this.select(sql.get("selectAll"));

			while (record.next()) {
				continents.add(this.resultSetToObject(record));
			}

			record.close();
		} catch (DatabaseIOException e) {
			throw new PersistenceIOException(e.getMessage());
		} catch (SQLException e) {
			throw new PersistenceIOException("Error occurred while receiving "
											+"a continent list from the database: "
											 +e.getMessage()+" - "
											 +e.getErrorCode());
		}

		return continents;
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public void save(ValueObject registrable) throws PersistenceIOException {
		Continent registrableContinent = (Continent)registrable;

		try {
			Object[] params = {registrableContinent.getName()};

			this.insert(sql.get("insert"), params);
		} catch (DatabaseIOException e) {
			try {
				Object[] params = {
					registrableContinent.getName(),
					registrableContinent.getId()
				};
				this.update(sql.get("update"), params);
			} catch (DatabaseDuplicateRecordException exe) {
				throw new WorldExistsException("Unable to serialize the continent. "
											  +"There is already a similar one.");
			} catch (DatabaseIOException ex) {
				throw new PersistenceIOException("Unable to serialize the "
											    +"continent object: "
											    +ex.getMessage());
			}
		}
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public void remove(ValueObject candidate) throws PersistenceIOException {
		Continent deletableContinent = (Continent)candidate;
		
		Object[] params = {deletableContinent.getId()};

		try {
			this.delete(sql.get("delete"), params);
		} catch (DatabaseIOException e) {
			throw new PersistenceIOException(e.getMessage());
		}
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	protected Continent resultSetToObject(ResultSet current) throws SQLException {
		Continent continent = new Continent();
		
		continent.setId(current.getInt(1));
		continent.setName(current.getString(2));

		return continent;
	}

	/**
	 * TODO: This method is not necessary. Please check the interface
	 * design to avoid such unused methods.
	 *  
	 */
	@Override
	public Vector<? extends ValueObject> readAll(ValueObject referencedCountry) throws PersistenceIOException {		
		// TODO Auto-generated method stub
		return null;
	}	
}
