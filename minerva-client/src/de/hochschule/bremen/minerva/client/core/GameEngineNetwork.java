package de.hochschule.bremen.minerva.client.core;

import java.io.File;
import java.util.Vector;

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
import de.hochschule.bremen.minerva.commons.vo.AttackResult;
import de.hochschule.bremen.minerva.commons.vo.Country;
import de.hochschule.bremen.minerva.commons.vo.CountryCard;
import de.hochschule.bremen.minerva.commons.vo.Mission;
import de.hochschule.bremen.minerva.commons.vo.Player;
import de.hochschule.bremen.minerva.commons.vo.World;

public class GameEngineNetwork implements GameEngine {

	public static GameEngineNetwork getEngine(){return null;}

	@Override
	public void login(Player player) throws GameAlreadyStartedException,
			WrongPasswordException, PlayerDoesNotExistException,
			NoPlayerSlotAvailableException, DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register(Player player) throws PlayerExistsException,
			DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vector<World> getWorldList() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<World> getWorldList(boolean lite) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void importWorld(File worldFile) throws WorldNotStorable,
			WorldFileNotFoundException, WorldFileExtensionException,
			WorldFileParseException, DataAccessException {
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
	public void startGame() throws NotEnoughPlayersLoggedInException,
			NoPlayerLoggedInException, WorldNotDefinedException {
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
	public void allocateArmy(Country allocatable)
			throws NotEnoughArmiesException, CountryOwnerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AttackResult attack(Country source, Country destination,
			int armyCount) throws CountriesNotInRelationException,
			NotEnoughArmiesException, IsOwnCountryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void move(Country source, Country destination, int armyCount)
			throws CountriesNotInRelationException, NotEnoughArmiesException,
			CountryOwnerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finishTurn() {
		// TODO Auto-generated method stub
		
	}
	
}
