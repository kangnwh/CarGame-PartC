package mycontroller;

/**
 * Operation
 * <p>
 * Author Ning Kang
 * Date 16/5/18
 */

public class Operation {

	enum OperationType{
		BRAKE,
		FORWARD_ACCE,
		REVERSE_ACCE,
//		TURN_LEFT,
//		TURN_RIGHT,
		TURN_NORTH,
		TURN_SOUTH,
		TURN_EAST,
		TURN_WEST
	}
	private OperationType operationType;
	private float argu;

	public Operation(OperationType operationType, float argu) {
		this.operationType = operationType;
		this.argu = argu;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public float getArgu() {
		return argu;
	}
}
