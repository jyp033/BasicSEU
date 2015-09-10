package zzzz;

public class Enemy extends GameObject {


	private Game game;

	public Enemy(Game game, String ref, int x, int y) {
		super(ref, x, y);

		this.game = game;

	}

	
	public void update() {
		y += dy;
		
		if (y >= Game.HEIGHT) {
			game.removeObject(this);

		}

	}
	
	public void collidedWith(GameObject other) {

	}

}
