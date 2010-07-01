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
//import java.util.logging.Level;
//import java.util.logging.Logger;

import de.hochschule.bremen.minerva.util.ColorTool;
import de.hochschule.bremen.minerva.vo.Continent;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.World;
import de.hochschule.bremen.minerva.persistence.Handler;
import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseDuplicateRecordException;
import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseIOException;
import de.hochschule.bremen.minerva.persistence.exceptions.CountryExistsException;
import de.hochschule.bremen.minerva.persistence.exceptions.CountryNotFoundException;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceException;
import de.hochschule.bremen.minerva.vo.ValueObject;

/**
 * TODO: Issue 4: Exception-Meldungen auslagern (http://code.google.com/p/minerva-game/issues/detail?id=4)
 */

/**
 * DOCME
 * 
 */
public class CountryHandler extends AbstractDatabaseHandler implements Handler {

	//private static Logger LOGGER = Logger.getLogger(CountryHandler.class.getName());
	
	private final static HashMap<String, String> sql = new HashMap<String, String>();

	static {
		sql.put("selectById", "select \"id\", \"token\", \"name\", \"color\", \"continent\", \"world\" from country where \"id\" = ?");
		sql.put("selectByName", "select \"id\", \"token\", \"name\", \"color\", \"continent\", \"world\" from country where \"name\" = ?");
		sql.put("selectAllByWorldId", "select \"id\", \"token\", \"name\", \"color\", \"continent\", \"world\" from country where \"world\" = ?");
		sql.put("selectAllByContinentId", "select \"id\", \"token\", \"name\", \"color\", \"continent\", \"world\" from country where \"continent\" = ?");
		sql.put("insert", "insert into country (\"token\", \"name\", \"color\", \"continent\", \"world\") values (?, ?, ?, ?, ?)");
		sql.put("update", "update country set \"token\" = ?, \"name\" = ?, \"color\" = ?, \"continent\" = ?, \"world\" = ? where \"id\" = ?");
		sql.put("delete", "delete from country where \"id\" = ?");
	}

	/**
	 * Reads ONE country with the given id from the database.
	 * 
	 * @param id - The country id
	 * @throws CountryNotFoundException, PersistenceIOException
	 * 
	 */
	@Override
	public Country read(int id) throws PersistenceException {
		Country country = null;
		Object[] params = {id};

		try {
			country = this.read(sql.get("selectById"), params);
		} catch (CountryNotFoundException e) {
			throw new CountryNotFoundException("The country with the id '"+id+"' wasn't found.");
		} catch (DatabaseIOException e) {
			throw new PersistenceException("Error occurred while reading "
					                       + "the country (id=" + id +") "
					                       + "from the database. Reason: "+e.getMessage());
		}

		return country;
	}

	/**
	 * DOCME
	 * 
	 */
	public Country read(String name) throws PersistenceException {
		Country country = null;
		Object[] params = {name};

		try {
			country = this.read(sql.get("selectByName"), params);
		} catch (CountryNotFoundException e) {
			throw new CountryNotFoundException("The country '"+name+"' wasn't found.");
		} catch (DatabaseIOException e) {
			throw new PersistenceException("Error occurred while reading "
					                       + "the country '" + name + "'"
					                       + "from the database. Reason: "+e.getMessage());
		}
		
		return country;
	}

	/**
	 * DOCME
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws CountryNotFoundException
	 * @throws DatabaseIOException
	 */
	private Country read(String sql, Object[] params) throws CountryNotFoundException, DatabaseIOException {
		Country country = null;

		try {
			ResultSet record = this.select(sql, params);

			if (record.next()) {
				country = this.resultSetToObject(record);
				record.close();
			} else {
				throw new CountryNotFoundException();
			}
		} catch (SQLException e) {
			throw new DatabaseIOException("Error while reading from result set: " + e.getErrorCode());
		}

		return country;
	}

	/**
	 * Reads all countries from the database.
	 * We also read the first world from the database and use
	 * this as reference for selecting the countries. Well, the
	 * first world is our default world ;)
	 * 
	 * This is a wrapper class for the private method:
	 * readAll(int byWorldId).
	 * 
	 * @return A collection with the selected countries.
	 * @throws PersistenceException
	 * 
	 */
	@Override
	public Vector<Country> readAll() throws PersistenceException {
		WorldHandler handler = new WorldHandler();
		Vector<World> worlds = handler.readAll();

		return this.readAll(worlds.firstElement());
	}

	/**
	 * Reads all countries by an given world value object
	 * 
	 * This is a wrapper class for the private method:
	 * readAll(int byWorldId).
	 * 
	 * @param byWorld - The world value object.
	 * @return A collection with the selected countries.
	 * @throws PersistenceException
	 *  
	 */
	public Vector<Country> readAll(ValueObject byVo) throws PersistenceException {
		Vector<Country> countries = null;
		
		if (byVo instanceof World) {
			countries = this.readAll((World)byVo);
		} else if (byVo instanceof Continent) {
			countries = this.readAll((Continent)byVo);
		} else {
			throw new PersistenceException("There is no method implementation for the given value object: "+byVo.getClass());
		}
		
		return countries;
	}

	/**
	 * Reads all countries from the database which are linked
	 * to the given continent. 
	 * 
	 * @param byContinent
	 * @return A collection with the selected countries.
	 * @throws PersistenceException
	 */
	private Vector<Country> readAll(Continent byContinent) throws PersistenceException {
		try {
			Object[] params = {byContinent.getId()};
			return this.readAll(sql.get("selectAllByContinentId"), params);
		} catch (PersistenceException e) {
			throw new PersistenceException("Error while reading all countries "
					+"from the database by the given continent id: "+byContinent.getId());
		}
	}
	
	/**
	 * Reads all countries from the database which are linked
	 * to the given world.
	 * 
	 * @param byWorldId - A int with the world id.
	 * @return A collection with the selected countries.
	 * @throws PersistenceException 
	 */
	private Vector<Country> readAll(World byWorld) throws PersistenceException {
		try {
			Object[] params = {byWorld.getId()};
			return this.readAll(sql.get("selectAllByWorldId"), params);
		} catch (PersistenceException e) {
			throw new PersistenceException("Error while reading all countries "
					+"from the database by the given world id: "+byWorld.getId());
		}
	}

	/**
	 * DOCME
	 * 
	 * @param sqlKey
	 * @param params
	 * @return
	 * @throws PersistenceException
	 */
	private Vector<Country> readAll(String sql, Object[] params) throws PersistenceException {
		Vector<Country> countries = new Vector<Country>();
		
		try {
			ResultSet record = this.select(sql, params);

			while (record.next()) {
				countries.add(this.resultSetToObject(record));
			}

			record.close();
		} catch (DatabaseIOException e) {
			throw new PersistenceException(e.getMessage());
		} catch (SQLException e) {
			throw new PersistenceException("SQL error code: "+e.getErrorCode());
		}

		return countries;		
	}
	
	/**
	 * DOCME
	 * 
	 */
	@Override
	public void remove(ValueObject candidate) throws PersistenceException {
		Country deletableCountry = (Country)candidate;
		Object[] params = {deletableCountry.getId()};

		try {
			this.delete(sql.get("delete"), params);
		} catch (DatabaseIOException e) {
			throw new PersistenceException(e.getMessage());
		}
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public void save(ValueObject country) throws PersistenceException {
		Country registrableCountry = (Country)country;

		try {
			try {
				// We try to load the country by the given id.
				// When this is not possible (CountryNotFoundException), we
				// will update the record else we will insert it.
				this.read(registrableCountry.getId());

				Object[] params = {
					registrableCountry.getToken(),
					registrableCountry.getName(),
					ColorTool.toHexCode(registrableCountry.getColor()),
					registrableCountry.getContinent().getId(),
					registrableCountry.getWorldId(),
					registrableCountry.getId()
				};

				this.update(sql.get("update"), params);
			} catch (CountryNotFoundException e) {
				Object[] params = {
					registrableCountry.getToken(),
					registrableCountry.getName(),
					ColorTool.toHexCode(registrableCountry.getColor()),
					registrableCountry.getContinent().getId(),
					registrableCountry.getWorldId()
				};

				this.insert(sql.get("insert"), params);
			}
		} catch (DatabaseDuplicateRecordException ex) {
			throw new CountryExistsException("Unable to serialize the "
						+"country: '"+registrableCountry.getName()+"'. There is already "
						+"a similar one.");
		} catch (DatabaseIOException e) {
			throw new PersistenceException("Unable to serialize the country: '"+registrableCountry.getName()+"'. Reason: "+e.getMessage());
		}

		// The country does not have a player id.
		// So we read the country object by the given name
		// to fulfill the referenced country value object.
		registrableCountry.setId(this.read(registrableCountry.getName()).getId());
		country = registrableCountry;
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
		country.setColor(ColorTool.fromHexCode(current.getString(4)));
		
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