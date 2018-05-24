package mycontroller.PathDiscovery;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * MyDiscoveryStrtegy
 * <p>
 * Author Ning Kang
 * Date 24/5/18
 */

public class MyDiscoveryStrtegy implements IDiscoveryStrategy {
	private static Comparator<Coordinate> upRightFirst = new UpRightFirst();
	private static Comparator<Coordinate> downRightFirst = new DownRightFirst();
	private static Comparator<Coordinate> upLeftFirst = new UpLeftFirst();
	private static Comparator<Coordinate> downLeftFirst = new DownLeftFirst();

	@Override
	public LinkedList<Coordinate> findPath(Coordinate current, Coordinate target, MapTile[][] map) {

		LinkedList<Coordinate> path = new LinkedList<>();

		ArrayList<Coordinate> nextAvailables = nextAvaialbePoints(current.x, current.y, map);

		/* no more available points before target is reached */
		if (nextAvailables.size() == 0) {
			return null;
		}

		/* sort the available points to let the overall trend direct to target */
		if (target.x >= current.x && target.y >= current.y) {
			nextAvailables.sort(upRightFirst);

		}else if (target.x >= current.x && target.y <= current.y) {
			nextAvailables.sort(downRightFirst);

		}else if (target.x <= current.x && target.y >= current.y) {
			nextAvailables.sort(upLeftFirst);

		}else if (target.x <= current.x && target.y <= current.y) {
			nextAvailables.sort(downLeftFirst);
		}


		for (Coordinate nextPoint : nextAvailables) {
			/* reach target, jsut return with path */
			if (nextPoint.equals(target)) {
				path.add(nextPoint);
				return path;
			}

			/* recursion. Mark current point as a wall so that it will go back to this point */
			MapTile[][] mapCopy = map.clone();
			mapCopy[current.x][current.y] = new MapTile(MapTile.Type.WALL);
			LinkedList<Coordinate> nextPath = findPath(nextPoint, target, mapCopy);
			if (nextPath != null) {
				path.add(nextPoint);
				path.addAll(nextPath);
				return path;
			}
		}


		return null;
	}


	private ArrayList<Coordinate> nextAvaialbePoints(int x, int y, MapTile[][] map) {
		ArrayList<Coordinate> availables = new ArrayList<Coordinate>();
		if (isAccessable(x - 1, y, map)) {
			availables.add(new Coordinate(x - 1, y));
		}
		if (isAccessable(x + 1, y, map)) {
			availables.add(new Coordinate(x + 1, y));
		}
		if (isAccessable(x, y - 1, map)) {
			availables.add(new Coordinate(x, y - 1));
		}
		if (isAccessable(x, y + 1, map)) {
			availables.add(new Coordinate(x, y + 1));
		}
		return availables;
	}

	private boolean isAccessable(int x, int y, MapTile[][] map) {
		if (x < 0 || y < 0 || x >= map.length || y >= map[0].length) {
			return false;
		}

		if (map[x][y]!=null && map[x][y].getType() == MapTile.Type.WALL) {
			return false;
		}

		return true;
	}


	private static class UpRightFirst implements Comparator<Coordinate> {
		@Override
		public int compare(Coordinate o1, Coordinate o2) {
			if (o1.x < o2.x || o1.y < o2.y) {
				return 1;
			}

			if (o1.x == o2.x && o1.y == o2.y) {
				return 0;
			}
			return -1;
		}
	}

	private static class DownRightFirst implements Comparator<Coordinate> {
		@Override
		public int compare(Coordinate o1, Coordinate o2) {
			if (o1.x < o2.x || o1.y > o2.y) {
				return 1;
			}

			if (o1.x == o2.x && o1.y == o2.y) {
				return 0;
			}
			return -1;
		}
	}

	private static class UpLeftFirst implements Comparator<Coordinate> {
		@Override
		public int compare(Coordinate o1, Coordinate o2) {
			if (o1.x > o2.x || o1.y < o2.y) {
				return 1;
			}

			if (o1.x == o2.x && o1.y == o2.y) {
				return 0;
			}
			return -1;
		}
	}

	private static class DownLeftFirst implements Comparator<Coordinate> {
		@Override
		public int compare(Coordinate o1, Coordinate o2) {
			if (o1.x > o2.x || o1.y > o2.y) {
				return 1;
			}

			if (o1.x == o2.x && o1.y == o2.y) {
				return 0;
			}
			return -1;
		}
	}


}
