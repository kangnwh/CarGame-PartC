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
	private final float CAR_SPEED = 3;


	public MyAIController(Car car) {
		super(car);
//		Coordinate co = new Coordinate((int)car.getX(),(int)car.getY());
		mapRecorder = new MapRecorder(new MyDiscoveryStrtegy());
		drive = new Drive();
//		path=new Path();
//		operations=new LinkedList<>();
//		targetPosition=new Coordinate(this.getPosition());
//		currentPosition=new Coordinate(this.getPosition());
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		// Gets what the car can see
		HashMap<Coordinate, MapTile> currentView = getView();
		mapRecorder.addPointsByCarView(currentView);

		if(getSpeed() < CAR_SPEED){
			applyForwardAcceleration();
		}

//		this.turnLeft(delta);
		this.handleNorth(this.getOrientation(),delta);


//		currentPosition=new Coordinate(this.getPosition());

//		/* if reaches a targe position, a strategy should be applied to find next target position */
//		if(targetPosition.equals(this.getPosition())){
//			targetPosition= NextPositionFactory.chooseNextPositionStrategy(this).
//					getNextPosition(this.mapRecorder,this);
//		}

		/* get how to do next to get to target position  */

//		if(operations.size()==0){
//			operations=drive.getOperations(path.findPath(currentPosition,targetPosition));
//		}

		//TODO check whether currentOperation is done if not, adjust , if done, get another
		/* Only turn operation need adjust based on car status/direction */
//		switch (currentOperation) {
//			case TURN_EAST:
//				if (this.getOrientation() == WorldSpatial.Direction.EAST) {
//					currentOperation = drive.getOperation(mapRecorder, this);
//					update(delta);
//				}else{
//					handleEast(delta);
//				}
//
//				break;
//			case TURN_WEST:
//				if (this.getOrientation() == WorldSpatial.Direction.WEST) {
//					currentOperation = drive.getOperation(mapRecorder, this);
//					update(delta);
//				}else{
//					handleWest(delta);
//				}
//
//				break;
//			case TURN_NORTH:
//				if (this.getOrientation() == WorldSpatial.Direction.NORTH) {
//					currentOperation = drive.getOperation(mapRecorder, this);
//					update(delta);
//				}else{
//					handleNorth(delta);
//				}
//
//				break;
//			case TURN_SOUTH:
//				if (this.getOrientation() == WorldSpatial.Direction.SOUTH) {
//					currentOperation = drive.getOperation(mapRecorder, this);
//					update(delta);
//				}else{
//					handleSouth(delta);
//				}
//
//				break;
//			case BRAKE:
//				this.applyBrake();
//				break;
//			case FORWARD_ACCE:
//				this.applyForwardAcceleration();
//				break;
//			case REVERSE_ACCE:
//				this.applyReverseAcceleration();
//				break;
//			default:
//				break;
//
//
//		}
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


	private void handleEast(WorldSpatial.Direction orientation,float delta) {
		switch(orientation){
			case EAST:
				break;
			case NORTH:
				if(!getOrientation().equals(WorldSpatial.Direction.EAST)){
					turnRight(delta);
				}
				break;
			case SOUTH:
				if(!getOrientation().equals(WorldSpatial.Direction.EAST)){
					turnLeft(delta);
				}
				break;
			case WEST:
				if(!getOrientation().equals(WorldSpatial.Direction.EAST)){
					turnRight(delta);
				}
				break;
		}

	}

	private void handleWest(WorldSpatial.Direction orientation,float delta) {
		switch(orientation){
			case EAST:
				if(!getOrientation().equals(WorldSpatial.Direction.WEST)){
					turnRight(delta);
				}
				break;
			case NORTH:
				if(!getOrientation().equals(WorldSpatial.Direction.WEST)){
					turnLeft(delta);
				}
				break;
			case SOUTH:
				if(!getOrientation().equals(WorldSpatial.Direction.WEST)){
					turnRight(delta);
				}
				break;
			case WEST:
				break;
		}
	}

	private void handleNorth(WorldSpatial.Direction orientation,float delta) {
		switch(orientation){
			case EAST:
				if(!getOrientation().equals(WorldSpatial.Direction.NORTH)){
					turnLeft(delta);
				}
			case NORTH:
				break;
			case SOUTH:
				if(!getOrientation().equals(WorldSpatial.Direction.NORTH)){
					turnLeft(delta);
				}
				break;
			case WEST:
				if(!getOrientation().equals(WorldSpatial.Direction.NORTH)){
					turnRight(delta);
				}
				break;
		}
	}

	private void handleSouth(WorldSpatial.Direction orientation,float delta) {
		switch(orientation){
			case EAST:
				if(!getOrientation().equals(WorldSpatial.Direction.SOUTH)){
					turnRight(delta);
				}
				break;
			case NORTH:
				if(!getOrientation().equals(WorldSpatial.Direction.SOUTH)){
					turnRight(delta);
				}
				break;
			case SOUTH:
				break;
			case WEST:
				if(!getOrientation().equals(WorldSpatial.Direction.SOUTH)){
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
