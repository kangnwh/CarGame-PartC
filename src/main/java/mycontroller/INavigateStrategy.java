package mycontroller;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;

import java.util.ArrayList;

/**
 * Strategy
 * <p>
 * Author Ning Kang
 * Date 16/5/18
 */

public interface INavigateStrategy {
	public Operation getOperation(MapTile[][] map, CarController carController);
}
