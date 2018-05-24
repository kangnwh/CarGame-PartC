package mycontroller.PathDiscovery;

import tiles.MapTile;
import utilities.Coordinate;

import java.util.LinkedList;

/**
 * MyDiscoveryStrategy
 * <p>
 * Author Ning Kang
 * Date 24/5/18
 */

public class TestDiscoveryStrategy implements IDiscoveryStrategy {
	@Override
	public LinkedList<Coordinate> findPath(Coordinate current, Coordinate target, MapTile[][] map) {
		LinkedList<Coordinate> coordinatesInPath = new LinkedList<>();
		coordinatesInPath.add(new Coordinate(3,3));
		coordinatesInPath.add(new Coordinate(4,3));
		coordinatesInPath.add(new Coordinate(5,3));
		coordinatesInPath.add(new Coordinate(7,3));
		coordinatesInPath.add(new Coordinate(7,4));
		coordinatesInPath.add(new Coordinate(7,5));
		return coordinatesInPath;
	}
}
