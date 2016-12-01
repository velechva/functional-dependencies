/*
 * Represents a Function Dependency
 *
 * @author Victor Velechovsky
 * @version 1.0
*/
public class FD {

	public char[] from; // List of attributes on the LHS
	public char[] to; // List of attributes on the RHS

	/*
	 * Create a Function Dependency
	 *
	 * @param from - List of attributes on the LHS
	 * @param to   - List of attributes on the RHS
	*/
	public FD(char[] from, char[] to) {
		this.from = from;
		this.to = to;
	}

}