package mycontroller;

import controller.CarController;
import mycontroller.PathDiscovery.AStarStrategy;
import mycontroller.PathDiscovery.MyDiscoveryStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tiles.HealthTrap;
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
	public static int MAX_HEALTH = 100;
	public static int STUCK_TIMER = 10;

	
	private MapRecorder mapRecorder;
	private Drive drive;
	private OperationType currentOperation;
	private final float CAR_SPEED = 2f;
	private CarStatus lastStatus;
	private int stuckTimer;




	public MyAIController(Car car) {
		super(car);
		Coordinate co = new Coordinate((int) car.getX(), (int) car.getY());

 		drive = new Drive(co);
 		mapRecorder = new MapRecorder(new MyDiscoveryStrategy(),this.getMap());
		mapRecorder = new MapRecorder(new AStarStrategy(),this.getMap());
		//TODO for tesing
//		mapRecorder = new MapRecorder(new TestDiscoveryStrategy(), this.getMap());
//		drive = new Drive(new Coordinate(6,5));


		currentOperation = OperationType.FORWARD_ACCE;
	}


	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		// Gets what the car can see
		HashMap<Coordinate, MapTile> currentView = getView();
		mapRecorder.addPointsByCarView(currentView);


//		if(this.getHealth()<100&&mapRecorder.getMapMatrix().get(new Coordinate(this.getPosition())) instanceof HealthTrap){
//			applyBrake();
//		}
		if(stuckCheck() || stuckTimer >0){
			applyReverseAcceleration();
			turnRight(delta);
			stuckTimer--;

		}else {
			handleOperation(delta);
			lastStatus = new CarStatus(this);
		}

	}
	
	private boolean stuckCheck(){
		/* stuck interrupt */
		if(lastStatus != null
				&& lastStatus.getAngle() == this.getAngle()
				&& lastStatus.getX() == this.getX()
				&& lastStatus.getY() == this.getY()
				&& (lastStatus.getHealth() > this.getHealth()||(mapRecorder.isHealth(new Coordinate(this.getPosition())))&&lastStatus.getHealth() == this.getHealth())){
			stuckTimer = STUCK_TIMER;
			return true;
		}
		return false;
	}

	private void handleOperation(float delta) {
		if(getSpeed() == 0){
			applyForwardAcceleration();
		}
		switch (currentOperation) {
			case TURN_EAST:
				if (this.getOrientation() != WorldSpatial.Direction.EAST) {
					handleEast(this.getOrientation(), delta);
				} else {
					currentOperation = drive.getOperation(mapRecorder, this);
//					handleOperation(delta);
				}
				break;
			case TURN_WEST:
				if (this.getOrientation() != WorldSpatial.Direction.WEST) {
					handleWest(this.getOrientation(), delta);
				} else {
					currentOperation = drive.getOperation(mapRecorder, this);
//					handleOperation(delta);
				}

				break;
			case TURN_NORTH:
				if (this.getOrientation() != WorldSpatial.Direction.NORTH) {
					handleNorth(this.getOrientation(), delta);
				} else {
					currentOperation = drive.getOperation(mapRecorder, this);
//					handleOperation(delta);
				}
				break;
			case TURN_SOUTH:
				if (this.getOrientation() != WorldSpatial.Direction.SOUTH) {
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

	private void handleEast(WorldSpatial.Direction orientation, float delta) {
		switch (orientation) {
			case EAST:
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
				break;
		}
	}

	private void handleNorth(WorldSpatial.Direction orientation, float delta) {
		switch (orientation) {
			case EAST:
				if (!getOrientation().equals(WorldSpatial.Direction.NORTH)) {
					turnLeft(delta);
				}
				break;
			case NORTH:
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
