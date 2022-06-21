package antsimulation.world.spawner;

import antsimulation.Main;
import antsimulation.hive.Hive;
import antsimulation.world.World;

public class Spawner {

    private final World world;

    public Spawner(World world) {
        this.world = world;
    }

    public void spawnHive(int amountOfAnts) {
        Hive newHive = new Hive(world.getRandomLocationAwayFromEdgeBy(Main.getWorld().getWidth() / 10f), amountOfAnts);
        world.addEntity(newHive);
    }
}
