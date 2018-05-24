package mycontroller;

import mycontroller.PathDiscovery.IDiscoveryStrategy;
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
	private HashMap<Integer,Coordinate> keys;
	private IDiscoveryStrategy discoveryStrategy;



	public MapRecorder(IDiscoveryStrategy discoveryStrategy, HashMap<Coordinate,MapTile> roadMap) {
		xLength = World.MAP_WIDTH;
		yLength = World.MAP_HEIGHT;
		mapMatrix=new HashMap<>();
		keys=new HashMap<>();
		this.discoveryStrategy = discoveryStrategy;
		for(Map.Entry entry:roadMap.entrySet()){
			Coordinate co = (Coordinate)entry.getKey();
			MapTile tile = (MapTile)entry.getValue();
			if(!tile.isType(MapTile.Type.ROAD)){
				mapMatrix.put(co,tile);
			}
			if(tile.isType(MapTile.Type.FINISH)){
				exit=co;
			}
		}

	}

//	private void initialRoad(HashMap<Coordinate,MapTile> roadMap){
//		for(Map.Entry<Coordinate,MapTile> entry:roadMap.entrySet()){
//			addPoint(entry.getKey().x,entry.getKey().y,entry.getValue());
//		}
//	}

	public void addPoint(int x, int y, MapTile tile){

		Coordinate co=new Coordinate(x,y);
		mapMatrix.put(co,tile);

		if(isKey(tile)){
			addKey(co,tile);
		}

	}

	public void addPoint(Coordinate co,MapTile tile){
		this.addPoint(co.x,co.y,tile);

	}

	public void addPointsByCarView(HashMap<Coordinate,MapTile> carView){
		for(Map.Entry entry:carView.entrySet()){
			Coordinate co = (Coordinate)entry.getKey();
			MapTile tile = (MapTile)entry.getValue();
			if(co.x > 0 && co.y > 0) {
				this.addPoint(co, tile);
			}
		}
	}

	public MapTile[][] getMap(){
		MapTile[][] map = new MapTile[xLength][yLength];
		for(Map.Entry entry:mapMatrix.entrySet()){
			Coordinate co = (Coordinate)entry.getKey();
			MapTile tile = (MapTile)entry.getValue();
			map[co.x][co.y] = tile;
		}
		return map;
	}


	public Coordinate getExit() {
		return exit;
	}


	public void addKey(Coordinate co,MapTile tile){
		keys.put(((LavaTrap)tile).getKey(),co);
	}

	public boolean isKey(MapTile tile){
		return tile instanceof LavaTrap&&((LavaTrap) tile).getKey()>0;
	}

	public Coordinate getKey(int key){
		return keys.get(key);
	}

	public boolean keyFounded(int key){
		return keys.containsKey(key);
	}

	public HashMap<Coordinate, MapTile> getMapMatrix() {
		return mapMatrix;
	}

	public LinkedList<Coordinate> findPath(Coordinate current, Coordinate target){
		return discoveryStrategy.findPath(current,target,getMap());
	}



}
