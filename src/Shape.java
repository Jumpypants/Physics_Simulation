import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Shape {
    private final ArrayList<Node> nodes = new ArrayList<>();
    private final ArrayList<Connector> connectors = new ArrayList<>();
    private final ArrayList<Node> edgeNodes = new ArrayList<>();

    public Shape (Node[] nodes, double stiffness) {
        Collections.addAll(this.nodes, nodes);

        createConnectors(stiffness);
        findEdgeNodes();
    }

    public void update (double deltaTime, double width, double height) {
        pullNodes(deltaTime);
        updateNodes(deltaTime, width, height);
    }

    public void draw (Graphics g) {
        for (Connector connector : connectors) {
            connector.draw(g);
        }

        for (Node node : nodes) {
            node.draw(g);
        }
    }

    public void drawFilled (Graphics g) {
        g.setColor(Color.BLUE);
        g.fillPolygon(getXPoints(), getYPoints(), nodes.size());
    }

    private int[] getYPoints() {
        int[] yPoints = new int[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            yPoints[i] = (int) nodes.get(i).getPosition()[1];
        }
        return yPoints;
    }

    private int[] getXPoints() {
        int[] xPoints = new int[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            xPoints[i] = (int) nodes.get(i).getPosition()[0];
        }
        return xPoints;
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

    private void findEdgeNodes() {
        for (Node node : nodes) {
            if (node.isEdge()) {
                edgeNodes.add(node);
            }
        }
    }

    private void updateNodes(double deltaTime, double width, double height) {
        for (Node node : nodes) {
            node.addForce(new double[] {0, 30000 * deltaTime}, deltaTime);

            node.update(deltaTime, 0.2, width, height);
        }
    }

    private void pullNodes(double deltaTime) {
        for (Connector connector : connectors) {
            connector.pullNodes(deltaTime);
        }
    }
}
