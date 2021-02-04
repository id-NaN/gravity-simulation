import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;



public class Window extends Canvas{
	public Window(Dimension size, String title, World world) {
		System.setProperty("sun.awt.noerasebackground", "true");
		JFrame frame = new JFrame(title);
		frame.setSize(size);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(world);
		frame.setVisible(true);
		world.start();
	}
}
