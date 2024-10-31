import java.awt.*;

public class Node {
	private boolean colliding = false;

	private double[] position, velocity;

	private double mass;

	public Node (double[] position, double[] velocity, double mass) {
		this.position = position;
		this.velocity = velocity;
		this.mass = mass;
	}

	public void update (double deltaTime, double airResistance) {
		position[0] += velocity[0] * deltaTime;
		position[1] += velocity[1] * deltaTime;

		velocity[0] *= 1 - airResistance;
		velocity[1] *= 1 - airResistance;

		if (position[1] > 900) {
			position[1] = 900;
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
}
