import java.awt.*;
import java.awt.geom.Point2D;

public class Connector {
	private boolean colliding = false;

	private Node node1, node2;
	private double relaxedLength, stiffness;

	public Connector(Node node1, Node node2, double stiffness) {
		this.node1 = node1;
		this.node2 = node2;
		this.stiffness = stiffness;
		this.relaxedLength = Point2D.distance(node1.getPosition()[0], node1.getPosition()[1], node2.getPosition()[0], node2.getPosition()[1]);
	}

	public void pullNodes (double deltaTime) {
		double dx = node2.getPosition()[0] - node1.getPosition()[0];
		double dy = node2.getPosition()[1] - node1.getPosition()[1];
		double distance = getLength();
		double force = stiffness * (distance - relaxedLength);
		double fx = force * dx / distance;
		double fy = force * dy / distance;
		node1.addForce(new double[] {fx, fy}, deltaTime);
		node2.addForce(new double[] {-fx, -fy}, deltaTime);

		// Damping
		double damping = 0.1;
		double dvx = node2.getVelocity()[0] - node1.getVelocity()[0];
		double dvy = node2.getVelocity()[1] - node1.getVelocity()[1];
		double fdx = damping * dvx;
		double fdy = damping * dvy;
		node1.addForce(new double[] {fdx, fdy}, deltaTime);
		node2.addForce(new double[] {-fdx, -fdy}, deltaTime);
	}

	public double getLength () {
		return Point2D.distance(node1.getPosition()[0], node1.getPosition()[1], node2.getPosition()[0], node2.getPosition()[1]);
	}

	public void draw (Graphics g) {
		if (colliding) {
			g.setColor(Color.CYAN);
		} else {
			// Set the color somewhere between red and green, green meaning fully relaxed and red meaning very stretched
			final double STRESS_FOR_FULL_RED = 5;
			double stress = Math.abs(getLength() - relaxedLength);
			int red = (int) (255 * Math.min(1, stress / STRESS_FOR_FULL_RED));
			int green = (int) (255 * Math.max(0, 1 - stress / STRESS_FOR_FULL_RED));
			g.setColor(new Color(red, green, 0));
		}
		g.drawLine((int) node1.getPosition()[0], (int) node1.getPosition()[1], (int) node2.getPosition()[0], (int) node2.getPosition()[1]);

		// Draw a short line for the normal
		double[] normal = getNormal();
		double midX = (node1.getPosition()[0] + node2.getPosition()[0]) / 2;
		double midY = (node1.getPosition()[1] + node2.getPosition()[1]) / 2;
		g.setColor(Color.BLACK);
		g.drawLine((int) midX, (int) midY, (int) (midX + normal[0] * 10), (int) (midY + normal[1] * 10));
	}

	public Node getNode1() {
		return node1;
	}

	public void setNode1(Node node1) {
		this.node1 = node1;
	}

	public Node getNode2() {
		return node2;
	}

	public void setNode2(Node node2) {
		this.node2 = node2;
	}

	public double getRelaxedLength() {
		return relaxedLength;
	}

	public void setRelaxedLength(double relaxedLength) {
		this.relaxedLength = relaxedLength;
	}

	public double getStiffness() {
		return stiffness;
	}

	public void setStiffness(double stiffness) {
		this.stiffness = stiffness;
	}

	public void setColliding (boolean colliding) {
		this.colliding = colliding;
	}

	public double[] getNormal() {
		double dx = node2.getPosition()[0] - node1.getPosition()[0];
		double dy = node2.getPosition()[1] - node1.getPosition()[1];
		double length = getLength();
		return new double[] {dy / length, -dx / length};
	}
}
