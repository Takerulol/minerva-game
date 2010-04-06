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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.hochschule.bremen.minerva.exceptions.WorldImportException;
import de.hochschule.bremen.minerva.vo.World;

public class WorldImporter {

	private File source = null;
	
	private final String[] sourceContent = {"world.xml"}; //, "map.png", "map-underlay.png"};
	private final String[] xmlContent = {""};
	private final String worldXmlFileName = "world.xml";

	/**
	 * Constructor with file source.
	 * 
	 * @param source - The new world file to import.
	 */
	public WorldImporter(File source) {
		this.source = source;
	}

	/**
	 * Constructor with file source as string.
	 * 
	 * @param source - The absolute path to the new world file.
	 */
	public WorldImporter(String source) {
		this.source = new File(source);
	}

	/**
	 * The import process.
	 * A 'new world file' is a zipped folder with the
	 * following files:
	 * 
	 * <ul>
	 * 	<li>world.xml - The world description file.
	 * 		Contains country and continent definition.</li>
	 *  <li>map.png - The map for the user interface.</li>
	 *  <li>DOCME</li>
	 * </ul>
	 * 
	 * So this method will unzip this folder an parse the 'world.xml'
	 * to create a new world value object which we will write to the
	 * database.
	 * 
	 * @throws WorldImportException - If the 'new world' file is not
	 *                                  valid or if it does not exist etc.
	 */
	public void exec() throws WorldImportException {
		List<File> files = null;
	
		// Unzip the files ...
		try {
			files = unzip();
		} catch (FileNotFoundException e) {
			throw new WorldImportException("The world file '"+this.source+"' does not exist.");
		} catch (IOException e) {
			throw new WorldImportException("An error occured while reading the world file: '"+this.source+"'. Please try again.");
		}

		// Check if the file structure is valid ...
		String missingFile = this.isFormatValid(files);
		if (missingFile != null) {
			throw new WorldImportException("Missing file '"+ missingFile +"' in whe world file: '"+this.source+"'");
		}

		// Parse the xml file. Create the value objects.
		/*World world = this.parseFromWorldXml(this.determineWorldXml(files));
		world.save();
		
		if (world == null) {
			throw new WorldNotValidException("The 'world.xml' is not valid. Please fix the data structure and try again.");
		}*/
		
		// TODO: Write the entries into the database.
		// TODO: Move png files to directories
	}

	/**
	 * Unzip the 'new world' file and return a file list.
	 * 
	 * @return A file list that consists of all files in the
	 *         'new world' file
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private List<File> unzip() throws FileNotFoundException, IOException {
		List<File> files = new ArrayList<File>();
		
		final int BUFFER = 2048;

        ZipFile zipFile = new ZipFile(this.source);
        Enumeration e = zipFile.entries();

        File outputDir = new File(this.source.getAbsolutePath().replace(".", "-"));
        outputDir.mkdir();

        while(e.hasMoreElements()) {
        	int count = 0;
        	byte data[] = new byte[BUFFER];

        	ZipEntry entry = (ZipEntry) e.nextElement();
            BufferedInputStream inputStream = new BufferedInputStream(zipFile.getInputStream(entry));

            File fileEntry = new File(outputDir + File.separator + entry.getName());
            FileOutputStream fos = new FileOutputStream(fileEntry);

            BufferedOutputStream output = new BufferedOutputStream(fos, BUFFER);
            while ((count = inputStream.read(data, 0, BUFFER)) != -1) {
            	output.write(data, 0, count);
            }

            output.flush();
            output.close();
            inputStream.close();

           files.add(fileEntry);
        }

		return files;
	}
	
	/**
	 * Check if the given file list contains all
	 * file that are defined in "this.sourceContent"
	 * 
	 * @param files A file list.
	 * 
	 * @return String with the missing file (name).
	 * 
	 */
	private String isFormatValid(List<File> files) {
		for (String neededFile : this.sourceContent) {
			boolean exist = false;

			for (File file : files) {
				if (neededFile.equals(file.getName())) {
					exist = true;
					break;
				}
			}
			
			if (!exist) {
				return neededFile;
			}
		}
		
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private World parseFromWorldXml() {
		World world = new World();
		
		
		return null;
	}
	
	/**
	 * Determines the 'world.xml' from a given file list.
	 * 
	 * @return The 'world.xml' file object.
	 */
	private File determineWorldXml(List<File> files) {
		for (File file : files) {
			if (file.getName().contains(this.worldXmlFileName)) {
				return file;
			}
		}
		return null;
	}
	
	/**
	 * Sets the (zipped) new world file.
	 * 
	 * @param source - The new world file.
	 */
	public void setSource(File source) {
		this.source = source;
	}

	/**
	 * Returns the (zipped) new world file.
	 * 
	 * @return The new world file object.
	 */
	public File getSource() {
		return this.source;
	}
}
