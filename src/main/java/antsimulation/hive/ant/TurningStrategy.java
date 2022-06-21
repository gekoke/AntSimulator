package antsimulation.hive.ant;

import org.mini2Dx.gdx.math.Vector2;

public interface TurningStrategy {

    Vector2 getDesiredDirection(float dt);
}
