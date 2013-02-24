package com.partyrock.location;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import com.partyrock.LightMaster;
import com.partyrock.element.ElementController;
import com.partyrock.element.blink.BlinkController;
import com.partyrock.settings.PersistentSettings;
import com.partyrock.settings.SectionSettings;
import com.partyrock.tools.PartyToolkit;

/**
 * Manages importing and saving the location file
 * @author Matthew
 * 
 */
public class LightLocationManager {
	private LightMaster master;
	private PersistentSettings location;

	public LightLocationManager(LightMaster master) {
		this.master = master;
	}

	/**
	 * Returns the location file, or null if it doesn't exist
	 */
	public PersistentSettings getLocation() {
		return location;
	}

	/**
	 * Set the location to be this file and save it
	 * @param f The new file to save to
	 */
	public void saveLocationToFile(File f) {
		location = new PersistentSettings(f);
		saveLocationFile();
	}

	/**
	 * Save the location file
	 */
	public void saveLocationFile() {
		// Make sure that the settings file contains the latest data
		updateElementsInSettings();

		try {
			location.save();
		} catch (IOException e) {
			System.err.println("Error writing location file!");
			e.printStackTrace();
		}
	}

	/**
	 * Have all elements save their data in their respective SectionSettings
	 * objects
	 */
	public void updateElementsInSettings() {
		SectionSettings elementSettings = location.getSettingsForSection("elements");
		ArrayList<ElementController> elements = master.getElements();
		for (int a = 0; a < elements.size(); a++) {
			ElementController element = elements.get(a);

			// Add the element to the list of all elements
			elementSettings.put("element" + a, element.getInternalID());

			// Save the element's data
			element.saveData(location.getSettingsForSection(element.getInternalID()));
		}
	}

	/**
	 * Loads a location file from a given file, and loads the elements inside
	 * the LightMaster
	 * @param f the file to load from
	 */
	public void loadLocation(File f) {
		if (location != null) {
			boolean save = PartyToolkit.openQuestion(master.getWindowManager().getMain().getShell(), "Would you like to save the existing location?", "Save Location File?");
			if (save) {
				saveLocationFile();
			}
		}

		location = new PersistentSettings(f);

		// Load the special elements section
		SectionSettings elements = location.getSettingsForSection("elements");
		Set<String> elementNames = elements.keySet();

		for (String element : elementNames) {
			addElementFromSettings(location.getSettingsForSection(elements.get(element)));
		}

		// Update the element list in any GUI windows that are open
		master.getWindowManager().updateElements();
	}

	/**
	 * Imports an element from the settings file
	 * @param settings The specific details about an element
	 */
	public void addElementFromSettings(SectionSettings settings) {
		String elementName = settings.getSectionName();
		// For an element, the first two characters describe the element type
		String elementType = elementName.substring(0, 2);

		ElementController controller = null;
		String name = settings.get("name");
		String id = settings.get("id");
		String internalID = elementName;

		// Load your element type here
		if (elementType.equals("bl")) {
			controller = new BlinkController(master, internalID, name, id);
		}

		if (controller != null) {
			master.addElement(controller);
		} else {
			System.err.println("Controller not constructed while loading location: " + internalID);
		}
	}

}