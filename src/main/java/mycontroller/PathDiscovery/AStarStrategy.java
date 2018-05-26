package mycontroller.PathDiscovery;

import tiles.HealthTrap;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;


/**
 * AStar algorithm
 * This algorithm calculate a path from start point to end point based on a matric of cost.
 * Each node in the map has a cost from start point.
 * Check here for detail: https://github.com/mattantonelli/AStar
 */

public class AStarStrategy implements IDiscoveryStrategy {


	/* cost of different type of maptile */
	public final static int ROAD_VALUE = 5;
	public final static int LAVA_VALUE = 100000;
	private final static int TURN_VALUE = 10;
	private final static int HEALTH_VALUE = 0;

	private PriorityQueue<Node> openList;
	private ArrayList<Node> closeList;
	private LinkedList<Coordinate> pathList;
	private Node current;
	private Node target;


	public LinkedList<Coordinate> findPath(Coordinate current, Coordinate target, MapTile[][] map) {
		pathList = new LinkedList<>();
		openList = new PriorityQueue<>();
		closeList = new ArrayList<>();


		this.current = new Node(current);
		if(map[current.x][current.y] instanceof LavaTrap){
			this.current.setG(LAVA_VALUE);
		}

		this.target = new Node(target);
		openList.add(this.current);
		moveNodes(map);

		return revertList(pathList);

	}

	/* revert the final list so that the very first point comes first */
	private LinkedList<Coordinate> revertList(LinkedList<Coordinate> list) {
		LinkedList<Coordinate> newList = new LinkedList<>();
		for (Coordinate co : list) {
			newList.addFirst(co);
		}
		try {
			/* remove the current point and a potiental current point */
			newList.removeFirst();
			newList.removeFirst();
		} catch (Exception e) {

		}
		return newList;
	}

	public int getCost() {
		if (openList.size() > 0) {
			return openList.peek().getG();
		} else {
			return 0;
		}
	}

	/**
	 *  try to move find the path which cost least
	 *
	 */
	private void moveNodes(MapTile[][] map) {
		while (!openList.isEmpty()) {
			if (isCoordinateInClose(target.getCoordinate())) {
				drawPath(map);
				break;
			}
			Node current = openList.poll();
			closeList.add(current);
			addNeighborNodeInOpen(map, current);
		}
	}

	private void drawPath(MapTile[][] maps) {
		while (target != null) {
			Coordinate c = target.getCoordinate();
			pathList.add(c);
			target = target.getParent();
		}
	}


	private void addNeighborNodeInOpen(MapTile[][] map, Node current) {
		int x = current.getCoordinate().x;
		int y = current.getCoordinate().y;

		addNeighborNodeInOpen(map, current, x - 1, y);

		addNeighborNodeInOpen(map, current, x, y - 1);

		addNeighborNodeInOpen(map, current, x + 1, y);

		addNeighborNodeInOpen(map, current, x, y + 1);
	}


	private void addNeighborNodeInOpen(MapTile[][] map, Node current, int x, int y) {
		if (isAccessable(x, y, map)) {

			Coordinate coord = new Coordinate(x, y);
			int cost = ROAD_VALUE;

			if (current.getParent().getCoordinate().x != x
					&& current.getParent().getCoordinate().y != y) {
				cost += TURN_VALUE;
			}

			if (map[x][y] instanceof LavaTrap) {
				cost *= LAVA_VALUE;
			}else if (map[x][y] instanceof HealthTrap){
				cost = HEALTH_VALUE;
			}


			int G = current.getG() + cost;
			Node child = findNodeInOpen(coord);
			if (child == null) {
				int H = calcH(target.getCoordinate(), coord);
				if (isEndNode(target.getCoordinate(), coord)) {
					child = target;
					child.setParent(current);
					child.setG(G);
					child.setH(H);
				} else {
					child = new Node(coord, current, G, H);
				}
				openList.add(child);
			} else if (child.getG() > G) {
				child.setG(G);
				child.setParent(current);
				openList.add(child);
			}
		}
	}

	private Node findNodeInOpen(Coordinate coord) {
		if (coord == null || openList.isEmpty()) return null;
		for (Node node : openList) {
			if (node.getCoordinate().equals(coord)) {
				return node;
			}
		}
		return null;
	}

	private int calcH(Coordinate end, Coordinate coord) {
		return Math.abs(end.x - coord.x)
				+ Math.abs(end.y - coord.y);
	}


	private boolean isEndNode(Coordinate end, Coordinate coord) {
		return coord != null && end.equals(coord);
	}


	private boolean isAccessable(int x, int y, MapTile[][] map) {

		// Whether in the map
		if (x < 0 || y < 0 || x >= map.length || y >= map[0].length) {
			return false;
		}

		// whether can access
		if (map[x][y] != null && map[x][y].getType() == MapTile.Type.WALL) {
			return false;
		}

		// whether already in close
		if (isCoordinateInClose(x, y)) return false;

		return true;
	}


	private boolean isCoordinateInClose(Coordinate coord) {
		return coord != null && isCoordinateInClose(coord.x, coord.y);
	}


	private boolean isCoordinateInClose(int x, int y) {
		if (closeList.isEmpty()) return false;
		for (Node node : closeList) {
			if (node.getCoordinate().x == x && node.getCoordinate().y == y) {
				return true;
			}
		}
		return false;
	}
}
