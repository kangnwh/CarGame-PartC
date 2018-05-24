package mycontroller;

import tiles.HealthTrap;
import tiles.MapTile;
import utilities.Coordinate;

/**
 * This strategy is to assign the next position to be a heal coordinate.
 * This is used when the algorithm realise the car's health point is not enough.
 * If the map information is not enough for this task, null will be returned.
 */

public class HealPositionStrategy implements INextPositionStrategy {
    public Coordinate getNextPosition(MapRecorder mapRecorder, MyAIController myAIController){
        MapTile[][] map=mapRecorder.getMap();
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[i].length;j++){
                if(map[i][j] instanceof HealthTrap){
                    return new Coordinate(i,j);
                }
            }
        }
        return null;
    }

}
