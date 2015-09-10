package zzzz;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {

	protected int x, y, dx, dy;

	protected Sprite sprite;

	private Rectangle me = new Rectangle();
	private Rectangle him = new Rectangle();

	public GameObject(String ref, int x, int y) {
		this.sprite = SpriteArray.get().getSprite(ref);
		this.x = x;
		this.y = y;
	}

	public abstract void update();

	public boolean collidesWith(GameObject other) {
		me.setBounds((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
		him.setBounds((int) other.x, (int) other.y, other.sprite.getWidth(),
				other.sprite.getHeight());

		return me.intersects(him);
	}

	public abstract void collidedWith(GameObject other);

	public void render(Graphics g) {
		sprite.draw(g, x, y);
	}

	// Getters and Setters -------------------------------

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}


}
