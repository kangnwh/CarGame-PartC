package mycontroller.PositionStrategy;

import controller.CarController;
import mycontroller.MapRecorder;
import mycontroller.MyAIController;
import utilities.Coordinate;
import world.Car;

public interface INextPositionStrategy {
    public Coordinate getNextPosition(MapRecorder map, CarController car);
}
