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

package de.hochschule.bremen.minerva.ui.gui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import de.hochschule.bremen.minerva.core.GameEngine;
import de.hochschule.bremen.minerva.core.GameEngineLocal;
import de.hochschule.bremen.minerva.exceptions.CountriesNotInRelationException;
import de.hochschule.bremen.minerva.exceptions.CountryOwnerException;
import de.hochschule.bremen.minerva.exceptions.IsOwnCountryException;
import de.hochschule.bremen.minerva.exceptions.NotEnoughArmiesException;
import de.hochschule.bremen.minerva.manager.ApplicationConfigurationManager;
import de.hochschule.bremen.minerva.ui.gui.MinervaGUI;
import de.hochschule.bremen.minerva.ui.gui.controls.MArmyCountIcon;
import de.hochschule.bremen.minerva.ui.gui.controls.MControl;
import de.hochschule.bremen.minerva.ui.gui.controls.MMessageBox;
import de.hochschule.bremen.minerva.ui.gui.controls.MSlidePanel;
import de.hochschule.bremen.minerva.ui.gui.listener.MMouseListener;
import de.hochschule.bremen.minerva.ui.gui.panels.subpanels.GamePanelControlbar;
import de.hochschule.bremen.minerva.ui.gui.resources.TextResources;
import de.hochschule.bremen.minerva.util.ColorTool;
import de.hochschule.bremen.minerva.util.MapTool;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.CountryCard;
import de.hochschule.bremen.minerva.vo.Mission;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.PlayerState;

/**
 * Prototype of the actual game screen.
 * 
 * @version $Id$
 * @since 1.0
 *
 */
public class GamePanel extends JLayeredPane implements MControl, TextResources {
	public MapPanel mapOverlay;
	public MapPanel mapUnderlay;
	public MSlidePanel slidePanel;
	public JLabel missionLabel;

	public GameEngine engine = MinervaGUI.getEngine();
	
	public HashMap<Country, MArmyCountIcon> armyIcons = new HashMap<Country, MArmyCountIcon>();

	private Player currentPlayer;

	private Country source = null;
	private Country destination = null;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2906065533734117968L;
	
	/**
	 * Contructor initializing screen.
	 *
	 */
	public GamePanel() {
		this.setPreferredSize(MinervaGUI.WINDOW_SIZE);
		this.setOpaque(true);

		String filepath;
		
		// the mission panel
		JPanel missionPanel = new JPanel();
		missionPanel.setBounds(0, 0, 1000, 30);
		missionPanel.setLayout(new MigLayout());
		missionPanel.setOpaque(false);
		missionPanel.setBorder(BorderFactory.createMatteBorder (0, 0, 1, 0, new Color(71, 73, 75)));
		this.add(missionPanel, 10000);

		JLabel yourMissionLabel = new JLabel(GAME_PANEL_YOUR_MISSION);
		yourMissionLabel.setFont(new Font(FONT.getFamily(), Font.BOLD, 12));
		yourMissionLabel.setForeground(new Color(1, 174, 253));
		missionPanel.add(yourMissionLabel);

		this.missionLabel = new JLabel();
		this.missionLabel.setFont(new Font(FONT.getFamily(), Font.ROMAN_BASELINE, 12));
		this.missionLabel.setForeground(new Color(186, 187, 188));
		missionPanel.add(missionLabel);

		//lower map
		filepath = ApplicationConfigurationManager.get().getWorldsAssetsDirectory() + this.engine.getGameWorld().getMapUnderlay();
		this.mapUnderlay = new MapPanel(filepath);
		this.mapUnderlay.setBounds(0,0,500,500);
		
		//upper map
		filepath = ApplicationConfigurationManager.get().getWorldsAssetsDirectory() + this.engine.getGameWorld().getMap();
		this.mapOverlay = new MapPanel(filepath);
		this.mapOverlay.setBounds(0,0,500,500);
		
		//control bar
		this.slidePanel = new MSlidePanel(new GamePanelControlbar());
		slidePanel.setBounds(0, this.getPreferredSize().height - slidePanel.getRelativeHeight(), slidePanel.getPreferredSize().width,slidePanel.getPreferredSize().height);
		
		//adds mouse listener to the upper map
		this.addMapListener();
		
		HashMap<Country, Point> countryAnchors = MapTool.getCountryAnchors(this.mapOverlay.getMapImage(), mapUnderlay.getMapImage(), this.engine.getGameWorld());

		
		//TODO: remove this when finished
//		Iterator iter = countryAnchors.entrySet().iterator();
//		while (iter.hasNext()) {
//			Map.Entry pairs = (Map.Entry)iter.next();
//			System.out.println(pairs.getKey());
//		}
//		for (Country country : this.game.getWorld().getCountries()) {
//			if (!(countryAnchors.containsKey(country))) {
//				System.out.println("lol"+country);
//			}
//		}
		
		this.add(slidePanel, 10000);
	
		
		for (Country country : this.engine.getGameWorld().getCountries()) {
			MArmyCountIcon aci = new MArmyCountIcon(Color.RED, countryAnchors.get(country));
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
				Country country = GamePanel.this.engine.getGameWorld().getCountry(color);
				
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
				
				if (GamePanel.this.currentPlayer.getState() == PlayerState.ALLOCATE_ARMIES) {
					GamePanel.this.allocate(country);
				} else if (GamePanel.this.currentPlayer.getState() == PlayerState.RELEASE_CARDS) {
					/*
					 * Nothing will happen here.
					 * You can't interact with the map, when you're trying to
					 * release cards.
					 */
				} else if (GamePanel.this.currentPlayer.getState() == PlayerState.ATTACK) {
					GamePanel.this.attack(country);
				} else if (GamePanel.this.currentPlayer.getState() == PlayerState.MOVE) {
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
			this.engine.allocateArmy(country);
		} catch (NotEnoughArmiesException e) {
			MMessageBox.error(e.getMessage());
		} catch (CountryOwnerException e) {
			MMessageBox.error(e.getMessage());
		}
		if (this.engine.getAllocatableArmyCount() == 0) {
			this.currentPlayer.setState(PlayerState.ATTACK);
		}
		this.updatePanel();
	}
	
	/**
	 * 
	 * @param card
	 */
	public void TurnCardIn(CountryCard card) {
		this.engine.releaseCard(card);
	}
	
	/**
	 * 
	 * @param series
	 */
	public void TurnSeriesIn(Vector<CountryCard> series) {
		this.engine.releaseCards(series);
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
				for (Country c : this.engine.getGameWorld().getNeighbours(this.source)) {
					this.armyIcons.get(c).mark(Color.RED);
				}
			}
		} else {
			this.destination = country;
			this.armyIcons.get(country).mark(Color.YELLOW);
			try {
				int wert = Integer.parseInt(JOptionPane.showInputDialog("Wieviele Armeen " +
						"sollen angreifen? (max: "+this.calcMaxAttackCount(this.source)+")",
						""+(this.calcMaxAttackCount(this.source))));
				
				try {
					//TODO: handle attack result
					this.engine.attack(this.source, this.destination, wert);
				} catch (CountriesNotInRelationException e) {
					MMessageBox.error(e.getMessage());
				} catch (NotEnoughArmiesException e) {
					MMessageBox.error(e.getMessage());
				} catch (IsOwnCountryException e) {
					MMessageBox.error(e.getMessage());
				}
			} catch (NumberFormatException e1) {
				//no need, just to make sure that the method doesn't end
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
				for (Country c : this.engine.getGameWorld().getNeighbours(this.source)) {
					this.armyIcons.get(c).mark(Color.RED);
				}
			}
		} else {
			this.destination = country;
			this.armyIcons.get(this.source).mark(Color.GREEN);
			this.armyIcons.get(country).mark(Color.YELLOW);
			
			try {
				int wert = Integer.parseInt(JOptionPane.showInputDialog("Wieviele Armeen " +
						"sollen bewegt werden? (max: "+(this.source.getArmyCount()-1)+")",
						""+(this.source.getArmyCount()-1)));
				try {
					this.engine.move(this.source, this.destination, wert);
				} catch (CountriesNotInRelationException e) {
					MMessageBox.error(e.getMessage());
				} catch (NotEnoughArmiesException e) {
					MMessageBox.error(e.getMessage());
				} catch (CountryOwnerException e) {
					MMessageBox.error(e.getMessage());
				}
			} catch (NumberFormatException e1) {
				//no need, just to make sure that the methods doesn't end
			}
			
			this.source = null;
			this.destination = null;
		}
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

//		this.refreshArmyCounts();
//		this.slidePanel.getControlBar().updateButtons();
//		this.slidePanel.getControlBar().setCurrentPlayerLabel(this.currentTurn.getCurrentPlayer());
//		this.slidePanel.getControlBar().setAllocatableArmiesLabel(" "+this.currentTurn.getAllocatableArmyCount()+" ");
//		this.slidePanel.getControlBar().updateCardList(this.currentTurn.getCurrentPlayer().getCountryCards());
//
//		searchPlayerMission : for (Mission mission : this.getGame().getMissions()) {
//			if (mission.getOwner() == this.getGame().getCurrentTurn().getCurrentPlayer()) {
//				this.missionLabel.setText(mission.getTitle());
//				break searchPlayerMission;
//			}
//		}
//
//		this.repaint();
//		this.updateUI();
		
		if (this.engine instanceof GameEngineLocal) {
			for (Player player : this.engine.getPlayers()) {
				if (player.getState() != PlayerState.IDLE)
				this.currentPlayer = player;
			}
		}
		
		if ((this.currentPlayer.getState() == PlayerState.RELEASE_CARDS) && (GamePanel.this.currentPlayer.getCountryCards().isEmpty())) {
			this.currentPlayer.setState(PlayerState.ALLOCATE_ARMIES);
		}

		this.refreshArmyCounts();
		this.slidePanel.getControlBar().updateButtons();
		this.slidePanel.getControlBar().setCurrentPlayerLabel(this.currentPlayer);
		this.slidePanel.getControlBar().setAllocatableArmiesLabel(" "+this.engine.getAllocatableArmyCount()+" ");
		this.slidePanel.getControlBar().updateCardList(this.currentPlayer.getCountryCards());

		searchPlayerMission : for (Mission mission : this.engine.getMissions()) {
			if (mission.getOwner() == this.currentPlayer) {
				this.missionLabel.setText(mission.getTitle());
				break searchPlayerMission;
			}
		}

		this.repaint();
		this.updateUI();
		
	}
	
	/**
	 * Repaints all ArmyCountIcons with correct parameters
	 */
	public void refreshArmyCounts() {
		Iterator<?> iter = this.armyIcons.entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry)iter.next();
			((MArmyCountIcon)pairs.getValue()).setPlayer(((Country)pairs.getKey()), this.getPlayer(((Country)pairs.getKey())));		}
	}
	
	/**
	 * Removes markings of all countries.
	 */
	public void unmarkAll() {
		Iterator<?> iter = this.armyIcons.entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry)iter.next();
			((MArmyCountIcon)pairs.getValue()).unmark();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * 
	 * @param currentPlayer
	 */
	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	/**
	 * 
	 * @param byCountry
	 * @return
	 */
	public Player getPlayer(Country byCountry) {
		for (Player player : this.engine.getPlayers()) {
			if (player.hasCountry(byCountry)) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * Calculates maximum number of armies which can attack from the selected country.
	 * 
	 * @param country Country where to calculate the maximum number to attack with.
	 * @return Maximum number of armies to attack with.
	 */
	public int calcMaxAttackCount(Country country) {
		int armyCount = country.getArmyCount();

		if (armyCount > 3) {
			return 3;
		} else {
			return armyCount-1;
		}
	}
	
}
