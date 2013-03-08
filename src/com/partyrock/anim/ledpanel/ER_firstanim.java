package com.partyrock.anim.ledpanel;

import java.util.ArrayList;
import java.util.EnumSet;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Shell;

import com.partyrock.LightMaster;
import com.partyrock.anim.ElementAnimation;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementType;
import com.partyrock.element.led.LEDPanelController;
import com.partyrock.settings.Saver;
import com.partyrock.settings.SectionSettings;

/**
 * This is a basic animation that will wipe an LED panel from top to bottom with a given color
 * 
 * @author Ehsan
 * 
 */
public class ER_firstanim extends ElementAnimation {

    // The color to fade to
    private Color color;
    private Color black = sysColor(0, 0, 0);

    // The number of rows we've faded
    private int fadedRows = -1;

    public ER_firstanim(LightMaster master, int startTime, String internalID,
            ArrayList<ElementController> elementList, double duration) {
        super(master, startTime, internalID, elementList, duration);

        // Tell the animation system to call our animation's step() method repeatedly so we can animate over time
        needsIncrements();

        // Set a new default color of white
        color = sysColor(255, 255, 255);
    }

    /**
     * This method is called once when the animation is created. You can use it to get user configurable settings like a
     * Color (as we do in this case)
     */
    @Override
    public void setup(Shell window) {
    	
    }

    public void trigger()
    {
    	for (ElementController controller : getElements())
    	{
    		LEDPanelController panel = (LEDPanelController) controller;
    		for (int r = 0; r<panel.getPanelHeight(); r++)
    		{
    			for (int c = 0; c< panel.getPanelWidth(); c++)
    			{
    				panel.setColor(r, c, black);
    			}
    		}
    	}
    	
    }
    /**
     * Since we're doing something over time, we need to implement increment()
     * 
     * @param percentage The percentage of the way through the animation we are. This is between 0 and 1
     */
    public void increment(double percentage) {
        int newFadedRows = fadedRows;

        // For every element we're given
        for (ElementController controller : getElements()) {
            // We only put LEDS in our getSupportedTypes(), so that's all we're going to get.
            LEDPanelController panel = (LEDPanelController) controller;

            // How many rows should be on based on our percentage?
            int rowsOn = (int) (percentage * panel.getPanelHeight()*2);

            // So if we haven't already done this
            if (fadedRows < rowsOn) {

                // The for every row we haven't done
            	if (rowsOn < 16)
            	{
	                for (int r = fadedRows + 1; r <= rowsOn && r < panel.getPanelHeight(); r++) 
	                {
	                	
	                    // and every column in that row
		                    for (int c = 0; c < (panel.getPanelWidth()/2); c++) 
		                    {
		                    	if (c%2 == 0) color = sysColor(200, 0, 200);
		                    	else color = sysColor(0, 200, 0);
		                        panel.setColor(r, c, color);
		                    }
		                    for (int k = (panel.getPanelWidth()/2); k < panel.getPanelWidth(); k++)
		                    {
		                    	if (k%2 == 0) color = sysColor(255, 10, 100);
		                    	else color = sysColor(88, 110, 190);
		                    	panel.setColor(r, k, color);
		                    }
	                }
                    
                }
            	else 
            	{
	                for (int r = fadedRows - 15; r <= rowsOn-15 && r < panel.getPanelHeight(); r++) 
	                {
	                
	                    // and every column in that row
	                    for (int c = 0; c < (panel.getPanelWidth()/2); c++) 
	                    {
	                    	if (c%2 == 0) color = sysColor(0, 0, 255);
	                    	else color = sysColor(51, 255, 100);
	                        panel.setColor(r, c, color);
	                    }
	                    for (int k = (panel.getPanelWidth()/2); k < panel.getPanelWidth(); k++)
	                    {
	                    	if (k%2 == 0) color = sysColor(223, 0, 255);
	                    	else color = sysColor(102, 0, 255);
	                    	panel.setColor(r, k, color);
	                    }
	                }
            	}
                newFadedRows = rowsOn;
            }
        }

        fadedRows = newFadedRows;

    }

    /**
     * This returns the kind of elements we support with this animation. In this case, it's simply LED Panels
     */
    public static EnumSet<ElementType> getSupportedTypes() {
        return EnumSet.of(ElementType.LEDS);
    }

    /**
     * Saves the variables to a file so we can reconstruct this Animation object the next time we restart the software
     */
    protected void saveSettings(SectionSettings settings) {
        settings.put("color", Saver.saveColor(color));
    }

    /**
     * Loads the variables saved in saveSettings() to make this animation match how it was before
     */
    protected void loadSettings(SectionSettings settings) {
        color = Saver.loadColor(settings.get("color"), this);
    }

}