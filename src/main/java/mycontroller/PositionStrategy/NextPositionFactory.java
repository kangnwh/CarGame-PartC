package mycontroller.PositionStrategy;

import controller.CarController;
import mycontroller.MapRecorder;

import static mycontroller.PositionStrategy.HealPositionStrategy.HEALTH_THRESHOLD;

public class NextPositionFactory {

	private static INextPositionStrategy healPositionStrategy = new HealPositionStrategy();
	private static INextPositionStrategy keyPositionStrategy = new KeyPositionStrategy();
	private static INextPositionStrategy exitPositionStrategy = new ExitPositionStrategy();
	private static INextPositionStrategy explorePositionStrategy = new ExplorePositionStrategy();


	public static INextPositionStrategy chooseNextPositionStrategy(CarController car, MapRecorder mapRecorder) {
		if (car.getHealth() < HEALTH_THRESHOLD && mapRecorder.hasHealthTrap()) {
			return healPositionStrategy;
		} else if (car.getKey() != 1 && mapRecorder.keyFounded(car.getKey() - 1)){
			return keyPositionStrategy;
		} else if (car.getKey() == 1) {
			return exitPositionStrategy;
		} else {
			return explorePositionStrategy;
		}
	}
}
