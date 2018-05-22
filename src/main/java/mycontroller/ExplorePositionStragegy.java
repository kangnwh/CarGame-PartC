package mycontroller;

import tiles.HealthTrap;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.World;

public class ExplorePositionStragegy {
    public Coordinate getNextPosition(MapRecorder mapRecorder, Car car){
        //Todo how to find a position for explore?
        MapTile[][] map=mapRecorder.getMap();
        int currentX=Math.round(car.getX());
        int currentY=Math.round(car.getY());
        int distance=Integer.MAX_VALUE;
        int exploreX=0;
        int exploreY=0;
        for(int i = 0; i< World.MAP_WIDTH; i++){
            for(int j=0;j<World.MAP_HEIGHT;j++){
                int tempDistance=(int)(Math.pow((currentX-i),2)+Math.pow((currentY-j),2));
                if(map[i][j]==null&&tempDistance<distance){
                    distance=tempDistance;
                    currentX=i;
                    currentY=j;
                }
            }
        }
        return new Coordinate(exploreX,exploreY);
    }

}
