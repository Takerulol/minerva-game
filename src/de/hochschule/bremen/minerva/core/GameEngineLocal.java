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
package de.hochschule.bremen.minerva.core;

import java.io.File;
import java.util.Vector;

import de.hochschule.bremen.minerva.core.logic.AttackResult;
import de.hochschule.bremen.minerva.core.logic.Game;
import de.hochschule.bremen.minerva.core.logic.Turn;
import de.hochschule.bremen.minerva.exceptions.CountriesNotInRelationException;
import de.hochschule.bremen.minerva.exceptions.CountryOwnerException;
import de.hochschule.bremen.minerva.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.exceptions.GameAlreadyStartedException;
import de.hochschule.bremen.minerva.exceptions.IsOwnCountryException;
import de.hochschule.bremen.minerva.exceptions.NoPlayerLoggedInException;
import de.hochschule.bremen.minerva.exceptions.NoPlayerSlotAvailableException;
import de.hochschule.bremen.minerva.exceptions.NotEnoughArmiesException;
import de.hochschule.bremen.minerva.exceptions.NotEnoughPlayersLoggedInException;
import de.hochschule.bremen.minerva.exceptions.PlayerAlreadyLoggedInException;
import de.hochschule.bremen.minerva.exceptions.PlayerDoesNotExistException;
import de.hochschule.bremen.minerva.exceptions.PlayerExistsException;
import de.hochschule.bremen.minerva.exceptions.WorldFileExtensionException;
import de.hochschule.bremen.minerva.exceptions.WorldFileNotFoundException;
import de.hochschule.bremen.minerva.exceptions.WorldFileParseException;
import de.hochschule.bremen.minerva.exceptions.WorldNotDefinedException;
import de.hochschule.bremen.minerva.exceptions.WorldNotStorable;
import de.hochschule.bremen.minerva.exceptions.WrongPasswordException;
import de.hochschule.bremen.minerva.manager.AccountManager;
import de.hochschule.bremen.minerva.manager.WorldManager;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.CountryCard;
import de.hochschule.bremen.minerva.vo.Mission;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.World;

/**
 * The local game engine.
 * 
 * A facade interface, that represents a lightweight subsystem access
 * for playing minerva as a "hot seat" game.
 * 
 * @since 1.0
 * @version $Id$
 * 
 */
public class GameEngineLocal implements GameEngine {

	private Game game = new Game();

	private static GameEngineLocal instance = null;

	/**
	 * Singleton pattern. It is not possible
	 * to create a CountryService in the common way.
	 * So this constructor is private.
	 * 
	 */
	private GameEngineLocal() {}

	/**
	 * Singleton pattern.
	 * Static method that controls the object creation.
	 * 
	 * @return A (local) game engine instance.
	 *
	 */
	public static GameEngineLocal getEngine() {
		if (GameEngineLocal.instance == null) {
			GameEngineLocal.instance = new GameEngineLocal();
		}
		
		return GameEngineLocal.instance;
	}

	/**
	 * Login a player into the current game
	 *
	 * @throws GameAlreadyStartedException It is not possible to login a player if the game is already running. 
	 * @throws NoPlayerSlotAvailableException 
	 * 
	 */
	@Override
	public void login(Player player) throws PlayerAlreadyLoggedInException, GameAlreadyStartedException, WrongPasswordException, PlayerDoesNotExistException, NoPlayerSlotAvailableException, DataAccessException {
		AccountManager.getInstance().login(player);

		this.game.addPlayer(player);
	}

	/**
	 * Registers a new player.
	 *
	 * @throws PlayerExistsException
	 * @throws DataAccessException
	 *
	 */
	@Override
	public void register(Player player) throws PlayerExistsException, DataAccessException {
		AccountManager.getInstance().createPlayer(player);
	}

	/**
	 * Returns a list with all available worlds in a flat view.
	 * Flat view means, that each world doesn't hold the complete
	 * country dependencies (cool, for requesting an world overview).
	 * 
	 * @return A vector with the available worlds.
	 *
	 * @throws DataAccessException 
	 *
	 */
	@Override
	public Vector<World> getWorldList() throws DataAccessException {
		return WorldManager.getInstance().getList();
	}

	/**
	 * Returns a "fat" list with all available worlds and
	 * the all country dependencies.
	 *
	 * @throws DataAccessException 
	 * 
	 */
	@Override
	public Vector<World> getWorldList(boolean lite) throws DataAccessException {
		return WorldManager.getInstance().getList(lite);
	}

	/**
	 * Imports a world file. World files will
	 * be generated by the "minerva - level editor".
	 * 
	 * @throws WorldNotStorable 
	 * @throws WorldFileParseException 
	 * @throws WorldFileExtensionException 
	 * @throws WorldFileNotFoundException 
	 * @throws DataAccessException 
	 * 
	 */
	@Override
	public void importWorld(File worldFile) throws WorldFileNotFoundException, WorldFileExtensionException, WorldFileParseException, WorldNotStorable, DataAccessException {
		WorldManager.getInstance().store(worldFile);
	}

	/**
	 * Returns the players, which are logged in
	 * and assigned to the currently running game session.
	 * 
	 * @return A vector with all logged in players, which are assigned to the current game.
	 *
	 */
	@Override
	public Vector<Player> getPlayers() {
		return this.game.getPlayers();
	}

	/**
	 * Returns all player missions.
	 * 
	 * @return A vector with all player missions.
	 * 
	 */
	@Override
	public Vector<Mission> getMissions() {
		return this.game.getMissions();
	}

	/**
	 * Sets the world on which the game should be played.
	 * Will only be defined if the game is not running.
	 * 
	 * @param The world to play on.
	 * 
	 */
	@Override
	public void setGameWorld(World world) {
		// Check if the game is running. If it running
		// it should not be possible to add the world.
		// Well, it is not necessary to inform the user,
		// so we will not throw an GameAlreadyStartedException
		// or something.
		if (!this.game.isRunning()) {
			this.game.setWorld(world);
		}
	}

	/**
	 * Starts the game session. Please verify, that
	 * you have already defined all neccessary data
	 * for starting a game.
	 * 
	 * @see GameEngineLocal#setGameWorld(World)
	 *
	 * @throws WorldNotDefinedException 
	 * @throws NoPlayerLoggedInException 
	 * @throws NotEnoughPlayersLoggedInException 
	 * 
	 */
	@Override
	public void startGame() throws NotEnoughPlayersLoggedInException, NoPlayerLoggedInException, WorldNotDefinedException {
		this.game.start();
	}

	/**
	 * Stops a game and set all logout for all players.
	 * After executing this method it is possible to login new
	 * players and start a new game session.
	 *
	 * @throws DataAccessException If player logout fails.
	 * 
	 */
	@Override
	public void killGame() throws DataAccessException {
		AccountManager.getInstance().logout();
		this.game = new Game();
	}

	/**
	 * Returns the world value object from
	 * the current game.
	 * 
	 * @return The world value object.
	 *
	 */
	public World getGameWorld() {
		return this.game.getWorld();
	}
	
	/**
	 * Release a country card.
	 *
	 * @param The releasable country card.
	 *
	 */
	@Override
	public void releaseCard(CountryCard card) {
		Turn turn = this.game.getCurrentTurn();
		turn.releaseCard(card);
	}

	/**
	 * Releases a country card series.
	 * 
	 * @param cards A vector with releasable country cards.
	 *
	 */
	@Override
	public void releaseCards(Vector<CountryCard> cards) {
		Turn turn = this.game.getCurrentTurn();
		turn.releaseCardSeries(cards);
	}

	/**
	 * Adds one army to the specified country.
	 *
	 * @param The country on which the army should be placed.
	 *
	 * @throws CountryOwnerException 
	 * @throws NotEnoughArmiesException 
	 * 
	 */
	@Override
	public void allocateArmy(Country allocatable) throws NotEnoughArmiesException, CountryOwnerException {
		Turn turn = this.game.getCurrentTurn();
		turn.allocateArmy(allocatable);
	}

	/**
	 * Attacks a country with an specified army count.
	 * 
	 * @param source The country from which the attack will be started.
	 * @param destination The country which should be attacked.
	 * @param armyCount The attacking army units.
	 * 
	 * @throws CountriesNotInRelationException
	 * @throws NotEnoughArmiesException
	 * @throws IsOwnCountryException
	 * 
	 */
	@Override
	public AttackResult attack(Country source, Country destination, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException, IsOwnCountryException {
		Turn turn = this.game.getCurrentTurn();
		
		return turn.attack(source, destination, armyCount);
	}

	/**
	 * Moves armies from one country to another country.
	 * 
	 * @param from The country FROM which we will move the armies.
	 * @param to The country TO which we will move the armies.
	 * @param armyCount How many army units should be moved?
	 *
	 * @throws CountryOwnerException 
	 * @throws NotEnoughArmiesException 
	 * @throws CountriesNotInRelationException 
	 * 
	 */
	@Override
	public void move(Country from, Country to, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException, CountryOwnerException {
		Turn turn = this.game.getCurrentTurn();
		turn.moveArmies(from, to, armyCount);
	}

	/**
	 * Starts the next turn.
	 * 
	 */
	@Override
	public void finishTurn() {
		this.game.nextTurn();
	}
}