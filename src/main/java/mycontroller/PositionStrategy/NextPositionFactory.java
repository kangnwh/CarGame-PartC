package mycontroller.PositionStrategy;

import controller.CarController;
import mycontroller.MapRecorder;
import mycontroller.MyAIController;
import mycontroller.PositionStrategy.*;
import world.Car;

public class NextPositionFactory {


	public static INextPositionStrategy chooseNextPositionStrategy(CarController car, MapRecorder mapRecorder) {
		if (car.getHealth() < 30) {
			return new HealPositionStrategy();
		} else if (car.getKey() != 1 && mapRecorder.keyFounded(car.getKey() - 1)) {
			return new KeyPositionStrategy();
		} else if (car.getKey() == 1 && mapRecorder.isExitFounded()) {
			return new ExitPositionStrategy();
		} else {
			return new ExplorePositionStrategy();
		}
	}
}
