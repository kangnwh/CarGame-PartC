package mycontroller;


import utilities.Coordinate;

/**
 * This strategy is used to find the next key position if it can.
 * The algorithm will try to use this strategy to find the next key position the car required.
 * If the map information is not enough for this task, null will be returned.
 *
 */

public class KeyPositionStrategy implements INextPositionStrategy {
    public Coordinate getNextPosition(MapRecorder mapRecorder,MyAIController myAIController){
        return mapRecorder.getKey(myAIController.getKey()-1);
    }
}
