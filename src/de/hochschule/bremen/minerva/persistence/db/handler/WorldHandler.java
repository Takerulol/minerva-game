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

import de.hochschule.bremen.minerva.vo.ValueObject;
import de.hochschule.bremen.minerva.vo.World;
import de.hochschule.bremen.minerva.persistence.Crudable;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.persistence.exceptions.WorldExistsException;
import de.hochschule.bremen.minerva.persistence.exceptions.WorldNotFoundException;
import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseDuplicateRecordException;
import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseIOException;

/**
 * DOCME
 * 
 */
public class WorldHandler extends AbstractDatabaseHandler implements Crudable {

	private final static HashMap<String, String> sql = new HashMap<String, String>();

	static {
		sql.put("selectById", "select id, token, name, description, author, version from world where id = ?");
		sql.put("selectAll", "select id, token, name, description, author, version from world order by name");
		sql.put("insert", "insert into world (token, name, description, author, version) values (?, ?, ?, ?, ?)");
		sql.put("update", "update world set token = ?, name = ?, description = ?, author = ?, version = ? where id = ?");
		sql.put("delete", "delete from world where id = ?");
	}

	/**
	 * DOCME
	 * @throws PersistenceIOException 
	 * 
	 */
	public World read(int id) throws PersistenceIOException {
		World world = null;
		Object[] params = {id};
		
		try {
			ResultSet record = this.select(sql.get("selectById"), params);
			if (record.next()) {
				world = this.resultSetToObject(record);
				record.close();
			} else {
				throw new WorldNotFoundException("Found no world with id: '"+id+"'.");
			}

		} catch (DatabaseIOException e) {
			throw new PersistenceIOException(e.getMessage());
		} catch (SQLException e) {
			throw new PersistenceIOException("Error occurred while reading the world (id="
											 +id+") from the database.");
		}

		return world;
	}

	/**
	 * Loads all worlds from the database and return a
	 * list with world value objects.
	 * 
	 * @see de.hochschule.bremen.minerva.vo.World
	 * 
	 */
	@Override
	public Vector<World> readAll() throws PersistenceIOException {
		Vector<World> worlds = new Vector<World>();

		try {
			ResultSet record = this.select(sql.get("selectAll"));

			while (record.next()) {
				worlds.add(this.resultSetToObject(record));
			}

			record.close();
		} catch (DatabaseIOException e) {
			throw new PersistenceIOException(e.getMessage());
		} catch (SQLException e) {
			throw new PersistenceIOException("Error occurred while receiving a world list from the database: "
											 +e.getMessage()+" - "+e.getErrorCode());
		}

		return worlds;
	}

	@Override
	public void save(ValueObject registrable) throws PersistenceIOException {
		World registrableWorld = (World)registrable;

		try {
			Object[] params = {
				registrableWorld.getToken(),
				registrableWorld.getName(),
				registrableWorld.getDescription(),
				registrableWorld.getAuthor(),
				registrableWorld.getVersion()
			};

			this.insert(sql.get("insert"), params);
		} catch (DatabaseIOException e) {
			try {
				Object[] params = {
					registrableWorld.getToken(),
					registrableWorld.getName(),
					registrableWorld.getDescription(),
					registrableWorld.getAuthor(),
					registrableWorld.getVersion(),
					registrableWorld.getId()
				};
				this.update(sql.get("update"), params);
			} catch (DatabaseDuplicateRecordException exe) {
				throw new WorldExistsException("Unable to serialize the world. There is already a similar one.");
			} catch (DatabaseIOException ex) {
				throw new PersistenceIOException("Unable to serialize the world object: "+ex.getMessage());
			}
		}
	}

	@Override
	public void remove(ValueObject candidate) throws PersistenceIOException {
		World deletableWorld = (World)candidate;
		Object[] params = {deletableWorld.getId()};

		try {
			this.delete(sql.get("delete"), params);
		} catch (DatabaseIOException e) {
			throw new PersistenceIOException(e.getMessage());
		}
	}

	/**
	 * DOCME
	 * 
	 * @param current
	 * @return
	 * @throws SQLException
	 */
	protected World resultSetToObject(ResultSet current) throws SQLException {
		World world = new World();

		world.setId(current.getInt(1));
		world.setToken(current.getString(2).trim());
		world.setName(current.getString(3).trim());
		world.setDescription(current.getString(4).trim());
		world.setAuthor(current.getString(5).trim());
		world.setVersion(current.getString(6).trim());

		return world;
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
