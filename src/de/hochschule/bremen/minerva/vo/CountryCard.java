/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: Player.java 224 2010-05-18 18:18:40Z andre.koenig $
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
package de.hochschule.bremen.minerva.vo;

import java.util.Vector;

public class CountryCard {

	private boolean countryIsLiberated = false;
	
	private Country reference = new Country();
	private Vector<CountryCard> countryCards = null;
	private String tradingPlayer;
	
	
	/**
	 * Sets the referenced country.
	 * 
	 * @param country
	 */
	public void setReference(Country country) {
		this.reference = country;
	}
	
	/**
	 * Returns the referenced country.
	 * 
	 * @return
	 */
	public Country getReference() {
		return this.reference;
	}
	
	/**
	 * DOCME
	 * 
	 * @param tradingPlayer
	 */
	public void setTradingPlayer(String tradingPlayer) {
		this.tradingPlayer = tradingPlayer;
	}

	/**
	 * DOCME
	 * 
	 * @return
	 */
	public String getTradingPlayer() {
		return tradingPlayer;
	}
	
	/**
	 * Request if the Country is liberated.
	 */
	public void liberateCountry(){
		if(countryIsLiberated){
			//The currentPlayer get CountryCard.
		}else{
			//The currentPlayer get nothing.
		}
	}
	/**
	 * This method is appropriate to change automatically the Cards of the player 
	 * if he has three or more cards.
	 */
	
	public void cardChange(){
	//	if(countryCards >= 3){
			//Change Cards into armies (and bring them into the play).
	//	}else{
			//Change nothing.
	}

	
	
	/**
	 * This method has to find out if the currentPlayer is a tradingPlayer 
	 * and where did he/she come in the ranking (algorithm-method).
	 * 
	 * @param currentPlayer
	 */
	public void tradingPlayer(Player currentPlayer){
		/*
		if(currentPlayer make cardChange = true){
			currentPlayer = tradingPlayer;			
		}
		*/
	}
	
	/**
	 * This method describes the algorithm:
	 * which player get how many armies for his/her CountryCards.
	 * 
	 * @param tradingPlayer
	 */
	public void tradingAlgorithm(Player tradingPlayer){
	/*	
		if(tradingPlayer = 1){
			// currentPlayer get 4 armies.
		}else if(tradingPlayer = 2){
			// currentPlayer get 6 armies.
		}else if(tradingPlayer = 3){
			// currentPlayer get 8 armies.
		}else if(tradingPlayer = 4){
			// currentPlayer get 10 armies.
		}else if(tradingPlayer = 5){
			// currentPlayer get 12 armies.
		}else if(tradingPlayer = 6){
			// currentPlayer get 15 armies.
		}else if(tradingPlayer = 7){
			// currentPlayer get 20 armies.
		}else if(tradingPlayer = 8){
			// currentPlayer get 25 armies.
		}else if(tradingPlayer > 8){
			// currentPlayer get (lastArmyCountStand+5armis).
		}
	*/
	}

		
}
