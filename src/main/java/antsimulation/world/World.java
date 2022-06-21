package antsimulation.world;

import antsimulation.Main;
import antsimulation.world.grid.Grid;
import antsimulation.world.spawner.Spawner;
import org.mini2Dx.gdx.math.Vector2;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class World implements Updatable, Displayable {

    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;

    private final Grid grid = new Grid(this, 320, 180);
    private final Spawner spawner = new Spawner(this);

    private final Set<Updatable> updatables = ConcurrentHashMap.newKeySet();
    private final Set<Displayable> displayables = ConcurrentHashMap.newKeySet();

    @Override
    public void update(float dt) {
        grid.update(dt);
        for (Updatable updatable : updatables) updatable.update(dt);
    }

    @Override
    public void display() {
        displayGround();
        grid.display();
        for (Displayable displayable : displayables) displayable.display();
    }

    private void displayGround() {
        Main.getApp().background(80, 80, 80);
    }

    public boolean inBounds(Vector2 position) {
        return (
            position.x >= 0
            && position.x <= WIDTH
            && position.y >= 0
            && position.y <= HEIGHT
        );
    }

    public boolean isObstacle(Vector2 position) {
        if (!inBounds(position)) return false;
        return grid.getNodeAt(position).isObstacle();
    }

    public Vector2 getRandomLocation() {
        float x = Main.getApp().random(0, WIDTH);
        float y = Main.getApp().random(0, HEIGHT);
        return new Vector2(x, y);
    }

    public Vector2 getRandomLocationAwayFromEdgeBy(float distance) {
        float x = Main.getApp().random(distance, WIDTH - distance);
        float y = Main.getApp().random(distance, HEIGHT - distance);
        return new Vector2(x, y);
    }

    public int getWidth() {
       return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public Grid getGrid() {
        return grid;
    }

    public void addEntity(Object entity) {
        if (entity instanceof Updatable) updatables.add((Updatable) entity);
        if (entity instanceof Displayable) displayables.add((Displayable) entity);
    }

    public Spawner getSpawner() {
        return spawner;
    }
}
