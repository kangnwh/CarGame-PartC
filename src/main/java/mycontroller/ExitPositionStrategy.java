package mycontroller;

import utilities.Coordinate;

public class ExitPositionStrategy implements INextPositionStrategy{
    public Coordinate getNextPosition(MapRecorder mapRecorder,MyAIController myAIController){
        return mapRecorder.getExit();
    }

}
