package mycontroller;

import tiles.HealthTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class HealPositionStrategy implements INextPositionStrategy {
    public Coordinate getNextPosition(MapRecorder mapRecorder){
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
