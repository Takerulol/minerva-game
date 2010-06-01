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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import java.util.Map.Entry;

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

/**
 * Represents a physical file that contains the data structure to describe
 * a world with it's countries and country dependencies. It is possible to
 * parse this file and store the information in the world value object attributes.
 * 
 * @version $Id$
 *
 */
public class WorldFile extends World {
	// TODO: If the country/neighbour mapping is not valid -> throw Exception (at the moment "NullPointerException" ); ).

	// TODO: Replace by the new WorldFileParseException.
	private static final String MESSAGE_FILE_NOT_WELLFORMED = "The world import file is not well-formed. ";
	
	private static final String WORLD_FILE_EXTENSION = ".world";

	private File worldFile = null;

	// We define an internal HashMap which contains the continent objects from the
	// world import file. It is necessary to save it temporally because of the different
	// id mapping structure in the world import file. It differs from the auto generated
	// ids in the persistence layer. This map is only for internal purposes and will not
	// stored via the persistence layer.
	private HashMap<Integer, Continent> extractedContinents = new HashMap<Integer, Continent>();

	// This temporally map contains the countries from the world import file.
	private HashMap<Integer, Country> extractedCountries = new HashMap<Integer, Country>();

	// The neighbor mapping. Because there is a different mapping in the worlds import file.
	private HashMap<Integer, Vector<Integer>> neighbourMapping = new HashMap<Integer, Vector<Integer>>();
	
	/**
	 * Registers the world import file object.
	 * 
	 * @param worldFile The world import file object (*.world)
	 * @see File
	 * 
	 */
	public WorldFile(File worldFile) {
		this.worldFile = worldFile;
	}

	/**
	 * Parses the world import file and pushs the data into the
	 * world value object attributes.
	 * 
	 * @throws WorldFileNotFoundException If the given "*.world" file was not found.
	 * @throws WorldFileParseException If the world import file is not "well-formed".
	 * @throws WorldFileExtensionException If the file extension is wrong.
	 * @see World 
	 * 
	 */
	public void parse() throws WorldFileExtensionException, WorldFileNotFoundException, WorldFileParseException {
		try {
			// Initial file validation.
			// Is this the correct file type? ...
			this.validate();
			
			Element dataSource = this.open();

			// Data source validation.
			// Is the world import file valid?
			this.validate(dataSource);

			this.extractMeta(dataSource);
			this.extractContinents(dataSource);
			this.extractCountries(dataSource);

		} catch (IOException e) {
			throw new WorldFileNotFoundException(e.getMessage());
		} catch (SAXException e) {
			throw new WorldFileParseException(e.getMessage());
		} catch (ParserConfigurationException e) {
			throw new WorldFileParseException(e.getMessage());
		}
	}

	/**
	 * Uses the world import file internal mapping structure
	 * and ports this to the real world value objects.<br />
	 * <br />
	 * <b>IMPORTANT:</b> Use this method after you've persisted the world
	 * before. After you've persisted the countries for example will
	 * have the generated ids from the persistence layer. Otherwise it
	 * is not possible to create the mapping.<br />
	 * <br />
	 * <b>NOTE:</b> You have to store the world object after creating the country
	 * dependencies to instruct the persistence layer to store this country
	 * relations.
	 * 
	 */
	public void createCountryDependencies() {
		for (Entry<Integer, Vector<Integer>> entry : this.neighbourMapping.entrySet()) {
			Country country = this.extractedCountries.get(entry.getKey());

			for (int neighbourId : entry.getValue()) {
				Country neighbour = this.extractedCountries.get(neighbourId);
				
				this.connectCountries(country, neighbour);
			}
		}
	}
	
	/**
	 * Opens the world import file and returns the document root.
	 * 
	 * @return The world import file root element.
	 * @throws ParserConfigurationException
	 * @throws SAXException Parser engine exception.
	 * @throws IOException File not found.
	 */
	private Element open() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(this.getWorldFile().toURI().toString());
		return doc.getDocumentElement();
	}

	/**
	 * Extracts the meta data from the world import file
	 * and pushs the data into the object attributes.
	 * 
	 * @param dataSource The document root @see WorldFile#open()
	 * @throws WorldFileParseException If the document is not well-formed.
	 * 
	 */
	private void extractMeta(Element dataSource) throws WorldFileParseException {
		// The worlds token
		String node = "token";
		this.setToken(this.extractText(dataSource, node));
		if (this.getToken().isEmpty()) {
			throw new WorldFileParseException(this.worldFile, node);
		}

		// The worlds name
		node = "name";
		this.setName(this.extractText(dataSource, node));
		if (this.getName().isEmpty()) {
			throw new WorldFileParseException(this.worldFile, node);
		}

		// The worlds description
		node = "description";
		this.setDescription(this.extractText(dataSource, node));
		if (this.getDescription().isEmpty()) {
			throw new WorldFileParseException(this.worldFile, node);
		}
		
		// The author
		node = "author";
		this.setAuthor(this.extractText(dataSource, node));
		if (this.getAuthor().isEmpty()) {
			throw new WorldFileParseException(this.worldFile, node);
		}

		// The version
		node = "version";
		this.setVersion(this.extractText(dataSource, node));
		if (this.getVersion().isEmpty()) {
			throw new WorldFileParseException(this.worldFile, node);
		}
	}
	
	/**
	 * Extracts the continent data from the world import file
	 * and saves the data in an temporally hash map.
	 * 
	 * @param dataSource The document root @see WorldFile#open()
	 * @throws WorldFileParseException If the document is not well-formed.
	 * 
	 */
	private void extractContinents(Element dataSource) throws WorldFileParseException {
		NodeList continents = dataSource.getElementsByTagName("continent");
		
		for (int i = 0; i < continents.getLength(); i++) {
			NamedNodeMap node = continents.item(i).getAttributes();
			Continent continent = new Continent();
			continent.setName(node.getNamedItem("name").getNodeValue());

			int id = Integer.parseInt(node.getNamedItem("id").getNodeValue());
			this.extractedContinents.put(id, continent);
		}
		
		if (this.extractedContinents.isEmpty()) {
			throw new WorldFileParseException(MESSAGE_FILE_NOT_WELLFORMED + "Missing 'continents' data.");
		}
	}

	/**
	 * Extracts the countries from the world import file.
	 * The countries will be pushed into the countries vector
	 * World#getCountries()
	 * 
	 * The world import file has an own "country-country" relation
	 * mapping (the id's from the persistence layer differs from the
	 * id's in the world import file).
	 * 
	 * Further the method will extract the country dependencies
	 * and add the correct continent to the country value object.
	 * 
	 * @param dataSource The worlds import file root element
	 * @throws WorldFileParseException If the world import file has the wrong data structure.
	 * 
	 */
	private void extractCountries(Element dataSource) throws WorldFileParseException {
		NodeList dataSourceCountries = dataSource.getElementsByTagName("country");
		
		for (int i = 0; i < dataSourceCountries.getLength(); i++) {
			NamedNodeMap dataSourceCountry = dataSourceCountries.item(i).getAttributes();

			Country country = new Country();
			country.setToken(dataSourceCountry.getNamedItem("token").getNodeValue());
			country.setName(dataSourceCountry.getNamedItem("name").getNodeValue());

			// TODO: Get the color from the file
			country.setColor(ColorTool.fromHexCode(dataSourceCountry.getNamedItem("color").getNodeValue()));

			int continentId = Integer.parseInt(dataSourceCountry.getNamedItem("continent").getNodeValue());
			country.setContinent(this.extractedContinents.get(continentId));

			int id = Integer.parseInt(dataSourceCountry.getNamedItem("id").getNodeValue());
			this.extractedCountries.put(id, country);

			// Extract the neighbours
			String neighbourValue = dataSourceCountry.getNamedItem("neighbours").getNodeValue();
			String[] neighbours = neighbourValue.split(",");
			Vector<Integer> neighbourIds = new Vector<Integer>();

			for (String neighbourId : neighbours) {
				neighbourIds.add(Integer.parseInt(neighbourId.trim()));
			}
			this.neighbourMapping.put(id, neighbourIds);

			this.addCountry(country);
		}

		if (this.extractedCountries.isEmpty()) {
			throw new WorldFileParseException(MESSAGE_FILE_NOT_WELLFORMED + "Missing 'country' data.");
		}
	}

	/**
	 * Common file validation.
	 * 
	 * @throws WorldFileExtensionException
	 */
	private void validate() throws WorldFileExtensionException {
		if (!this.worldFile.getName().endsWith(WORLD_FILE_EXTENSION)) {
			throw new WorldFileExtensionException(this.worldFile, WORLD_FILE_EXTENSION);
		}
	}
	
	/**
	 * Checks if the data sources given data structure
	 * is valid. If it is not valid the method will raise
	 * the WorldFileParseException.
	 *
	 * @throws WorldFileParseException The world import file is not well-formed.
	 *
	 */
	private void validate(Element dataSource) throws WorldFileParseException {
		Node node = dataSource.getElementsByTagName("meta").item(0);
		if (node == null || node.getChildNodes().getLength() <= 0) {
			throw new WorldFileParseException(MESSAGE_FILE_NOT_WELLFORMED + "Missing 'meta' section.");
		}
		
		node = dataSource.getElementsByTagName("continents").item(0);
		if (node == null || node.getChildNodes().getLength() <= 0) {
			throw new WorldFileParseException(MESSAGE_FILE_NOT_WELLFORMED + "Missing 'continents' section.");
		}

		node = dataSource.getElementsByTagName("countries").item(0);
		if (node == null || node.getChildNodes().getLength() <= 0) {
			throw new WorldFileParseException(MESSAGE_FILE_NOT_WELLFORMED + "Missing 'countries' section.");
		}
	}

	/**
	 * Extracts the text from an given xml node.
	 * For example <tag>text</tag>.
	 * 
	 * @param tag The tag name from which this method will extract the text.
	 * @return The tag content.
	 * 
	 */
	private String extractText(Element root, String tag) {
		NodeList nodes = root.getElementsByTagName(tag);
		return (nodes.getLength() > 0) ? nodes.item(0).getTextContent() : "";
	}

	/**
	 * The world import file object.
	 * 
	 * @return The world import file.
	 * 
	 */
	public File getWorldFile() {
		return this.worldFile;
	}
}