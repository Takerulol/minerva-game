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
import de.hochschule.bremen.minerva.persistence.Handler;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.persistence.exceptions.WorldExistsException;
import de.hochschule.bremen.minerva.persistence.exceptions.WorldNotFoundException;
import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseDuplicateRecordException;
import de.hochschule.bremen.minerva.persistence.db.exceptions.DatabaseIOException;

/**
 * DOCME
 * 
 */
public class WorldHandler extends AbstractDatabaseHandler implements Handler {

	private final static HashMap<String, String> sql = new HashMap<String, String>();

	static {
		sql.put("selectById", "select \"id\", \"token\", \"name\", \"description\", \"author\", \"version\" from world where \"id\" = ?");
		sql.put("selectByName", "select \"id\", \"token\", \"name\", \"description\", \"author\", \"version\" from world where \"name\" = ?");
		sql.put("selectAll", "select \"id\", \"token\", \"name\", \"description\", \"author\", \"version\" from world order by \"name\"");
		sql.put("insert", "insert into world (\"token\", \"name\", \"description\", \"author\", \"version\") values (?, ?, ?, ?, ?)");
		sql.put("update", "update world set \"token\" = ?, \"name\" = ?, \"description\" = ?, \"author\" = ?, \"version\" = ? where \"id\" = ?");
		sql.put("delete", "delete from world where \"id\" = ?");
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
			world = this.read(sql.get("selectById"), params);
		} catch (WorldNotFoundException e) {
			throw new WorldNotFoundException("The world with the id '"+id+"' wasn't found.");
		} catch (DatabaseIOException e) {
			throw new PersistenceIOException("Error occurred while reading "
                    + "the world (id=" + id +") "
                    + "from the database. Reason: "+e.getMessage());
		}
		
		return world;
	}

	/**
	 * DOCME
	 * 
	 */
	public World read(String name) throws PersistenceIOException {
		World world = null;
		Object[] params = {name};

		try {
			world = this.read(sql.get("selectByName"), params);
		} catch (WorldNotFoundException e) {
			throw new WorldNotFoundException("The world '"+name+"' wasn't found.");
		} catch (DatabaseIOException e) {
			throw new PersistenceIOException("Error occurred while reading "
					                       + "the world '" + name +"' "
					                       + "from the database. Reason: "+e.getMessage());
		}
		
		return world;
	}

	/**
	 * DOCME
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws WorldNotFoundException
	 * @throws DatabaseIOException
	 */
	private World read(String sql, Object[] params) throws WorldNotFoundException, DatabaseIOException {
		World world = null;

		try {
			ResultSet record = this.select(sql, params);

			if (record.next()) {
				world = this.resultSetToObject(record);
				record.close();
			} else {
				throw new WorldNotFoundException();
			}
		} catch (SQLException e) {
			throw new DatabaseIOException("Error while reading from result set: " + e.getErrorCode());
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
	public void save(ValueObject world) throws PersistenceIOException {
		World registrableWorld = (World)world;

		try {
			try {
				// We try to load the world by the given id.
				// When this is not possible (WorldNotFoundException), we
				// will update the record else we will insert it.
				this.read(registrableWorld.getId());

				Object[] params = {
					registrableWorld.getToken(),
					registrableWorld.getName(),
					registrableWorld.getDescription(),
					registrableWorld.getAuthor(),
					registrableWorld.getVersion(),
					registrableWorld.getId()
				};
				this.update(sql.get("update"), params);
			} catch (WorldNotFoundException e) {
				Object[] params = {
					registrableWorld.getToken(),
					registrableWorld.getName(),
					registrableWorld.getDescription(),
					registrableWorld.getAuthor(),
					registrableWorld.getVersion()
				};

				this.insert(sql.get("insert"), params);
			}
		} catch (DatabaseDuplicateRecordException ex) {
			throw new WorldExistsException("Unable to serialize the "
					+"world '"+registrableWorld.getName()+"'. There is already "
					+"a similar one.");
		} catch (DatabaseIOException e) {
			throw new PersistenceIOException("Unable to serialize the world '"+registrableWorld.getName()+"'. Reason: "+e.getMessage());
		}

		// The player does not have a player id.
		// So we read the player object by the given username
		// to fulfill the referenced player value object.
		registrableWorld.setId(this.read(registrableWorld.getName()).getId());
		world = registrableWorld;
	}

	/**
	 * DOCME
	 * 
	 */
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

		// TODO: Replace trim. Use correct database datatype.
		world.setId(current.getInt(1));
		world.setToken(current.getString(2));
		world.setName(current.getString(3));
		world.setDescription(current.getString(4));
		world.setAuthor(current.getString(5));
		world.setVersion(current.getString(6));

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
