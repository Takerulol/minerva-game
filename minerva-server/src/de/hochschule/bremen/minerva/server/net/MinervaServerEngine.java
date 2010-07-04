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
package de.hochschule.bremen.minerva.server.net;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

import de.hochschule.bremen.minerva.commons.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.commons.net.ServerEngine;
import de.hochschule.bremen.minerva.commons.vo.World;
import de.hochschule.bremen.minerva.server.manager.WorldManager;
import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.NameBindingException;

/**
 * The server engine, which implements the server protocol.
 * Methods, which can be invoked from any client.
 *
 * @since 1.0
 * @version $Id$
 * 
 */
public class MinervaServerEngine implements ServerEngine {

	private static final long serialVersionUID = 1911446743019185828L;
	
	/**
	 * DOCME
	 * 
	 * @param name
	 * @param port
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws NameBindingException
	 *
	 */
	public MinervaServerEngine(String name, int port) throws UnknownHostException, IOException, NameBindingException {
		Registry registry = Simon.createRegistry(port);
		registry.bind(name, this);
	}

	/**
	 * DOCME
	 *
	 * @throws DataAccessException 
	 * 
	 */
	@Override
	public Vector<World> getWorlds(boolean flatView) throws DataAccessException {
		return WorldManager.getInstance().getList(flatView);
	}
}
