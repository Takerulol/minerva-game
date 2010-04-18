package de.hochschule.bremen.minerva.persistence.service;

import java.util.List;
import java.util.Vector;

import de.hochschule.bremen.minerva.persistence.Crudable;
import de.hochschule.bremen.minerva.persistence.exceptions.CountryNotFoundException;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.ValueObject;

/*
 * TODO: Documentation.
 */

public class PlayerService extends PersistenceService {

	private static PlayerService instance = null;

	private Crudable storageHandler = PlayerService.storage.createHandler(Player.class);

	/**
	 * Singleton pattern. It is not possible
	 * to create a CountryService in the common way.
	 * So this constructor is private.
	 * 
	 */
	private PlayerService() {}
	
	/**
	 * Singleton pattern.
	 * Static method that controls the object creation.
	 * 
	 * @return DOCME
	 */
	public static PlayerService getInstance() {
		if (PlayerService.instance == null) {
			PlayerService.instance = new PlayerService();
		}
		return PlayerService.instance;
	}
	
	@Override
	public void delete(Object candidate) throws PersistenceIOException {
		Player deletablePlayer = (Player)candidate;
		
		storageHandler.remove(deletablePlayer);
	}

	@Override
	public ValueObject load(int id) throws PersistenceIOException {
		try {
			return (Player)storageHandler.read(id);
		} catch (Exception e) {
			throw new CountryNotFoundException(e.getMessage());
		}
	}

	@Override
	public List<Player> loadAll() throws PersistenceIOException {
		
		Vector<Player> players = (Vector)storageHandler.readAll();
		return players;
	}

	@Override
	public void save(Object candidate) throws PersistenceIOException {
		Player registrablePlayer = (Player)candidate;
		storageHandler.save(registrablePlayer);
	}

}
