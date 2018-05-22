package mycontroller;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;


import java.util.HashMap;


public class MyAIController extends CarController{
//	private MyNavigateStrategy strategy ;
	private MapRecorder mapRecorder;


	public MyAIController(Car car) {
		super(car);
		Coordinate co = new Coordinate((int)car.getX(),(int)car.getY());
//		strategy = new MyNavigateStrategy(co);
		mapRecorder = new MapRecorder();
		mapRecorder.addPointsByCarView(car.getView());
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		// Gets what the car can see
		HashMap<Coordinate, MapTile> currentView = getView();
		mapRecorder.addPointsByCarView(currentView);

//		Operation operation = strategy.getOperation(mapRecorder.getMap(),this);
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
