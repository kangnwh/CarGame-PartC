package mycontroller.PathDiscovery;

import mycontroller.OperationType;
import utilities.Coordinate;

import java.util.LinkedList;

/**
 * DiscoveryStrategy
 * <p>
 * Author Ning Kang
 * Date 24/5/18
 */

public interface IDiscoveryStrategy {
	LinkedList<Coordinate> findPath(Coordinate current, Coordinate target);
}