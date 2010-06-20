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
	static final String LOGIN_PANEL_BUTTON                            = "Let's rock!";
	static final String LOGIN_PANEL_STATUS_WHILE_LOGIN                = "Login ...";
	static final String LOGIN_PANEL_MESSAGE_INPUT_INCOMPLETE          = "<html>User/Passwort Eingabe <br>ist unvollständig.";
	static final String LOGIN_PANEL_MESSAGE_USER_INPUT_INCOMPLETE     = "<html>User Eingabe ist <br>unvollständig.";
	static final String LOGIN_PANEL_MESSAGE_PASSWORD_INPUT_INCOMPLETE = "<html>Die Eingabe des Passworts ist <br>unvollständig.";
}
