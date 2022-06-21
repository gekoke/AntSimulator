package antsimulation.hive.ant;

import org.mini2Dx.gdx.math.Vector2;

interface FoodSeekingStrategy {

    Vector2 getDesiredDirection(float dt);
}
