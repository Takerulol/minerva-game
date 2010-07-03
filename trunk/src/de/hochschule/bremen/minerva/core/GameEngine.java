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
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.CountryCard;
import de.hochschule.bremen.minerva.vo.Mission;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.World;

/**
 * The game engine.
 * A facade interface, that represents a lightweight subsystem access.
 * 
 * @since 1.0
 * @version $Id$
 * 
 */
public interface GameEngine {

	// --------------------------------------
	// -- login and registration subsystem --
	// --------------------------------------
	public void login(Player player) throws PlayerAlreadyLoggedInException, GameAlreadyStartedException, WrongPasswordException, PlayerDoesNotExistException, NoPlayerSlotAvailableException, DataAccessException;

	public void register(Player player) throws PlayerExistsException, DataAccessException ;

	// ---------------------
	// -- world subsystem --
	// ---------------------
	public Vector<World> getWorldList() throws DataAccessException;

	public Vector<World> getWorldList(boolean lite) throws DataAccessException ;

	public void importWorld(File worldFile) throws WorldNotStorable, WorldFileNotFoundException, WorldFileExtensionException, WorldFileParseException, DataAccessException;

	// ----------------------------
	// -- game core logic subsystem
	// ----------------------------
	public Vector<Player> getPlayers();

	public Vector<Mission> getMissions();

	public void startGame() throws NotEnoughPlayersLoggedInException, NoPlayerLoggedInException, WorldNotDefinedException;

	public void killGame() throws DataAccessException ;

	public void setGameWorld(World world);

	public World getGameWorld();

	public void releaseCard(CountryCard card);

	public void releaseCards(Vector<CountryCard> cards);

	public void allocateArmy(Country allocatable) throws NotEnoughArmiesException, CountryOwnerException;

	public AttackResult attack(Country source, Country destination, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException, IsOwnCountryException;
	
	public void move(Country source, Country destination, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException, CountryOwnerException;

	public void finishTurn();
}