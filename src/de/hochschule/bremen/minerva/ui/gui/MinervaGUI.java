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
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import de.hochschule.bremen.minerva.exceptions.AppConfigurationNotFoundException;
import de.hochschule.bremen.minerva.exceptions.AppConfigurationNotReadableException;
import de.hochschule.bremen.minerva.manager.ApplicationConfigurationManager;
import de.hochschule.bremen.minerva.ui.UserInterface;
import de.hochschule.bremen.minerva.ui.gui.panels.Background;
import de.hochschule.bremen.minerva.ui.gui.panels.TestPanel;
import de.hochschule.bremen.minerva.vo.ApplicationConfiguration;


public class MinervaGUI extends JFrame implements UserInterface {
	private Image image = null;
	
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
		//this.setResizable(false);
		this.setVisible(true);
		//this.setLayout(new BorderLayout());
		
	}
	
//	public void paint(Graphics g) {
//		g.drawImage(bgbild, 0,0,1000,700,this);
//	}
	
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
		this.init();
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
		
		TestPanel test = new TestPanel();
//		JPanel test1 = new JPanel();
//		test1.setLayout(new FlowLayout());
//		test1.setSize(1000, 700);
//		
		Background bg = new Background(TestPanel.class);
		//bg.setSize(1000, 700);
//		for (int i = 0; i < 20; i++)
//		bg.add(new JLabel("Test"));
		
		this.add(bg);
		//test1.add(new Background("",""));
		//test.setAlignmentX(LEFT_ALIGNMENT);
		//test.setAlignmentY(TOP_ALIGNMENT);
		//this.setLayout(null);
		//this.add(test);
		//this.repaint();
		this.pack();
		//System.out.println(this.getBounds().height);
		//System.out.println(this.getSize().height);
		//,BorderLayout.NORTH
	}

}
