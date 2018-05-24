package mycontroller;

import controller.CarController;
import mycontroller.PositionStrategy.NextPositionFactory;
import utilities.Coordinate;

import java.util.LinkedList;

public class Drive {

	private LinkedList<Coordinate> coordinatesInPath;
	private Coordinate targetPosition;
	private Coordinate nextPosition;

	public Drive(Coordinate initPosition) {
		this.coordinatesInPath = new LinkedList<>();
		coordinatesInPath = new LinkedList<>();
		targetPosition = initPosition;
		coordinatesInPath.add(new Coordinate(3,3));
		coordinatesInPath.add(new Coordinate(4,3));
		coordinatesInPath.add(new Coordinate(5,3));
		coordinatesInPath.add(new Coordinate(6,3));
		coordinatesInPath.add(new Coordinate(6,4));
		coordinatesInPath.add(new Coordinate(6,5));
	}

	public OperationType getOperation(MapRecorder mapRecorder, CarController car) {
		Coordinate currentPosition = new Coordinate((int) car.getX(), (int) car.getY());
		/* if reaches a targe position, a strategy should be applied to find next target position */
		if (targetPosition.equals(currentPosition)) {
			targetPosition = NextPositionFactory.chooseNextPositionStrategy(car, mapRecorder).
					getNextPosition(mapRecorder, car);
		}

		if (coordinatesInPath.size() == 0) {
			coordinatesInPath = mapRecorder.findPath(currentPosition, targetPosition);
		}

		if(nextPosition.equals(currentPosition)){
			nextPosition = coordinatesInPath.poll();
		}


		//TODO calculate operation to get to nextPositon

		/* coordinates in a path must be adjacent */
		if (nextPosition.x != currentPosition.x) {
			return moveX(car,currentPosition,nextPosition);

		} else {
			return moveY(car,currentPosition,nextPosition);
		}

//		return null;
	}

	private OperationType moveX(CarController car, Coordinate current, Coordinate next) {
		int currentX = current.x;
		int targetX = next.x;
		switch (car.getOrientation()) {
			case EAST:
				if(currentX > targetX) {
					return OperationType.TURN_WEST;
				}else if(currentX < targetX){
					return OperationType.FORWARD_ACCE;
				}
				break;
			case WEST:
				if(currentX > targetX) {
					return OperationType.FORWARD_ACCE;
				}else if(currentX < targetX){
					return OperationType.TURN_EAST;
				}
				break;
			case NORTH:
			case SOUTH:
				if(currentX > targetX) {
					return OperationType.TURN_WEST;
				}else if(currentX < targetX){
					return OperationType.TURN_EAST;
				}
				break;
		}

		return null;
	}

	private OperationType moveY(CarController car, Coordinate current, Coordinate next) {
		int currentY = current.y;
		int nextY = next.y;
		switch (car.getOrientation()) {
			case EAST:
			case WEST:
				if(currentY > nextY) {
					return OperationType.TURN_WEST;
				}else if(currentY < nextY){
					return OperationType.TURN_NORTH;
				}
				break;
			case NORTH:
				if(currentY > nextY) {
					return OperationType.TURN_SOUTH;
				}else if(currentY < nextY){
					return OperationType.FORWARD_ACCE;
				}
				break;
			case SOUTH:
				if(currentY > nextY) {
					return OperationType.FORWARD_ACCE;
				}else if(currentY < nextY){
					return OperationType.TURN_NORTH;
				}
				break;
		}

		return null;
	}


}
