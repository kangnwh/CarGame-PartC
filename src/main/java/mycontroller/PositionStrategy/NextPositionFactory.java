package mycontroller.PositionStrategy;

import controller.CarController;
import mycontroller.MapRecorder;

import static mycontroller.PositionStrategy.HealPositionStrategy.HEALTH_THRESHOLD;

/**
 * The factory is used to return different position strategies according in different condition
 * If health is little, the car will go to the healthTrap,if keys is found but not get, the car will go to get the key,
 * if all keys are get, the car will go to the exit, else the car will explore the maze.
 */
public class NextPositionFactory {

	private static INextPositionStrategy healPositionStrategy = new HealPositionStrategy();
	private static INextPositionStrategy keyPositionStrategy = new KeyPositionStrategy();
	private static INextPositionStrategy exitPositionStrategy = new ExitPositionStrategy();
	private static INextPositionStrategy explorePositionStrategy = new ExplorePositionStrategy();


	/**
	 * Choose appropriate strategies to get the next position to go
	 * @param car the carController to get the car status
	 * @param mapRecorder the map to get position information
	 * @return appropriate position strategy
	 */
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
