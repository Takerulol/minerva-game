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
package de.hochschule.bremen.minerva.commons.net;

import java.util.Vector;

import de.root1.simon.SimonRemote;
import de.root1.simon.exceptions.SimonRemoteException;
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
import de.hochschule.bremen.minerva.commons.vo.Player;
import de.hochschule.bremen.minerva.commons.vo.World;

/**
 * The server protocol.
 * Contains methods, which are callable via minerva clients.
 * 
 * @since 1.0
 * @version $Id$
 * 
 */
public interface ServerEngine extends SimonRemote {

	// Login a player and define the callback method, which will be called if the player is the next player.
	public Player login(Player player) throws SimonRemoteException, PlayerAlreadyLoggedInException, GameAlreadyStartedException, WrongPasswordException, PlayerDoesNotExistException, NoPlayerSlotAvailableException, DataAccessException;

	// Registers a new player.
	public void register(Player player) throws SimonRemoteException, PlayerExistsException, DataAccessException;

	// Returns all available world (flatView = no country dependencies)
	public Vector<World> getWorlds(boolean flatView) throws SimonRemoteException, DataAccessException;

	// Starts a game. Note. This method is only executable by the gamemaster.
	public void startGame() throws SimonRemoteException, NotEnoughPlayersLoggedInException, NoPlayerLoggedInException, WorldNotDefinedException;
	
	// Kills the current game and logs all players out.
	public void killGame(boolean createNewOne) throws SimonRemoteException, DataAccessException;

	// Sets the world to play on.
	public void setGameWorld(World world) throws SimonRemoteException;

	// Returns the world the game is running on.
	public World getGameWorld() throws SimonRemoteException;
}
