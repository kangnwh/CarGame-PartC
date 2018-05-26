package mycontroller;

import controller.CarController;
import mycontroller.PathDiscovery.AStarStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;

import java.util.HashMap;

/**
 * Controller pattern applied here.
 * This class is responsible for coordinate data from all other classes and give final command to the car.
 */

public class MyAIController extends CarController {

	public static Logger logger = LogManager.getFormatterLogger();
	public static int MAX_HEALTH = 100; //The max health of car
	public static int STUCK_TIMER = 30; //The times for car to do the operation when get stuck
	public static int CAPTURE_INTERVAL = 15;// the interval to capture the car status for detecting stucking


	private MapRecorder mapRecorder;
	private Drive drive;
	private OperationType currentOperation;
	private final float CAR_SPEED = 1.5f;
	private CarStatus lastStatus;
	private int stuckTimer;
	private int captureTimer;


	public static void printLog(String msg) {
		logger.info(msg);
	}


	public MyAIController(Car car) {
		super(car);
		Coordinate co = new Coordinate((int) car.getX(), (int) car.getY());

		drive = new Drive(co);
		mapRecorder = new MapRecorder(new AStarStrategy(), this.getMap());
		captureTimer = CAPTURE_INTERVAL;
		currentOperation = OperationType.FORWARD_ACCE;
	}


	@Override
	public void update(float delta) {

		// Gets what the car can see
		HashMap<Coordinate, MapTile> currentView = getView();
		mapRecorder.addPointsByCarView(currentView);

		// if the car is stuck, reverse and turn right for 10 delta to readjust its orientation

		if (captureTimer > 0) {
			captureTimer--;
		} else if (lastStatus == null) {
			captureTimer = CAPTURE_INTERVAL;
			lastStatus = new CarStatus(this);
		} else {
			captureTimer = CAPTURE_INTERVAL;
			if (stuckCheck()) {
				stuckTimer = STUCK_TIMER;

			}
			lastStatus = null;
		}

		if (stuckTimer > 0) {
			solveStuck(delta);
			stuckTimer--;
		} else {
			handleOperation(delta);

		}
		printLog(currentOperation.toString());

	}

	/**
	 * Check if the car is stuck on the wall
	 *
	 * @return true if the car get stuck on the wall
	 */
	private boolean stuckCheck() {
		/* stuck interrupt */
		CarStatus newStatus = new CarStatus(this);
		if (lastStatus != null
				&& lastStatus.equals(newStatus)
				&& lastStatus.getHealth() >= this.getHealth()
				) {
			printLog("stucked !");
			return true;
		}

		return false;
	}


	/**
	 * Control the car's move according to the current operation
	 *
	 * @param delta the time step
	 */
	private void solveStuck(float delta) {

		Coordinate co = new Coordinate((int) this.getX(), (int) this.getY());
		Coordinate left = new Coordinate((int) this.getX() - 1, (int) this.getY());
		Coordinate right = new Coordinate((int) this.getX() + 1, (int) this.getY());
		Coordinate up = new Coordinate((int) this.getX(), (int) this.getY() + 1);
		Coordinate down = new Coordinate((int) this.getX() - 1, (int) this.getY() - 1);


		float angle = getAngle();
		HashMap<Coordinate,MapTile> currentMap = mapRecorder.getMapMatrix();
		if(currentMap.get(left)!=null && currentMap.get(left).getType() != MapTile.Type.WALL){
			if(angle > 180 && angle < 270) stuckTurnLeft(delta);
			else if(angle < 180 && angle > 90) stuckTurnRight(delta);
			else applyForwardAcceleration();
			return ;
		}

		if(currentMap.get(right)!=null && currentMap.get(right).getType() != MapTile.Type.WALL){
			if(angle > 0 && angle < 90) stuckTurnLeft(delta);
			else if(angle < 360 && angle > 270) stuckTurnRight(delta);
			else applyForwardAcceleration();
			return ;
		}

		if(currentMap.get(up)!=null && currentMap.get(up).getType() != MapTile.Type.WALL){
			if(angle > 90 && angle < 180) stuckTurnLeft(delta);
			else if(angle < 90 && angle > 0) stuckTurnRight(delta);
			else applyForwardAcceleration();
			return ;
		}

		if(currentMap.get(down)!=null && currentMap.get(down).getType() != MapTile.Type.WALL){
			if(angle > 270 && angle < 360) stuckTurnLeft(delta);
			else if(angle < 270 && angle > 180) stuckTurnRight(delta);
			else applyForwardAcceleration();
			return ;
		}

//		switch (currentOperation) {
//			case TURN_WEST:
//				if (angle > 0 && angle <= 180) {
//					stuckTurnLeft(delta);
//				} else {
//					stuckTurnRight(delta);
//				}
//				break;
//			case TURN_EAST:
//				if (angle > 0 && angle <= 180) {
//					stuckTurnRight(delta);
//				} else {
//					stuckTurnLeft(delta);
//				}
//				break;
//			case TURN_NORTH:
//				if (angle > 90 && angle <= 270) {
//					stuckTurnRight(delta);
//				} else {
//					stuckTurnLeft(delta);
//				}
//				break;
//			case TURN_SOUTH:
//				if (angle > 90 && angle <= 270) {
//					stuckTurnLeft(delta);
//				} else {
//					stuckTurnRight(delta);
//				}
//				break;
//			case FORWARD_ACCE:
//				stuckBackward(delta);
//				break;
//			case BRAKE:
//			case REVERSE_ACCE:
//				stuckForward(delta);
//				break;
//		}

	}

	/**
	 * Reverse to solve the stuck
	 * @param delta time step
	 */
	private void stuckBackward(float delta) {
		printLog("solve stuck - backward");
		if (stuckTimer > STUCK_TIMER / 2) {
			applyReverseAcceleration();
		} else {
			turnLeft(delta);
		}
	}

	/**
	 * Move forward to solve the stuck
	 * @param delta time step
	 */
	private void stuckForward(float delta) {
		printLog("solve stuck - forward");
		if (stuckTimer > STUCK_TIMER / 2) {
			applyForwardAcceleration();
		} else {
			turnLeft(delta);
		}
	}

	/**
	 * Turn right to solve the stuck
	 * @param delta time step
	 */
	private void stuckTurnRight(float delta) {
		printLog("solve stuck - right");
		if (stuckTimer < STUCK_TIMER / 2) {
			applyForwardAcceleration();
			turnRight(delta);
		} else {
			applyReverseAcceleration();
			turnLeft(delta);
		}
	}

	/**
	 * Turn left to solve the stuck
	 * @param delta time step
	 */
	private void stuckTurnLeft(float delta) {
		printLog("solve stuck - left");

		if (stuckTimer < STUCK_TIMER / 2) {
			applyForwardAcceleration();
			turnLeft(delta);
		} else {
			applyReverseAcceleration();
			turnRight(delta);

		}
	}

	/**
	 * Move the car according to different operation
	 * @param delta time step
	 */
	private void handleOperation(float delta) {
		Coordinate co = new Coordinate(Math.round(this.getX()), Math.round(this.getY()));
		if (mapRecorder.isHealth(co) && getHealth() < MAX_HEALTH) {
			applyBrake();
			return;
		} else if (getSpeed() == 0) {
			applyForwardAcceleration();
		}

		int angle = Math.round(getAngle());
		switch (currentOperation) {
			case TURN_EAST:
				if (angle != WorldSpatial.EAST_DEGREE_MIN) {
					handleEast(this.getOrientation(), delta);
				} else {
					currentOperation = drive.getOperation(mapRecorder, this);
				}
				break;
			case TURN_WEST:
				if (angle != WorldSpatial.WEST_DEGREE) {
					handleWest(this.getOrientation(), delta);
				} else {
					currentOperation = drive.getOperation(mapRecorder, this);
				}

				break;
			case TURN_NORTH:
				if (angle != WorldSpatial.NORTH_DEGREE) {
					handleNorth(this.getOrientation(), delta);
				} else {
					currentOperation = drive.getOperation(mapRecorder, this);
				}
				break;
			case TURN_SOUTH:
				if (angle != WorldSpatial.SOUTH_DEGREE) {
					handleSouth(this.getOrientation(), delta);

				} else {
					currentOperation = drive.getOperation(mapRecorder, this);
				}
				break;
			case BRAKE:
				this.applyBrake();
				currentOperation = drive.getOperation(mapRecorder, this);
				break;
			case FORWARD_ACCE:
				if (getSpeed() < CAR_SPEED) {
					this.applyForwardAcceleration();
				}
				currentOperation = drive.getOperation(mapRecorder, this);
				break;
			case REVERSE_ACCE:
				if (getSpeed() < CAR_SPEED) {
					this.applyReverseAcceleration();
				}
				currentOperation = drive.getOperation(mapRecorder, this);
				break;
			default:
				currentOperation = drive.getOperation(mapRecorder, this);
				break;
		}
	}

	/**
	 * Turn the orientation of the car to East
	 *
	 * @param orientation the current orientation of car
	 * @param delta       the time step
	 */
	private void handleEast(WorldSpatial.Direction orientation, float delta) {
		switch (orientation) {
			case EAST:
				if (getAngle() > WorldSpatial.EAST_DEGREE_MIN) {
					turnRight(delta);
				} else if (getAngle() < WorldSpatial.EAST_DEGREE_MIN) {
					turnLeft(delta);
				}
				break;
			case NORTH:
				if (!getOrientation().equals(WorldSpatial.Direction.EAST)) {
					turnRight(delta);
				}
				break;
			case SOUTH:
				if (!getOrientation().equals(WorldSpatial.Direction.EAST)) {
					turnLeft(delta);
				}
				break;
			case WEST:
				if (!getOrientation().equals(WorldSpatial.Direction.EAST)) {
					turnRight(delta);
				}
				break;
		}

	}

	/**
	 * Turn the orientation of the car to West
	 *
	 * @param orientation the current orientation of car
	 * @param delta       the time step
	 */
	private void handleWest(WorldSpatial.Direction orientation, float delta) {
		switch (orientation) {
			case EAST:
				if (!getOrientation().equals(WorldSpatial.Direction.WEST)) {
					turnRight(delta);
				}
				break;
			case NORTH:
				if (!getOrientation().equals(WorldSpatial.Direction.WEST)) {
					turnLeft(delta);
				}
				break;
			case SOUTH:
				if (!getOrientation().equals(WorldSpatial.Direction.WEST)) {
					turnRight(delta);
				}
				break;
			case WEST:
				if (getAngle() > WorldSpatial.WEST_DEGREE) {
					turnRight(delta);
				} else if (getAngle() < WorldSpatial.WEST_DEGREE) {
					turnLeft(delta);
				}
				break;
		}
	}

	/**
	 * Turn the orientation of the car to North
	 *
	 * @param orientation the current orientation of car
	 * @param delta       the time step
	 */
	private void handleNorth(WorldSpatial.Direction orientation, float delta) {
		switch (orientation) {
			case EAST:
				if (!getOrientation().equals(WorldSpatial.Direction.NORTH)) {
					turnLeft(delta);
				}
				break;
			case NORTH:
				if (getAngle() > WorldSpatial.NORTH_DEGREE) {
					turnRight(delta);
				} else if (getAngle() < WorldSpatial.NORTH_DEGREE) {
					turnLeft(delta);
				}
				break;
			case SOUTH:
				if (!getOrientation().equals(WorldSpatial.Direction.NORTH)) {
					turnLeft(delta);
				}
				break;
			case WEST:
				if (!getOrientation().equals(WorldSpatial.Direction.NORTH)) {
					turnRight(delta);
				}
				break;
		}
	}

	/**
	 * Turn the orientation of the car to South
	 *
	 * @param orientation the current orientation of car
	 * @param delta       the time step
	 */
	private void handleSouth(WorldSpatial.Direction orientation, float delta) {
		switch (orientation) {
			case EAST:
				if (!getOrientation().equals(WorldSpatial.Direction.SOUTH)) {
					turnRight(delta);
				}
				break;
			case NORTH:
				if (!getOrientation().equals(WorldSpatial.Direction.SOUTH)) {
					turnRight(delta);
				}
				break;
			case SOUTH:
				if (getAngle() > WorldSpatial.SOUTH_DEGREE) {
					turnRight(delta);
				} else if (getAngle() < WorldSpatial.SOUTH_DEGREE) {
					turnLeft(delta);
				}
				break;
			case WEST:
				if (!getOrientation().equals(WorldSpatial.Direction.SOUTH)) {
					turnLeft(delta);
				}
				break;
		}
	}

}
