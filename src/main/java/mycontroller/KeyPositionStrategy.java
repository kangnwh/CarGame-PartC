package mycontroller;

import tiles.HealthTrap;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class KeyPositionStrategy implements INextPositionStrategy {
    public Coordinate getNextPosition(MapRecorder mapRecorder,int key){
        return mapRecorder.getKey(key);
    }
}
