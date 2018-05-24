package mycontroller;

import utilities.Coordinate;

/**
 * This strategy is used to find the exit position.
 * This can only take effect when all keys are got by the car and
 * the exit position is already discovered and recorded in MapRecorder.
 *
 * If the map information is not enough for this task, null will be returned.
 *
 */
public class ExitPositionStrategy implements INextPositionStrategy{
    public Coordinate getNextPosition(MapRecorder mapRecorder,MyAIController myAIController){
        return mapRecorder.getExit();
    }

}
