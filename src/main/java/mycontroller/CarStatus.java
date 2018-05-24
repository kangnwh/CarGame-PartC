package mycontroller;

import controller.CarController;

/**
 * CarStatus
 * <p>
 * Author Ning Kang
 * Date 25/5/18
 */

public class CarStatus {
	final float angle;
	final float x;
	final float y;
	final float health;

//	public CarStatus(float angle, float x, float y, float health) {
//		this.angle = angle;
//		this.x = x;
//		this.y = y;
//		this.health = health;
//	}

	public CarStatus(CarController carController) {
		this.angle = carController.getAngle();
		this.x = carController.getX();
		this.y = carController.getY();
		this.health = carController.getHealth();
	}

	public float getAngle() {
		return angle;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getHealth() {
		return health;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CarStatus carStatus = (CarStatus) o;

		if (Float.compare(carStatus.angle, angle) != 0) return false;
		if (Float.compare(carStatus.x, x) != 0) return false;
		if (Float.compare(carStatus.y, y) != 0) return false;
		return Float.compare(carStatus.health, health) == 0;
	}

}
