import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.Arrays;


class Particle {
	double[] velocity;
	double[] position;
	double mass;
	Color color;
	public Particle (double[] input_velocity, double[] input_position, Color input_color, double mass) {
		velocity = input_velocity;
		position = input_position;
		color = input_color;
		this.mass = mass;
	}

	void update(Particle[] particles, double gravitationalConstant) {
		position[0] += velocity[0];
		position[1] += velocity[1];

		for (Particle particle : particles) {
			if (particle != this) {
				applyGravity(particle, gravitationalConstant);
			}
		}
	}

	void draw(Graphics graphics, Dimension window_size) {
		graphics.setColor(color);
		graphics.fillOval(
			(int)(position[0] * window_size.width / 1024 - 5),
			(int)(position[1] * window_size.height / 576 - 5), 10, 10);
	}

	void applyGravity(Particle particle, double gravitationalConstant) {
		double distance = Math.sqrt(
			Util.square(position[0] - particle.position[0]) +
			Util.square(position[1] - particle.position[1])
		);
		double force = gravitationalConstant * particle.mass / Util.square(distance);
		if (distance == 0) {
			force = 0;
		}
		double angle = Math.atan2(position[0] - particle.position[0], position[1] - particle.position[1]);
		double[] direction = new double[] {
			Math.sin(angle),
			Math.cos(angle)
		};

		velocity[0] -= Math.sin(angle) * force;
		velocity[1] -= Math.cos(angle) * force;
	}
}