package antsimulation.threading;

import antsimulation.world.World;

import java.util.HashSet;
import java.util.Set;

public class ThreadPool {

    private final static float TIME_LAPSE_SPEED_SCALE = 4;

    private final double tickRate;
    private final World world;

    private boolean running = false;
    private boolean fast = false;
    private Set<LogicThread> logicThreads = new HashSet<>();

    public ThreadPool(World world, int tickRateInHz) {
        this.world = world;
        this.tickRate = 1000000000.0 / tickRateInHz;  // To nanoseconds.
    }

    public void start() {
        logicThreads = new HashSet<>();
        spawnThreads();
        running = true;
    }

    private void spawnThreads() {
        logicThreads.add(new LogicThread(world, tickRate));
        for (LogicThread logicThread : logicThreads) {
            logicThread.setSimulationSpeedScale((fast) ? TIME_LAPSE_SPEED_SCALE : 1);
            logicThread.start();
        }
    }

    public void stop() {
        if (!running) throw new IllegalStateException("Can not stop a pool that is not running.");

        for (LogicThread logicThread : logicThreads) logicThread.shutDown();
        running = false;
    }

    public void toggleRunning() {
        if (running) stop();
        else start();
    }

    public void toggleTickRate() {
         for (LogicThread logicThread : logicThreads) {
             logicThread.setSimulationSpeedScale((fast) ? 1 : TIME_LAPSE_SPEED_SCALE);
         }
         fast = !fast;
    }
}
