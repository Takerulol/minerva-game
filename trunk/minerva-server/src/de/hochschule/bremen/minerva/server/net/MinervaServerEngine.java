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
import de.hochschule.bremen.minerva.commons.exceptions.GameAlreadyStartedException;
import de.hochschule.bremen.minerva.commons.exceptions.NoPlayerSlotAvailableException;
import de.hochschule.bremen.minerva.commons.exceptions.PlayerAlreadyLoggedInException;
import de.hochschule.bremen.minerva.commons.exceptions.PlayerDoesNotExistException;
import de.hochschule.bremen.minerva.commons.exceptions.PlayerExistsException;
import de.hochschule.bremen.minerva.commons.exceptions.WrongPasswordException;
import de.hochschule.bremen.minerva.commons.net.ServerEngine;
import de.hochschule.bremen.minerva.commons.vo.Player;
import de.hochschule.bremen.minerva.commons.vo.World;
import de.hochschule.bremen.minerva.server.core.logic.Game;
import de.hochschule.bremen.minerva.server.manager.AccountManager;
import de.hochschule.bremen.minerva.server.manager.WorldManager;
import de.hochschule.bremen.minerva.server.util.ConsoleLogger;
import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.NameBindingException;
import de.root1.simon.exceptions.SimonRemoteException;

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
	
	private static final ConsoleLogger LOGGER = ConsoleLogger.getLogger(); 
	
	private Game game = new Game();

	/**
	 * DOCME
	 * 
	 */
	public MinervaServerEngine(String name, int port) throws UnknownHostException, IOException, NameBindingException {
		Registry registry = Simon.createRegistry(port);
		registry.bind(name, this);
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public Player login(Player player) throws SimonRemoteException, PlayerAlreadyLoggedInException, GameAlreadyStartedException, WrongPasswordException, PlayerDoesNotExistException, NoPlayerSlotAvailableException, DataAccessException {
		// TODO: Implement client callback method, which will be called if the user is the current player.
		LOGGER.log("login() this player: "+player.toString());
		AccountManager.getInstance().login(player);

		this.game.addPlayer(player);

		return player;
	}

	/**
	 * DOCME
	 * 
	 */
	public void register(Player player) throws SimonRemoteException, PlayerExistsException, DataAccessException {
		LOGGER.log("register() this player: "+player.toString());
		AccountManager.getInstance().createPlayer(player);
	}
	
	/**
	 * Returns a vector with all available worlds.
	 *
	 * @param flatView Should each world provides the whole country dependency graph? 
	 * @return A vector with all available worlds.
	 *
	 * @throws SimonRemoteException
	 * @throws DataAccessException
	 *
	 */
	@Override
	public Vector<World> getWorlds(boolean flatView) throws DataAccessException {
		LOGGER.log("getWorlds()");
		return WorldManager.getInstance().getList(flatView);
	}

	public World getGameWorld() {
		return this.game.getWorld();
	}
}