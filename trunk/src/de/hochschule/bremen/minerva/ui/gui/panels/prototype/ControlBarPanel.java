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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.hochschule.bremen.minerva.vo.Player;

/**
 * Panel sliced
 *
 * @version $Id$
 * @since 1.0
 */
public class ControlBarPanel extends JPanel implements ActionListener {

	private int relativeHeight;
	private MSlidePanel slidePanel;
	private GamePanel gamePanel;
	
	private JButton allocateButton;
	private JLabel allocatableArmies;
	private JButton cardButton;
	private JButton attackButton;
	private JButton moveButton;
	private JButton endTurnButton;
	
	private JButton slideButton;
	private JPanel upperHalf;
	private JPanel lowerHalf;
	private JLabel currentPlayerLabel;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2420450895831821731L;
	
	/**
	 * 
	 */
	public ControlBarPanel() {
		init();
	}
	
	/**
	 * Initializes ControlBarPanel.
	 */
	private void init() {
		this.setLayout(new BorderLayout());
		this.setSize(200,30);
		//upper half
		this.upperHalf = new JPanel();
		this.upperHalf.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		
		this.currentPlayerLabel = new JLabel("CURRENT PLAYER");
		
		this.allocateButton = new JButton("Armeen setzen");
		this.allocatableArmies = new JLabel("0");
		this.cardButton = new JButton("Karten eintauschen");
		this.attackButton = new JButton("Angriff");
		this.moveButton = new JButton("Armeen verschieben");
		this.endTurnButton = new JButton("Zug beenden");
		
		this.upperHalf.add(this.currentPlayerLabel);
		
		this.upperHalf.add(this.allocateButton);
		this.upperHalf.add(this.allocatableArmies);
		this.upperHalf.add(this.cardButton);
		this.upperHalf.add(this.attackButton);
		this.upperHalf.add(this.moveButton);
		this.upperHalf.add(this.endTurnButton);
		
		
		//lower half
		this.lowerHalf = new JPanel();
		this.lowerHalf.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		this.lowerHalf.add(new JButton("Test"));
		this.lowerHalf.add(new JButton("Test"));
		this.lowerHalf.add(new JButton("Test"));
		this.lowerHalf.add(new JButton("Test"));
		this.lowerHalf.add(new JButton("Test"));
		
		
		
		this.add(this.upperHalf, BorderLayout.NORTH);
		this.add(this.lowerHalf, BorderLayout.SOUTH);
		
		this.updateUI();
	}
	
	public void setSlidePanel(MSlidePanel slidePanel) {
		this.slidePanel = slidePanel;
		this.relativeHeight = (int) lowerHalf.getPreferredSize().getHeight();
		this.slideButton = new JButton("Slide");
		this.upperHalf.add(this.slideButton);
		this.addSlideListeners();
	}
	
	/**
	 * Adds action listeners to
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
	 * 
	 * @return
	 */
	public int getRelativeHeight() {
		return this.relativeHeight;	
	}
	
	public void setCurrentPlayerLabel(Player currentPlayer) {
		this.currentPlayerLabel.setText(" "+currentPlayer.getUsername()+" ");
		this.currentPlayerLabel.setForeground(currentPlayer.getColor());
	}
	
	public void addListeners(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.endTurnButton.addActionListener(this);
		this.allocateButton.addActionListener(this);
		this.cardButton.addActionListener(this);
		this.attackButton.addActionListener(this);
		this.moveButton.addActionListener(this);
	}
	
	public void setAllocatableArmiesLabel(String allocatableArmies) {
		this.allocatableArmies.setText(" "+allocatableArmies+" ");
	}
	
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
			}
		} else {
			this.allocateButton.setEnabled(true);
			this.cardButton.setEnabled(true);
			this.attackButton.setEnabled(true);
			this.moveButton.setEnabled(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO: implementation
		if (e.getSource() == this.endTurnButton) {
			this.gamePanel.setGameState(GamePanel.ALLOCATE);
			this.gamePanel.nextTurn();
		} else if ((e.getSource() == this.allocateButton) && (this.gamePanel.getGameState() < GamePanel.CARD_TURN_IN)) {
			this.gamePanel.setGameState(GamePanel.ALLOCATE);
		} else if ((e.getSource() == this.cardButton) && (this.gamePanel.getGameState() < GamePanel.ATTACK)) {
			this.gamePanel.setGameState(GamePanel.CARD_TURN_IN);
		} else if ((e.getSource() == this.attackButton) && (this.gamePanel.getGameState() < GamePanel.MOVE)) {
			this.gamePanel.setGameState(GamePanel.ATTACK);
		} else if (e.getSource() == this.moveButton) {
			this.gamePanel.setGameState(GamePanel.MOVE);
		}
		this.gamePanel.updatePanel();
	}
}
