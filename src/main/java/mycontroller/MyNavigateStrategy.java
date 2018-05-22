//package mycontroller;
//
//import controller.CarController;
//import tiles.MapTile;
//import utilities.Coordinate;
//import world.Car;
//
//import java.util.ArrayList;
//
///**
// * MyNavigateStrategy
// * <p>
// * Author Ning Kang
// * Date 16/5/18
// */
//
//public class MyNavigateStrategy implements INavigateStrategy {
//	private Coordinate nextPosition;
//
//	public MyNavigateStrategy(Coordinate originalPosition) {
//		this.nextPosition = originalPosition;
//	}
//
//	@Override
//	public Operation getOperation(MapTile[][] map, CarController carController) {
//		if (nextPosition.x == carController.getX() && nextPosition.y == carController.getY()) {
//			nextPosition = calculateNextPosition(map, carController);
//		}
//
//		return calculateNextOperation(map,carController);
//	}
//
//	private Coordinate calculateNextPosition(MapTile[][] map, CarController carController) {
//		//TODO algorithm needed
//		return new Coordinate(map.length / 2, map[0].length / 2);
//	}
//
//	private Operation calculateNextOperation(MapTile[][] map,CarController carController) {
//
//		//TODO algorithm needed
//		return new Operation(Operation.OperationType.FORWARD_ACCE,0.2f);
//	}
//}
