package mycontroller;

import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;

import java.util.HashMap;
import java.util.Map;

/**
 * MapRecorder
 * <p>
 * Author Ning Kang
 * Date 16/5/18
 */

public class MapRecorder {
	private HashMap<Coordinate, MapTile> mapMatrix;
	private int xLength;
	private int yLength;
	private boolean exitFounded;
	private Coordinate exit;
	private HashMap<Integer,Coordinate> keys;

	public MapRecorder() {
		xLength = 1;
		yLength = 1;
		exitFounded=false;
		exit=null;
		mapMatrix = new HashMap<>();
		keys=new HashMap<>();
	}

	public boolean isExitFounded() {
		return exitFounded;
	}

	public void setExitFounded(boolean exitFounded) {
		this.exitFounded = exitFounded;
	}

	public void addPoint(int x, int y, MapTile tile){
		if(x>xLength){
			xLength = x;
		}
		if(y>yLength){
			yLength = y;
		}

		Coordinate co=new Coordinate(x,y);
		mapMatrix.put(co,tile);

		if(isExit(tile)){
			setExit(co);
		}
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
		MapTile[][] map = new MapTile[xLength+1][yLength+1];
		for(Map.Entry entry:mapMatrix.entrySet()){
			Coordinate co = (Coordinate)entry.getKey();
			MapTile tile = (MapTile)entry.getValue();
			map[co.x][co.y] = tile;
		}
		return map;
	}

	public boolean isExit(MapTile tile){
		return tile.getType()==MapTile.Type.FINISH;
	}

	public Coordinate getExit() {
		return exit;
	}

	public void setExit(Coordinate exit) {
		setExitFounded(true);
		this.exit = exit;
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

}
