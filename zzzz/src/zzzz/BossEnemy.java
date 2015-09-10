package zzzz;

public class BossEnemy extends GameObject {

	private Game game;
	private static int HEALTH = 20;

	public BossEnemy(Game game, String ref, int x, int y) {
		super(ref, x, y);

		this.game = game;

	}

	public void update() {
		y += dy;

		y = Game.clamp(y, 0, (Game.HEIGHT/2));
		
		if (y >= Game.HEIGHT) {
			game.removeObject(this);
			game.setBossBattle(false);
		}

	}

	public void collidedWith(GameObject other) {
		if (other instanceof Projectile) {
			game.removeObject(other);
			HEALTH--;

			if (HEALTH <= 0) {

				game.removeObject(this);
				game.setBossBattle(false);
			}
		}
	}
}
