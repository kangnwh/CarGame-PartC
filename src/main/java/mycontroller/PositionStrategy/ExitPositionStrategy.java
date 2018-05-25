package mycontroller.PositionStrategy;

import controller.CarController;
import mycontroller.MapRecorder;
import mycontroller.MyAIController;
import utilities.Coordinate;
import world.Car;

/**
 * This strategy is used to find the exit position.
 * This can only take effect when all keys are got by the car and
 * the exit position is already discovered and recorded in MapRecorder.
 *
 * If the map information is not enough for this task, null will be returned.
 *
 */
public class ExitPositionStrategy implements INextPositionStrategy {
    public Coordinate getNextPosition(MapRecorder mapRecorder,  CarController car){
        MyAIController.printLog("Exit Strategy");
        return mapRecorder.getExit();
    }

}
