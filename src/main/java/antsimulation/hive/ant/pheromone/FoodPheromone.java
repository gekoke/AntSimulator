package antsimulation.hive.ant.pheromone;

import antsimulation.Main;
import antsimulation.world.grid.Node;
import processing.core.PConstants;

public class FoodPheromone extends Pheromone {

    private static final float MAX_LIFE_TIME = 5f;

    public FoodPheromone(Node parent) {
        super(parent, MAX_LIFE_TIME, Type.FOOD);
    }

    @Override
    protected void onUpdate() {
    }

    protected void onDisplay() {
        Main.getApp().noStroke();
        Main.getApp().fill(0, 240, 0, 255 * (intensity / MAX_LIFE_TIME));
        Main.getApp().rectMode(PConstants.CENTER);
        Main.getApp().square(parent.getLocation().x, parent.getLocation().y, radius);
    }
}
