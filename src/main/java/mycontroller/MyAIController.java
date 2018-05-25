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
	public static int STUCK_TIMER = 20; //The times for car to do the operation when get stuck
	public static int CAPTURE_INTERVAL = 15;// the interval to capture the car status for detecting stucking


	private MapRecorder mapRecorder;
	private Drive drive;
	private OperationType currentOperation;
	private final float CAR_SPEED = 1.5f;
	private CarStatus lastStatus;
	private int stuckTimer;
	private int captuerTimer;


	public static void printLog(String msg) {
		logger.info(msg);
	}


	public MyAIController(Car car) {
		super(car);
		Coordinate co = new Coordinate((int) car.getX(), (int) car.getY());

		drive = new Drive(co);
		mapRecorder = new MapRecorder(new AStarStrategy(), this.getMap());
		captuerTimer = CAPTURE_INTERVAL;
		currentOperation = OperationType.FORWARD_ACCE;
	}


	@Override
	public void update(float delta) {

		// Gets what the car can see
		HashMap<Coordinate, MapTile> currentView = getView();
		mapRecorder.addPointsByCarView(currentView);

		//if the car is stuck, reverse and turn right for 10 delta to readjust its orientation

		if (captuerTimer > 0) {
			captuerTimer--;
			if (stuckCheck()) {
				stuckTimer = STUCK_TIMER;
//				drive.skipThisPosition();
//				currentOperation = drive.getOperation(mapRecorder, this);
			}
		}else{
			lastStatus = new CarStatus(this);
			captuerTimer = CAPTURE_INTERVAL;
		}

		if(stuckTimer > 0){
			solveStuck(delta);
			stuckTimer--;
		} else {
			handleOperation(delta);

		}
//		printLog(currentOperation.toString());

	}

	/**
	 * Check if the car is stuck on the wall
	 * @return true if the car get stuck on the wall
	 */
	private boolean stuckCheck(){
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
	 * @param delta the time step
	 */
	private void solveStuck(float delta) {

		float angle = lastStatus.getAngle();

		switch (currentOperation) {
			case TURN_WEST:
				if (angle > 0 || angle <= 180) {
					stuckTurnLeft(delta);
				} else {
					stuckTurnRight(delta);
				}
				break;
			case TURN_EAST:
				if (angle > 0 || angle <= 180) {
					stuckTurnRight(delta);
				} else {
					stuckTurnLeft(delta);
				}
				break;
			case TURN_NORTH:
				if (angle > 90 || angle <= 270) {
					stuckTurnRight(delta);
				} else {
					stuckTurnLeft(delta);
				}
				break;
			case TURN_SOUTH:
				if (angle > 90 || angle <= 270) {
					stuckTurnLeft(delta);
				} else {
					stuckTurnRight(delta);
				}
				break;
			case FORWARD_ACCE:
			case BRAKE:
			case REVERSE_ACCE:
				stuckTurnRight(delta);
				break;
		}

	}

	private void stuckTurnRight(float delta){
		if(stuckTimer < STUCK_TIMER / 2){
			applyForwardAcceleration();
			turnRight(delta);
		}else{
			applyReverseAcceleration();
			turnLeft(delta);
		}
//		int choice = stuckTimer % 6;
//		switch(choice){
//			case 0:
//			case 1:
//			case 2:
//				applyReverseAcceleration();
//				turnLeft(delta);
//				break;
//			case 3:
//			case 4:
//			case 5:
//				applyForwardAcceleration();
//				turnRight(delta);
//				break;
//		}
	}
	private void stuckTurnLeft(float delta){
		if(stuckTimer < STUCK_TIMER / 2){
			applyForwardAcceleration();
			turnLeft(delta);
		}else{
			applyReverseAcceleration();
			turnRight(delta);

		}
//		int choice = stuckTimer % 6;
//		switch(choice){
//			case 0:
//			case 1:
//			case 2:
//				applyReverseAcceleration();
//				turnRight(delta);
//				break;
//			case 3:
//			case 4:
//			case 5:
//				applyForwardAcceleration();
//				turnLeft(delta);
//				break;
//		}
	}

	private void handleOperation(float delta) {
		Coordinate co = new Coordinate(Math.round(this.getX()), Math.round(this.getY()));
		if(mapRecorder.isHealth(co) && getHealth() < MAX_HEALTH){
			applyBrake();
			return ;
		}else if (getSpeed() == 0) {
			applyForwardAcceleration();
		}

		int angle =Math.round(getAngle());
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
				this.applyReverseAcceleration();
				currentOperation = drive.getOperation(mapRecorder, this);
				break;
			default:
				currentOperation = drive.getOperation(mapRecorder, this);
				break;
		}
	}

	/**
	 * Turn the orientation of the car to East
	 * @param orientation the current orientation of car
	 * @param delta the time step
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
	 * @param orientation the current orientation of car
	 * @param delta the time step
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
				}else if(getAngle() < WorldSpatial.WEST_DEGREE){
					turnLeft(delta);
				}
				break;
		}
	}

	/**
	 * Turn the orientation of the car to North
	 * @param orientation the current orientation of car
	 * @param delta the time step
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
				}else if(getAngle() < WorldSpatial.NORTH_DEGREE){
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
	 * @param orientation the current orientation of car
	 * @param delta the time step
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
				}else if(getAngle() < WorldSpatial.SOUTH_DEGREE){
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

	private void updateCarStatus() {
		//TODO pending
	}

//	public MapRecorder getMapRecorder() {
//		return mapRecorder;
//	}
}
