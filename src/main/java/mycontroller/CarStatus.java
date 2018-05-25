package mycontroller;

import controller.CarController;

/**
 * CarStatus
 * This is used to get the current status of a car
 */

public class CarStatus {
	private final float angle;  //angle of the car
	private final float x;      //x coordinate of the car
	private final float y;      //y coordinate of the car
	private final float health; //health of the car

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

	/**
	 * Compare if two status of a car is equal
	 * @param o Object
	 * @return true if two car status is equal
	 */
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
