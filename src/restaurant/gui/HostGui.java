package restaurant.gui;


import restaurant.CustomerAgent;
import restaurant.WaiterAgent;
import restaurant.HostAgent;
import restaurant.interfaces.Host;

import java.awt.*;

public class HostGui implements Gui {

    private Host agent = null;

    public HostGui(Host host) {
        this.agent = host;
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
