package mycontroller;

import controller.CarController;
import mycontroller.PositionStrategy.*;
import utilities.Coordinate;

import java.util.LinkedList;

import static mycontroller.MyAIController.MAX_HEALTH;
import static mycontroller.PositionStrategy.HealPositionStrategy.HEALTH_THRESHOLD;

public class Drive {

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

	// provide the operation for the next controller

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
//			while(nextPosition!=null){
//
//			}
		}

		return result;

	}


	/* in some special situations, the original navigation need to be interrupted for other strategy   */
	private boolean interruptCheck(MapRecorder mapRecorder, CarController car) {

		if (coordinatesInPath.size() == 0) return true;

		/* target reaches interrupt*/
		if (Math.abs(targetPosition.x - car.getX()) <= COORDINATE_DEVIATION
				&& Math.abs(targetPosition.y - car.getY()) <= COORDINATE_DEVIATION) {
			return true;
		}

		/* recorded check interruptCheck */
		if (mapRecorder.isRecorded(targetPosition) && (nextPositionStrategy instanceof ExplorePositionStrategy)) {
			return true;
		}

		/* health check interruptCheck */
		if (car.getHealth() <= HEALTH_THRESHOLD) {
			if (nextPositionStrategy instanceof HealPositionStrategy && mapRecorder.isHealth(nextPosition)) {
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

		Coordinate currentPosition = new Coordinate(Math.round(car.getX()), Math.round(car.getY()));
		if (mapRecorder.isLava(currentPosition)) return true;




		return false;

	}

	//TODO debug log print

	private void printPathInfo() {
		String log = String.format("Current:(%s) || Target:(%s) || ", nextPosition, targetPosition);
		for (Coordinate co : coordinatesInPath) {
			log = log + "(" + co + ")" + ",";
		}
		MyAIController.logger.info(log);

	}

	// handling the operation with different situations

	private OperationType turn(CarController car, float currentX, float currentY, Coordinate next) {
		int targetX = next.x;
		int targetY = next.y;
		switch (car.getOrientation()) {
			case EAST:
				if (currentX < targetX) {
					if (currentY < targetY) {
						return OperationType.TURN_NORTH;
					}
					return OperationType.TURN_SOUTH;
				} else {
					return OperationType.TURN_WEST;
				}
			case WEST:
				if (currentX > targetX) {
					if (currentY < targetY) {
						return OperationType.TURN_NORTH;
					}
					return OperationType.TURN_SOUTH;
				} else {
					return OperationType.TURN_EAST;
				}
			case NORTH:
				if (currentY < targetY) {
					if (currentX > targetX) {
						return OperationType.TURN_WEST;
					}
					return OperationType.TURN_EAST;
				} else {
					return OperationType.TURN_SOUTH;
				}
			case SOUTH:
				if (currentY > targetY) {
					if (currentX > targetX) {
						return OperationType.TURN_WEST;
					}
					return OperationType.TURN_EAST;
				} else if (currentX <= targetX) {
					return OperationType.TURN_NORTH;
				}
				break;
			default:
				return OperationType.FORWARD_ACCE;
		}

		return null;
	}

	// Handling the move of horizontal

	private OperationType moveX(CarController car, float currentX, Coordinate next) {
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

	// Handling the move of vertical

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
