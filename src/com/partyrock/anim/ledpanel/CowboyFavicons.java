package com.partyrock.anim.ledpanel;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

import net.sf.image4j.codec.ico.ICODecoder;

import org.eclipse.swt.widgets.Shell;

import com.partyrock.LightMaster;
import com.partyrock.anim.ElementAnimation;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementType;
import com.partyrock.element.led.LEDPanelController;
import com.partyrock.settings.Saver;
import com.partyrock.settings.SectionSettings;
import com.partyrock.tools.PartyToolkit;
import com.partyrock.tools.net.NetManager;

public class CowboyFavicons extends ElementAnimation {

    private BufferedImage favicon;
    private File file;
    BufferedImage [] images;

    public CowboyFavicons(LightMaster master, int startTime, String internalID,
            ArrayList<ElementController> panels, double duration) {
        super(master, startTime, internalID, panels, duration);
        favicon = null;
        
        needsIncrements();
    }

    /**
     * Called when the animation is run
     */
    @Override
    public void trigger() {
        for (ElementController element : getElements()) {
            LEDPanelController panel = (LEDPanelController) element;
            
            for (int r = 0; r < panel.getPanelHeight(); r++) {
                for (int c = 0; c < panel.getPanelWidth(); c++) {
                    int color = favicon.getRGB(c, r);
                    panel.setColor(r, c, (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);
                }
            }
            
        }
    }
    
    public void increment(double percentage) {
        // For every element we're given
        for (ElementController controller : getElements()) {
            // We only put LEDS in our getSupportedTypes(), so that's all we're going to get.
            LEDPanelController panel = (LEDPanelController) controller;

            // How many rows should be on based on our percentage?
            int rowsOn = (int) (percentage*(images.length));
         //   if(rowsOn%75 == 0)
           // {
            	System.out.println(rowsOn);
            if(rowsOn<images.length)
            {
            // So if we haven't already done this
            favicon = images[rowsOn];
            //System.out.println(rowsOn);
            for (int r = 0; r < panel.getPanelHeight(); r++) {
                for (int c = 0; c < panel.getPanelWidth(); c++) {
                    int color = favicon.getRGB(c, r);
                    panel.setColor(r, c, (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);
             //   }
            }
            }}
            
        }


    }


    @Override
    public void setup(Shell window) {
    	downloadFavicon();
    }

    private void downloadFavicon() 
    {
        // Download the favicon to the computer
        //file = new File("Party Rock Icons/Cowboy 1.ico");
        File folder = new File("Party Rock Icons/Cowboy Favicons");
        File [] files = folder.listFiles();
        images = new BufferedImage[files.length];
        
        for (int i = 0; i < files.length; i++)
	    {
        	
        	file = new File(files[i].getPath());
        	//System.out.println(file.exists());
	        // Read it in as a BufferedImage
	        try {
	            images[i] = ICODecoder.read(file).get(0);
//	            System.out.println(images[i].getHeight());
	            if (images[i].getHeight() != 16 || images[i].getWidth() != 16) {
	            	
	                images[i] = (BufferedImage) images[i].getScaledInstance(16, 16, Image.SCALE_SMOOTH);
	            }
	        } catch (IOException e) {
	            System.out.println("Error reading image file");
	            e.printStackTrace();
	        }
	        favicon = images[i];
	    }
	        // Delete the file
	       // folder.delete();
	    
	}

    /**
     * Returns the types of elements this animation supports
     */
    public static EnumSet<ElementType> getSupportedTypes() {
        return EnumSet.of(ElementType.LEDS);
    }

    @Override
    protected void saveSettings(SectionSettings settings) {
    	settings.put("file", Saver.saveLocalFile(file));
    }

    @Override
    protected void loadSettings(SectionSettings settings) {
    	file = Saver.loadLocalFile(settings.get("file"), this);
    	
    	downloadFavicon();
    }

}
