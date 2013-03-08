package com.partyrock.gui.music;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import com.partyrock.LightMaster;
import com.partyrock.config.PartyConstants;
import com.partyrock.gui.LightWindow;
import com.partyrock.gui.select.MarkRenderer;

/**
 * Handles rendering of the music bar
 * 
 * @author Matthew
 * 
 */
public class MusicRenderer {
    private LightWindow main;
    private LightMaster master;

    public MusicRenderer(LightWindow window) {
        this.main = window;
        this.master = window.getMaster();
    }

    /**
     * Renders a music bar on the given GC based on where the music is. Assumes that it's given a GC that has the first
     * ELEMENT_NAME_COLUMN_SIZE pixels for the element name, and the next pixels are all for the show
     * 
     */
    public void renderMusic(GC gc, Rectangle bounds, boolean triangles) {
        gc.setForeground(main.getDisplay().getSystemColor(SWT.COLOR_GREEN));
        gc.setBackground(main.getDisplay().getSystemColor(SWT.COLOR_GREEN));
        double currentTime = master.getShowManager().getCurrentTime();

        // The music hasn't started
        if (currentTime == -1) {
            return;
        }

        MarkRenderer.drawMarker(gc,
                (int) (PartyConstants.ELEMENT_NAME_COLUMN_SIZE + (currentTime * PartyConstants.PIXELS_PER_SECOND)),
                bounds.height, triangles);
    }

}
