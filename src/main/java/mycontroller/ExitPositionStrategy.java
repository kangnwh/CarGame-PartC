package mycontroller;

import tiles.MapTile;
import utilities.Coordinate;

public class ExitPositionStrategy implements INextPositionStrategy{
    public Coordinate getNextPosition(MapRecorder mapRecorder){
        return mapRecorder.getExit();
    }

}
