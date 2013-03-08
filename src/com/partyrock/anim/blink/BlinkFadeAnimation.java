package com.partyrock.anim.blink;

import java.util.ArrayList;
import java.util.EnumSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Shell;

import com.partyrock.LightMaster;
import com.partyrock.anim.ElementAnimation;
import com.partyrock.element.ElementController;
import com.partyrock.element.ElementType;
import com.partyrock.element.blink.BlinkController;
import com.partyrock.settings.Saver;
import com.partyrock.settings.SectionSettings;

/**
 * Fades a blink to a given color
 * 
 * @author Matthew
 * 
 */
public class BlinkFadeAnimation extends ElementAnimation {

    private Color color;

    public BlinkFadeAnimation(LightMaster master, int startTime, String internalID,
            ArrayList<ElementController> blinks, double duration) {
        super(master, startTime, internalID, blinks, duration);
        color = sysColor(SWT.COLOR_RED);
    }

    @Override
    public void setup(Shell window) {
        ColorDialog dialog = new ColorDialog(window);
        dialog.setRGB(color.getRGB());
        dialog.setText("Choose a color for the Blink");
        RGB rgb = dialog.open();

        if (rgb != null) {
            color = new Color(window.getDisplay(), rgb);
        }
    }

    @Override
    public void trigger() {
        for (ElementController blinkController : getElements()) {
            BlinkController blink = (BlinkController) blinkController;
            blink.setColor(color);
        }
    }

    public static EnumSet<ElementType> getSupportedTypes() {
        return EnumSet.of(ElementType.BLINK);
    }

    @Override
    protected void saveSettings(SectionSettings settings) {
        settings.put("color", Saver.saveColor(color));
    }

    @Override
    protected void loadSettings(SectionSettings settings) {
        color = Saver.loadColor(settings.get("color"), this);
    }
}
