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
	private int xLength;
	private int yLength;
	private Coordinate exit;
	private HashMap<Integer, Coordinate> keys;
	private IDiscoveryStrategy discoveryStrategy;
	private Boolean hasHealthTrap;


	public MapRecorder(IDiscoveryStrategy discoveryStrategy, HashMap<Coordinate, MapTile> roadMap) {
		xLength = World.MAP_WIDTH;
		yLength = World.MAP_HEIGHT;
		mapMatrix = new HashMap<>();
		keys = new HashMap<>();
		this.discoveryStrategy = discoveryStrategy;
		hasHealthTrap=false;
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

	public void addPoint(int x, int y, MapTile tile) {
		if(x < xLength && y < yLength) {
			Coordinate co = new Coordinate(x, y);
			mapMatrix.put(co, tile);

			if (isKey(tile)) {
				addKey(co, tile);
			}
		}

	}

	public void addPoint(Coordinate co, MapTile tile) {
		this.addPoint(co.x, co.y, tile);

	}

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

	public MapTile[][] getMap() {
		MapTile[][] map = new MapTile[xLength][yLength];
		for (Map.Entry<Coordinate,MapTile> entry : mapMatrix.entrySet()) {
			Coordinate co = entry.getKey();
			MapTile tile = entry.getValue();
			map[co.x][co.y] = tile;
		}
		return map;
	}

	public Boolean isRecorded(Coordinate coordinate) {
		return mapMatrix.get(coordinate) != null;
	}

	public Coordinate getExit() {
		return exit;
	}


	public void addKey(Coordinate co, MapTile tile) {
		keys.put(((LavaTrap) tile).getKey(), co);
	}

	public boolean isKey(MapTile tile) {
		return tile instanceof LavaTrap && ((LavaTrap) tile).getKey() > 0;
	}

	public Coordinate getKey(int key) {
		return keys.get(key);
	}

	public boolean keyFounded(int key) {
		return keys.containsKey(key);
	}

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

	public boolean hasHealthTrap(){
		return this.hasHealthTrap;
	}

	public boolean isLava(Coordinate coordinate){
		MapTile mapTile = mapMatrix.get(coordinate);
		if(mapTile != null && mapTile instanceof LavaTrap){
			return true;
		}
		return false;

	}

	public boolean isHealth(Coordinate coordinate){
		MapTile mapTile = mapMatrix.get(coordinate);
		if(mapTile != null && mapTile instanceof HealthTrap){
			return true;
		}
		return false;

	}

	public HashMap<Coordinate, MapTile> getMapMatrix() {
		return mapMatrix;
	}

	public IDiscoveryStrategy getDiscoveryStrategyInstance(){
		return discoveryStrategy;
	}
}
