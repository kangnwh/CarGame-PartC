package mycontroller;

import tiles.HealthTrap;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;

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
