package mycontroller.PositionStrategy;

import controller.CarController;
import mycontroller.MapRecorder;
import mycontroller.MyAIController;
import tiles.MapTile;
import utilities.Coordinate;

import static mycontroller.MyAIController.MAX_HEALTH;
import static mycontroller.PathDiscovery.AStarStrategy.LAVA_VALUE;
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
		Coordinate target = null;
		if (car.getHealth() < HEALTH_THRESHOLD && mapRecorder.hasHealthTrap()) {
			target = healPositionStrategy.getNextPosition(mapRecorder, car);
			if (target != null
					&& mapRecorder.getDiscoveryStrategyInstance().getCost() / LAVA_VALUE * 13 < car.getHealth()
					&& mapRecorder.getDiscoveryStrategyInstance().getCost() / LAVA_VALUE * 13 < MAX_HEALTH - car.getHealth()) {
				MyAIController.printLog("Heal Strategy");
				return healPositionStrategy;
			}

		}

		if (car.getKey() != 1 && mapRecorder.keyFounded(car.getKey() - 1)) {
			MyAIController.printLog("Key Strategy");
			return keyPositionStrategy;
		}

		if (car.getKey() == 1) {
			MyAIController.printLog("Exit Strategy");
			return exitPositionStrategy;
		}

		MyAIController.printLog("Explorer Strategy");
		return explorePositionStrategy;

	}
}
