package mycontroller.PositionStrategy;

import controller.CarController;
import mycontroller.MapRecorder;

import static mycontroller.PositionStrategy.HealPositionStrategy.HEALTH_THRESHOLD;

public class NextPositionFactory {

	public static INextPositionStrategy chooseNextPositionStrategy(CarController car, MapRecorder mapRecorder) {
		if (car.getHealth() < HEALTH_THRESHOLD && mapRecorder.hasHealthTrap()) {
			return new HealPositionStrategy();
		} else if (car.getKey() != 1 && mapRecorder.keyFounded(car.getKey() - 1)) {
			return new KeyPositionStrategy();
		} else if (car.getKey() == 1) {
			return new ExitPositionStrategy();
		} else {
			return new ExplorePositionStrategy();
		}
	}
}
