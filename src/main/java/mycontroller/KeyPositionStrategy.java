package mycontroller;


import utilities.Coordinate;

public class KeyPositionStrategy implements INextPositionStrategy {
    public Coordinate getNextPosition(MapRecorder mapRecorder,MyAIController myAIController){
        return mapRecorder.getKey(myAIController.getKey()-1);
    }
}
