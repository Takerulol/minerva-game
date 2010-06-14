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

import java.awt.Dimension;
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

		this.setResizable(false);
		this.setVisible(true);
	

		currentPanel = new StartPanel();
	
		this.add(currentPanel);

		currentPanel.updateUI();
		this.pack();
		
		
		// BEWARE: LOTS OF COMMENTS!
		
//		JMenu menu = new JMenu("Test");
//		JMenuItem mitem = new JMenuItem("Testor");
//		menu.add(mitem);
		
//		String path = "D:/testbg2.png";
//		File file = new File(path);
//
//		try {
//			bgbild = ImageIO.read(file);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//this.setJMenuBar(menu);
		
//		TestPanel test = new TestPanel();
//		JPanel test1 = new JPanel();
//		test1.setLayout(new FlowLayout());
//		test1.setSize(1000, 700);
//		
//		Background bg = new Background(TestPanel.class);
		//bg.setSize(1000, 700);
//		for (int i = 0; i < 20; i++)
//		bg.add(new JLabel("Test"));
		
//		this.add(bg);
		//test1.add(new Background("",""));
		//test.setAlignmentX(LEFT_ALIGNMENT);
		//test.setAlignmentY(TOP_ALIGNMENT);
		//this.setLayout(null);
		//this.add(test);
		//this.repaint();
		//this.pack();
		//System.out.println(this.getBounds().height);
		//System.out.println(this.getSize().height);
		//,BorderLayout.NORTH
	}

}
