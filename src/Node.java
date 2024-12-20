import java.awt.*;

public class Node {
	private boolean colliding = false;
	private final boolean isEdge;

	private double[] position, velocity;

	private double mass;

	public Node (double[] position, double[] velocity, double mass, boolean isEdge) {
		this.position = position;
		this.velocity = velocity;
		this.mass = mass;
		this.isEdge = isEdge;
	}

	public void update (double deltaTime, double airResistance, double width, double height) {
		velocity[0] *= 1 - airResistance;
		velocity[1] *= 1 - airResistance;

		System.out.println(width);

		if (Math.abs(velocity[0]) > 1100) velocity[0] /= 2 / deltaTime;
		if (Math.abs(velocity[1]) > 1100) velocity[1] /= 2 / deltaTime;

		position[0] += velocity[0] * deltaTime;
		position[1] += velocity[1] * deltaTime;

		if (position[1] > height) {
			position[1] = height;
			velocity[1] = -1 * velocity[1];
		} else if (position[1] < 0) {
			position[1] = 0;
			velocity[1] = -1 * velocity[1];
		}
		if (position[0] > width) {
			position[0] = width;
			velocity[0] = -1 * velocity[0];
		} else if (position[0] < 0) {
			position[0] = 0;
			velocity[0] = -1 * velocity[0];
		}
	}

	public void addForce (double[] force, double deltaTime) {
		velocity[0] += force[0] / mass * deltaTime;
		velocity[1] += force[1] / mass * deltaTime;
	}

	public void draw (Graphics g) {
		if (colliding) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.BLACK);
		}

		g.fillOval((int) position[0] - 5, (int) position[1] - 5, 10, 10);
	}

	public double[] getPosition() {
		return position;
	}

	public void setPosition(double[] position) {
		this.position = position;
	}

	public double[] getVelocity() {
		return velocity;
	}

	public void setVelocity(double[] velocity) {
		this.velocity = velocity;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public void setColliding(boolean b) {
		colliding = b;
	}

	public boolean isEdge() {
		return isEdge;
	}
}
