import utilities.Coordinate;

import java.util.HashMap;

/**
 * test
 * <p>
 * Author Ning Kang
 * Date 16/5/18
 */

public class test {

	public static void main(String[] args){
		Coordinate i1 = new Coordinate(1,2);
		Coordinate i2 = new Coordinate(1,2);
		HashMap<Coordinate,String> h = new HashMap<>();
		h.put(i1,"a");
		h.put(i2,"b");
		System.out.println(i1.hashCode());
		System.out.println(i2.hashCode());
		System.out.println(h);
	}
}
