package game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	private final String TITLE = "RandomGame";
	private final int WIDTH = 1280;
	private final int HEIGHT = (WIDTH / 16 * 9);
	private final float SCALE = 1.0f;
	private final double GAME_HERTZ = 30.0;
	private final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
	private final int MAX_UPDATES_BEFORE_RENDER = 5;
	private final double TARGET_FPS = 60;
	private final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

	private JFrame frame;
	private boolean isRunning;

	private int fps = 60;
	private int frameCount = 0;

	public static void main(String[] args) {
		new Game().start();
	}

	public Game() {
		setMinimumSize(new Dimension((int) (WIDTH * SCALE),
				(int) (HEIGHT * SCALE)));
		setMaximumSize(new Dimension((int) (WIDTH * SCALE),
				(int) (HEIGHT * SCALE)));
		setPreferredSize(new Dimension((int) (WIDTH * SCALE),
				(int) (HEIGHT * SCALE)));
		frame = new JFrame("TITLE");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();

		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void run() {
		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();

		int lastSecondTime = (int) (lastUpdateTime / 1000000000);

		while (isRunning) {
			double now = System.nanoTime();
			int updateCount = 0;

			while (now - lastUpdateTime > TIME_BETWEEN_UPDATES
					&& updateCount < MAX_UPDATES_BEFORE_RENDER) {
				update();
				lastUpdateTime += TIME_BETWEEN_UPDATES;
				updateCount++;
			}

			if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
				lastUpdateTime = now - TIME_BETWEEN_UPDATES;
			}

			float interpolation = Math.min(1.0f,
					(float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES));
			render(interpolation);
			lastRenderTime = now;
			
			int thisSecond = (int) (lastUpdateTime / 1000000000);
            if (thisSecond > lastSecondTime)
            {
//               System.out.println("NEW SECOND " + thisSecond + " " + frameCount);
               fps = frameCount;
               frameCount = 0;
               lastSecondTime = thisSecond;
            }
            
            while ( now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES)
            {
               Thread.yield();
               try {Thread.sleep(1);} catch(Exception e) {} 
            
               now = System.nanoTime();
            }
		}
	}

	private void update() {

	}

	private void render(float interpolation) {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		//Render
		
		g.dispose();
	    bs.show();
	}

	public synchronized void start() {
		isRunning = true;
		new Thread(this).start();
	}

	public synchronized void stop() {
		if (!this.isRunning) {
			return;
		}
		this.isRunning = false;
	}

	public boolean isRunning() {
		return isRunning;
	}

}
