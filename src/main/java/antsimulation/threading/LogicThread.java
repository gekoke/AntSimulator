package antsimulation.threading;

import antsimulation.world.World;

public class LogicThread extends Thread {

    private final World world;
    private final double tickRate;
    private final float tickRateInSeconds;

    private boolean running = false;
    private float simulationSpeedScale = 1;

    LogicThread(World world, double tickRate) {
        this.world = world;
        this.tickRate = tickRate;
        this.tickRateInSeconds = (float) (tickRate / 1000000000.0);
    }

    @Override
    public void run() {
        running = true;

        double delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / tickRate;
            lastTime = now;

            while (delta >= 1) {
                world.update(simulationSpeedScale * tickRateInSeconds);
                delta--;
            }
        }
    }

    void shutDown() {
        running = false;
    }

    void setSimulationSpeedScale(float factor) {
        this.simulationSpeedScale = factor;
    }
}
