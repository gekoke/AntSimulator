package antsimulation.world.grid;

import antsimulation.world.Displayable;
import antsimulation.world.Updatable;
import antsimulation.world.World;
import org.mini2Dx.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grid implements Updatable, Displayable {

    private final World parent;
    private final Node[][] nodes;
    private final int width;
    private final int height;
    private final double nodeWidth;
    private final double nodeHeight;

    public Grid(World parent, int width, int height) {
        this.parent = parent;
        this.nodes = new Node[height][width];
        this.width = width;
        this.height = height;
        this.nodeHeight = parent.getHeight() / (double) height;
        this.nodeWidth = parent.getWidth() / (double) width;

        populateGridWithNodes();
    }

    private void populateGridWithNodes() {
        for (int j = 0; j < nodes.length; j++) {
            for (int i = 0; i < nodes[0].length; i++) {
                nodes[j][i] = new Node(i, j, nodeWidth, nodeHeight);
            }
        }
    }

    public Node getNodeAt(Vector2 location) {
        final int y = Math.min((int) (location.y / nodeHeight), height - 1);
        final int x = Math.min((int) (location.x / nodeWidth), width - 1);
        if (!parent.inBounds(new Vector2(x, y))) {
            throw new RuntimeException(String.format("Queried position (%d, %d) not in world bounds!", x, y));
        }

        return nodes[y][x];
    }

    /**
     * Return nodes (incl. center) around center in a rectangle with given
     * width, ordered by row, starting from top-left.
     * @param center The node around which to query.
     * @param width The width of the queried square.
     * @return A list of nodes around the given center node.
     */
    public List<Node> getNodesInSquare(Node center, int width) {
        if (width <= 0) throw new IllegalArgumentException("That would not return anything.");
        if (width % 2 == 0) throw new IllegalArgumentException("Only odd widths make sense (or else square is uneven).");
        if (width == 1) return Collections.singletonList(center);

        final int offsetToEdge = width / 2;
        List<Node> outputNodes = new ArrayList<>();

        for (int j = center.getYIndex() - offsetToEdge; j <= center.getYIndex() + offsetToEdge; j++) {
            for (int i = center.getXIndex() - offsetToEdge; i <= center.getXIndex() + offsetToEdge; i++) {
                if (indicesInGrid(i, j)) outputNodes.add(nodes[j][i]);
            }
        }

        return outputNodes;
    }

    private boolean indicesInGrid(int x, int y) {
        return (
                x >= 0 && x < nodes[0].length
                && y >= 0 && y < nodes.length
        );
    }

    @Override
    public void display() {
        for (Node[] nodeRow : nodes)
            for (Node node : nodeRow)
                node.display();
    }

    @Override
    public void update(float dt) {
        for (Node[] nodeRow : nodes)
            for (Node node : nodeRow)
                node.update(dt);
    }

    public double getNodeWidth() {
        return nodeWidth;
    }
}
