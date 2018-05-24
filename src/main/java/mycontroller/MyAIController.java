package mycontroller;

import controller.CarController;
import mycontroller.PathDiscovery.MyDiscoveryStrtegy;
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
	private MapRecorder mapRecorder;
	private Drive drive;
	private OperationType currentOperation;
//	private Path path;
//	private LinkedList<Operation> operations;
//	private Coordinate targetPosition;
//	private LinkedList<Coordinate> coordinatesInPath;
//	private Coordinate currentPosition;


	public MyAIController(Car car) {
		super(car);
		mapRecorder = new MapRecorder(new MyDiscoveryStrtegy());
		drive = new Drive();
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		// Gets what the car can see
		HashMap<Coordinate, MapTile> currentView = getView();
		mapRecorder.addPointsByCarView(currentView);

		//TODO check whether currentOperation is done if not, adjust , if done, get another
		/* Only turn operation need adjust based on car status/direction */
		switch (currentOperation) {
			case TURN_EAST:
				if (this.getOrientation() == WorldSpatial.Direction.EAST) {
					currentOperation = drive.getOperation(mapRecorder, this);
					update(delta);
				}else{
					handleEast(delta);
				}

				break;
			case TURN_WEST:
				if (this.getOrientation() == WorldSpatial.Direction.WEST) {
					currentOperation = drive.getOperation(mapRecorder, this);
					update(delta);
				}else{
					handleWest(delta);
				}

				break;
			case TURN_NORTH:
				if (this.getOrientation() == WorldSpatial.Direction.NORTH) {
					currentOperation = drive.getOperation(mapRecorder, this);
					update(delta);
				}else{
					handleNorth(delta);
				}

				break;
			case TURN_SOUTH:
				if (this.getOrientation() == WorldSpatial.Direction.SOUTH) {
					currentOperation = drive.getOperation(mapRecorder, this);
					update(delta);
				}else{
					handleSouth(delta);
				}

				break;
			case BRAKE:
				this.applyBrake();
				break;
			case FORWARD_ACCE:
				this.applyForwardAcceleration();
				break;
			case REVERSE_ACCE:
				this.applyReverseAcceleration();
				break;
			default:
				break;


		}
	}

//	private void handleOperation(Operation operation){
//		switch (operation.getOperationType()){
//			case BRAKE:
//				handleBrake();
//				break;
//			case FORWARD_ACCE:
//				handleForward();
//				break;
//			case TURN_LEFT:
//				handleTurnLeft(operation.getArgu());
//				break;
//			case TURN_RIGHT:
//				handleTurnRight(operation.getArgu());
//				break;
//			case REVERSE_ACCE:
//				handleReverse();
//				break;
//		}
//	}

	private void handleEast(float delta) {
		//TODO pending
		float angle = this.getAngle();
		if (this.getOrientation() == WorldSpatial.Direction.EAST) {

		}

		this.turnLeft(delta);
	}

	private void handleWest(float delta) {
		//TODO pending
		this.turnRight(delta);
	}

	private void handleNorth(float delta) {
		//TODO pending
		this.turnLeft(delta);
	}

	private void handleSouth(float delta) {
		//TODO pending
		this.turnRight(delta);
	}

	private void updateCarStatus() {
		//TODO pending
	}

	public MapRecorder getMapRecorder() {
		return mapRecorder;
	}
}
