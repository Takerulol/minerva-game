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
package de.hochschule.bremen.minerva.ui.gui.resources;

/**
 * GUI text resources.<br />
 * Yeah. It's better to use resource bundles. But this is a lightweight
 * solution. Not so nice, but it works :\
 * 
 * @version $Id$
 * @since 1.0
 *
 */
public interface TextResources {

	// Login panel
	public static final String LOGIN_PANEL_BUTTON                            = "Let's rock!";
	public static final String LOGIN_PANEL_STATUS_WHILE_LOGIN                = "Login ...";
	public static final String LOGIN_PANEL_MESSAGE_INPUT_INCOMPLETE          = "Bitte geben Sie Ihre Zugangsdaten ein.";
	public static final String LOGIN_PANEL_MESSAGE_USER_INPUT_INCOMPLETE     = "Bitte geben Sie einen Username ein.";
	public static final String LOGIN_PANEL_MESSAGE_PASSWORD_INPUT_INCOMPLETE = "Bitte geben Sie ein Passwort ein.";

	// Registration panel
	public static final String REGISTRATION_BUTTON_DONE = "Fertig!";
	public static final String REGISTRATION_BUTTON_BACK = "Zurück";
	public static final String REGISTRATION_TOOLTIP_EMAIL = "Nein, du bekommst kein Spam. Versprochen ;)";
}
