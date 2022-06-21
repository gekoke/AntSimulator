package antsimulation.hive.ant;

import antsimulation.Main;
import antsimulation.hive.ant.pheromone.Pheromone;
import antsimulation.world.grid.Node;
import org.mini2Dx.gdx.math.Vector2;

class DefaultFoodSeekingStrategy extends PheromoneSeekingStrategy implements FoodSeekingStrategy {

    private static final float DECISION_COOLDOWN = 0.05f;
    private static final float MAX_DIRECTION_CHANGING_ANGLE = 25;
    private static final int QUERY_AREA_WIDTH = 5;
    private static final float RANDOMNESS_COEFFICIENT = 0.15f;

    private final Ant ant;

    private float cooldown;

    DefaultFoodSeekingStrategy(Ant ant) {
        this.ant = ant;
    }

    @Override
    public Vector2 getDesiredDirection(float dt) {
        cooldown = Math.max(0, cooldown - dt);

        if (cooldown == 0) {
            cooldown = DECISION_COOLDOWN;

            Node attractiveNode = getMostAttractiveNodeByPheromoneType(ant, QUERY_AREA_WIDTH, Pheromone.Type.FOOD, RANDOMNESS_COEFFICIENT);
            if (attractiveNode.getPheromoneStrength(Pheromone.Type.FOOD) == 0) return calculateRandomWanderDirection();
            return attractiveNode.getLocation().sub(ant.getLocation());
        }
        return ant.getDesiredDirection();
    }

    private Vector2 calculateRandomWanderDirection() {
        return ant.getDesiredDirection().rotateDeg(Main.getApp().random(-MAX_DIRECTION_CHANGING_ANGLE, MAX_DIRECTION_CHANGING_ANGLE));
    }
}
