package mycontroller.PositionStrategy;

import controller.CarController;
import mycontroller.MapRecorder;
import mycontroller.MyAIController;
import tiles.MapTile;
import utilities.Coordinate;
import world.World;

import static mycontroller.PathDiscovery.AStarStrategy.ROAD_VALUE;

/**
 * This strategy is used to find the next unknown position (a position that has not been recorded in MapRecorder).
 * This is used to make sure every "useful" coordinate can be discovered and recorded in MapRecorder.
 * This strategy find the most nearest point that has a cost value > 3*ROAD_VALUE and has not been discovered
 */

public class ExplorePositionStrategy implements INextPositionStrategy {
	private static final int MIN_DISTANCE = 6;

	public Coordinate getNextPosition(MapRecorder mapRecorder, CarController car) {

		//Todo how to find a position for explore?
		MapTile[][] map=mapRecorder.getMap();
		int currentX=Math.round(car.getX());
		int currentY=Math.round(car.getY());
		int distance=Integer.MAX_VALUE;
		int exploreX=0;
		int exploreY=0;
		for(int i = 1; i< World.MAP_WIDTH; i++){
			for(int j=1;j<World.MAP_HEIGHT;j++){
//				int tempDistance=(int)(Math.pow((currentX-i),2)+Math.pow((currentY-j),2));
				int tempDistance=(Math.abs(currentX-i)+Math.abs(currentY-j));
				if(map[i][j]==null && tempDistance < distance && tempDistance > MIN_DISTANCE){
					distance = tempDistance;
					exploreX=i;
					exploreY=j;
				}
			}
		}
		MyAIController.printLog(String.format("Explorer Strategy:(%d,%d)",exploreX,exploreY));
		if(exploreX == 0 && exploreY==0){
			MyAIController.printLog("Maps were explored except the exit.");
			return mapRecorder.getExit();
		}
		return new Coordinate(exploreX,exploreY);
//
//		int G = 0;//Integer.MAX_VALUE;
//		Coordinate target = null;
//		Coordinate current = new Coordinate((int) car.getX(), (int) car.getY());
//		MapTile[][] map = mapRecorder.getMap();
//
//		for (int i = 0; i < World.MAP_WIDTH; i++) {
//			for (int j = 0; j < World.MAP_HEIGHT; j++) {
//				if (map[i][j] == null) {
//					Coordinate temp = new Coordinate(i,j);
//					mapRecorder.findPath(current,temp,car).size();
//					int tempG = mapRecorder.getDiscoveryStrategyInstance().getCost();
//					if(tempG > G){
//						target = temp;
//						G = tempG;
//					}
//				}
//			}
//		}

//		int G = Integer.MAX_VALUE;
//		int minG = 0;// ROAD_VALUE * 3;
//		Coordinate target = null;
//		Coordinate current = new Coordinate((int) car.getX(), (int) car.getY());
//		MapTile[][] map = mapRecorder.getMap();
//
//		for (int i = 0; i < World.MAP_WIDTH; i++) {
//			for (int j = 0; j < World.MAP_HEIGHT; j++) {
//				if (map[i][j] == null || map[i][j].getType() == MapTile.Type.FINISH) {
//					Coordinate temp = new Coordinate(i, j);
//					mapRecorder.findPath(current, temp, car).size();
//					int tempG = mapRecorder.getDiscoveryStrategyInstance().getCost();
//					if (tempG < G) {
//						target = temp;
//						G = tempG;
//						if(tempG == minG){
//							return target;
//						}
//					}
//				}
//			}
//		}

//		MyAIController.printLog(String.format("Explorer Strategy:(%s)", target.toString()));
//		return target;

	}
}