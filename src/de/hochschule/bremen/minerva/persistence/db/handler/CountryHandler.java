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

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import de.hochschule.bremen.minerva.vo.Continent;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.World;
import de.hochschule.bremen.minerva.persistence.Crudable;
import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseDuplicateRecordException;
import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseIOException;
import de.hochschule.bremen.minerva.persistence.exceptions.CountryExistsException;
import de.hochschule.bremen.minerva.persistence.exceptions.CountryNotFoundException;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.vo.AbstractValueObject;

public class CountryHandler extends AbstractDatabaseHandler implements Crudable {

	private final static HashMap<String, String> sql = new HashMap<String, String>();

	static {
		sql.put("selectById", "select id, token, name, color, continent, world from country where id = ?");
		sql.put("selectAllByWorldId", "select id, token, name, color, continent, world from country where world = ?");
		sql.put("insert", "insert into country (token, name, color, continent, world) values (?, ?, ?, ?, ?)");
		sql.put("update", "update country set token = ?, name = ?, color = ?, continent = ?, world = ? where id = ?");
		sql.put("delete", "delete from country where id = ?");
	}

	/**
	 * DOCME
	 */
	@Override
	public Country read(int id) throws PersistenceIOException {
		Country country = null;
		Object[] params = {id};
		
		try {
			ResultSet record = this.select(sql.get("selectById"), params);
			if (record.next()) {
				country = this.resultSetToObject(record);
				record.close();
			} else {
				throw new CountryNotFoundException("Found no country with id: '"+id+"'.");
			}

		} catch (DatabaseIOException e) {
			throw new PersistenceIOException(e.getMessage());
		} catch (SQLException e) {
			throw new PersistenceIOException("Error occurred while reading the country (id="
											 +id+") from the database.");
		}

		return country;
	}

	/**
	 * Loads all countries from the database. There is no given world.
	 * So we load the first world via WorldHandler. Well, the default world ;)
	 * 
	 */
	@Override
	public Vector<Country> readAll() throws PersistenceIOException {
		WorldHandler handler = new WorldHandler();
		Vector<World> worlds = handler.readAll();

		return this.readAll(worlds.firstElement());
	}

	/**
	 * DOCME
	 * 
	 * @param byWorld
	 * @return
	 * @throws PersistenceIOException 
	 */
	public Vector<Country> readAll(World byWorld) throws PersistenceIOException {
		return this.readAll(byWorld.getId());
	}

	/**
	 * DOCME
	 * 
	 * @param byWorldId
	 * @return
	 * @throws PersistenceIOException 
	 */
	public Vector<Country> readAll(int byWorldId) throws PersistenceIOException {
		Vector<Country> countries = new Vector<Country>();

		try {
			ResultSet record = this.select(sql.get("selectAllByWorldId"));

			while (record.next()) {
				countries.add(this.resultSetToObject(record));
			}

			record.close();
		} catch (DatabaseIOException e) {
			throw new PersistenceIOException(e.getMessage());
		} catch (SQLException e) {
			throw new PersistenceIOException("Error occurred while receiving a country list from the database (world id = "
											 +byWorldId+"): "+e.getMessage()+" - "+e.getErrorCode());
		}

		return countries;
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public void remove(AbstractValueObject candidate) throws PersistenceIOException {
		Country deletableCountry = (Country)candidate;
		Object[] params = {deletableCountry.getId()};

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
	public void save(AbstractValueObject registrable) throws PersistenceIOException {
		Country registrableCountry = (Country)registrable;

		try {
			Object[] params = {
				registrableCountry.getToken(),
				registrableCountry.getName(),
				Integer.toHexString(registrableCountry.getColor().getRGB()),
				registrableCountry.getContinent().getId(),
				registrableCountry.getWorldId()
			};

			this.insert(sql.get("insert"), params);
		} catch (DatabaseIOException e) {
			try {
				Object[] params = {
					registrableCountry.getToken(),
					registrableCountry.getName(),
					Integer.toHexString(registrableCountry.getColor().getRGB()),
					registrableCountry.getContinent().getId(),
					registrableCountry.getWorldId(),
					registrableCountry.getId()
				};

				this.update(sql.get("update"), params);
			} catch (DatabaseDuplicateRecordException exe) {
				throw new CountryExistsException("Unable to serialize the country. There is already a similar one.");
			} catch (DatabaseIOException ex) {
				throw new PersistenceIOException("Unable to serialize the country object: "+ex.getMessage());
			}
		}
	}

	/**
	 * DOCME
	 */
	@Override
	protected Country resultSetToObject(ResultSet current) throws SQLException {
		Country country = new Country();
		
		country.setId(current.getInt(1));
		country.setToken(current.getString(2));
		country.setName(current.getString(3));
		country.setColor(Color.decode(current.getString(4)));
		
		// Note that we only save the continent id.
		// Please verify that you will load the continent
		// data from the database in a next step.
		Continent continent = new Continent();
		continent.setId(current.getInt(5));
		country.setContinent(continent);

		country.setWorldId(current.getInt(6));

		return country;
	}
}
