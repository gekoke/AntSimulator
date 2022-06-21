package antsimulation.world.objects.food;

import antsimulation.Main;
import antsimulation.world.Displayable;
import antsimulation.world.grid.Node;

public class Wall implements Displayable {

    private final Node parent;
    private boolean isPresent = false;

    public Wall(Node parent) {
        this.parent = parent;
    }

    @Override
    public void display() {
        if (isPresent) {
            Main.getApp().noStroke();
            Main.getApp().fill(30, 30, 30);
            Main.getApp().square(parent.getLocation().x, parent.getLocation().y, (float) parent.getWidth());
        }
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        this.isPresent = present;
    }
}
