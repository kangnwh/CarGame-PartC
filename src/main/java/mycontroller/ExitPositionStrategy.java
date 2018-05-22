package mycontroller;

import tiles.MapTile;
import utilities.Coordinate;

public class ExitPositionStrategy implements INextPositionStrategy{
    public Coordinate getNextPosition(MapRecorder mapRecorder,MyAIController myAIController){
        return mapRecorder.getExit();
    }

}
