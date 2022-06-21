package antsimulation.settingshandler;

public class SettingsHandler {

    private boolean pheromonesVisible = true;
    private boolean wallMode = false;
    private boolean antsVisible = true;

    public void togglePheromoneVisibility() {
        pheromonesVisible = !pheromonesVisible;
    }

    public boolean isPheromonesVisible() {
        return pheromonesVisible;
    }

    public void toggleWallMode() {
        wallMode = !wallMode;
    }

    public boolean isWallModeActive() {
        return wallMode;
    }

    public void toggleAntVisibility() {
        antsVisible = !antsVisible;
    }

    public boolean isAntsVisible() {
        return antsVisible;
    }
}
