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
 */

public class ExplorePositionStrategy implements INextPositionStrategy {
	public Coordinate getNextPosition(MapRecorder mapRecorder, CarController car) {


		int G = 0;//Integer.MAX_VALUE;
		int minG = ROAD_VALUE * 3;
		Coordinate target = null;
		Coordinate current = new Coordinate((int) car.getX(), (int) car.getY());
		MapTile[][] map = mapRecorder.getMap();

//		int currentX = Math.round(car.getX());
//		int currentY = Math.round(car.getY());
//		int distance = Integer.MAX_VALUE;
//		int exploreX = -1;
//		int exploreY = -1;

		for (int i = 0; i < World.MAP_WIDTH; i++) {
			for (int j = 0; j < World.MAP_HEIGHT; j++) {
//				int tempDistance = (int) (Math.pow((currentX - i), 2) + Math.pow((currentY - j), 2));
				if (map[i][j] == null) {
//					distance = tempDistance;
//					exploreX = i;
//					exploreY = j;

					Coordinate temp = new Coordinate(i,j);
					mapRecorder.findPath(current,temp,car).size();
					int tempG = mapRecorder.getDiscoveryStrategyInstance().getCost();
					if(tempG > G){
						target = temp;
						G = tempG;
//						if(tempG == minG){
//							return target;
//						}
					}
				}
			}
		}


		MyAIController.printLog(String.format("Explorer Strategy:(%s)",target.toString()));
		return target;

	}
}