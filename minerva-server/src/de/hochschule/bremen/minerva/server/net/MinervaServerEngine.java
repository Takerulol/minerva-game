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

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

import javax.imageio.ImageIO;

import de.hochschule.bremen.minerva.commons.exceptions.CountriesNotInRelationException;
import de.hochschule.bremen.minerva.commons.exceptions.CountryOwnerException;
import de.hochschule.bremen.minerva.commons.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.commons.exceptions.GameAlreadyStartedException;
import de.hochschule.bremen.minerva.commons.exceptions.IsOwnCountryException;
import de.hochschule.bremen.minerva.commons.exceptions.NoPlayerLoggedInException;
import de.hochschule.bremen.minerva.commons.exceptions.NoPlayerSlotAvailableException;
import de.hochschule.bremen.minerva.commons.exceptions.NotEnoughArmiesException;
import de.hochschule.bremen.minerva.commons.exceptions.NotEnoughPlayersLoggedInException;
import de.hochschule.bremen.minerva.commons.exceptions.PlayerAlreadyLoggedInException;
import de.hochschule.bremen.minerva.commons.exceptions.PlayerDoesNotExistException;
import de.hochschule.bremen.minerva.commons.exceptions.PlayerExistsException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldNotDefinedException;
import de.hochschule.bremen.minerva.commons.exceptions.WrongPasswordException;
import de.hochschule.bremen.minerva.commons.net.ClientExecutables;
import de.hochschule.bremen.minerva.commons.net.ServerExecutables;
import de.hochschule.bremen.minerva.commons.vo.AttackResult;
import de.hochschule.bremen.minerva.commons.vo.Country;
import de.hochschule.bremen.minerva.commons.vo.CountryCard;
import de.hochschule.bremen.minerva.commons.vo.Mission;
import de.hochschule.bremen.minerva.commons.vo.Player;
import de.hochschule.bremen.minerva.commons.vo.PlayerState;
import de.hochschule.bremen.minerva.commons.vo.World;
import de.hochschule.bremen.minerva.server.core.logic.Game;
import de.hochschule.bremen.minerva.server.core.logic.Turn;
import de.hochschule.bremen.minerva.server.manager.AccountManager;
import de.hochschule.bremen.minerva.server.manager.ApplicationConfigurationManager;
import de.hochschule.bremen.minerva.server.manager.WorldManager;
import de.hochschule.bremen.minerva.server.util.ConsoleLogger;
import de.hochschule.bremen.minerva.server.vo.ApplicationConfiguration;
import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.NameBindingException;
import de.root1.simon.exceptions.SimonRemoteException;

/**
 * The server engine, which implements the server protocol.
 * In other words: Contains methods, which can be invoked from any client.
 * It's like a facade for accessing the minerva subsystems.
 * 
 * This facade was the 'GameEngineLocal' in the "hotseat" version ;)
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
		LOGGER.log("login(): login this player: "+player.toString());
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
		LOGGER.log("register(): this player: "+player.toString());
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
		LOGGER.log("getWorlds(): flatView:"+flatView);
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
		LOGGER.log("startGame(): THE GAME WAS STARTED!");
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
		LOGGER.log("killGame(): THE GAME WAS KILLED! :o");
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
		LOGGER.log("setGameWorld(): Gamemaster defined the following world to play on: '"+world.getName()+"'");
		
		ApplicationConfiguration appConfig = ApplicationConfigurationManager.get();
		String filepath = appConfig.getAssetsWorldDirectory();

		world.setMapImage(this.loadMapImage(filepath + world.getMap()));
		world.setMapUnderlayImage(this.loadMapImage(filepath + world.getMapUnderlay()));

		this.game.setWorld(world);
	}

	/**
	 * DOCME
	 *
	 */
	@Override
	public World getGameWorld() throws SimonRemoteException, DataAccessException {
		LOGGER.log("getGameWorld(): The game world was requested ...");
		return this.game.getWorld();
	}

	/**
	 * DOCME
	 *
	 */
	@Override
	public Vector<Player> getGamePlayers() throws SimonRemoteException {
		LOGGER.log("getGamePlayers(): The game world players was requested ...");
		return this.game.getPlayers();
	}

	/**
	 * DOCME
	 *
	 */
	@Override
	public Vector<Mission> getGameMissions() throws SimonRemoteException {
		LOGGER.log("getGameMissions()");
		return this.game.getMissions();
	}
	
	/**
	 * DOCME
	 *
	 */
	@Override
	public boolean isGameFinished() throws SimonRemoteException {
		LOGGER.log("isGameFinished(): Game finished? -> " + ((this.game.isFinished()) ? "Yes :(" : "No :)"));
		return this.game.isFinished();
	}

	/**
	 * DOCME
	 *
	 */
	@Override
	public byte[] getGameMapImage() throws SimonRemoteException {
		LOGGER.log("getGameMapImage(): Load the map image (world = '" + this.game.getWorld().getName() + "').");

		WritableRaster raster = this.game.getWorld().getMapImage().getRaster();
		return ((DataBufferByte)raster.getDataBuffer()).getData();
	}

	/**
	 * DOCME
	 *
	 */
	@Override
	public byte[] getGameMapUnderlayImage() throws SimonRemoteException {
		LOGGER.log("getGameMapImage(): Load the map image underlay (world = '" + this.game.getWorld().getName() + "').");

		WritableRaster raster = this.game.getWorld().getMapUnderlayImage().getRaster();
		return ((DataBufferByte)raster.getDataBuffer()).getData();
	}

	/**
	 * DOCME
	 *
	 */
	@Override
	public Player getGameWinner() throws SimonRemoteException {
		LOGGER.log("getGameMissions(): AND THE WINNER IS: " + ((this.game.isFinished()) ? this.game.getWinner() : "nobody - Game is still running."));
		return this.game.getWinner();
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public void releaseCard(CountryCard card) throws SimonRemoteException {
		LOGGER.log("releaseCard(): Release the card from country: '"+card.getReference().getName()+"'");
		Turn turn = this.game.getCurrentTurn();
		turn.releaseCard(card);
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public void releaseCards(Vector<CountryCard> cards) throws SimonRemoteException {
		LOGGER.log("releaseCards(): Release a card stack.");
		Turn turn = this.game.getCurrentTurn();
		turn.releaseCardSeries(cards);
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public int getAllocatableArmyCount() throws SimonRemoteException {
		int armyCount = this.game.getCurrentTurn().getAllocatableArmyCount();
		LOGGER.log("getAllocatableArmyCount(): Allocatable armies in this turn: "+armyCount);
		return armyCount;
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public void allocateArmy(Country allocatable) throws SimonRemoteException, NotEnoughArmiesException, CountryOwnerException {
		LOGGER.log("allocateArmy(): Allocate one army on country '"+ allocatable.getName() +"'");
        Turn turn = this.game.getCurrentTurn();
        turn.allocateArmy(allocatable);
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public AttackResult attack(Country source, Country destination, int armyCount) throws SimonRemoteException, CountriesNotInRelationException, NotEnoughArmiesException, IsOwnCountryException {
		Turn turn = this.game.getCurrentTurn();
		AttackResult result = turn.attack(source, destination, armyCount);
		LOGGER.log("attack(): Attacking '"+destination.getName()+"' from '"+source.getName()+"' with " + armyCount + " army units. Result: "+result.toString());

        return turn.attack(source, destination, armyCount);
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public void move(Country source, Country destination, int armyCount) throws SimonRemoteException, CountriesNotInRelationException, NotEnoughArmiesException, CountryOwnerException {
		LOGGER.log("move(): Move "+armyCount+" army units from '"+source.getName()+"' to '"+destination.getName()+"'.");

		Turn turn = this.game.getCurrentTurn();
        turn.moveArmies(source, destination, armyCount);
	}

	/**
	 * DOCME
	 * 
	 */
	@Override
	public void finishTurn() throws SimonRemoteException {
        this.game.nextTurn();
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

	/**
	 * DOCME
	 *
	 * @param filepath
	 * @return
	 */
	private BufferedImage loadMapImage(String filepath) {
		BufferedImage map = null; 
		try {
			map = ImageIO.read(new File(filepath));
		} catch (IOException e) {
			LOGGER.error("Loading map image failed. Path: '"+filepath+"'. Reason: " + e.getMessage());
		}
		return map;
	}
}