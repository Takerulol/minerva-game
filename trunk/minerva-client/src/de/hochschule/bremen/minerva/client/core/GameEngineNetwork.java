/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: ApplicationConfiguration.java 699 2010-07-04 17:07:33Z andre.koenig $
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
package de.hochschule.bremen.minerva.client.core;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import de.hochschule.bremen.minerva.client.manager.ApplicationConfigurationManager;
import de.hochschule.bremen.minerva.client.vo.ApplicationConfiguration;
import de.hochschule.bremen.minerva.commons.core.GameEngine;
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
import de.hochschule.bremen.minerva.commons.exceptions.WorldFileExtensionException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldFileNotFoundException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldFileParseException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldNotDefinedException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldNotStorable;
import de.hochschule.bremen.minerva.commons.exceptions.WrongPasswordException;
import de.hochschule.bremen.minerva.commons.net.ClientExecutables;
import de.hochschule.bremen.minerva.commons.net.ServerExecutables;
import de.hochschule.bremen.minerva.commons.vo.AttackResult;
import de.hochschule.bremen.minerva.commons.vo.Country;
import de.hochschule.bremen.minerva.commons.vo.CountryCard;
import de.hochschule.bremen.minerva.commons.vo.Mission;
import de.hochschule.bremen.minerva.commons.vo.Player;
import de.hochschule.bremen.minerva.commons.vo.World;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;
import de.root1.simon.exceptions.SimonRemoteException;

/**
 * The network game engine.
 *
 * @see ApplicationConfigurationManager
 * @version $Id: ApplicationConfiguration.java 699 2010-07-04 17:07:33Z andre.koenig $
 * @since 1.0
 * 
 */
public class GameEngineNetwork extends Observable implements GameEngine, ClientExecutables {

	private static final long serialVersionUID = 7809319868203138075L;

	private static GameEngineNetwork instance = null;
	
	private ServerExecutables serverEngine = null;
	
	private Player clientPlayer = null;
	
	/**
	 * DOCME
	 *
	 * @throws LookupFailedException 
	 * @throws IOException 
	 * @throws SimonRemoteException 
	 * @throws EstablishConnectionFailed 
	 * 
	 */
	private GameEngineNetwork() throws EstablishConnectionFailed, SimonRemoteException, IOException, LookupFailedException {
		ApplicationConfiguration appConfg = ApplicationConfigurationManager.get();

		String serverHost = appConfg.getServerHost();
		String serverName = appConfg.getServerName();
		int serverPort = Integer.parseInt(appConfg.getServerPort());

		this.serverEngine = (ServerExecutables)Simon.lookup(serverHost, serverPort, serverName);		
	}

	/**
	 * DOCME
	 * 
	 * @return
	 * @throws DataAccessException 
	 *
	 */
	public static GameEngineNetwork getEngine() throws DataAccessException {
		if (GameEngineNetwork.instance == null) {
			try {
				GameEngineNetwork.instance = new GameEngineNetwork();
			} catch (EstablishConnectionFailed e) {
				throw new DataAccessException(e);
			} catch (SimonRemoteException e) {
				throw new DataAccessException(e);
			} catch (IOException e) {
				throw new DataAccessException(e, true);
			} catch (LookupFailedException e) {
				throw new DataAccessException(e, ApplicationConfigurationManager.get().getServerName());
			}
		}

		return GameEngineNetwork.instance;
	}

	/**
	 * DOCME
	 *
	 */
	@Override
	public void login(Player player) throws PlayerAlreadyLoggedInException, GameAlreadyStartedException, WrongPasswordException, PlayerDoesNotExistException, NoPlayerSlotAvailableException, DataAccessException {
		try {
			this.serverEngine.login(player, GameEngineNetwork.getEngine());
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * DOCME
	 *
	 */
	@Override
	public void register(Player player) throws PlayerExistsException, DataAccessException{
		try {
			this.serverEngine.register(player);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * DOCME
	 *
	 */
	@Override
	public Vector<World> getWorldList() throws DataAccessException {
		try {
			return this.serverEngine.getWorlds(false);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e.getMessage());
		}
	}

	@Override
	public Vector<World> getWorldList(boolean lite) throws DataAccessException {
		try {
			return this.serverEngine.getWorlds(lite);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e.getMessage());
		}
	}

	@Override
	public void importWorld(File worldFile) throws WorldNotStorable, WorldFileNotFoundException, WorldFileExtensionException, WorldFileParseException, DataAccessException {}

	/**
	 * DOCME
	 *
	 */
	@Override
	public void startGame() throws NotEnoughPlayersLoggedInException, NoPlayerLoggedInException, WorldNotDefinedException, DataAccessException {
		try {
			this.serverEngine.startGame();
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * DOCME
	 *
	 */
	@Override
	public void killGame(boolean createNewOne) throws DataAccessException {
		try {
			this.serverEngine.killGame(createNewOne);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

	@Override
	public Vector<Player> getGamePlayers() throws DataAccessException {
		try {
			return this.serverEngine.getGamePlayers();
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

	@Override
	public void setGameWorld(World world) throws DataAccessException {
		try {
			this.serverEngine.setGameWorld(world);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

	@Override
	public World getGameWorld() throws DataAccessException {
		try {
			return this.serverEngine.getGameWorld();
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}


	@Override
	public Vector<Mission> getGameMissions() throws DataAccessException {
		try {
			return this.serverEngine.getGameMissions();
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

	@Override
	public boolean isGameFinished() throws DataAccessException {
		try {
			return this.serverEngine.isGameFinished();
		} catch (SimonRemoteException e) {
			 throw new DataAccessException(e);
		}
	}

	@Override
	public Player getGameWinner() throws DataAccessException {
		try {
			return this.serverEngine.getGameWinner();
		} catch (SimonRemoteException e) {
			 throw new DataAccessException(e);
		}
	}

	@Override
	public void releaseCard(CountryCard card) throws DataAccessException {
		try {
			this.serverEngine.releaseCard(card);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

	@Override
	public void releaseCards(Vector<CountryCard> cards) throws DataAccessException {
		try {
			this.serverEngine.releaseCards(cards);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

	@Override
	public int getAllocatableArmyCount() throws DataAccessException {
		try {
			return this.serverEngine.getAllocatableArmyCount();
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

	@Override
	public void allocateArmy(Country allocatable) throws NotEnoughArmiesException, CountryOwnerException, DataAccessException {
		try {
			this.serverEngine.allocateArmy(allocatable);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

	@Override
	public AttackResult attack(Country source, Country destination, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException, IsOwnCountryException, DataAccessException {
		try {
			return this.serverEngine.attack(source, destination, armyCount);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

	@Override
	public void move(Country source, Country destination, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException, CountryOwnerException, DataAccessException {
		try {
			this.serverEngine.move(source, destination, armyCount);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

	@Override
	public void finishTurn() throws DataAccessException {
		try {
			this.serverEngine.finishTurn();
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * DOCME
	 *
	 */
	public Player getClientPlayer() {
		return this.clientPlayer;
	}

	/**
	 * DOCME
	 *
	 */
	public void addObserver(Observer o) {
		super.addObserver(o);
	}

	/**
	 * DOCME
	 *
	 */
	@Override
	public void refreshPlayer(Player player) {
		System.out.println("Server hat refreshPlayer aufgerufen: "+player);
		this.clientPlayer = player;

		this.setChanged();
		this.notifyObservers();
	}
}