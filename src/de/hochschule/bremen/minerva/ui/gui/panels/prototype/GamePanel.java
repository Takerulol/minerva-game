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

package de.hochschule.bremen.minerva.ui.gui.panels.prototype;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

import de.hochschule.bremen.minerva.core.logic.Game;
import de.hochschule.bremen.minerva.core.logic.Turn;
import de.hochschule.bremen.minerva.exceptions.CountriesNotInRelationException;
import de.hochschule.bremen.minerva.exceptions.CountryOwnerException;
import de.hochschule.bremen.minerva.exceptions.IsOwnCountryException;
import de.hochschule.bremen.minerva.exceptions.NotEnoughArmiesException;
import de.hochschule.bremen.minerva.manager.ApplicationConfigurationManager;
import de.hochschule.bremen.minerva.manager.SessionManager;
import de.hochschule.bremen.minerva.ui.gui.MinervaGUI;
import de.hochschule.bremen.minerva.ui.gui.listener.MMouseListener;
import de.hochschule.bremen.minerva.util.ColorTool;
import de.hochschule.bremen.minerva.util.MapTool;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.CountryCard;
import de.hochschule.bremen.minerva.vo.World;

/**
 * Prototype of the actual game screen.
 * 
 * @version $Id$
 * @since 1.0
 *
 */
public class GamePanel extends JLayeredPane {
	public MapPanel mapOverlay;
	public MapPanel mapUnderlay;
	public MSlidePanel slidePanel;
	public HashMap<Country, ArmyCountIcon> armyIcons = new HashMap<Country, ArmyCountIcon>();
	public World world;
	private Game game;
	private Turn currentTurn;
	
	public static final int ALLOCATE = 0;
	public static final int CARD_TURN_IN = 1;
	public static final int ATTACK = 2;
	public static final int MOVE = 3;
	public static final int OTHER_PLAYER = 4;
	
	private int gameState = 0;
	private Country source = null;
	private Country destination = null;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2906065533734117968L;
	
	/**
	 * Contructor initializing screen.
	 */
	public GamePanel() {
		this.setPreferredSize(MinervaGUI.WINDOW_SIZE);
		this.setOpaque(true);
		//this.setBackground(Color.LIGHT_GRAY);
		
		this.game = SessionManager.get(MinervaGUI.getSessionId());
		
		String filepath;
		
		//lower map
		filepath = ApplicationConfigurationManager.get().getWorldsAssetsDirectory() + this.game.getWorld().getMapUnderlay();
		this.mapUnderlay = new MapPanel(filepath);
		this.mapUnderlay.setBounds(0,0,500,500);
		
		
		//upper map
		filepath = ApplicationConfigurationManager.get().getWorldsAssetsDirectory() + this.game.getWorld().getMap();
		this.mapOverlay = new MapPanel(filepath);
		this.mapOverlay.setBounds(0,0,500,500);
		
		//control bar
		this.slidePanel = new MSlidePanel(new ControlBarPanel());
		slidePanel.setBounds(0, this.getPreferredSize().height - slidePanel.getRelativeHeight(), slidePanel.getPreferredSize().width,slidePanel.getPreferredSize().height);
		
		//adds mouse listener to the upper map
		this.addMapListener();
		
		HashMap<Country, Point> countryAnchors = MapTool.getCountryAnchors(this.mapOverlay.getMapImage(), mapUnderlay.getMapImage(), this.game.getWorld());

		//initializing the first turn
		this.currentTurn = this.game.nextTurn();

		slidePanel.getControlBar().setCurrentPlayerLabel(this.currentTurn.getCurrentPlayer());
		this.add(slidePanel, 10000);
		
		for (Country country : this.game.getWorld().getCountries()) {
			ArmyCountIcon aci = new ArmyCountIcon(Color.RED, countryAnchors.get(country));
			this.armyIcons.put(country,aci);
			this.add(aci,-10000);
		}
		
		

		this.refreshArmyCounts();
		
		
		
		//Adding everything up
		this.add(this.mapOverlay,-20000);
		this.add(this.mapUnderlay,-30000);
		
		slidePanel.getControlBar().addListeners(this);
		
		this.validate();
		this.updateUI();
		this.updatePanel();
	}

	/**
	 * Adds the action listener on the upper map to interact with mouse clicks
	 */
	private void addMapListener() {
		this.mapOverlay.addMouseListener(new MMouseListener() {
			public void mouseClicked(MouseEvent e) {
				GamePanel.this.unmarkAll();
				
				Color color = ColorTool.fromInteger(GamePanel.this.mapUnderlay.getMapImage().getRGB(e.getX(), e.getY()));
				Country country = GamePanel.this.game.getWorld().getCountry(color);
				
//				String hexcode = ColorTool.toHexCode(color);
//				GamePanel.this.armyIcons.get(country).mark(Color.RED);
//				
//				System.out.println("Neighbors:");
//				for (Country c : world.getNeighbours(country)) {
//					GamePanel.this.armyIcons.get(c).mark(Color.BLUE);
//					System.out.println(c.getName());
//				}
//				
//				System.out.println("Farbe: "+hexcode+" "+country);
				
				int gameState = GamePanel.this.getGameState();
				if (gameState == 0) {
					GamePanel.this.allocate(country);
				} else if (GamePanel.this.getGameState() == GamePanel.CARD_TURN_IN) {
					//TODO: implementation, also wrong place ... button in control bar will be used instead of map
				} else if (GamePanel.this.getGameState() == GamePanel.ATTACK) {
					GamePanel.this.attack(country);
				} else if (GamePanel.this.getGameState() == GamePanel.MOVE) {
					GamePanel.this.move(country);
				}
				GamePanel.this.updatePanel();
			}
		});
	}
	
	/**
	 * Allocates one army on a destined country
	 * @param country country toput an army on
	 */
	private void allocate(Country country) {
		try {
			this.currentTurn.allocateArmy(country);
		} catch (NotEnoughArmiesException e) {
			this.errorDialog(e.getMessage());
		} catch (CountryOwnerException e) {
			this.errorDialog(e.getMessage());
		}
		if (this.currentTurn.getAllocatableArmyCount() == 0) {
			this.setGameState(GamePanel.CARD_TURN_IN);
		}
		this.updatePanel();
	}
	
	/**
	 * 
	 * @param card
	 */
	private void TurnCardIn(CountryCard card) {
		//TODO: implementation
	}
	
	/**
	 * 
	 * @param series
	 */
	private void TurnSeriesIn(Vector<CountryCard> series) {
		//TODO: implementation
	}
	
	/**
	 * Attack one country from another.
	 * Use it once to set the sources country and twice to set destination country.
	 * After setting the destination you'll get an option pane to the army count.
	 * @param country first source then destination
	 */
	private void attack(Country country) {
		if (this.source == null) {
			if (country.getArmyCount() > 1) {
				this.source = country;
				this.armyIcons.get(this.source).mark(Color.GREEN);
				for (Country c : this.game.getWorld().getNeighbours(this.source)) {
					this.armyIcons.get(c).mark(Color.RED);
				}
			}
		} else {
			this.destination = country;
			this.armyIcons.get(country).mark(Color.YELLOW);
			
			int wert = Integer.parseInt(JOptionPane.showInputDialog("Wieviele Armeen " +
					"sollen angreifen? (max: "+this.currentTurn.calcMaxAttackCount(this.source)+")",
					""+(this.source.getArmyCount()-1)));
			try {
				this.currentTurn.attack(this.source, this.destination, wert);
			} catch (CountriesNotInRelationException e) {
				this.errorDialog(e.getMessage());
			} catch (NotEnoughArmiesException e) {
				this.errorDialog(e.getMessage());
			} catch (IsOwnCountryException e) {
				this.errorDialog(e.getMessage());
			}
			this.source = null;
			this.destination = null;
		}	
	}
	
	/**
	 * Moves units from one to another country.
	 * Use it once to set the sources country and twice to set destination country.
	 * After setting the destination you'll get an option pane to the army count.
	 * @param country first source then destination
	 */
	private void move(Country country) {
		if (this.source == null) {
			if (country.getArmyCount() > 1) {
				this.source = country;
				this.armyIcons.get(this.source).mark(Color.GREEN);
				for (Country c : this.game.getWorld().getNeighbours(this.source)) {
					this.armyIcons.get(c).mark(Color.RED);
				}
			}
		} else {
			this.destination = country;
			this.armyIcons.get(this.source).mark(Color.GREEN);
			this.armyIcons.get(country).mark(Color.YELLOW);
			
			int wert = Integer.parseInt(JOptionPane.showInputDialog("Wieviele Armeen " +
					"sollen bewegt werden? (max: "+(this.source.getArmyCount()-1)+")",
					""+(this.source.getArmyCount()-1)));
			try {
				this.currentTurn.moveArmies(this.source, this.destination, wert);
			} catch (CountriesNotInRelationException e) {
				this.errorDialog(e.getMessage());
			} catch (NotEnoughArmiesException e) {
				this.errorDialog(e.getMessage());
			} catch (CountryOwnerException e) {
				this.errorDialog(e.getMessage());
			}
			this.source = null;
			this.destination = null;
		}
	}

	/**
	 * Opens JDialog with given error text
	 * @param errorText String of error text
	 */
	private void errorDialog(String errorText) {
		JOptionPane.showMessageDialog(this,
			    errorText,
			    "Fehler",
			    JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Updates whole panel, when something changed during the game
	 */
	public void updatePanel() {
		//TODO: extension needed?
		
		//use this for server/client implementation
//		if (this.currentTurn.getCurrentPlayer() != MinervaGUI.getInstance().getPlayer()) {
//			this.setGameState(GamePanel.OTHER_PLAYER);
//		}
		
		this.refreshArmyCounts();
		this.slidePanel.getControlBar().updateButtons();
		this.slidePanel.getControlBar().setCurrentPlayerLabel(this.currentTurn.getCurrentPlayer());
		this.slidePanel.getControlBar().setAllocatableArmiesLabel(" "+this.currentTurn.getAllocatableArmyCount()+" ");
		this.slidePanel.getControlBar().updateMissionText();
		this.slidePanel.getControlBar().updateCardList(this.currentTurn.getCurrentPlayer().getCountryCards());
		
		this.repaint();
		this.updateUI();
	}
	
	/**
	 * Repaints all ArmyCountIcons with correct parameters
	 */
	@SuppressWarnings("unchecked")
	public void refreshArmyCounts() {
		Iterator iter = this.armyIcons.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry pairs = (Map.Entry)iter.next();
			((ArmyCountIcon)pairs.getValue()).setPlayer(((Country)pairs.getKey()), this.game.getPlayer(((Country)pairs.getKey())));
		}
	}
	
	/**
	 * Removes markings of all countries.
	 */
	@SuppressWarnings("unchecked")
	public void unmarkAll() {
		Iterator iter = this.armyIcons.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry pairs = (Map.Entry)iter.next();
			((ArmyCountIcon)pairs.getValue()).unmark();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Game getGame() {
		return this.game;
	}
	
	/**
	 * 
	 */
	public void nextTurn() {
		this.currentTurn = game.nextTurn();
		this.updatePanel();
	}
	
	/**
	 * 
	 * @return
	 */
	public Turn getTurn() {
		return this.currentTurn;
	}

	/**
	 * 
	 * @param gameState
	 */
	public void setGameState(int gameState) {
		this.gameState = gameState;
	}

	/**
	 * 
	 * @return
	 */
	public int getGameState() {
		return gameState;
	}
	
}
