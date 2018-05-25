package mycontroller;

import controller.CarController;
import mycontroller.PositionStrategy.NextPositionFactory;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import tiles.MapTile;
import utilities.Coordinate;

import java.util.LinkedList;

import static mycontroller.MyAIController.MAX_HEALTH;
import static mycontroller.PositionStrategy.HealPositionStrategy.HEALTH_THRESHOLD;

public class Drive {

	private LinkedList<Coordinate> coordinatesInPath;
	private Coordinate targetPosition;
	private Coordinate nextPosition;
	private float COORDINATE_DEVIATION = 0.4f;


	public Drive(Coordinate initPosition) {
		this.coordinatesInPath = new LinkedList<>();
		coordinatesInPath = new LinkedList<>();
		targetPosition = initPosition;
		nextPosition = initPosition;
	}

	public OperationType getOperation(MapRecorder mapRecorder, CarController car) {
		Coordinate currentPosition = new Coordinate(Math.round(car.getX()), Math.round(car.getY()));

		/* if reaches a targe position, a strategy should be applied to find next target position */

		if (Math.abs(targetPosition.x - car.getX()) <= COORDINATE_DEVIATION && Math.abs(targetPosition.y - car.getY()) <= COORDINATE_DEVIATION) {
			targetPosition = NextPositionFactory.chooseNextPositionStrategy(car, mapRecorder).
					getNextPosition(mapRecorder, car);
		}


		/* health check interrupt */
		if (car.getHealth() <= HEALTH_THRESHOLD) {
			targetPosition = NextPositionFactory.chooseNextPositionStrategy(car, mapRecorder).getNextPosition(mapRecorder, car);
			coordinatesInPath.clear();
		}

		/* recorded check interrupt */
		if (mapRecorder.isRecorded(targetPosition)) {
			targetPosition = NextPositionFactory.chooseNextPositionStrategy(car, mapRecorder).getNextPosition(mapRecorder, car);
			coordinatesInPath.clear();
		}

		if (coordinatesInPath.size() == 0) {
			coordinatesInPath = mapRecorder.findPath(currentPosition, targetPosition);
		}


		if (nextPosition == null || (Math.abs(nextPosition.x - car.getX()) <= COORDINATE_DEVIATION && Math.abs(nextPosition.y - car.getY()) <= COORDINATE_DEVIATION)) {
			nextPosition = coordinatesInPath.poll();
			if(mapRecorder.isHealth(currentPosition) && car.getHealth() < MAX_HEALTH){
				nextPosition = currentPosition;
				return car.getSpeed()>0 ? OperationType.BRAKE:OperationType.FORWARD_ACCE;
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
		}catch(Exception e){
			while (coordinatesInPath.size()==0){
				MyAIController.logger.info(String.format("nextPosition:({%s})",nextPosition));
				mapRecorder.addPoint(targetPosition.x,targetPosition.y, new MapTile(MapTile.Type.ROAD));
				targetPosition = NextPositionFactory.chooseNextPositionStrategy(car, mapRecorder).
						getNextPosition(mapRecorder, car);
				coordinatesInPath = mapRecorder.findPath(currentPosition, targetPosition);
				nextPosition = coordinatesInPath.poll();
			}
			//			System.exit(-1);
		}
		if (mapRecorder.isLava(currentPosition)) {
			coordinatesInPath = mapRecorder.findPath(currentPosition, targetPosition);
		}

		return result;

	}


	//TODO debug log print
	private void printPathInfo() {
		String log = String.format("Current:(%s) || Target:(%s) || ",nextPosition.toString(), targetPosition.toString());
		for (Coordinate co : coordinatesInPath) {
			log = log + "(" + co + ")" + ",";
		}
		MyAIController.logger.info(log);

	}

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
