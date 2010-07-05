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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

import de.hochschule.bremen.minerva.commons.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.commons.exceptions.GameAlreadyStartedException;
import de.hochschule.bremen.minerva.commons.exceptions.NoPlayerLoggedInException;
import de.hochschule.bremen.minerva.commons.exceptions.NoPlayerSlotAvailableException;
import de.hochschule.bremen.minerva.commons.exceptions.NotEnoughPlayersLoggedInException;
import de.hochschule.bremen.minerva.commons.exceptions.PlayerAlreadyLoggedInException;
import de.hochschule.bremen.minerva.commons.exceptions.PlayerDoesNotExistException;
import de.hochschule.bremen.minerva.commons.exceptions.PlayerExistsException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldNotDefinedException;
import de.hochschule.bremen.minerva.commons.exceptions.WrongPasswordException;
import de.hochschule.bremen.minerva.commons.net.ClientExecutables;
import de.hochschule.bremen.minerva.commons.net.ServerExecutables;
import de.hochschule.bremen.minerva.commons.vo.Mission;
import de.hochschule.bremen.minerva.commons.vo.Player;
import de.hochschule.bremen.minerva.commons.vo.PlayerState;
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
public class MinervaServerEngine implements ServerExecutables {

	private static final long serialVersionUID = 1911446743019185828L;

	private static final ConsoleLogger LOGGER = ConsoleLogger.getLogger(); 

	private Game game = new Game();

	private HashMap<Player, ClientExecutables> clients = new HashMap<Player, ClientExecutables>();

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
	public void login(Player player, ClientExecutables clientExecutables) throws SimonRemoteException, PlayerAlreadyLoggedInException, GameAlreadyStartedException, WrongPasswordException, PlayerDoesNotExistException, NoPlayerSlotAvailableException, DataAccessException {
		LOGGER.log("login this player: "+player.toString());
		AccountManager.getInstance().login(player);

		if (this.game.getPlayerCount() == 0) {
			player.setMaster(true);
		}
		
		player.setState(PlayerState.GAME_INIT);
		
		this.game.addPlayer(player);

		// Put the client in our central client map.
		this.clients.put(player, clientExecutables);

		this.notifyClients();
	}

	/**
	 * DOCME
	 * 
	 */
	public void register(Player player) throws SimonRemoteException, PlayerExistsException, DataAccessException {
		LOGGER.log("register this player: "+player.toString());
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
	public Vector<World> getWorlds(boolean flatView) throws SimonRemoteException, DataAccessException {
		LOGGER.log("getWorlds - flatView: "+flatView);
		return WorldManager.getInstance().getList(flatView);
	}

	/**
	 * DOCME
	 *
	 * @return
	 *
	 */
	@Override
	public void startGame() throws SimonRemoteException, NotEnoughPlayersLoggedInException, NoPlayerLoggedInException, WorldNotDefinedException, DataAccessException {
		LOGGER.log("startGame");
		this.game.start();
		
		this.notifyClients();
	}

	/**
	 * DOCME
	 *
	 * @return
	 *
	 */
	@Override
	public void killGame(boolean createNewOne) throws SimonRemoteException, DataAccessException {
		AccountManager.getInstance().logout();
		
		if (createNewOne) {
			this.game = new Game();
		}
	}

	/**
	 * DOCME
	 *
	 */
	@Override
	public void setGameWorld(World world) throws SimonRemoteException, DataAccessException {
		this.game.setWorld(world);
	}

	/**
	 * DOCME
	 *
	 */
	@Override
	public World getGameWorld() throws SimonRemoteException, DataAccessException {
		return this.game.getWorld();
	}

	/**
	 * DOCME
	 *
	 */
	@Override
	public Vector<Player> getGamePlayers() throws SimonRemoteException {
		return this.game.getPlayers();
	}

	/**
	 * DOCME
	 *
	 */
	@Override
	public Vector<Mission> getGameMissions() throws SimonRemoteException {
		return this.game.getMissions();
	}
	
	/**
	 * DOCME
	 *
	 */
	@Override
	public boolean isGameFinished() throws SimonRemoteException {
		return this.game.isFinished();
	}

	/**
	 * DOCME
	 *
	 */
	@Override
	public Player getGameWinner() throws SimonRemoteException {
		return this.game.getWinner();
	}	
	
	/**
	 * DOCME
	 * @throws SimonRemoteException 
	 *
	 */
	private void notifyClients() throws SimonRemoteException {
		LOGGER.log("notifyClients");
		Iterator<Entry<Player, ClientExecutables>> iter = this.clients.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Player, ClientExecutables> entry = iter.next();
			ClientExecutables client = entry.getValue();
			
			client.refreshPlayer(entry.getKey());
		}
	}
}