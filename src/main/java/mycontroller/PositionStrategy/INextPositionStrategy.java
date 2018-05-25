package mycontroller.PositionStrategy;

import controller.CarController;
import mycontroller.MapRecorder;
import mycontroller.MyAIController;
import utilities.Coordinate;
import world.Car;

/**
 * INextPositionStrategy
 * This is the interface of position strategies to find the target position where the car should go next.
 * All position strategies should implement this interface.
 */
public interface INextPositionStrategy {

    /**
     * This method is used to find the next position for the car to go according to
     * @param map from map to get the known position information to judge where to go
     * @param car from carController to get the current status of a car to judge where to go
     * @return the next position for the car to go
     */
    public Coordinate getNextPosition(MapRecorder map, CarController car);
}
