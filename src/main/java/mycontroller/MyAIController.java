package mycontroller;

import controller.CarController;
import org.omg.CORBA.CurrentOperations;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


public class MyAIController extends CarController{
	private MapRecorder mapRecorder;
	private Drive drive;
	private Path path;
	private Queue<Operation> operations;
	private Coordinate targetPosition;
	private Coordinate currentPosition;


	public MyAIController(Car car) {
		super(car);
		Coordinate co = new Coordinate((int)car.getX(),(int)car.getY());
		mapRecorder = new MapRecorder();
		mapRecorder.addPointsByCarView(car.getView());
		drive=new Drive(this,mapRecorder);
		path=new Path(mapRecorder);
		operations=new LinkedList<>();
		targetPosition=new Coordinate(this.getPosition());
		currentPosition=new Coordinate(this.getPosition());
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		// Gets what the car can see
		HashMap<Coordinate, MapTile> currentView = getView();
		mapRecorder.addPointsByCarView(currentView);

		currentPosition=new Coordinate(this.getPosition());
		if(targetPosition.equals(currentPosition)){
			targetPosition=NextPositionFactory.chooseNextPositionStrategy(this).
					getNextPosition(this.mapRecorder,this);
		}

		if(operations.size()==0){
			operations=drive.getOperations(path.findPath(currentPosition,targetPosition));
		}

		handleOperation(operations.poll());



	}

	private void handleOperation(Operation operation){
		switch (operation.getOperationType()){
			case BRAKE:
				handleBrake();
				break;
			case FORWARD_ACCE:
				handleForward();
				break;
			case TURN_LEFT:
				handleTurnLeft(operation.getArgu());
				break;
			case TURN_RIGHT:
				handleTurnRight(operation.getArgu());
				break;
			case REVERSE_ACCE:
				handleReverse();
				break;
		}
	}


	private void handleForward(){
		this.applyForwardAcceleration();
	}
	private void handleBrake(){
		this.applyBrake();
	}
	private void handleTurnLeft(float delta){
		this.turnLeft(delta);
	}
	private void handleTurnRight(float delta){
		this.turnRight(delta);
	}
	private void handleReverse(){
		this.applyReverseAcceleration();
	}

	private void updateCarStatus(){

	}

	public MapRecorder getMapRecorder() {
		return mapRecorder;
	}
}
