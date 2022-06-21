package antsimulation.world.grid;

import antsimulation.hive.ant.pheromone.FoodPheromone;
import antsimulation.hive.ant.pheromone.HomePheromone;
import antsimulation.hive.ant.pheromone.Pheromone;
import antsimulation.world.Displayable;
import antsimulation.world.Locatable;
import antsimulation.world.Updatable;
import antsimulation.world.objects.food.FoodChunk;
import antsimulation.world.objects.food.FoodSource;
import antsimulation.world.objects.food.Wall;
import org.mini2Dx.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Node implements Updatable, Displayable, Locatable {

    private final int xIndex;
    private final int yIndex;
    private final double width;
    private final double height;

    private final Map<Pheromone.Type, Pheromone> pheromones = new HashMap<>();
    private final Vector2 position;
    private final FoodSource foodSource;
    private final Wall wall;

    Node(int xIndex, int yIndex, double width, double height) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.width = width;
        this.height = height;
        this.position = new Vector2((float) ((this.xIndex + 0.5) * width), (float) ((this.yIndex + 0.5f) * height));
        this.foodSource = new FoodSource(this);
        this.wall = new Wall(this);
        pheromones.put(Pheromone.Type.HOME, new HomePheromone(this));
        pheromones.put(Pheromone.Type.FOOD, new FoodPheromone(this));
    }

    @Override
    public void update(float dt) {
        for (Pheromone pheromone : pheromones.values()) pheromone.update(dt);
    }

    @Override
    public void display() {
        for (Pheromone pheromone : pheromones.values()) pheromone.display();
        foodSource.display();
        wall.display();
    }

    public Optional<FoodChunk> giveFood() {
        return foodSource.takeChunk();
    }

    public void replenishFood() {
        foodSource.replenish();
        wall.setPresent(false);
        pheromones.get(Pheromone.Type.FOOD).setIntensity(Float.MAX_VALUE);
    }

    public double getWidth() {
        return width;
    }

    @Override
    public Vector2 getLocation() {
        return position.cpy();
    }

    public int getYIndex() {
        return yIndex;
    }

    public int getXIndex() {
        return xIndex;
    }

    public float getPheromoneStrength(Pheromone.Type type) {
        if (wall.isPresent()) return 0;
        return pheromones.get(type).getStrength();
    }

    public void depositPheromone(Pheromone.Type pheromoneType, float intensity) {
        pheromones.get(pheromoneType).addIntensity(intensity);
    }

    public void maskPheromone(Pheromone.Type pheromoneType) {
        pheromones.get(pheromoneType).scaleIntensity(0.95f);
    }

    public void handleFoodSourceDepletion() {
        pheromones.get(Pheromone.Type.FOOD).setIntensity(0);
    }

    public boolean isObstacle() {
        return (wall.isPresent());
    }

    public void addWall() {
        wall.setPresent(true);
    }
}
