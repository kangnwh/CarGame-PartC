package tiles;

import world.Car;

public class GrassTrap extends TrapTile {
	public String getTrap() { return "grass"; }

	public void applyTo(Car car, float delta) {
		// No effect - until you hit something bad!
	}

	public boolean canAccelerate() {
		return true;
	}
	
	public boolean canTurn(int a) {
		return false;
	}

	public boolean canTurn() {
		return false;
	}
}
