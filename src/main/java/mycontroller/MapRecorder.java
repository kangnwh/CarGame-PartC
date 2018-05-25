package mycontroller;

import controller.CarController;
import mycontroller.PathDiscovery.IDiscoveryStrategy;
import tiles.HealthTrap;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;
import world.World;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * MapRecorder
 * This is used to record all coordinates that the car can see and generate a map for drive decision making
 */

public class MapRecorder {

	private HashMap<Coordinate, MapTile> mapMatrix;
	private int xLength;  //Width of the map
	private int yLength;  //Height of the map
	private Coordinate exit; //The exit of the maze
	private HashMap<Integer, Coordinate> keys; //Keys in the map
	private IDiscoveryStrategy discoveryStrategy; //The strategy to find the path
	private Boolean hasHealthTrap;  //If healthtrap has been found


	public MapRecorder(IDiscoveryStrategy discoveryStrategy, HashMap<Coordinate, MapTile> roadMap) {
		xLength = World.MAP_WIDTH;
		yLength = World.MAP_HEIGHT;
		mapMatrix = new HashMap<>();
		keys = new HashMap<>();
		this.discoveryStrategy = discoveryStrategy;
		hasHealthTrap=false;

		//Import the road map getting from the world to the mapRecorder,but only put these which are not road type
		for (Map.Entry<Coordinate, MapTile> entry : roadMap.entrySet()) {
			Coordinate co = entry.getKey();
			MapTile tile = entry.getValue();
			if (!tile.isType(MapTile.Type.ROAD)) {
				mapMatrix.put(co, tile);
			}
			if (tile.isType(MapTile.Type.FINISH)) {
				exit = co;
			}
		}

	}

	/**
	 * Add the position which has been founded to the map
	 * @param x x coordinate of the position
	 * @param y y coordinate of the position
	 * @param tile type of the position
	 */
	public void addPoint(int x, int y, MapTile tile) {
		if(x < xLength && y < yLength) {
			Coordinate co = new Coordinate(x, y);
			mapMatrix.put(co, tile);

			if (isKey(tile)) {
				addKey(co, tile);
			}
		}

	}

	/**
	 * Add the position which has been founded to the map
	 * @param co coordinate of the position
	 * @param tile type of the position
	 */
	public void addPoint(Coordinate co, MapTile tile) {
		this.addPoint(co.x, co.y, tile);

	}

	/**
	 * Add what the car can detect to the mapRecorder
	 * @param carView the view around your car
	 */
	public void addPointsByCarView(HashMap<Coordinate, MapTile> carView) {
		for (Map.Entry entry : carView.entrySet()) {
			Coordinate co = (Coordinate) entry.getKey();
			MapTile tile = (MapTile) entry.getValue();
			if (co.x > 0 && co.y > 0) {
				this.addPoint(co, tile);
			}
			if(tile instanceof HealthTrap){
				hasHealthTrap=true;
			}
		}
	}

	/**
	 * Translate the map from HashMap to array for search easily
	 * @return the map of array form
	 */
	public MapTile[][] getMap() {
		MapTile[][] map = new MapTile[xLength][yLength];
		for (Map.Entry<Coordinate,MapTile> entry : mapMatrix.entrySet()) {
			Coordinate co = entry.getKey();
			MapTile tile = entry.getValue();
			map[co.x][co.y] = tile;
		}
		return map;
	}

	/**
	 * Judge if one position has been found
	 * @param coordinate the coordinate of a position
	 * @return if the position is recorded in the map
	 */
	public Boolean isRecorded(Coordinate coordinate) {
		return mapMatrix.get(coordinate) != null;
	}

	/**
	 * Find the exit of the maze
	 * @return coordinate of exit
	 */
	public Coordinate getExit() {
		return exit;
	}

	/**
	 * Put the key in key map
	 * @param co the coordinate of a key
	 * @param tile the type of the tile
	 */
	public void addKey(Coordinate co, MapTile tile) {
		keys.put(((LavaTrap) tile).getKey(), co);
	}

	/**
	 * Judge is one tile contains key
	 * @param tile the type of the position
	 * @return true if the type of the tile is lava and it has a key on it
	 */
	public boolean isKey(MapTile tile) {
		return tile instanceof LavaTrap && ((LavaTrap) tile).getKey() > 0;
	}

	/**
	 * Get the coordinate of the key according to its number
	 * @param key the number of the key
	 * @return the coordinate of the key
	 */
	public Coordinate getKey(int key) {
		return keys.get(key);
	}

	/**
	 * Check if the key of certain number is founded
	 * @param key the key number
	 * @return true if key is found
	 */
	public boolean keyFounded(int key) {
		return keys.containsKey(key);
	}

	/**
	 * Find the path between two position
	 * @param current the current position of the car
	 * @param target the target position of the car
	 * @return the path composed by the coordinate linkedList
	 */
	public LinkedList<Coordinate> findPath(Coordinate current, Coordinate target, CarController car) {
		MapTile[][] tempMap = getMap();
		int x = (int)car.getX();
		int y = (int)car.getY();
		switch(car.getOrientation()){
			//TODO useless !!!! WHY !!!
			case SOUTH:
				if(y+1 < yLength) tempMap[x][y+1] = new MapTile(MapTile.Type.WALL);
				if(y+2 < yLength) tempMap[x][y+2] = new MapTile(MapTile.Type.WALL);
				break;
			case NORTH:
				if(y-1 >= 0) tempMap[x][y-1] = new MapTile(MapTile.Type.WALL);
				if(y-2 >=0 ) tempMap[x][y-2] = new MapTile(MapTile.Type.WALL);
				break;
			case WEST:
				if(x+1 < xLength) tempMap[x+1][y] = new MapTile(MapTile.Type.WALL);
				if(x+2 < xLength ) tempMap[x+2][y] = new MapTile(MapTile.Type.WALL);
				break;
			case EAST:
				if(x-1 >= 0) tempMap[x-1][y] = new MapTile(MapTile.Type.WALL);
				if(x-2 >=0 ) tempMap[x-2][y] = new MapTile(MapTile.Type.WALL);
				break;
		}
		return discoveryStrategy.findPath(current, target, tempMap);
	}

	/**
	 * Check if healthTrap has been found
	 * @return true if healthTrap has been found
	 */
	public boolean hasHealthTrap(){
		return this.hasHealthTrap;
	}

	/**
	 * Check if a position is lava
	 * @param coordinate the coordinate of a position
	 * @return true if the position is lava
	 */
	public boolean isLava(Coordinate coordinate){
		MapTile mapTile = mapMatrix.get(coordinate);
		if(mapTile != null && mapTile instanceof LavaTrap){
			return true;
		}
		return false;

	}

	/**
	 * Judge if a position is healthTrap
	 * @param coordinate coordinate the the position
	 * @return true is ite is a healthTrap
	 */
	public boolean isHealth(Coordinate coordinate){
		MapTile mapTile = mapMatrix.get(coordinate);
		if(mapTile != null && mapTile instanceof HealthTrap){
			return true;
		}
		return false;

	}

	/**
	 * Get the mapMatrix
	 * @return maoMatrix
	 */
	public HashMap<Coordinate, MapTile> getMapMatrix() {
		return mapMatrix;
	}

	public IDiscoveryStrategy getDiscoveryStrategyInstance(){
		return discoveryStrategy;
	}
}
