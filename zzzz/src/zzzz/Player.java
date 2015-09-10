package zzzz;


public class Player extends GameObject {

	private Game game;
	private int timer = 30;
	
	public Player(Game game, String ref, int x, int y) {
		super(ref, x, y);

		this.game = game;
	}

	public void update() {	
		x += dx;
		y += dy;

		x = Game.clamp(x, 0, Game.WIDTH - 38);
		y = Game.clamp(y, 0, Game.HEIGHT - 66);

//		collision();
	}
	
	
	public void collidedWith(GameObject other) {
		if(other instanceof Enemy) {
			game.notifyDeath();
		}
	}

}
