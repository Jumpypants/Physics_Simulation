import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Shape {
    private final ArrayList<Node> nodes = new ArrayList<>();
    private final ArrayList<Connector> connectors = new ArrayList<>();

    public Shape (Node[] nodes, double stiffness) {
        Collections.addAll(this.nodes, nodes);

        createConnectors(stiffness);
    }

    public void update (double deltaTime) {
        pullNodes(deltaTime);
        updateNodes(deltaTime);
    }

    public void draw (Graphics g) {
        for (Connector connector : connectors) {
            connector.draw(g);
        }

        for (Node node : nodes) {
            node.draw(g);
        }
    }

    public Node[] getNodes() {
        return nodes.toArray(new Node[0]);
    }

    public Connector[] getConnectors() {
        return connectors.toArray(new Connector[0]);
    }

    private void createConnectors (double stiffness) {
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                connectors.add(new Connector(nodes.get(i), nodes.get(j), stiffness));
            }
        }
    }

    private void updateNodes(double deltaTime) {
        for (Node node : nodes) {
            node.addForce(new double[] {0, 3 * 9.8}, deltaTime);

            node.update(deltaTime, 0.001);
        }
    }

    private void pullNodes(double deltaTime) {
        for (Connector connector : connectors) {
            connector.pullNodes(deltaTime);
        }
    }
}
