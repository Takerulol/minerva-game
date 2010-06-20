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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import de.hochschule.bremen.minerva.exceptions.PlayerAlreadyLoggedInException;
import de.hochschule.bremen.minerva.exceptions.PlayerDoesNotExistException;
import de.hochschule.bremen.minerva.exceptions.WrongPasswordException;
import de.hochschule.bremen.minerva.manager.AccountManager;
import de.hochschule.bremen.minerva.persistence.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.ui.gui.MinervaGUI;
import de.hochschule.bremen.minerva.ui.gui.controls.MPasswordField;
import de.hochschule.bremen.minerva.ui.gui.controls.MTextField;
import de.hochschule.bremen.minerva.ui.gui.listener.MMouseListener;
import de.hochschule.bremen.minerva.vo.Player;

/**
 * Panel for the login screen
 * @author cbollmann
 *
 */
public class LoginPanel extends JLayeredPane {
	
	private Background background;
	private MTextField username;
	private MPasswordField password;
	private JButton loginButton;
	private JLabel statusLabel;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6914010423171152967L;

	/**
	 * Constructor initializing the panel
	 */
	public LoginPanel() {
		
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
		
		//status label
		this.statusLabel = new JLabel();
		this.statusLabel.setBounds(614, 328, 160, 48);
		
		//adding everything to panel
		this.add(this.loginButton,20);
		this.add(this.username,20);
		this.add(this.password,20);
		this.add(this.statusLabel,20);
		this.add(this.background,10);
		
		this.updateUI();
		this.addListeners();
	}

	/**
	 * Returns string of the username typed in 
	 * @return username
	 */
	public String getUsername() {
		return this.username.getText();
	}
	
	/**
	 * Returns string of the password typed in clear text
	 * @return password
	 */
	public String getPassword() {
		return String.valueOf(this.password.getPassword());
	}
	/**
	 * Adds a listener to login button
	 * @param l ActionListener
	 */
	public void addLoginButtonListener(ActionListener l) {
		this.loginButton.addActionListener(l);
	}
	/**
	 * Sets text for status label
	 * @param text status 
	 */
	public void setStatusText(String text) {
		this.statusLabel.setText(text);
	}
	
	/**
	 * logs player in username field in
	 */
	private void login() {
		LoginPanel.this.setStatusText(null);
		
		Player player = new Player();
		
		player.setUsername(LoginPanel.this.getUsername());
		player.setPassword(LoginPanel.this.getPassword());
		
		if (player.getUsername().isEmpty()) {
			if (player.getPassword().isEmpty()) {
				LoginPanel.this.setStatusText("<html>User/Passwort Eingabe <br>ist unvollständig.");
			} else {
				LoginPanel.this.setStatusText("<html>User Eingabe ist <br>unvollständig.");
			}
		} else if (player.getPassword().isEmpty()) {
			LoginPanel.this.setStatusText("<html>Passwort Eingabe ist <br>unvollständig.");
		} else {
			try {
				AccountManager.getInstance().login(player);
			} catch (PlayerAlreadyLoggedInException e1) {
				try {
					AccountManager.getInstance().logout(player);
					this.login();
				} catch (PlayerDoesNotExistException e2) {
				} catch (DataAccessException e2) {
					LoginPanel.this.setStatusText(e2.getMessage());
				}
				LoginPanel.this.setStatusText("<html>Jemand ist bereits mit <br>diesem Namen eingeloggt. <br>Bitte warten...");
			} catch (WrongPasswordException e1) {
				LoginPanel.this.setStatusText("<html>Das eingegebene Passwort <br>ist falsch.");
			} catch (PlayerDoesNotExistException e1) {
				LoginPanel.this.setStatusText("<html>Es existiert kein Account <br>mit diesem Username.");
			} catch (DataAccessException e1) {
				//TODO: text handling
				//LoginPanel.this.setStatusText(e1.getMessage());
				LoginPanel.this.setStatusText("<html>Datenbankfehler");
			}
			
			//panel swaping
			if (player.isLoggedIn()) {
				GameInitPanel gip = new GameInitPanel();
				//TODO: setting needed parameters
				MinervaGUI.getInstance().changePanel(gip);
			}
		}
	}
	
	/**
	 * Adds all action listeners to this panel
	 */
	private void addListeners() {
		this.addMouseListener(new MMouseListener() {
			public void mouseClicked(MouseEvent e) {
				int mx = e.getX();
				int my = e.getY();
				
				//rectangle for "hier" area to be clicked
				if ((mx < 777) && (mx > 749) && (my < 486) && (my > 469)) {
					//750 470 26 15
					MinervaGUI.getInstance().changePanel(new RegistrationPanel());
				}	
			}
		});
		this.addLoginButtonListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LoginPanel.this.login();
			}
		});
	}
}
