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

import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import de.hochschule.bremen.minerva.core.logic.Game;
import de.hochschule.bremen.minerva.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.exceptions.GameAlreadyStartedException;
import de.hochschule.bremen.minerva.exceptions.PlayerAlreadyLoggedInException;
import de.hochschule.bremen.minerva.exceptions.PlayerDoesNotExistException;
import de.hochschule.bremen.minerva.exceptions.WrongPasswordException;
import de.hochschule.bremen.minerva.manager.AccountManager;
import de.hochschule.bremen.minerva.manager.SessionManager;
import de.hochschule.bremen.minerva.ui.gui.MinervaGUI;
import de.hochschule.bremen.minerva.ui.gui.controls.MButton;
import de.hochschule.bremen.minerva.ui.gui.controls.MMessageBox;
import de.hochschule.bremen.minerva.ui.gui.controls.MPasswordField;
import de.hochschule.bremen.minerva.ui.gui.controls.MTextField;
import de.hochschule.bremen.minerva.ui.gui.listener.MKeyListener;
import de.hochschule.bremen.minerva.ui.gui.listener.MMouseListener;
import de.hochschule.bremen.minerva.ui.gui.listener.MMouseMotionListener;
import de.hochschule.bremen.minerva.ui.gui.resources.TextResources;
import de.hochschule.bremen.minerva.vo.Player;

/**
 * Panel for the login screen
 * 
 * @version $Id$
 * @since 1.0
 *
 */
public class LoginPanel extends JLayeredPane implements TextResources {
	
	private Background background;
	private MTextField username;
	private MPasswordField password;
	private MButton loginButton;
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

		//login button
		this.loginButton = new MButton(TextResources.LOGIN_PANEL_BUTTON);

		Rectangle buttonRectangle = new Rectangle();
		buttonRectangle.height = (int)this.loginButton.getPreferredSize().getHeight();
		buttonRectangle.width = (int)this.loginButton.getPreferredSize().getWidth();
		buttonRectangle.x = ((this.username.getX() + this.username.getWidth()) - buttonRectangle.width);
		buttonRectangle.y = 328;

		this.loginButton.setBounds(buttonRectangle);

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
		
		this.repaint();
		
		// Set the focus to the username textfield.
		SwingUtilities.invokeLater(new Runnable() {public void run() {LoginPanel.this.username.requestFocus();}});
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
	private void addLoginButtonListener(ActionListener l) {
		this.loginButton.addActionListener(l);
	}

	/**
	 * Sets text for status label
	 * @param text status 
	 * 
	 */
	public void setStatusText(String text) {
		this.statusLabel.setText(text);
	}
	
	/**
	 * Execute a login with the given login credentials.
	 * 
	 */
	private void login() {
		LoginPanel.this.setStatusText(null);
		
		Player player = new Player();
		player.setUsername(LoginPanel.this.getUsername());
		player.setPassword(LoginPanel.this.getPassword());

		if (this.areLoginCredentialsValid(player)) {
			LoginPanel.this.setStatusText(TextResources.LOGIN_PANEL_STATUS_WHILE_LOGIN);

			try {
				AccountManager.getInstance().login(player);
			} catch (PlayerAlreadyLoggedInException e) {
				// Nothing to do. Everything is fine. Recycle user object.
				// The buddy is already logged in. Have a nice day dude ...
				// Anyway, we have to inform the user ...
				MMessageBox.show(e.getMessage());
			} catch (WrongPasswordException e) {
				MMessageBox.error(e.getMessage());
			} catch (PlayerDoesNotExistException e) {
				MMessageBox.show(e.getMessage());
			} catch (DataAccessException e) {
				MMessageBox.show(e.getMessage());
				Runtime.getRuntime().exit(ERROR);
			}

			if (player.isLoggedIn()) {
				// Try to add a new player to the game.
				try {
					Game game = SessionManager.get(MinervaGUI.getSessionId());

					if (game.getPlayerCount() == 0) {
						player.setMaster(true);
					}

					game.addPlayer(player);

					MinervaGUI.getInstance().changePanel(new GameInitPanel());
				} catch (GameAlreadyStartedException e) {
					MMessageBox.show(e.getMessage());
				}
			}

			LoginPanel.this.setStatusText(null);
		}
	}

	/**
	 * Check if the login credentials are valid.
	 * Informs the user via message box if a error occurred.
	 * 
	 * @return
	 * 
	 */
	private boolean areLoginCredentialsValid(Player player) {
		boolean valid = false;

		this.username.setValid(true);
		this.password.setValid(true);
		
		if (player.getUsername().isEmpty() && player.getPassword().isEmpty()) {
			MMessageBox.show(LOGIN_PANEL_MESSAGE_INPUT_INCOMPLETE);
			this.username.setValid(valid);
			this.password.setValid(valid);

			this.username.requestFocus();
		} else if (player.getUsername().isEmpty()) {
			MMessageBox.show(LOGIN_PANEL_MESSAGE_USER_INPUT_INCOMPLETE);
			this.username.setValid(valid);
		} else if (player.getPassword().isEmpty()) {
			MMessageBox.show(LOGIN_PANEL_MESSAGE_PASSWORD_INPUT_INCOMPLETE);
			this.password.setValid(valid);
		} else {
			valid = true;
		}

		return valid;
	}
	
	/**
	 * Determines if the given coordinates are inside the
	 * "registration link rectangle".
	 * 
	 * @param x The checkable x coordinate.
	 * @param y The checkable y coordinate.
	 * @return Is inside the "registration link rectangle"?
	 * 
	 */
	private boolean isRegistrationLink(int x, int y) {
		if ((x < 777) && (x > 749) && (y < 486) && (y > 469)) {
			return true;
		}
		return false;
	}

	/**
	 * Adds all action listeners to this panel
	 * 
	 */
	private void addListeners() {
		
		// MMouseMotionListener for sending a interaction feedback to the
		// user if he move the mouse over the "registration link"
		this.addMouseMotionListener(new MMouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
				if (LoginPanel.this.isRegistrationLink(e.getX(), e.getY())) {
					LoginPanel.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
				} else {
					LoginPanel.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});

		// MMouseListener for handling the click on the "registration link".
		this.addMouseListener(new MMouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (LoginPanel.this.isRegistrationLink(e.getX(), e.getY())) {
					MinervaGUI.getInstance().changePanel(new RegistrationPanel());					
				}
			}
		});

		// Check if the user pressed the ENTER key.
		// If so the app tries to log in with the defined data.
		this.username.addKeyListener(new MKeyListener() {
			public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		        	LoginPanel.this.login();
		        }
			}
		});this.password.addKeyListener(new MKeyListener() {
			public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		        	LoginPanel.this.login();
		        }
			}
		});

		// Handling the login event if the user pressed on the login button. 
		this.addLoginButtonListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LoginPanel.this.login();
			}
		});
	}
}
