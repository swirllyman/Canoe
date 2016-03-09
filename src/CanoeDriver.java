
/**
 * 
 * @author Joe
 *
 */
public class CanoeDriver {

	/**
	 * Main driver method for the canoe program.
	 * Create Canoe of any size.
	 * It will yield a size x size matrix
	 * @param args
	 */
	public static void main(String[] args) {
		new Canoe(7);
		tests();

	}
	
	
	static void tests(){
		new Canoe(100);
		new Canoe(200);
		new Canoe(400);
		new Canoe(600);
		new Canoe(800);
	}
}
