package antsimulation.hive.ant;

import antsimulation.Main;
import antsimulation.hive.Hive;
import antsimulation.hive.ant.pheromone.Pheromone;
import antsimulation.world.Displayable;
import antsimulation.world.Locatable;
import antsimulation.world.Updatable;
import antsimulation.world.grid.Node;
import antsimulation.world.objects.food.FoodChunk;
import org.mini2Dx.gdx.math.Vector2;
import processing.core.PConstants;
import processing.core.PImage;

import java.util.Optional;

import static java.lang.Math.max;

public class Ant implements Updatable, Displayable, Locatable {

    static final float TURN_SPEED = 120f;

    private static final float RADIUS = 6f;
    private static final PImage ANT_TEXTURE = Main.getApp().loadImage("ant.png");
    private static final PImage ANT_CARRYING_FOOD_TEXTURE = Main.getApp().loadImage("ant_carrying_food.png");

    private static final float PHEROMONE_COOLDOWN = 0.22f;

    private final float movementSpeed = 35f;
    private final Hive hive;

    private final TurningStrategy turningStrategy = new DefaultTurningStrategy(
            this,
            new DefaultFoodCarryingStrategy(this),
            new DefaultFoodSeekingStrategy(this)
    );

    private Vector2 position;
    private final Vector2 currentDirection = new Vector2().setToRandomDirection().setLength(movementSpeed);
    private Vector2 desiredDirection = new Vector2().setToRandomDirection().setLength(movementSpeed);

    private float timeUntilPheromoneDeposit;
    private float awayFromHomeTime = 0f;

    private FoodChunk carriedFood;

    public Ant(Vector2 startingLocation, Hive hive) {
        this.position = startingLocation.cpy();
        this.hive = hive;
        this.timeUntilPheromoneDeposit = varyCooldown(PHEROMONE_COOLDOWN);
    }

    @Override
    public void update(float dt) {
        if (carryingFood()) {
            attemptToDepositPheromone(Pheromone.Type.FOOD, dt);
            attemptToGiveHiveFoodChunk();
        } else {
            checkForFood();
            attemptToDepositPheromone(Pheromone.Type.HOME, dt);
        }
        desiredDirection = turningStrategy.getDesiredDirection(dt);
        turn(dt);
        move(dt);
        rescueIfStuck();
        maskPheromones();
        reduceCooldowns(dt);
    }

    private void attemptToGiveHiveFoodChunk() {
        if (Vector2.dst2(position.x, position.y, hive.getLocation().x, hive.getLocation().y) < hive.getRadiusSquared()) {
            giveFoodChunk(hive);
        }
    }

    private void giveFoodChunk(Hive hive) {
        hive.receiveFoodChunk(carriedFood);
        awayFromHomeTime = 0;
        carriedFood = null;
        turnAround();
    }

    private void reduceCooldowns(float dt) {
        timeUntilPheromoneDeposit = max(0, timeUntilPheromoneDeposit - dt);
    }

    private void maskPheromones() {
        getNode().maskPheromone(Pheromone.Type.FOOD);
    }

    private void attemptToDepositPheromone(Pheromone.Type pheromoneType, float dt) {
        if (timeUntilPheromoneDeposit == 0) {
            // Tweaked version of Jean Tampon's code snippet in their ant simulator.
            // This makes sure ants that have travelled for longer without making it back to the hive
            // (who are presumably also farther away from the hive) deposit less pheromones.
            final float coefficient = 0.01f;
            final float intensity = (float) (40f * Math.exp(-coefficient * awayFromHomeTime));

            getNode().depositPheromone(pheromoneType, intensity);
            timeUntilPheromoneDeposit = varyCooldown(PHEROMONE_COOLDOWN);
            awayFromHomeTime += dt;
        }
    }

    private void rescueIfStuck() {
        if (Main.getWorld().isObstacle(position)) returnToHive();
    }

    private void returnToHive() {
        position = hive.getLocation().cpy();
    }

    private float varyCooldown(float initialCooldown) {
        final float rangeStartFactor = 0.7f;
        final float rangeEndFactor = 1.15f;

        final float randomCoolDownFactor = Main.getApp().random(rangeStartFactor, rangeEndFactor);
        return randomCoolDownFactor * initialCooldown;
    }

    private void turn(float dt) {
        final float turnDelta = TURN_SPEED * dt;
        final int modifier = (currentDirection.dot(desiredDirection.cpy().rotate90(1)) > 0) ? -1 : 1;

        currentDirection.rotateDeg(modifier * turnDelta);
    }

    private void move(float dt) {
        Vector2 attemptedPos = this.position.cpy().add(currentDirection.cpy().setLength(currentDirection.len() * dt));

        if (Main.getWorld().inBounds(attemptedPos) && !Main.getWorld().isObstacle(attemptedPos)) position = attemptedPos;
        else {
            turnAround();
        }
    }

    private void checkForFood() {
        Optional<FoodChunk> foodChunk = getNode().giveFood();
        foodChunk.ifPresent(this::takeFood);
    }

    private void takeFood(FoodChunk foodChunk) {
        carriedFood = foodChunk;
        awayFromHomeTime = 0;
        turnAround();
    }

    private void turnAround() {
        desiredDirection.rotateDeg(180);
        currentDirection.rotateDeg(180);
    }

    Node getNode() {
        return Main.getWorld().getGrid().getNodeAt(getLocation());
    }

    @Override
    public void display() {
        if (Main.getSettingsHandler().isAntsVisible()) drawAnt();
    }

    private void drawAnt() {
        Main.getApp().noStroke();
        Main.getApp().pushMatrix();

        Main.getApp().translate(position.x, position.y);
        Main.getApp().rotate(currentDirection.angleRad());

        Main.getApp().beginShape();
        if (carryingFood()) {
            Main.getApp().texture(ANT_CARRYING_FOOD_TEXTURE);
        } else {
            Main.getApp().texture(ANT_TEXTURE);
        }
        Main.getApp().vertex(-RADIUS, -RADIUS, 0, 0);
        Main.getApp().vertex(+RADIUS, -RADIUS, ANT_TEXTURE.width, 0);
        Main.getApp().vertex(+RADIUS, +RADIUS, ANT_TEXTURE.width, ANT_TEXTURE.height);
        Main.getApp().vertex(-RADIUS, +RADIUS, 0, ANT_TEXTURE.height);
        Main.getApp().endShape(PConstants.CLOSE);

        Main.getApp().popMatrix();
    }

    @Override
    public Vector2 getLocation() {
        return position.cpy();
    }

    boolean carryingFood() {
        return (carriedFood != null);
    }

    Vector2 getDesiredDirection() {
        return desiredDirection.cpy();
    }

    public Node getSensorNode() {
        final Vector2 sensorPosition = position.cpy().add(currentDirection.cpy().setLength(4 * RADIUS));
        if (Main.getWorld().inBounds(sensorPosition)) return Main.getWorld().getGrid().getNodeAt(sensorPosition);
        return getNode();
    }
}
