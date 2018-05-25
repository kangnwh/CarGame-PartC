package mycontroller.PathDiscovery;

import mycontroller.MapRecorder;
import mycontroller.OperationType;
import tiles.MapTile;
import utilities.Coordinate;

import java.util.LinkedList;

/**
 * DiscoveryStrategy
 * The interface of different strategies of find the path between two position.
 * All path finder strategies should implement this interface.
 */

public interface IDiscoveryStrategy {
	/**
	 * Find the path between two position according to the giving map
	 * @param current current position of a car
	 * @param target target position of a car
	 * @param map map of all walls and known roads and traps
	 * @return the coordinate linkedList representing the path of two positions
	 */
	LinkedList<Coordinate> findPath(Coordinate current, Coordinate target, MapTile[][] map);
}
