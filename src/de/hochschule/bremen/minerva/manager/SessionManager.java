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
package de.hochschule.bremen.minerva.manager;

import java.util.HashMap;

import de.hochschule.bremen.minerva.core.logic.Game;
import de.hochschule.bremen.minerva.util.HashTool;

/**
 * Session manager that provides the functionality to store game sessions.
 * 
 * @version $Id$
 * @since 1.0
 *
 */
public class SessionManager {

	private static HashMap<String, Game> SESSIONS = new HashMap<String, Game>();

	/**
	 * DOCME
	 * 
	 * @param game
	 * @return
	 * 
	 */
	public static String set(Game game) {
		String sessionId = SessionManager.generateSessionId();
		
		SessionManager.SESSIONS.put(sessionId, game);
		return sessionId;
	}

	/**
	 * DOCME
	 * 
	 * @param sessionId
	 * @return
	 * 
	 */
	public static Game get(String sessionId) {
		return SESSIONS.get(sessionId);
	}

	/**
	 * Generates unique session ids.
	 * 
	 * @return The session id.
	 * 
	 */
	private static String generateSessionId() {
		String unixTimestamp = String.valueOf((System.currentTimeMillis()/1000));
		return HashTool.md5(unixTimestamp);
	}
}