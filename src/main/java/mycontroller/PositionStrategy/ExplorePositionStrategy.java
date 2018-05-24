package mycontroller.PositionStrategy;

import controller.CarController;
import mycontroller.MapRecorder;
import tiles.MapTile;
import utilities.Coordinate;
import world.World;

/**
 * This strategy is used to find the next unknown position (a position that has not been recorded in MapRecorder).
 * This is used to make sure every "useful" coordinate can be discovered and recorded in MapRecorder.
 */

public class ExplorePositionStrategy implements INextPositionStrategy {
	public Coordinate getNextPosition(MapRecorder mapRecorder, CarController car) {
		//Todo how to find a position for explore?
		MapTile[][] map = mapRecorder.getMap();
		int currentX = Math.round(car.getX());
		int currentY = Math.round(car.getY());
		int distance = Integer.MAX_VALUE;
		int exploreX = 0;
		int exploreY = 0;
		for (int i = 0; i < World.MAP_WIDTH; i++) {
			for (int j = 0; j < World.MAP_HEIGHT; j++) {
				int tempDistance = (int) (Math.pow((currentX - i), 2) + Math.pow((currentY - j), 2));
				if (map[i][j] == null && tempDistance < distance) {
					distance = tempDistance;
					exploreX = i;
					exploreY = j;
				}
			}
		}
		return new Coordinate(exploreX, exploreY);
	}

}
