package mycontroller.PathDiscovery;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * MyDiscoveryStrategy
 * <p>
 * Author Ning Kang
 * Date 24/5/18
 */

public class MyDiscoveryStrategy implements IDiscoveryStrategy {


	@Override
	public LinkedList<Coordinate> findPath(Coordinate current, Coordinate target, MapTile[][] map) {

		return null;
	}


	@Override
	public int getCost() {
		return 0;
	}
}
