/*
 * Represents a Function Dependency Graph
 *
 * @author Victor Velechovsky
 * @version 1.0
*/public class FGraph {

	public FD[] dependencies; // List of functional dependencies
	public char[] attributes; // List of attributes of the relation

	/*
	 * Create a Function Dependency Graph
	 *
	 * @param dependencies - List of functional dependencies
	 * @param attributes   - List of attriutes of the relation
	*/
	public FGraph(FD[] dependencies, char[] attributes) {
		this.dependencies = dependencies;
		this.attributes = attributes;
	}
	
}