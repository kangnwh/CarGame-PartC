package mycontroller.PositionStrategy;

import controller.CarController;
import mycontroller.MapRecorder;
import mycontroller.MyAIController;
import mycontroller.PathDiscovery.IDiscoveryStrategy;
import tiles.HealthTrap;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;

/**
 * This strategy is to assign the next position to be a heal coordinate.
 * This is used when the algorithm realise the car's health point is not enough.
 * If the map information is not enough for this task, null will be returned.
 */

public class HealPositionStrategy implements INextPositionStrategy {
    public static final float HEALTH_THRESHOLD = 45.0f;
    private INextPositionStrategy explorePositionStrategy;

    public HealPositionStrategy() {
        this.explorePositionStrategy = new ExplorePositionStrategy();
    }

    public Coordinate getNextPosition(MapRecorder mapRecorder, CarController car){
        int G = Integer.MAX_VALUE;
        Coordinate target = null;
        Coordinate current = new Coordinate((int) car.getX(), (int) car.getY());

        MapTile[][] map=mapRecorder.getMap();
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[i].length;j++){
                if(map[i][j] instanceof HealthTrap){
                    Coordinate temp = new Coordinate(i,j);
                    mapRecorder.findPath(current,temp,car);
                    int tempG = mapRecorder.getDiscoveryStrategyInstance().getCost();
                    if(tempG < G){
                        target = temp;
                        G = tempG;
                    }
                }
            }
        }
        if(target == null){
            MyAIController.printLog("Explorer Strategy");
            target = this.explorePositionStrategy.getNextPosition(mapRecorder,car);
        }
        MyAIController.printLog("Heal Strategy");
        return target;
    }

}
