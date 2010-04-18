/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: CountryHandler.java 85 2010-04-18 14:24:04Z andre.koenig $
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

import de.hochschule.bremen.minerva.persistence.Crudable;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.vo.ValueObject;

public class PlayerHandler extends AbstractDatabaseHandler implements Crudable {

	private final static HashMap<String, String> sql = new HashMap<String, String>();

	static {
		sql.put("selectById", "select id, username, password, last_name, first_name, email, last_login from player where username = ?");
		sql.put("insert", "insert into player (username, password, last_name, first_name, email, last_login) values (?, ?, ?, ?, ?, ?)");
		sql.put("update", "update player set username = ?, password = ?, last_name = ?, first_name = ?, email = ?, last_login = ? where username = ?");
		sql.put("delete", "delete from player where username = ?");
	}

	
	
	@Override
	protected ValueObject resultSetToObject(ResultSet current) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueObject read(int id) throws PersistenceIOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<? extends ValueObject> readAll()
			throws PersistenceIOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<? extends ValueObject> readAll(ValueObject reference)
			throws PersistenceIOException {
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
