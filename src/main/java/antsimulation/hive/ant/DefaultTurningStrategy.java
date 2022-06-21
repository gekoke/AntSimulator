package antsimulation.hive.ant;

import org.mini2Dx.gdx.math.Vector2;

class DefaultTurningStrategy implements TurningStrategy {

    private final Ant ant;
    private final FoodCarryingStrategy foodCarryingStrategy;
    private final FoodSeekingStrategy foodSeekingStrategy;

    DefaultTurningStrategy(Ant ant, FoodCarryingStrategy foodCarryingStrategy, FoodSeekingStrategy wanderingStrategy) {
        this.ant = ant;
        this.foodCarryingStrategy = foodCarryingStrategy;
        this.foodSeekingStrategy = wanderingStrategy;
    }

    @Override
    public Vector2 getDesiredDirection(float dt) {
        if (ant.carryingFood()) return foodCarryingStrategy.getDesiredDirection(dt);
        else return foodSeekingStrategy.getDesiredDirection(dt);
    }
}
