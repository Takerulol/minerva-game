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

/**
 * Result of one attack saving the attacker, defender, armies lost and if the attack was
 * a success.
 * 
 * @since 1.0
 * @version $Id$
 *
 */
public class AttackResult {
	private Player attacker;
	private Player defender;
	private int lostAttackerArmies;
	private int lostDefenderArmies;
	private boolean win;
	

	/**
	 * Constructor of the AttackResult.
	 * 
	 * @param attacker Attacking player.
	 * @param defender Defending player.
	 * @param lostArmies Armies lost by the attacker.
	 * @param defeatedArmies Armies lost by the defender.
	 * @param win Was the attack a success?
	 */
	public AttackResult(Player attacker, Player defender, int lostArmies, int defeatedArmies, boolean win) {
		setAttacker(attacker);
		setDefender(defender);
		setLostAttackerArmies(lostArmies);
		setLostDefenderArmies(defeatedArmies);
		setWin(win);
	}
	
	/**
	 * Sets lost armies of the attacker.
	 * 
	 * @param lostAttackerArmies Number of lost armies.
	 */
	private void setLostAttackerArmies(int lostAttackerArmies) {
		this.lostAttackerArmies = lostAttackerArmies;
	}
	
	/**
	 * Gets lost armies of the attacker.
	 * 
	 * @return Number of lost armies.
	 */
	public int getLostAttackerArmies() {
		return lostAttackerArmies;
	}
	
	/**
	 * Sets lost armies of the defender.
	 * 
	 * @param lostDefenderArmies Number of lost armies.
	 */
	private void setLostDefenderArmies(int lostDefenderArmies) {
		this.lostDefenderArmies = lostDefenderArmies;
	}
	
	/**
	 * Gets lost armies of the defender.
	 * 
	 * @return Number of lost armies.
	 */
	public int getLostDefenderArmies() {
		return lostDefenderArmies;
	}
	
	/**
	 * Sets success of the attack.
	 * 
	 * @param win Success yes/no?
	 */
	private void setWin(boolean win) {
		this.win = win;
	}
	
	/**
	 * Returns success of the attack.
	 * 
	 * @return Success yes/no?
	 */
	public boolean isWin() {
		return win;
	}

	/**
	 * Sets attacking player.
	 *  
	 * @param attacker Attacking player.
	 */
	private void setAttacker(Player attacker) {
		this.attacker = attacker;
	}

	/**
	 * Gets attacking player.
	 * 
	 * @return Attacking player.
	 */
	public Player getAttacker() {
		return attacker;
	}

	/**
	 * Sets defending player.
	 * 
	 * @param defender Defending player.
	 */
	private void setDefender(Player defender) {
		this.defender = defender;
	}

	/**
	 * Gets defending player.
	 * 
	 * @return Defending player.
	 */
	public Player getDefender() {
		return defender;
	}
	
	/**
	 * Gets the whole AttackResult as a string.
	 * 
	 * @return String of AttackResult.
	 */
	public String toString() {
		return (""+attacker.getUsername()+" attacked "+defender.getUsername()+", lost "+lostAttackerArmies+ ((lostAttackerArmies > 1)? " armies" : " army")+" and defeated "+lostDefenderArmies+((lostDefenderArmies > 1)? " armies. " : " army. ")+attacker.getUsername()+ ((win) ? " won" : " did not win")+ " the country.");
	}

}
