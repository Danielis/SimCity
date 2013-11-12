package restaurant.gui;


import restaurant.CustomerAgent;
import restaurant.WaiterAgent;
import restaurant.HostAgent;

import java.awt.*;

public class HostGui implements Gui {

    private HostAgent agent = null;

    public HostGui(HostAgent agent) {
        this.agent = agent;
    }

    public void updatePosition() {
    }

    public void draw(Graphics2D g) {/*
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);*/
    }

    public boolean isPresent() {
        return true;
    }
}
