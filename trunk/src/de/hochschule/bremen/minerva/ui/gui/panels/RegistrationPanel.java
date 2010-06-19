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


public class RegistrationPanel extends JLayeredPane {

	private Background background;
	private MTextField firstName;
	private MTextField lastName;
	private MTextField username;
	private MPasswordField password;
	private MPasswordField passwordRetype;
	private MTextField email;
	private JButton registerButton;

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6086001895310561512L;

	public RegistrationPanel() {
		this.setPreferredSize(MinervaGUI.WINDOW_SIZE);
		this.setOpaque(true);
		
		//background
		this.background = new Background(this.getClass());
		this.background.setBounds(0, 0, 500, 500);
		
		//firstname field
		this.firstName = new MTextField();
		this.firstName.setBounds(614, 153, 117, 25);
		this.firstName.setOpaque(true);
		
		//lastname field
		this.lastName = new MTextField();
		this.lastName.setBounds(741, 153, 117, 25);
		this.lastName.setOpaque(true);
		
		//username field
		this.username = new MTextField();
		this.username.setBounds(614, 217, 244, 25);
		this.username.setOpaque(true);
		
		//password field
		this.password = new MPasswordField();
		this.password.setBounds(614, 286, 244, 25);
		this.password.setOpaque(true);
		
		//password retype
		this.passwordRetype = new MPasswordField();
		this.passwordRetype.setBounds(614, 357, 244, 25);
		this.passwordRetype.setOpaque(true);
		
		//email
		this.email = new MTextField();
		this.email.setBounds(614, 429, 244, 25);
		this.email.setOpaque(true);
		
		//TODO: resizing font
		//login button
		this.registerButton = new JButton("Fertig!");
		this.registerButton.setBounds(793, 476, 65, 26);
		//this.registerButton.setFont(new Font("Arial",Font.BOLD,9));
		
		//adding everything to panel
		this.add(this.firstName,20);
		this.add(this.lastName,20);
		this.add(this.username,20);
		this.add(this.password,20);
		this.add(this.passwordRetype,20);
		this.add(this.email,20);
		this.add(this.registerButton,20);
		this.add(this.background,10);
	}
}
