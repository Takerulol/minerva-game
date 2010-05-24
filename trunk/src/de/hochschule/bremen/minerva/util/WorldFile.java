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
package de.hochschule.bremen.minerva.util;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.hochschule.bremen.minerva.exceptions.WorldFileExtensionException;
import de.hochschule.bremen.minerva.exceptions.WorldFileNotFoundException;
import de.hochschule.bremen.minerva.exceptions.WorldFileParseException;
import de.hochschule.bremen.minerva.vo.Continent;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.World;

public class WorldFile extends World {
	
	private static final String MESSAGE_FILE_NOT_WELLFORMED = "The world import file is not well-formed. ";

	// The xml tag sections
	private static final String TAG_META = "meta";

	private static final String TAG_CONTINENTS = "continents";

	private static final String TAG_COUNTRIES = "countries";
	
	
	private File worldFile = null;

	// We define an internal HashMap which contains the continent objects from the
	// world import file. It is necessary to save it temporally because of the different
	// id mapping structure in the world import file. It differs from the auto generated
	// ids in the persistence layer.
	private HashMap<Integer, Continent> temporallyContinents = new HashMap<Integer, Continent>();
	
	// DOCME
	private HashMap<Integer, Country> temporallyCountries = new HashMap<Integer, Country>();
	
	/**
	 * DOCME
	 * 
	 * @param worldFile
	 */
	public WorldFile(File worldFile) {
		this.worldFile = worldFile;
	}

	/**
	 * DOCME
	 * @throws WorldFileNotFoundException - If the given "*.world" file was not found.
	 * @throws WorldFileParseException 
	 * @throws WorldFileExtensionException 
	 * 
	 */
	public void parse() throws WorldFileExtensionException, WorldFileNotFoundException, WorldFileParseException {
		if (!this.worldFile.getName().endsWith(".world")) {
			throw new WorldFileExtensionException("The file '"+this.worldFile.getName() +
					  "' does not have the correct extension. Please verify to import a valid "
					+ "world import file (*.world).");
		}

		try {
			Element root = this.open();

			this.validate(root);
			this.extractMeta(root);
			this.extractContinents(root);
			this.extractCountries(root);
		} catch (IOException e) {
			throw new WorldFileNotFoundException(e.getMessage());
		} catch (SAXException e) {
			throw new WorldFileParseException(e.getMessage());
		} catch (ParserConfigurationException e) {
			throw new WorldFileParseException(e.getMessage());
		}
	}

	/**
	 * DOCME
	 * 
	 */
	public void createCountryDependencies() {
		
	}
	
	/**
	 * Starts the parser and returns the document root.
	 * 
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Element open() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(this.getWorldFile().toURI().toString());
		return doc.getDocumentElement();
	}

	/**
	 * DOCME
	 * 
	 * @param tag
	 * @return
	 */
	private String extractText(Element root, String tag) {
		NodeList nodes = root.getElementsByTagName(tag);
		return (nodes.getLength() > 0) ? nodes.item(0).getTextContent() : "";
	}

	/**
	 * Extracts the meta data from the world import file
	 * and pushs the data into the object attributes.
	 * 
	 * @param root - The document root @see {@link WorldFile#open()}
	 * @throws WorldFileParseException - If the document is not well-formed.
	 */
	private void extractMeta(Element root) throws WorldFileParseException {
		// The worlds token
		this.setToken(this.extractText(root, "token"));
		if (this.getToken().isEmpty()) {
			throw new WorldFileParseException(MESSAGE_FILE_NOT_WELLFORMED + "Missing 'token'.");
		}

		// The worlds name
		this.setName(this.extractText(root, "name"));
		if (this.getName().isEmpty()) {
			throw new WorldFileParseException(MESSAGE_FILE_NOT_WELLFORMED + "Missing 'name'.");
		}

		// The worlds description
		this.setDescription(this.extractText(root, "description"));
		if (this.getDescription().isEmpty()) {
			throw new WorldFileParseException(MESSAGE_FILE_NOT_WELLFORMED + "Missing 'description'.");
		}
		
		// The author
		this.setAuthor(this.extractText(root, "author"));
		if (this.getAuthor().isEmpty()) {
			throw new WorldFileParseException(MESSAGE_FILE_NOT_WELLFORMED + "Missing 'author'.");
		}

		// The version
		this.setVersion(this.extractText(root, "version"));
		if (this.getVersion().isEmpty()) {
			throw new WorldFileParseException(MESSAGE_FILE_NOT_WELLFORMED + "Missing 'version'.");
		}
	}
	
	/**
	 * Extracts the continent data from the world import file
	 * and saves the data in an temporally hash map.
	 * 
	 * @param root - The document root @see {@link WorldFile#open()}
	 * @throws WorldFileParseException - If the document is not well-formed.
	 */
	private void extractContinents(Element root) throws WorldFileParseException {
		NodeList continents = root.getElementsByTagName("continent");
		
		for (int i = 0; i < continents.getLength(); i++) {
			NamedNodeMap node = continents.item(i).getAttributes();
			Continent continent = new Continent();
			continent.setName(node.getNamedItem("name").getNodeValue());

			int id = Integer.parseInt(node.getNamedItem("id").getNodeValue());
			this.temporallyContinents.put(id, continent);
		}
		
		if (this.temporallyContinents.isEmpty()) {
			throw new WorldFileParseException(MESSAGE_FILE_NOT_WELLFORMED + "Missing 'continents' data.");
		}
	}

	/**
	 * DOCME
	 * 
	 * @param root
	 * @param tag
	 * TODO: Add well-formed checks
	 * TODO: Get the color from the file
	 * @throws WorldFileParseException 
	 */
	private void extractCountries(Element root) throws WorldFileParseException {
		NodeList countries = root.getElementsByTagName("country");
		
		for (int i = 0; i < countries.getLength(); i++) {
			NamedNodeMap node = countries.item(i).getAttributes();
			Country country = new Country();
			country.setToken(node.getNamedItem("token").getNodeValue());
			country.setName(node.getNamedItem("name").getNodeValue());
			country.setColor(Color.BLACK);//node.getNamedItem("color").getNodeValue());
			
			int continentId = Integer.parseInt(node.getNamedItem("continent").getNodeValue());
			country.setContinent(this.temporallyContinents.get(continentId));
			
			System.out.println(country.toString());
			
			int id = Integer.parseInt(node.getNamedItem("id").getNodeValue());
			this.temporallyCountries.put(id, country);
		}

		if (this.temporallyCountries.isEmpty()) {
			throw new WorldFileParseException(MESSAGE_FILE_NOT_WELLFORMED + "Missing 'country' data.");
		}
	}
	
	/**
	 * DOCME
	 * @throws WorldFileParseException 
	 * 
	 */
	public void validate(Element root) throws WorldFileParseException {
		Node node = root.getElementsByTagName(TAG_META).item(0);
		if (node == null || node.getChildNodes().getLength() <= 0) {
			throw new WorldFileParseException(MESSAGE_FILE_NOT_WELLFORMED + "Missing 'meta' section.");
		}
		
		node = root.getElementsByTagName(TAG_CONTINENTS).item(0);
		if (node == null || node.getChildNodes().getLength() <= 0) {
			throw new WorldFileParseException(MESSAGE_FILE_NOT_WELLFORMED + "Missing 'continents' section.");
		}

		node = root.getElementsByTagName(TAG_COUNTRIES).item(0);
		if (node == null || node.getChildNodes().getLength() <= 0) {
			throw new WorldFileParseException(MESSAGE_FILE_NOT_WELLFORMED + "Missing 'countries' section.");
		}
	}

	/**
	 * DOCME
	 * 
	 * @return
	 */
	public File getWorldFile() {
		return this.worldFile;
	}
}