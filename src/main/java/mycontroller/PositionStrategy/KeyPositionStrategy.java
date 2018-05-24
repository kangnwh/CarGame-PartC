package mycontroller.PositionStrategy;


import controller.CarController;
import mycontroller.MapRecorder;
import mycontroller.MyAIController;
import utilities.Coordinate;
import world.Car;

/**
 * This strategy is used to find the next key position if it can.
 * The algorithm will try to use this strategy to find the next key position the car required.
 * If the map information is not enough for this task, null will be returned.
 *
 */

public class KeyPositionStrategy implements INextPositionStrategy {
    public Coordinate getNextPosition(MapRecorder mapRecorder, CarController car){
        return mapRecorder.getKey(car.getKey()-1);
    }
}
