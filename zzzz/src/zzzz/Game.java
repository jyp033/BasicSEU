package zzzz;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends Canvas implements Runnable, KeyListener {

	public static final int HEIGHT = 640, WIDTH = HEIGHT / 12 * 9;

	private static int moveSpeed = 2;

	private boolean running = false;
	private boolean bossBattle = false;
	private boolean bosstime = false;

	private int bossTimer = 2000;
	private int bulletTimer = 30;
	private int enemyTimer = 40;

	private Player player;
	private ArrayList objects = new ArrayList();
	private ArrayList removeList = new ArrayList();
	private String message = "";

	private boolean movingLeft, movingRight, movingUp, movingDown = false;

	private Random r;

	private Thread thread;

	public Game() {

		new Window(WIDTH, HEIGHT, "Game", this);

		addKeyListener(this);
		initObjects();

		start();
	}

	public synchronized void start() {
		objects.clear();
		initObjects();

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

	private void initObjects() {

		player = new Player(this, "sprites/spaceship.png", WIDTH / 2 - 22,
				HEIGHT - 132);

		objects.add(player);

	}

	private void spawnEnemy() {

		r = new Random();

		GameObject alien = new Enemy(this, "sprites/rock.png",
				r.nextInt(WIDTH), 0);

		alien.setDy(1);
		objects.add(alien);
	}

	private void spawnBoss() {

		r = new Random();
		bossBattle = true;
		GameObject boss = new BossEnemy(this, "sprites/alien.gif",
				(WIDTH/2 -50), 0);
		boss.setDy(1);
		objects.add(boss);
	}

	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		long delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				delta--;
			}

			if (running) {
				bulletTimer--;
				enemyTimer--;
				bossTimer--;

				if (bossTimer <= 0) {
					spawnBoss();
					bossTimer = 2000;
				}

				if (enemyTimer <= 0 && !bossBattle) {
					spawnEnemy();
					enemyTimer = 40;
				}

				if (bulletTimer <= 0) {
					shoot();
					bulletTimer = 30;
				}

				for (int p = 0; p < objects.size(); p++) {
					for (int s = p + 1; s < objects.size(); s++) {
						GameObject me = (GameObject) objects.get(p);
						GameObject him = (GameObject) objects.get(s);

						if (me instanceof BossEnemy) {
							if (him instanceof Enemy) {
								continue;
							}
						} else if (him instanceof BossEnemy) {
							if (me instanceof Enemy) {
								continue;
							}
						} else {
							if (me.collidesWith(him)) {
								me.collidedWith(him);
								him.collidedWith(me);

							}
						}
					}
				}

				objects.removeAll(removeList);
				removeList.clear();
				render();
			}

			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}

			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		stop();

	}

	public void update() {
		for (int i = 0; i < objects.size(); i++) {
			GameObject entity = (GameObject) objects.get(i);

			entity.update();
		}
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		// Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();

		// background
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// objects
		for (int i = 0; i < objects.size(); i++) {
			GameObject entity = (GameObject) objects.get(i);

			entity.render(g);
		}

		r = new Random();
		for (int i = 0; i < r.nextInt(100); i++) {
			g.setColor(Color.white);
			g.fillRect(r.nextInt(WIDTH), r.nextInt(HEIGHT), 2, 2);
		}

		g.dispose();
		bs.show();
	}

	public void removeObject(GameObject object) {
		removeList.add(object);
	}

	public void notifyDeath() {
		message = "Oh no! They got you, try again?";

	}

	public void notifyWin() {
		message = "Well done! You Win!";

	}

	private void shoot() {
		Projectile bullet = new Projectile(this, "sprites/bullet.png",
				player.getX() + 12, player.getY() - 8);
		objects.add(bullet);
	}

	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			player.setDy(-moveSpeed);
			movingUp = true;
			break;
		case KeyEvent.VK_A:
			player.setDx(-moveSpeed);
			movingLeft = true;
			break;
		case KeyEvent.VK_S:
			player.setDy(moveSpeed);
			movingDown = true;
			break;
		case KeyEvent.VK_D:
			player.setDx(moveSpeed);
			movingRight = true;
			break;
		}
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			movingUp = false;
			break;
		case KeyEvent.VK_A:
			movingLeft = false;
			break;
		case KeyEvent.VK_S:
			movingDown = false;
			break;
		case KeyEvent.VK_D:
			movingRight = false;
			break;
		}

		if (!movingLeft && !movingRight)
			player.setDx(0);

		if (!movingUp && !movingDown)
			player.setDy(0);
	}

	public void keyTyped(KeyEvent e) {

	}

	public static int clamp(int var, int min, int max) {
		if (var >= max)
			return var = max;
		else if (var <= min)
			return var = min;
		else
			return var;

	}

	// ------MAIN------------------------------------------
	public static void main(String argv[]) {
		Game g = new Game();
	}

	// Getters and Setters ------------------------------

	public boolean isBossBattle() {
		return bossBattle;
	}

	public void setBossBattle(boolean bossBattle) {
		this.bossBattle = bossBattle;
	}

}
