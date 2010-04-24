package de.hochschule.bremen.minerva.persistence.service;

import java.util.List;
import java.util.Vector;

import de.hochschule.bremen.minerva.persistence.Handler;
import de.hochschule.bremen.minerva.persistence.FilterParameter;
import de.hochschule.bremen.minerva.persistence.exceptions.PlayerNotFoundException;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.ValueObject;

//TODO: DOCME the class and two methods.

/**
 * DOCME
 */
public class PlayerService extends PersistenceService {

	private static PlayerService instance = null;

	private Handler handler = PlayerService.storage.createHandler(Player.class);

	/**
	 * Singleton pattern. It is not possible
	 * to create a PlayerService in the common way.
	 * So this constructor is private.
	 */
	private PlayerService() {}
	
	/**
	 * Singleton pattern.
	 * Static method that controls the object creation.
	 * 
	 * @return PlayerService.instance
	 */
	public static PlayerService getInstance() {
		if (PlayerService.instance == null) {
			PlayerService.instance = new PlayerService();
		}
		return PlayerService.instance;
	}
	
	/**
	 * Method that delete the player object.
	 * 
	 * @throws PersistenceIOException
	 */
	@Override
	public void delete(Object candidate) throws PersistenceIOException {
		Player deletablePlayer = (Player)candidate;
		
		handler.remove(deletablePlayer);
	}

	/**
	 * DOCME
	 * 
	 * @throws PlayerNotFoundException, PersistenceIOException
	 */
	@Override
	public ValueObject load(int id) throws PersistenceIOException {
		try {
			return (Player)handler.read(new FilterParameter(id));
		} catch (Exception e) {
			throw new PlayerNotFoundException(e.getMessage());
		}
	}

	/**
	 * DOCME
	 * 
	 * @throws PersistenceIOException
	 * @return players
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Player> loadAll() throws PersistenceIOException {
		
		Vector<Player> players = (Vector)handler.readAll();
		return players;
	}

	/**
	 * Method that save the object.
	 * 
	 * @throws PersistenceIOException
	 */
	@Override
	public void save(Object candidate) throws PersistenceIOException {
		Player registrablePlayer = (Player)candidate;
		handler.save(registrablePlayer);
	}

}
