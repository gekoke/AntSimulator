package antsimulation.hive;

import antsimulation.Main;
import antsimulation.hive.ant.Ant;
import antsimulation.hive.ant.pheromone.Pheromone;
import antsimulation.world.Displayable;
import antsimulation.world.Locatable;
import antsimulation.world.Updatable;
import antsimulation.world.grid.Grid;
import antsimulation.world.grid.Node;
import antsimulation.world.objects.food.FoodChunk;
import org.mini2Dx.gdx.math.Vector2;

import java.util.List;

public class Hive implements Displayable, Locatable, Updatable {

    private static final float RADIUS = 16;
    private static final float RADIUS_SQUARED = RADIUS * RADIUS;

    private final Vector2 location;

    private int storedFoodChunks = 0;

    public Hive(Vector2 location, int initialAntAmount) {
        this.location = location;
        spawnAnts(initialAntAmount);
    }

    public void spawnAnts(int amount) {
        for (int i = 0; i < amount; i++) spawnAnt();
    }

    private void spawnAnt() {
        Vector2 spawnLocation = location.cpy().add(new Vector2().setToRandomDirection().setLength(Main.getApp().random(0, RADIUS)));
        Ant newAnt = new Ant(spawnLocation, this);

        Main.getWorld().addEntity(newAnt);
    }

    public void update(float dt) {
        releaseHomePheromones();
    }

    private void releaseHomePheromones() {
        Grid worldGrid = Main.getWorld().getGrid();
        List<Node> homeNodes = worldGrid.getNodesInSquare(
                worldGrid.getNodeAt(location),
                Math.max(1, ((int) (worldGrid.getNodeWidth() / (2 * RADIUS))))
        );

        for (Node node : homeNodes) node.depositPheromone(Pheromone.Type.HOME, Float.MAX_VALUE);
    }

    public float getRadiusSquared() {
        return RADIUS_SQUARED;
    }

    @Override
    public Vector2 getLocation() {
        return location;
    }

    public void receiveFoodChunk(FoodChunk foodChunk) {
        storedFoodChunks++;
    }

    @Override
    public void display() {
        displayHive();
        displayCollectedFoodCount();
    }

    private void displayHive() {
        Main.getApp().strokeWeight(2);
        Main.getApp().stroke(35, 25, 3);
        Main.getApp().fill(40, 30, 5);
        Main.getApp().circle(location.x, location.y, 2 * RADIUS);
    }

    private void displayCollectedFoodCount() {
        Main.getApp().fill(255);
        Main.getApp().text(storedFoodChunks, location.x - RADIUS, location.y);
    }
}
