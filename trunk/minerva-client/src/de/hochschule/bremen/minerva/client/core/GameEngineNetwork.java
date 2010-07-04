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
import de.hochschule.bremen.minerva.commons.exceptions.PlayerDoesNotExistException;
import de.hochschule.bremen.minerva.commons.exceptions.PlayerExistsException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldFileExtensionException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldFileNotFoundException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldFileParseException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldNotDefinedException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldNotStorable;
import de.hochschule.bremen.minerva.commons.exceptions.WrongPasswordException;
import de.hochschule.bremen.minerva.commons.net.ServerEngine;
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
public class GameEngineNetwork implements GameEngine {

	private static GameEngineNetwork instance = null;
	
	private ServerEngine serverEngine = null;
	
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

		this.serverEngine = (ServerEngine)Simon.lookup(serverHost, serverPort, serverName);		
	}
	
	/**
	 * DOCME
	 * 
	 * @return
	 *
	 * @throws EstablishConnectionFailed
	 * @throws SimonRemoteException
	 * @throws IOException
	 * @throws LookupFailedException
	 *
	 */
	public static GameEngineNetwork getEngine() {
		if (GameEngineNetwork.instance == null) {
			try {
				GameEngineNetwork.instance = new GameEngineNetwork();
			} catch (EstablishConnectionFailed e) {
				// TODO: Handle exception
				e.printStackTrace();
			} catch (SimonRemoteException e) {
				// TODO: Handle exception
				e.printStackTrace();
			} catch (IOException e) {
				// TODO: Handle exception
				e.printStackTrace();
			} catch (LookupFailedException e) {
				// TODO: Handle exception
				e.printStackTrace();
			}
		}

		return GameEngineNetwork.instance;
	}

	@Override
	public void login(Player player) throws GameAlreadyStartedException, WrongPasswordException, PlayerDoesNotExistException, NoPlayerSlotAvailableException, DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register(Player player) throws PlayerExistsException, DataAccessException {
		// TODO Auto-generated method stub
		
	}

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
	public void importWorld(File worldFile) throws WorldNotStorable, WorldFileNotFoundException, WorldFileExtensionException, WorldFileParseException, DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vector<Player> getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<Mission> getMissions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startGame() throws NotEnoughPlayersLoggedInException, NoPlayerLoggedInException, WorldNotDefinedException {
		// TODO Auto-generated method stub
	}

	@Override
	public void killGame(boolean createNewOne) throws DataAccessException {
		// TODO Auto-generated method stub
	}

	@Override
	public void setGameWorld(World world) {
		// TODO Auto-generated method stub
	}

	@Override
	public World getGameWorld() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isGameFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Player getGameWinner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void releaseCard(CountryCard card) {
		// TODO Auto-generated method stub
	}

	@Override
	public void releaseCards(Vector<CountryCard> cards) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getAllocatableArmyCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void allocateArmy(Country allocatable) throws NotEnoughArmiesException, CountryOwnerException {
		// TODO Auto-generated method stub
	}

	@Override
	public AttackResult attack(Country source, Country destination, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException, IsOwnCountryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void move(Country source, Country destination, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException, CountryOwnerException {
		// TODO Auto-generated method stub
	}

	@Override
	public void finishTurn() {
		// TODO Auto-generated method stub
	}
}