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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import de.hochschule.bremen.minerva.ui.gui.controls.MMessageBox;
import de.hochschule.bremen.minerva.ui.gui.controls.MPlayerIcon;
import de.hochschule.bremen.minerva.ui.gui.controls.MSlidePanel;
import de.hochschule.bremen.minerva.vo.CavalerieCard;
import de.hochschule.bremen.minerva.vo.CountryCard;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.SoldierCard;

/**
 * Panel sliced in halves.
 * The upper half contains current player, allocatable army count, game state buttons
 * (allocate, turn cards in, attack, move, end turn) and a slide button.
 * 
 * The lower half contains game informations country cards ...
 *
 * @version $Id$
 * @since 1.0
 *
 */
public class ControlBarPanel extends JPanel implements ActionListener {

	private int relativeHeight;
	private MSlidePanel slidePanel;
	private GamePanel gamePanel;
	
	//upper half
	private JButton allocateButton;
	private JLabel allocatableArmies;
	private JButton cardButton;
	private JButton attackButton;
	private JButton moveButton;
	private JButton endTurnButton;
	
	//lower half
	private JList cardList;
	private DefaultListModel model;
	private JButton turnIn;
	
	//slide
	private JButton slideButton;
	private JPanel upperHalf;
	private JPanel lowerHalf;

	private JPanel currentPlayerArea;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2420450895831821731L;
	
	/**
	 * Constructor
	 */
	public ControlBarPanel() {
		this.init();
	}
	
	/**
	 * Initializes ControlBarPanel.
	 */
	private void init() {
		this.setLayout(new BorderLayout());
		this.setOpaque(false);

		this.initUpperHalf();
		this.initLowerHalf();
		
		this.add(this.upperHalf, BorderLayout.NORTH);
		this.add(this.lowerHalf, BorderLayout.SOUTH);
		
		this.setPreferredSize(new Dimension(1000, 220));
		
		this.updateUI();
	}

	/**
	 * Initialization of the UPPER half of the control bar
	 */
	private void initUpperHalf() {
		//upper half
		this.upperHalf = new JPanel();
		this.upperHalf.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		this.upperHalf.setOpaque(false);

		this.currentPlayerArea = new JPanel();
		this.currentPlayerArea.setOpaque(false);

		// TODO: MOVE THIS TO THE TEXT RESOURCES!!!!!!
		this.allocateButton = new JButton("Armeen setzen");
		this.allocatableArmies = new JLabel("0");
		this.cardButton = new JButton("Karten eintauschen");
		this.attackButton = new JButton("Angriff");
		this.moveButton = new JButton("Armeen verschieben");
		this.endTurnButton = new JButton("Zug beenden");
		
		this.upperHalf.add(this.currentPlayerArea);
		
		this.upperHalf.add(this.allocateButton);
		this.upperHalf.add(this.allocatableArmies);
		this.upperHalf.add(this.cardButton);
		this.upperHalf.add(this.attackButton);
		this.upperHalf.add(this.moveButton);
		this.upperHalf.add(this.endTurnButton);
	}

	/**
	 * Initialization of the LOWER half of the control bar
	 */
	private void initLowerHalf() {
		//lower half
		this.lowerHalf = new JPanel();
		this.lowerHalf.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		this.lowerHalf.setOpaque(false);
		
		//constructing all objects
		this.model = new DefaultListModel();
		this.cardList = new JList(this.model);
		this.turnIn = new JButton("Turn in!");
		
		//card list
		this.cardList.setVisibleRowCount(5);
		DefaultListSelectionModel selection = new DefaultListSelectionModel();
		selection.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.cardList.setSelectionModel(selection);
		this.cardList.setPreferredSize(new Dimension(250,125));
		this.cardList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.CYAN),"Cards"));

		this.lowerHalf.add(this.cardList);
		this.lowerHalf.add(this.turnIn);
	}
	
	/**
	 * Sets slide panel if this panel is inside one. This method is automatically used
	 * if the control bar is deployed in a slide panel.
	 * 
	 * @param slidePanel the slide panel
	 */
	public void setSlidePanel(MSlidePanel slidePanel) {
		this.slidePanel = slidePanel;
		this.relativeHeight = (int) lowerHalf.getPreferredSize().getHeight();

		// TODO: MOVE TO TEXT RESOURCES!!!
		this.slideButton = new JButton("Slide");
		this.upperHalf.add(this.slideButton);
		this.addSlideListeners();
	}
	
	/**
	 * Adds action listeners to the slide button if control bar is inside a slide panel
	 */
	private void addSlideListeners() {
		this.slideButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ControlBarPanel.this.slidePanel.isMinimized()) {
					ControlBarPanel.this.slidePanel.slideUp();
				} else {
					ControlBarPanel.this.slidePanel.slideDown();
				}			
			}
		});
	}
	
	/**
	 * Gets relative height of the control bar.
	 * @return relative height
	 */
	public int getRelativeHeight() {
		return this.relativeHeight;	
	}
	
	/**
	 * sets the label for the current player
	 * @param currentPlayer name of current player
	 */
	public void setCurrentPlayerLabel(Player currentPlayer) {
		this.currentPlayerArea.removeAll();
		this.currentPlayerArea.add(new MPlayerIcon(currentPlayer, true));
	}

	/**
	 * update the card list of the current player to the given one.
	 * @param cards card list
	 */
	public void updateCardList(Vector<CountryCard> cards) {
		this.model.clear();
		for (CountryCard card : cards) {
			String text;
			if (card instanceof SoldierCard) {
				// TODO: MOVE THE STRINGS TO THE TEXT RESOURCES!!!
				text = "Symbol: Soldier, Land: "+card.getReference().getName();
			} else if (card instanceof CavalerieCard) {
				text = "Symbol: Cavalerie, Land: "+card.getReference().getName();
			} else {
				text = "Symbol: Canon, Land: "+card.getReference().getName();
			}
			model.addElement(text);
		}
	}
	
	/**
	 * Adds listeners to all Buttons and sets the game panel needed for their states.
	 * @param gamePanel game panel
	 */
	public void addListeners(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.endTurnButton.addActionListener(this);
		this.allocateButton.addActionListener(this);
		this.cardButton.addActionListener(this);
		this.attackButton.addActionListener(this);
		this.moveButton.addActionListener(this);
		this.turnIn.addActionListener(this);
	}
	
	/**
	 * Sets text of the label for allocatable armies.
	 * @param allocatableArmies count as string
	 */
	public void setAllocatableArmiesLabel(String allocatableArmies) {
		this.allocatableArmies.setText(" "+allocatableArmies+" ");
	}
	
	/**
	 * Updates the state of all buttons
	 */
	public void updateButtons() {
		if (this.gamePanel.getGameState() > 0) {
			this.allocateButton.setEnabled(false);
			if (this.gamePanel.getGameState() > 1) {
				this.cardButton.setEnabled(false);
				if (this.gamePanel.getGameState() > 2) {
					this.attackButton.setEnabled(false);
					if (this.gamePanel.getGameState() > 3) {
						this.moveButton.setEnabled(false);
					}
				}
			} else {
				this.turnIn.setEnabled(true);
			}
		} else {
			this.allocateButton.setEnabled(true);
			this.cardButton.setEnabled(true);
			this.attackButton.setEnabled(true);
			this.moveButton.setEnabled(true);
			this.turnIn.setEnabled(false);
		}
	}

	/**
	 * Buttons actions of this panel
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO: implementation
		if (e.getSource() == this.endTurnButton) {
			this.gamePanel.setGameState(GamePanel.ALLOCATE);
			this.gamePanel.nextTurn();
			this.gamePanel.unmarkAll();
		} else if ((e.getSource() == this.allocateButton) && (this.gamePanel.getGameState() < GamePanel.CARD_TURN_IN)) {
			this.gamePanel.setGameState(GamePanel.ALLOCATE);
		} else if ((e.getSource() == this.cardButton) && (this.gamePanel.getGameState() < GamePanel.ATTACK)) {
			this.gamePanel.setGameState(GamePanel.CARD_TURN_IN);
		} else if ((e.getSource() == this.attackButton) && (this.gamePanel.getGameState() < GamePanel.MOVE)) {
			this.gamePanel.setGameState(GamePanel.ATTACK);
		} else if (e.getSource() == this.moveButton) {
			this.gamePanel.setGameState(GamePanel.MOVE);
		} else if (e.getSource() == this.turnIn) {
			if (this.cardList.getSelectedIndices().length == 1) {
				this.gamePanel.TurnCardIn(this.gamePanel.getCurrentTurn().getCurrentPlayer().getCountryCards().get(this.cardList.getSelectedIndex()));
			} else if (this.cardList.getSelectedIndices().length == 3) {
				Vector<CountryCard> series = new Vector<CountryCard>();
				for (int i = 0; i < 3; i++) {
					series.add(this.gamePanel.getCurrentTurn().getCurrentPlayer().getCountryCards().get(this.cardList.getSelectedIndices()[i]));
				}
				this.gamePanel.TurnSeriesIn(series);
			} else {
				//TODO: Better exception message!!!!
				MMessageBox.error("Das geht nicht.");
			}
		}
		this.gamePanel.updatePanel();
	}
}