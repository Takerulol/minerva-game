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
package de.hochschule.bremen.minerva.manager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import de.hochschule.bremen.minerva.exceptions.AppConfigurationNotFoundException;
import de.hochschule.bremen.minerva.exceptions.AppConfigurationNotReadableException;
import de.hochschule.bremen.minerva.vo.AppConfiguration;

/**
 * 
 * @version $Id$
 * @since 1.0
 *
 */
public class AppConfigurationManager {

	// The expected file in which the application configuration will be stored.
	private static final String APPCONFIGURATION_FILENAME = "application.configuration";

	// Value that identifies a "comment line"
	private static final String COMMENT_IDENTIFIER = "--";

	// The key/value delimiter
	private static final String SPLIT_IDENTIFIER = "=";

	// Method invocation mapping. This hash map tells if a specific key was found, which method on the
	// AppConfiguration object should be invoked (AppConfigurationManager#fill).
	// The value (notation: key=value) will be passed to this invoked method.
	private static final HashMap<String, String> voMethodInvocationMapping = new HashMap<String, String>();

	// Method/Key initialization.
	static {
		voMethodInvocationMapping.put("directory.assets.worlds", "setWorldsAssetsDirectory");
		voMethodInvocationMapping.put("directory.assets.userinterface", "setUserInterfaceAssetsDirectory");
	}

	// The manager instance
	private static AppConfigurationManager manager = null;

	// The cached application configuration. We do not need to parse application
	// configuration file a second time if it was done before.
	private static AppConfiguration cachedConfiguration = new AppConfiguration();

	/**
	 * Singleton pattern. It is not possible
	 * to create a ApplicationConfigurationManager in the common way.
	 * So this constructor is private.
	 * 
	 */
	private AppConfigurationManager() {}

	/**
	 * Reads the application configuration from the file
	 * and returns the object.
	 * 
	 * @see AppConfiguration
	 * @return The application configuration object
	 * @throws AppConfigurationNotFoundException 
	 * @throws AppConfigurationNotReadableException 
	 * 
	 */
	public static AppConfiguration load() throws AppConfigurationNotFoundException, AppConfigurationNotReadableException {
		if (AppConfigurationManager.manager == null || AppConfigurationManager.cachedConfiguration == null) {
			AppConfigurationManager.manager = new AppConfigurationManager();
			AppConfigurationManager.manager.parse();
		}
		
		return AppConfigurationManager.cachedConfiguration;
	}

	/**
	 * Stores a application configuration.
	 * 
	 * @param storableConfiguration The configuration which should be stored.
	 * 
	 */
	public static void store(AppConfiguration storableConfiguration) {
		// TODO: It is not necessary at the moment. Implement in later releases. It is just for api demonstration.
		
		cachedConfiguration = storableConfiguration;
	}
	
	/**
	 * DOCME
	 * 
	 * @throws AppConfigurationNotFoundException The application configuration was not found in the application root. 
	 * @throws AppConfigurationNotReadableException 
	 * 
	 */
	private void parse() throws AppConfigurationNotFoundException, AppConfigurationNotReadableException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(AppConfigurationManager.APPCONFIGURATION_FILENAME));
			String line = "";

			while (!(line = reader.readLine()).isEmpty()) {
				
				// Ignore comments and "whitespace" lines.
				if (!this.isCommentLine(line)) {
					if (!line.isEmpty()) {
						
						// DOCME line
						String[] keyValue = line.split(SPLIT_IDENTIFIER);
						this.fill(keyValue[0], keyValue[1]);
					}
				}
			}

			reader.close();
		} catch (FileNotFoundException e) {
			throw new AppConfigurationNotFoundException(AppConfigurationManager.APPCONFIGURATION_FILENAME);
		} catch (IOException e) {
			throw new AppConfigurationNotReadableException(AppConfigurationManager.APPCONFIGURATION_FILENAME, e.getMessage());
		}
	}

	/**
	 * DOCME
	 * 
	 * @param configuration
	 * @param key
	 * @param value
	 * 
	 */
	private void fill(String key, String value) {
		String methodName = voMethodInvocationMapping.get(key);

		try {
			System.out.println(key);
			Method method = cachedConfiguration.getClass().getDeclaredMethod(methodName, String.class);
			method.invoke(cachedConfiguration, value);
		} catch (SecurityException e) {
			// TRASH! If this problem occurs, the developer missed to fill the "methods" hash map correctly.
		} catch (NoSuchMethodException e) {
			// TRASH! If this problem occurs, the developer missed to fill the "methods" hash map correctly.
		} catch (IllegalArgumentException e) {
			// TRASH! If this problem occurs, the developer missed to fill the "methods" hash map correctly.
		} catch (IllegalAccessException e) {
			// TRASH! If this problem occurs, the developer missed to fill the "methods" hash map correctly.
		} catch (InvocationTargetException e) {
			// TRASH! If this problem occurs, the developer missed to fill the "methods" hash map correctly.
		}
	}

	/**
	 * Check if the given line is a comment line or not
	 * 
	 * @param line The line which should be analyzed.
	 * @return true/false
	 * 
	 */
	private boolean isCommentLine(String line) {
		return line.startsWith(COMMENT_IDENTIFIER);
	}
}
