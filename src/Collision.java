public class Collision {
    private final Node NODE;
    private final Connector CONNECTOR;
    private final double TIME;
    private final double S;

    public Collision(double time, double s, Node node, Connector connector) {
        this.TIME = time;
        this.S = s;
        this.NODE = node;
        this.CONNECTOR = connector;
    }

    public double getTime() {
        return TIME;
    }

    public double getS() {
        return S;
    }

    public Node getNode() {
        return NODE;
    }

    public Connector getConnector() {
        return CONNECTOR;
    }

    public void collide() {
        double[] jPos = CONNECTOR.getNode1().getPosition();
        double[] kPos = CONNECTOR.getNode2().getPosition();

        // Get the normal of the connector
        double[] normal = CONNECTOR.getNormal();

        // Calculate the position of the collision point P
        double[] P = new double[] {
                (1 - S) * jPos[0] + S * kPos[0],
                (1 - S) * jPos[1] + S * kPos[1]
        };

        // Get velocities of the nodes
        double[] vA = NODE.getVelocity();
        double[] vJ = CONNECTOR.getNode1().getVelocity();
        double[] vK = CONNECTOR.getNode2().getVelocity();

        // Masses of nodes
        double mA = NODE.getMass();
        double mJ = CONNECTOR.getNode1().getMass();
        double mK = CONNECTOR.getNode2().getMass();

        // Calculate the velocity of the collision point P (linear interpolation of vJ and vK)
        double[] vP = new double[] {
                (1 - S) * vJ[0] + S * vK[0],
                (1 - S) * vJ[1] + S * vK[1]
        };

        // Decompose velocities into normal components
        double vA_normal = vA[0] * normal[0] + vA[1] * normal[1];
        double vP_normal = vP[0] * normal[0] + vP[1] * normal[1];

        // Apply the coefficient of restitution

        // TODO: implement the coefficient of restitution
        //double e = getElasticity();
        double e = 0.1;
        double v_rel_normal = vA_normal - vP_normal;
        double v_rel_normal_post = -e * v_rel_normal;

        // Conservation of momentum in the normal direction
        double totalMass = mA + mJ * (1 - S) + mK * S;
        double delta_v_normal = (v_rel_normal - v_rel_normal_post) / totalMass;

        // Update velocities (normal components)
        vA[0] += -delta_v_normal * normal[0] * mA;
        vA[1] += -delta_v_normal * normal[1] * mA;

        vJ[0] += delta_v_normal * normal[0] * (1 - S) * mJ;
        vJ[1] += delta_v_normal * normal[1] * (1 - S) * mJ;

        vK[0] += delta_v_normal * normal[0] * S * mK;
        vK[1] += delta_v_normal * normal[1] * S * mK;

        // Update the velocities of all the nodes
        NODE.setVelocity(vA);
        CONNECTOR.getNode1().setVelocity(vJ);
        CONNECTOR.getNode2().setVelocity(vK);

        // Move the node very slightly along the collision normal to avoid multiple collisions
        double[] newPosition = new double[] {
                P[0] + normal[0] * 10,
                P[1] + normal[1] * 10
        };
        NODE.setPosition(newPosition);
    }

    public static Collision find (Connector connector, Node node, double timeLeft) {
        // Extract the positions and velocities
        double[] nodePosition = node.getPosition();
        double[] nodeVelocity = node.getVelocity();

        double[] pos1 = connector.getNode1().getPosition();
        double[] pos2 = connector.getNode2().getPosition();

        double vx = nodeVelocity[0];
        double vy = nodeVelocity[1];

        // Calculate the relative differences
        double dx1 = pos1[0] - nodePosition[0];
        double dy1 = pos1[1] - nodePosition[1];
        double dx2 = pos2[0] - pos1[0];
        double dy2 = pos2[1] - pos1[1];

        // Define the system of equations for t and s:
        double denominator = -(dx2 * vy - dy2 * vx);

        // If denominator is 0, the node is moving parallel to the segment
        if (denominator == 0) {
            return null;  // No collision
        }

        // Solve for s
        double s = (dx1 * vy - dy1 * vx) / denominator;

        // Check if s is within the range [0, 1] (i.e., the intersection is on the segment)
        System.out.println("s: " + s);
        if (s < -0.1 || s >= 1.1) {
            return null;  // No collision
        }

        // Solve for t using the x-component (or y-component, they should give the same result)
        //double t = (dx1 + s * dx2) / vx;
        double t = (dx1 * dy2 - dy1 * dx2) / denominator;

        System.out.println("t: " + t + ", timeLeft: " + timeLeft);

        // Check if t is positive (future collision) and within the time left
        if (t <= 0 || t > timeLeft) {
            return null;  // No collision in the future or outside time window
        }

        System.out.println("collision");

        // Return a new Collision object with this node, the connector, and the time of collision
        return new Collision(t, s, node, connector);
    }
}
