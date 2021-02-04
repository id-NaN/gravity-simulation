import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;


public class World extends Canvas implements Runnable {
	public static Dimension window_size = new Dimension(1024, 576);
	private Thread thread;
	private boolean running = false;

	private ArrayList<Particle> particles;

	public final double GRAVITATIONAL_CONSTANT = 100;
	
	public World() {
		new Window(window_size, "Gravity", this);
	}
	
	
	public static void main(String[] args) {
		World world = new World();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// adding all particles
		world.addParticle(new Particle(
				new double[] {2, 0}, // velocity
				new double[] {512, 100}, // position
				new Color(255, 0, 0), 0 // color and mass
				));
		world.addParticle(new Particle(
				new double[] {0, 0},
				new double[] {512, 288},
				new Color(0, 255, 0), 10
				));
	}
	
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void addParticle(Particle particle) {
		particles.add(particle);
	}
	
	
	public void run() {
		particles = new ArrayList<Particle>();

		long start_time = System.nanoTime();
		long last_time = start_time;
		long ticks_per_second = 60;
		long ns_per_tick = 1_000_000_000 / ticks_per_second;
		
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		long now;
		long wait_time;
		while (running) {
			now = System.nanoTime();
			delta += (now - last_time) / ns_per_tick;
			last_time = now;
			while (delta >= 1) {				
				tick();
				delta -= 1;
			}
			if (running) {
				render();
			frames += 1;
			}
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
			wait_time = (ns_per_tick - (System.nanoTime() - now)) / 1000000;
			
			if (wait_time > 0) {
				try {
					Thread.sleep(wait_time);
				} catch (InterruptedException e) {}
			}
		}
		stop();
	}
	
	
	private void tick() {
		Particle particle;
		Particle[] particlesArray = new Particle[particles.size()];
		for (int i = 0; i < particles.size(); i ++) {
			particlesArray[i] = particles.get(i);
		}
		for (int i = 0; i < particles.size(); i ++) {
			particle = particles.get(i);
			particle.update(particlesArray, GRAVITATIONAL_CONSTANT);
		}
	}
	
	
	private void render() {
		window_size = getParent().getSize();
		BufferStrategy buffer_strategy = this.getBufferStrategy();
		if (buffer_strategy == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics2D graphics = (Graphics2D)buffer_strategy.getDrawGraphics();
		
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, window_size.width, window_size.height);

		Particle particle;
		for (int i = 0; i < particles.size(); i ++) {
			particle = particles.get(i);
			particle.draw(graphics, window_size);
		}
		
		graphics.dispose();
		buffer_strategy.show();
	}
}

