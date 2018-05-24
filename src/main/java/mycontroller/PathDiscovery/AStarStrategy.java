package mycontroller.PathDiscovery;

import mycontroller.PathDiscovery.IDiscoveryStrategy;
import mycontroller.PathDiscovery.Node;
import tiles.MapTile;
import utilities.Coordinate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * ClassName: AStarStrategy
 *
 * @author kesar
 * @Description: A星算法
 */
public class AStarStrategy implements IDiscoveryStrategy {
	public final static MapTile.Type BAR = MapTile.Type.WALL; // 障碍值
	public final static MapTile.Type PATH = null; // 路径
	public final static int DIRECT_VALUE = 10; // 横竖移动代价

	PriorityQueue<Node> openList; // 优先队列(升序)
	ArrayList<Node> closeList;
	LinkedList<Coordinate> pathList;
	Node current;
	Node target;


	public LinkedList<Coordinate> findPath(Coordinate current, Coordinate target, MapTile[][] map) {
		pathList = new LinkedList<>();
		openList = new PriorityQueue<>();
		closeList = new ArrayList<>();


		this.current = new Node(current);
		this.target = new Node(target);
		openList.add(this.current);
		moveNodes(map);

		return revertList(pathList);

	}

	private LinkedList<Coordinate> revertList(LinkedList<Coordinate> list){
		LinkedList<Coordinate> newList = new LinkedList<>();
		for(Coordinate co:list){
			newList.addFirst(co);
		}
		return newList;
	}
//	/**
//	 * 开始算法
//	 */
//	public void start(MapInfo mapInfo)
//	{
//		if(mapInfo==null) return;
//		// clean
//		openList.clear();
//		closeList.clear();
//		// 开始搜索
//		openList.add(mapInfo.start);
//
//	}

	/**
	 * 移动当前结点
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

	/**
	 * 在二维数组中绘制路径
	 */
	private void drawPath(MapTile[][] maps) {
//		if(target==null||maps==null) return;
//		System.out.println("总代价：" + target.getG());
		while (target != null) {
			Coordinate c = target.getCoordinate();
			pathList.add(c);
			target = target.getParent();
		}
	}

	/**
	 * 添加所有邻结点到open表
	 */
	private void addNeighborNodeInOpen(MapTile[][] map, Node current) {
		int x = current.getCoordinate().x;
		int y = current.getCoordinate().y;
		// 左
		addNeighborNodeInOpen(map, current, x - 1, y, DIRECT_VALUE);
		// 上
		addNeighborNodeInOpen(map, current, x, y - 1, DIRECT_VALUE);
		// 右
		addNeighborNodeInOpen(map, current, x + 1, y, DIRECT_VALUE);
		// 下
		addNeighborNodeInOpen(map, current, x, y + 1, DIRECT_VALUE);
	}

	/**
	 * 添加一个邻结点到open表
	 */
	private void addNeighborNodeInOpen(MapTile[][] map, Node current, int x, int y, int value) {
		if (isAccessable(x, y, map)) {
//			Node end = mapInfo.end;
			Coordinate coord = new Coordinate(x, y);
			int G = current.getG() + value; // 计算邻结点的G值
			Node child = findNodeInOpen(coord);
			if (child == null) {
				int H = calcH(target.getCoordinate(), coord); // 计算H值
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

	/**
	 * 从Open列表中查找结点
	 */
	private Node findNodeInOpen(Coordinate coord) {
		if (coord == null || openList.isEmpty()) return null;
		for (Node node : openList) {
			if (node.getCoordinate().equals(coord)) {
				return node;
			}
		}
		return null;
	}


	/**
	 * 计算H的估值：“曼哈顿”法，坐标分别取差值相加
	 */
	private int calcH(Coordinate end, Coordinate coord) {
		return Math.abs(end.x - coord.x)
				+ Math.abs(end.y - coord.y);
	}

	/**
	 * 判断结点是否是最终结点
	 */
	private boolean isEndNode(Coordinate end, Coordinate coord) {
		return coord != null && end.equals(coord);
	}

	/**
	 * 判断结点能否放入Open列表
	 */
//	private boolean canAddNodeToOpen(MapInfo mapInfo,int x, int y)
//	{
//		// 是否在地图中
//		if (x < 0 || x >= mapInfo.width || y < 0 || y >= mapInfo.hight) return false;
//		// 判断是否是不可通过的结点
//		if (mapInfo.maps[y][x] == BAR) return false;
//		// 判断结点是否存在close表
//		if (isCoordinateInClose(x, y)) return false;
//
//		return true;
//	}
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

	/**
	 * 判断坐标是否在close表中
	 */
	private boolean isCoordinateInClose(Coordinate coord) {
		return coord != null && isCoordinateInClose(coord.x, coord.y);
	}

	/**
	 * 判断坐标是否在close表中
	 */
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
