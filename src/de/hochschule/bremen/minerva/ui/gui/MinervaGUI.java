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

package de.hochschule.bremen.minerva.ui.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import de.hochschule.bremen.minerva.exceptions.AppConfigurationNotFoundException;
import de.hochschule.bremen.minerva.exceptions.AppConfigurationNotReadableException;
import de.hochschule.bremen.minerva.manager.ApplicationConfigurationManager;
import de.hochschule.bremen.minerva.ui.UserInterface;
import de.hochschule.bremen.minerva.ui.gui.panels.*;


public class MinervaGUI extends JFrame implements UserInterface {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8038646974358166493L;
	
	public static final Dimension WINDOW_SIZE = new Dimension(1000,700);
	
	private JLayeredPane currentPanel;
	


	/**
	 * 
	 */
	public MinervaGUI() {
	
	}
	
	/**
	 * 
	 */
	public void run() {
		try {
			ApplicationConfigurationManager.setup();
		} catch (AppConfigurationNotFoundException e) {
			System.out.println(e.getMessage());
			Runtime.getRuntime().exit(ERROR);
		} catch (AppConfigurationNotReadableException e) {
			System.out.println(e.getMessage());
			Runtime.getRuntime().exit(ERROR);
		}

		//initialization
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.black);
		this.setResizable(false);
		this.setTitle(ApplicationConfigurationManager.get().getAppName());
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(ApplicationConfigurationManager.get().getAppIconPath()));
	
		//start screen
		currentPanel = new StartPanel();
		this.add(currentPanel);
		currentPanel.updateUI();
		
		//login screen after 5 seconds
		Timer timer = new Timer(100,new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() instanceof Timer) {
					MinervaGUI.this.changePanel(new LoginPanel());
					((Timer) e.getSource()).stop();
				}
			}
		});
		timer.start();
		
		//packs frame to the size of its panel
		this.pack();
		
		//centers frame on the screen
		this.centerFrame();
	
		//lets show the frame .. yeeeaaaahhh =D
		this.setVisible(true);
	}
	
	/**
	 * Puts main frame in the center of the screen
	 */
	private void centerFrame() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int X = (screen.width / 2) - (this.getWidth() / 2); // Center horizontally.
		int Y = (screen.height / 2) - (this.getHeight() / 2); // Center vertically.
		this.setBounds(X,Y , this.getWidth(),getHeight());
	}
	
	private void changePanel(JLayeredPane newPanel) {
		this.remove(currentPanel);
		this.currentPanel = newPanel;
		this.add(currentPanel);
		this.currentPanel.updateUI();
		this.setSize(this.getWidth(), this.getHeight()-1);
		this.pack();
	}

}
