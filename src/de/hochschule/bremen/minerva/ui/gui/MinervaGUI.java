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

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.*;
import de.hochschule.bremen.minerva.ui.UserInterface;
import de.hochschule.bremen.minerva.ui.gui.panels.TestPanel;


public class MinervaGUI extends JFrame implements UserInterface {

	
	/**
	 * TODO: generated id
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public MinervaGUI() {
		//JFrame frame = new JFrame("Minerva");
		
	}
	
	/**
	 * Initializing the Frame
	 */
	private void init() {
		this.setSize(1000, 700);
		this.setVisible(true);
		//this.setLayout(new BorderLayout());
		
	}
	
	/**
	 * 
	 */
	public void run() {
		this.init();
		JMenu menu = new JMenu("Test");
		JMenuItem mitem = new JMenuItem("Testor");
		menu.add(mitem);
		
		//this.setJMenuBar(menu);
		TestPanel test = new TestPanel();
		test.setAlignmentX(LEFT_ALIGNMENT);
		test.setAlignmentY(TOP_ALIGNMENT);
		this.add(test);
		this.pack();
		
		//,BorderLayout.NORTH
	}
	
	

}
