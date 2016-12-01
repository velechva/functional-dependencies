/*
 * Represents a Function Dependency
 *
 * @author Victor Velechovsky
 * @version 1.0
*/
public class FD implements Comparable<FD> {

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

	public String toString() {
		String s = "";

		for(char c : from) s += c + " ";

		s += "-> ";

		for(char c : to) s += c + " ";

		return s;
	}

	@Override
	public int compareTo(FD o) {
		return toString().compareTo(o.toString());
	}

	@Override
	public boolean equals(Object object) {
		FD obj = (FD) object;

		if(! from.equals(obj.from)) return false;
		if(!   to.equals(obj.to))   return false;

		return true;
	}

}