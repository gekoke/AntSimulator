package antsimulation.hive.ant;

import org.mini2Dx.gdx.math.Vector2;

interface FoodCarryingStrategy {

    Vector2 getDesiredDirection(float dt);
}
