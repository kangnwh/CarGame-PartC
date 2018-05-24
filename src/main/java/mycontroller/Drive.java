package mycontroller;

import controller.CarController;
import mycontroller.PositionStrategy.NextPositionFactory;
import utilities.Coordinate;

import java.util.LinkedList;

public class Drive {

	private LinkedList<Coordinate> coordinatesInPath;
	private Coordinate targetPosition;
	private Coordinate nextPosition;
	private float COORDINATE_DEVIATION = 0.3f;

	public Drive(Coordinate initPosition) {
		this.coordinatesInPath = new LinkedList<>();
		coordinatesInPath = new LinkedList<>();
		targetPosition = initPosition;
		nextPosition = initPosition;
	}

	public OperationType getOperation(MapRecorder mapRecorder, CarController car) {
		Coordinate currentPosition = new Coordinate(Math.round(car.getX()), Math.round( car.getY()));
		/* if reaches a targe position, a strategy should be applied to find next target position */
//		if (targetPosition.equals(currentPosition)) {
		 if (Math.abs(nextPosition.x - car.getX()) <= COORDINATE_DEVIATION && Math.abs(nextPosition.y - car.getY()) <= COORDINATE_DEVIATION){
			targetPosition = NextPositionFactory.chooseNextPositionStrategy(car, mapRecorder).
					getNextPosition(mapRecorder, car);
		}

		if (coordinatesInPath.size() == 0) {
			coordinatesInPath = mapRecorder.findPath(currentPosition, targetPosition);
			printPathInfo();
		}

		if (nextPosition == null || nextPosition.equals(currentPosition)) {
			nextPosition = coordinatesInPath.poll();
		}

		//TODO calculate operation to get to nextPositon

		/* coordinates in a path must be adjacent */
		OperationType result = OperationType.FORWARD_ACCE;
		if (Math.abs(nextPosition.x - car.getX()) > COORDINATE_DEVIATION) {
			result = moveX(car, car.getX(), nextPosition);

		} else if(Math.abs(nextPosition.y - car.getY()) > COORDINATE_DEVIATION) {
			result = moveY(car, car.getY(), nextPosition);
		}
		MyAIController.logger.info(result);
		return result;

	}

	//TODO debug log print
	private void printPathInfo() {
		String log= "";
		for (Coordinate co : coordinatesInPath) {
			log = log + "("+ co+")" + ",";
		}
		MyAIController.logger.info(log);

	}

	private OperationType moveX(CarController car,float currentX, Coordinate next) {
//		float currentX = x;
		int targetX = next.x;
		switch (car.getOrientation()) {
			case EAST:
				if (currentX > targetX) {
					return OperationType.TURN_WEST;
				} else if (currentX < targetX) {
					return OperationType.FORWARD_ACCE;
				}
				break;
			case WEST:
				if (currentX > targetX) {
					return OperationType.FORWARD_ACCE;
				} else if (currentX < targetX) {
					return OperationType.TURN_EAST;
				}
				break;
			case NORTH:
			case SOUTH:
				if (currentX > targetX) {
					return OperationType.TURN_WEST;
				} else if (currentX <= targetX) {
					return OperationType.TURN_EAST;
				}
				break;
			default:
				return OperationType.FORWARD_ACCE;
		}

		return null;
	}

	private OperationType moveY(CarController car, float currentY, Coordinate next) {
//		float currentY = currentY;
		int nextY = next.y;
		switch (car.getOrientation()) {
			case EAST:
			case WEST:
				if (currentY > nextY) {
					return OperationType.TURN_SOUTH;
				} else if (currentY <= nextY) {
					return OperationType.TURN_NORTH;
				}
				break;
			case NORTH:
				if (currentY > nextY) {
					return OperationType.TURN_SOUTH;
				} else if (currentY < nextY) {
					return OperationType.FORWARD_ACCE;
				}
				break;
			case SOUTH:
				if (currentY > nextY) {
					return OperationType.FORWARD_ACCE;
				} else if (currentY < nextY) {
					return OperationType.TURN_NORTH;
				}
				break;
			default:
				return OperationType.FORWARD_ACCE;
		}

		return null;
	}


}
