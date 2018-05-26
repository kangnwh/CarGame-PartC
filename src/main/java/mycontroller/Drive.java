package mycontroller;

import controller.CarController;
import mycontroller.PositionStrategy.*;
import tiles.MapTile;
import utilities.Coordinate;

import java.util.LinkedList;

import static mycontroller.MyAIController.MAX_HEALTH;
import static mycontroller.PositionStrategy.HealPositionStrategy.HEALTH_THRESHOLD;

/**
 * Drive
 * This is used to calculate which point should go to and the path how to go to that point. Then based on the status of the car and the next
 * position in the calculated path, Drive provides an OPERATION to CarController so that CarController can control the car accordingly.
 */


public class Drive {
	private static int EXIT_STEP_THREHOLD = 10;
	private LinkedList<Coordinate> coordinatesInPath;
	private Coordinate targetPosition;
	private Coordinate nextPosition;
	private float COORDINATE_DEVIATION = 0.4f;
	private INextPositionStrategy nextPositionStrategy;


	public Drive(Coordinate initPosition) {
		this.coordinatesInPath = new LinkedList<>();
		coordinatesInPath = new LinkedList<>();
		nextPositionStrategy = new ExplorePositionStrategy();
		targetPosition = initPosition;
		nextPosition = initPosition;

	}


	/**
	 * This method provides the operation to carController to move the car to next position
	 * @param mapRecorder the map
	 * @param car aiController
	 * @return operation for the car to move
	 */
	public OperationType getOperation(MapRecorder mapRecorder, CarController car) {

		Coordinate currentPosition = new Coordinate(Math.round(car.getX()), Math.round(car.getY()));

		/* in some special situations, the original navigation need to be interrupted for other strategy   */
		if (interruptCheck(mapRecorder, car)) {
			nextPositionStrategy = NextPositionFactory.chooseNextPositionStrategy(car, mapRecorder);
			targetPosition = nextPositionStrategy.getNextPosition(mapRecorder, car);
			coordinatesInPath.clear();
			coordinatesInPath = mapRecorder.findPath(currentPosition, targetPosition,car);
			printPathInfo();

		}

		if (nextPosition == null
				|| (Math.abs(nextPosition.x - car.getX()) <= COORDINATE_DEVIATION
				&& Math.abs(nextPosition.y - car.getY()) <= COORDINATE_DEVIATION)) {
			nextPosition = coordinatesInPath.poll();
			if (mapRecorder.isHealth(currentPosition) && car.getHealth() < MAX_HEALTH) {
				nextPosition = currentPosition;
				return car.getSpeed() == 0 ? OperationType.FORWARD_ACCE : OperationType.BRAKE;
			}
		}

		/* coordinates in a path must be adjacent */
		OperationType result = OperationType.FORWARD_ACCE;
		try {
			if (Math.abs(nextPosition.x - car.getX()) > COORDINATE_DEVIATION) {
				result = moveX(car, car.getX(), nextPosition);

			} else if (Math.abs(nextPosition.y - car.getY()) > COORDINATE_DEVIATION) {
				result = moveY(car, car.getY(), nextPosition);
			}
		} catch (Exception e) {
			MyAIController.logger.info(String.format("nextPosition:({%s})", nextPosition));

			while(nextPosition == null){
				mapRecorder.addPoint(targetPosition, new MapTile(MapTile.Type.ROAD));
				targetPosition = nextPositionStrategy.getNextPosition(mapRecorder,car);
				coordinatesInPath = mapRecorder.findPath(currentPosition,targetPosition,car);
				nextPosition = coordinatesInPath.poll();
			}
		}

		return result;

	}


	/**
	 * in some special situations, the original navigation need to be interrupted for other strategy
	 * @param mapRecorder the map
	 * @param car aiController
	 * @return true if the navigation should be interrupted
	 */
	private boolean interruptCheck(MapRecorder mapRecorder, CarController car) {

		if (coordinatesInPath.size() == 0) return true;

		/* target reaches interrupt*/
		if (Math.abs(targetPosition.x - car.getX()) <= COORDINATE_DEVIATION
				&& Math.abs(targetPosition.y - car.getY()) <= COORDINATE_DEVIATION) {
			return true;
		}

		/* recorded check interruptCheck */
		if ((mapRecorder.isRecorded(targetPosition) && !targetPosition.equals(mapRecorder.getExit()))
				&& (nextPositionStrategy instanceof ExplorePositionStrategy)) {
			return true;
		}

		/* health check interruptCheck */
		if (car.getHealth() <= HEALTH_THRESHOLD && mapRecorder.hasHealthTrap()) {
			if(car.getKey() == 1 && Math.abs(mapRecorder.getExit().x-car.getX()) + Math.abs(mapRecorder.getExit().y-car.getY()) <=EXIT_STEP_THREHOLD) {
				return false;
			}
			if ((nextPositionStrategy instanceof HealPositionStrategy) && mapRecorder.isHealth(targetPosition)) {
				return false;
			}else{
				return true;
			}
		}

		/* whether if next key is found */
		if(mapRecorder.getKey(car.getKey() - 1) != null ){
			if(!(nextPositionStrategy instanceof KeyPositionStrategy)) {
				return true;
			}else{
				return false;
			}
		}

		return false;

	}

	/**
	 * Print the path information
	 */
	private void printPathInfo() {
		String log = String.format("Current:(%s) || Target:(%s) || ", nextPosition, targetPosition);
		for (Coordinate co : coordinatesInPath) {
			log = log + "(" + co + ")" + ",";
		}
		MyAIController.logger.info(log);

	}


	/**
	 * Handling the move of horizontal
	 * @param car aiController
	 * @param currentX current X coordinate
	 * @param next next Position to go
	 * @return The operation for car to move
	 */
	private OperationType moveX(CarController car, float currentX, Coordinate next) {
		int targetX = next.x;
		switch (car.getOrientation()) {
			case EAST:
				if (currentX > targetX) {
					return OperationType.REVERSE_ACCE;
				} else if (currentX < targetX) {
					return OperationType.FORWARD_ACCE;
				}
				break;
			case WEST:
				if (currentX > targetX) {
					return OperationType.FORWARD_ACCE;
				} else if (currentX < targetX) {
					return OperationType.REVERSE_ACCE;
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

	/**
	 * Handling the move of vertical
	 * @param car aiController
	 * @param currentY current Y coordinate
	 * @param next next Position to go
	 * @return The operation for car to move
	 */
	private OperationType moveY(CarController car, float currentY, Coordinate next) {
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
					return OperationType.REVERSE_ACCE;
				} else if (currentY < nextY) {
					return OperationType.FORWARD_ACCE;
				}
				break;
			case SOUTH:
				if (currentY > nextY) {
					return OperationType.FORWARD_ACCE;
				} else if (currentY < nextY) {
					return OperationType.REVERSE_ACCE;
				}
				break;
			default:
				return OperationType.FORWARD_ACCE;
		}

		return null;
	}


}
