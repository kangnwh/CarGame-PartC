package mycontroller.PathDiscovery;

import utilities.Coordinate;


public class Node implements Comparable<Node>
{

	private Coordinate coordinate;
	private Node parent;
	private int G;
	private int H;

	public Node(int x, int y)
	{
		this.coordinate = new Coordinate(x, y);
	}

	public Node(Coordinate co)
	{
		this.coordinate = co;
		this.parent = new Node(co.x,co.y);
	}

	public Node(Coordinate coord, Node parent, int g, int h)
	{
		this.coordinate = coord;
		this.parent = parent;
		G = g;
		H = h;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getG() {
		return G;
	}

	public void setG(int g) {
		G = g;
	}

	public int getH() {
		return H;
	}

	public void setH(int h) {
		H = h;
	}

	@Override
	public int compareTo(Node o)
	{
		if (o == null) return -1;
		if (G + H > o.G + o.H)
			return 1;
		else if (G + H < o.G + o.H) return -1;
		return 0;
	}
}
