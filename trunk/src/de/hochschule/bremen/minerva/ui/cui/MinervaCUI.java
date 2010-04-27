/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: UserInterface.java 117 2010-04-22 18:19:46Z cbollmann@stud.hs-bremen.de $
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
package de.hochschule.bremen.minerva.ui.cui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.hochschule.bremen.minerva.core.Game;
import de.hochschule.bremen.minerva.core.Turn;
import de.hochschule.bremen.minerva.ui.cui.exceptions.CuiException;
import de.hochschule.bremen.minerva.ui.cui.exceptions.InvalidInputFormatException;

public class MinervaCUI {

	private Game game = null;
	private BufferedReader console = null;
	
	/**
	 * DOCME
	 * 
	 */
	public MinervaCUI() {
		this.console = new BufferedReader(new InputStreamReader(System.in));
	}

	/**
	 * DOCME
	 * 
	 */
	public void run() {
		this.gameInit();
		
		do {
			Turn turn = game.nextTurn();
			
		} while (!game.isFinished());
	}

	private void gameInit() {
		this.out("Hallo Welt");
		this.out("Bitte geben Sie die Anzahl der Spieler an: ");
		
	}

	/**
	 * DOCME
	 * 
	 * @return
	 * @throws CuiException 
	 * @throws InvalidInputFormatException 
	 */
	private int readInt() throws CuiException, InvalidInputFormatException {
		int returnValue = 0;
		
		try {
			String line = this.read();
			returnValue = Integer.parseInt(line);
		} catch (NumberFormatException e) {
			throw new InvalidInputFormatException();
		} catch (CuiException e) {
			throw new CuiException("Error while reading from console.");
		}
		
		return returnValue;
	}

	/**
	 * DOCME
	 * 
	 * @return
	 * @throws CuiException
	 */
	private String read() throws CuiException {
		try {
			return this.console.readLine();
		} catch (IOException e) {
			throw new CuiException();
		}
	}
	
	/**
	 * DOCME
	 * 
	 * @param message
	 */
	private void out(String message) {
		System.out.print(message);
	}

	
}
