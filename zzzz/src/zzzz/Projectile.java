package zzzz;


public class Projectile extends GameObject {

	private int moveSpeed = -10;

	private Game game;

	public Projectile(Game game, String sprite, int x, int y) {
		super(sprite, x, y);

		this.game = game;

		dy = moveSpeed;
	}

	public void update() {
		y +=dy;

		// if we shot off the screen, remove ourselfs
		if (y <= 0) {
			game.removeObject(this);
		}
	}

	public void collidedWith(GameObject other) {
		if (other instanceof Enemy) {
//			// remove the affected entities
			game.removeObject(this);
			game.removeObject(other);
//			
//			// notify the game that the alien has been killed
//			game.enemyKilled();
		}
	}

}
