package com.partyrock.element.led;

import com.partyrock.element.ElementExecutor;

public class LEDPanelExecutor extends ElementExecutor {
    @SuppressWarnings("unused")
    private LEDPanelController controller;

    public LEDPanelExecutor(LEDPanelController controller) {
        super(controller);
        this.controller = controller;
    }

    public void setColor(int r, int c, int red, int green, int blue) {
        // TODO: Implement me :)
    }
}
