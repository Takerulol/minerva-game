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

import de.hochschule.bremen.minerva.vo.Player;

public class AttackResult {
	private Player attacker;
	private Player defender;
	private int lostAttackerArmies;
	private int lostDefenderArmies;
	private boolean win;
	

	/**
	 * DOCME
	 * @param attacker
	 * @param defender
	 * @param lostArmies
	 * @param defeatedArmies
	 * @param win
	 */
	public AttackResult(Player attacker, Player defender, int lostArmies, int defeatedArmies, boolean win) {
		setAttacker(attacker);
		setDefender(defender);
		setLostAttackerArmies(lostArmies);
		setLostDefenderArmies(defeatedArmies);
		setWin(win);
	}
	
	/**
	 * 
	 * @param lostAttackerArmies
	 */
	private void setLostAttackerArmies(int lostAttackerArmies) {
		this.lostAttackerArmies = lostAttackerArmies;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getLostAttackerArmies() {
		return lostAttackerArmies;
	}
	
	/**
	 * 
	 * @param lostDefenderArmies
	 */
	private void setLostDefenderArmies(int lostDefenderArmies) {
		this.lostDefenderArmies = lostDefenderArmies;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getLostDefenderArmies() {
		return lostDefenderArmies;
	}
	
	/**
	 * 
	 * @param win
	 */
	private void setWin(boolean win) {
		this.win = win;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isWin() {
		return win;
	}

	/**
	 * 
	 * @param attacker
	 */
	private void setAttacker(Player attacker) {
		this.attacker = attacker;
	}

	/**
	 * 
	 * @return
	 */
	public Player getAttacker() {
		return attacker;
	}

	/**
	 * 
	 * @param defender
	 */
	private void setDefender(Player defender) {
		this.defender = defender;
	}

	/**
	 * 
	 * @return
	 */
	public Player getDefender() {
		return defender;
	}
	
	/**
	 * 
	 * @return
	 */
	public String toString() {
		return (""+attacker.getUsername()+" attacked "+defender.getUsername()+", lost "+lostAttackerArmies+ ((lostAttackerArmies > 1)? " armies" : " army")+" and defeated "+lostDefenderArmies+((lostDefenderArmies > 1)? " armies. " : " army. ")+attacker.getUsername()+ ((win) ? " won" : " did not win")+ " the country.");
	}

}
