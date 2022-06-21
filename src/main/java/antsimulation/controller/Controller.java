package antsimulation.controller;

import antsimulation.Main;
import antsimulation.world.World;
import antsimulation.world.grid.Node;
import org.mini2Dx.gdx.math.Vector2;

public class Controller {

    private final World world;

    public Controller(World world) {
        this.world = world;
    }

    public void handleMouseDrag() {
        Node targetNode = world.getGrid().getNodeAt(new Vector2(Main.getApp().mouseX, Main.getApp().mouseY));

        if (!Main.getSettingsHandler().isWallModeActive()) targetNode.replenishFood();
        else targetNode.addWall();
    }

    public void handleKeyPress() {
        final char key = Main.getApp().key;

        switch (key) {
            case 'a':
                handleAntVisibilityToggle();
                break;
            case 's':
                handleTimeScale();
                break;
            case 'w':
                handleWallToggle();
                break;
            case 'f':
                handlePheromoneToggle();
                break;
            case 'p':
                handlePause();
                break;
            default:
                break;
        }
    }

    private void handleAntVisibilityToggle() {
        Main.getSettingsHandler().toggleAntVisibility();
    }

    private void handlePause() {
        Main.getLogicPool().toggleRunning();
    }

    private void handleTimeScale() {
        Main.getLogicPool().toggleTickRate();
    }

    private void handlePheromoneToggle() {
        Main.getSettingsHandler().togglePheromoneVisibility();
    }

    private void handleWallToggle() {
        Main.getSettingsHandler().toggleWallMode();
    }
}
