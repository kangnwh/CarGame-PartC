package mycontroller;

import controller.CarController;
import mycontroller.PositionStrategy.NextPositionFactory;
import utilities.Coordinate;

import java.util.LinkedList;

public class Drive {
//  private MyAIController controller;
//  private MapRecorder mapRecorder;
//  private Queue<Operation> operations;
//  private Coordinate target;
//  private LinkedList<Coordinate> keyPoints;
//	private Path pathDiscovery;
	private LinkedList<Coordinate> coordinatesInPath;
	private Coordinate targetPosition;

	public Drive() {
//        pathDiscovery = new Path();
//        this.operations=new LinkedList<>();
		this.coordinatesInPath = new LinkedList<>();
//        this.controller=controller;
//        this.mapRecorder=mapRecorder;
	}

	public OperationType getOperation(MapRecorder mapRecorder, CarController car) {
		Coordinate currentPosition = new Coordinate((int)car.getX(),(int)car.getY());
		/* if reaches a targe position, a strategy should be applied to find next target position */
		if (targetPosition.equals(currentPosition)) {
			targetPosition = NextPositionFactory.chooseNextPositionStrategy(car, mapRecorder).
					getNextPosition(mapRecorder, car);
		}

		if (coordinatesInPath.size() == 0) {
			coordinatesInPath = mapRecorder.findPath(currentPosition,targetPosition);
		}

		Coordinate nextPosition = coordinatesInPath.poll();
		//TODO calculate operation to get to nextPositon


		return null;

	}


}
