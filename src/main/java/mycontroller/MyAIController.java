package mycontroller;

import controller.CarController;
import mycontroller.PathDiscovery.AStarStrategy;
import mycontroller.PathDiscovery.MyDiscoveryStrategy;
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
	public static int STUCK_TIMER = 10; //The times for car to do the operation when get stuck


	private MapRecorder mapRecorder;
	private Drive drive;
	private OperationType currentOperation;
	private final float CAR_SPEED = 1.5f;
	private CarStatus lastStatus;
	private int stuckTimer;


	public static void printLog(String msg) {

		logger.info(msg);
	}


	public MyAIController(Car car) {
		super(car);
		Coordinate co = new Coordinate((int) car.getX(), (int) car.getY());

		drive = new Drive(co);
//		mapRecorder = new MapRecorder(new MyDiscoveryStrategy(), this.getMap());
		mapRecorder = new MapRecorder(new AStarStrategy(), this.getMap());
		//TODO for tesing
//		mapRecorder = new MapRecorder(new TestDiscoveryStrategy(), this.getMap());
//		drive = new Drive(new Coordinate(6,5));


 		drive = new Drive(co);
		mapRecorder = new MapRecorder(new AStarStrategy(),this.getMap());
		currentOperation = OperationType.FORWARD_ACCE;
	}


	@Override
	public void update(float delta) {

		// Gets what the car can see
		HashMap<Coordinate, MapTile> currentView = getView();
		mapRecorder.addPointsByCarView(currentView);

		//if the car is stuck, reverse and turn right for 10 delta to readjust its orientation
		if (stuckCheck() || stuckTimer > 0) {
			solveStuck(delta);
			stuckTimer--;
		} else {
			handleOperation(delta);
			lastStatus = new CarStatus(this);
		}
		printLog(currentOperation.toString());
	}

	/**
	 * Check if the car is stuck on the wall
	 * @return true if the car get stuck on the wall
	 */
	private boolean stuckCheck(){
		/* stuck interrupt */
		float angle = getAngle();
		int targetDegree = 0;
		if (lastStatus != null
				&& lastStatus.getAngle() == this.getAngle()
				&& lastStatus.getX() == this.getX()
				&& lastStatus.getY() == this.getY()
				&& lastStatus.getHealth() >= this.getHealth()
				) {
			switch (currentOperation) {
				case TURN_SOUTH:
					targetDegree = WorldSpatial.SOUTH_DEGREE;
					break;
				case TURN_NORTH:
					targetDegree = WorldSpatial.NORTH_DEGREE;
					break;
				case TURN_EAST:
					targetDegree = WorldSpatial.EAST_DEGREE_MIN;
					break;
				case TURN_WEST:
					targetDegree = WorldSpatial.WEST_DEGREE;
					break;
			}
			stuckTimer = Math.round(Math.abs(targetDegree - angle) / 0.3f) * STUCK_TIMER;
			printLog("stucked !");
			return true;
		}

//		Coordinate currentPosition = new Coordinate(Math.round(this.getX()), Math.round(this.getY()));

//		if (lastStatus != null
//				&& lastStatus.getAngle() == this.getAngle()
//				&& lastStatus.getX() == this.getX()
//				&& lastStatus.getY() == this.getY()
//				&& this.getHealth() == MAX_HEALTH
//				&& mapRecorder.isHealth(currentPosition)
//				) {
//			stuckTimer = STUCK_TIMER;
//			return true;
//		}
		return false;
	}


	/**
	 * Control the car's move according to the current operation
	 * @param delta the time step
	 */
	private void solveStuck(float delta) {
		currentOperation = drive.getOperation(mapRecorder,this);
		float angle = lastStatus.getAngle();

		switch (currentOperation) {
			case TURN_WEST:
				if (angle == WorldSpatial.WEST_DEGREE) {
					return;
				}
				if (angle > 0 || angle <= 180) {
					turnRight(delta);
				} else {
					turnLeft(delta);
				}
				break;
			case TURN_EAST:
				if (angle == WorldSpatial.EAST_DEGREE_MIN) {
					return;
				}
				if (angle > 0 || angle <= 180) {
					turnLeft(delta);
				} else {
					turnRight(delta);
				}
				break;
			case TURN_NORTH:
				if (angle == WorldSpatial.NORTH_DEGREE) {
					return;
				}
				if (angle > 90 || angle <= 270) {
					turnRight(delta);
				} else {
					turnLeft(delta);
				}
				break;
			case TURN_SOUTH:
				if (angle == WorldSpatial.SOUTH_DEGREE) {
					return;
				}
				if (angle > 90 || angle <= 270) {
					turnLeft(delta);
				} else {
					turnRight(delta);
				}
				break;
			case FORWARD_ACCE:
			case BRAKE:
			case REVERSE_ACCE:
				if (angle != WorldSpatial.EAST_DEGREE_MIN
						&& angle != WorldSpatial.WEST_DEGREE
						&& angle != WorldSpatial.NORTH_DEGREE
						&& angle != WorldSpatial.SOUTH_DEGREE) {
					if (angle <= 45 && angle > 315) {
						turnLeft(delta);
					} else if (angle > 45 && angle <= 135) {
						turnRight(delta);
					} else if (angle > 135 && angle <= 270) {
						turnLeft(delta);
					} else {
						turnRight(delta);
					}
				}else{
					stuckTimer = 0;
				}
				break;
		}
		applyReverseAcceleration();

//		turnRight(delta);
//		if(angle <= 45 && angle > 315){
//			turnLeft(delta);
//			currentOperation = OperationType.TURN_EAST;
//		}else if( angle > 90 && angle <=180){
//			currentOperation = OperationType.TURN_EAST;
//			turnRight(delta);
//		}else if(angle > 180 && angle <=270){
//			turnLeft(delta);
//		}else{
//			turnRight(delta);
//		}
	}

	private void handleOperation(float delta) {
		if (getSpeed() == 0) {
			applyForwardAcceleration();
		}
		float angle = getAngle();
		switch (currentOperation) {
			case TURN_EAST:
				if (angle != WorldSpatial.EAST_DEGREE_MIN) {
					handleEast(this.getOrientation(), delta);
				} else {
					currentOperation = drive.getOperation(mapRecorder, this);
//					handleOperation(delta);
				}
				break;
			case TURN_WEST:
				if (angle != WorldSpatial.WEST_DEGREE) {
					handleWest(this.getOrientation(), delta);
				} else {
					currentOperation = drive.getOperation(mapRecorder, this);
//					handleOperation(delta);
				}

				break;
			case TURN_NORTH:
				if (angle != WorldSpatial.NORTH_DEGREE) {
					handleNorth(this.getOrientation(), delta);
				} else {
					currentOperation = drive.getOperation(mapRecorder, this);
//					handleOperation(delta);
				}
				break;
			case TURN_SOUTH:
				if (angle != WorldSpatial.SOUTH_DEGREE) {
					handleSouth(this.getOrientation(), delta);

				} else {
					currentOperation = drive.getOperation(mapRecorder, this);
//					handleOperation(delta);
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
