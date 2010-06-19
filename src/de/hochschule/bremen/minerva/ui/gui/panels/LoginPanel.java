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

import javax.swing.JButton;
import javax.swing.JLayeredPane;

import de.hochschule.bremen.minerva.ui.gui.MinervaGUI;
import de.hochschule.bremen.minerva.ui.gui.controls.MPasswordField;
import de.hochschule.bremen.minerva.ui.gui.controls.MTextField;

public class LoginPanel extends JLayeredPane {
	
	private Background background;
	private MTextField username;
	private MPasswordField password;
	private JButton loginButton;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6914010423171152967L;

	public LoginPanel() {
//		super();
//		this.setPreferredSize(MinervaGUI.WINDOW_SIZE);
//		this.setLayout(new BorderLayout());
//		this.background = new Background(this.getClass());
//		this.add(this.background,BorderLayout.NORTH,DEFAULT_LAYER);
//		System.out.println(DEFAULT_LAYER);
//		
//		this.username = new JPanel();
//		this.username.setLayout(new BorderLayout());
//		MTextField mt = new MTextField();
//		mt.setBounds(0,0,200,50);
//		this.username.add(mt, BorderLayout.NORTH);
//		this.password = new MPasswordField();
//		
////		this.username.setLocation(500,500);
////		this.password.setLocation(500,600);
//		
//		this.username.setBounds(500, 500, this.username.getWidth(), this.username.getHeight());
//		this.username.setPreferredSize(new Dimension(500,500));
//		
//		this.add(this.username,BorderLayout.NORTH,1);
//		//this.add(this.password,BorderLayout.NORTH,2);
		
		this.setPreferredSize(MinervaGUI.WINDOW_SIZE);
		this.setOpaque(true);
		
		//background
		this.background = new Background(this.getClass());
		this.background.setBounds(0, 0, 500, 500);
		
		//username field
		this.username = new MTextField();
		this.username.setBounds(614, 212, 244, 25);
		this.username.setOpaque(true);
		
		//password field
		this.password = new MPasswordField();
		this.password.setBounds(614, 281, 244, 25);
		this.password.setOpaque(true);
		
		//TODO: resizing font
		//login button
		this.loginButton = new JButton("Let's go!");
		this.loginButton.setBounds(793, 328, 65, 26);
		//this.loginButton.setFont(new Font("Arial",Font.BOLD,9));
		
		//adding everything to panel
		this.add(this.loginButton,20);
		this.add(this.username,20);
		this.add(this.password,20);
		this.add(this.background,10);
		this.updateUI();
		
		
	}
}
