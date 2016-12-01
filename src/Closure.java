/*
 * Calculate the closure of a set of attributes
 *
 * @author Victor Velechovsky
 * @version 1.0
*/
import java.lang.reflect.Array;
import java.util.*;

public class Closure {

	public FGraph graph; // Functional dependency graph
	public char[] attributes; // List of attributes of underlying relation

	public FD[] dependencies; // Dependencies of underlying relation

	public char[] inputs; // What the closure of is being calculated
	public Set<Character> outputs; // Closure of the inputs

	// Unit test
	public static void main(String [] args) {
		// Names of the functional dependencies
		char[] attributes = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};

		FD[] functionalDependencies = new FD[10];

		// Functional dependencies
		functionalDependencies[0] = new FD(new char[]{'A'}, new char[]{'B'});
		functionalDependencies[1] = new FD(new char[]{'A', 'B', 'D'}, new char[]{'F', 'G', 'H'});
		functionalDependencies[2] = new FD(new char[]{'A', 'E', 'H'}, new char[]{'B', 'D'});
		functionalDependencies[3] = new FD(new char[]{'B', 'C'}, new char[]{'E', 'H'});
		functionalDependencies[4] = new FD(new char[]{'C'}, new char[]{'A', 'C', 'G'});
		functionalDependencies[5] = new FD(new char[]{'C'}, new char[]{'A', 'F', 'H'});
		functionalDependencies[6] = new FD(new char[]{'D', 'E'}, new char[]{'H', 'B'});
		functionalDependencies[7] = new FD(new char[]{'D', 'F'}, new char[]{'A', 'C'});
		functionalDependencies[8] = new FD(new char[]{'E'}, new char[]{'F'});
		functionalDependencies[9] = new FD(new char[]{'H'}, new char[]{'E', 'A'});

		FGraph graph = new FGraph(functionalDependencies, attributes);

		// Inputs to the closure
		char[] inputs = {'E'};

		Closure closure = new Closure(graph, inputs);

		for (Character c: closure.getClosure()) {
			System.out.println(c.toString() + ' ');
		}

	}

	/*
	 * Calculate the closure of a set of FD's
	 *
	 * @param graph  - FD graph
	 * @param inputs - input attributes
	*/
	public Closure(FGraph graph, char[] inputs) {
		this.graph = graph;
		this.inputs = inputs;

		this.dependencies = graph.dependencies;

		outputs	= new TreeSet<>();

		for(char c: inputs) {
			outputs.add(c);
		}

		Set<Character> inSet = new TreeSet<>();

		for (char c: inputs) {
			inSet.add(c);
		}

		Set<Set<Character>> powerSet = powerSet(inSet);

		// Repeat 20 times
		for(int j = 0; j < 20; j++) {
			// For each subset of the inputs
			for(Set<Character> cSet : powerSet) {
				// Convert set to char array
				char[] charArrayOfSet = new char[cSet.size()];

				int i = 0;
				for(Character c: cSet) {
					charArrayOfSet[i] = c;
					i++;
				}

				// Perform a one depth search
				oneDepthSearch(charArrayOfSet);
			}
		}
	}

	/*
	 * Get the calculate value of the closure
	 *
	 * @return A set (no duplicates) of all the attributes within the closure of the inputs
	*/
	public Set<Character> getClosure() {
		return outputs;
	}

	private Set<Character> copy() {
		TreeSet<Character> newSet = new TreeSet<>();

		for(Character c : outputs) {
			newSet.add(c);
		}

		return newSet;
	}

	// Calculate all the subsets of a set
	private static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
		Set<Set<T>> sets = new HashSet<Set<T>>();

		if (originalSet.isEmpty()) {
			sets.add(new HashSet<T>());

			return sets;
		}

		List<T> list = new ArrayList<T>(originalSet);

		T head = list.get(0);

		Set<T> rest = new HashSet<T>(list.subList(1, list.size()));

		for (Set<T> set : powerSet(rest)) {
			Set<T> newSet = new HashSet<T>();

			newSet.add(head);
			newSet.addAll(set);

			sets.add(newSet);
			sets.add(set);
		}

		return sets;
	}

	// Perform a one-depth-deep search by checking all possible FD edges
	private void oneDepthSearch(char[] start) {
		for (FD fd : dependencies) {
			// If you have all the LHS elements to reach the RHS
			if(passes(start, fd.from)) {
				// Add the RHS to the set
				addToSet(fd);
			}
		}
	}

	private void printSet() {

	}

	private boolean passes(char[] start, char[] from) {
		char[] union = concatenate(start, outputsArray());

		for(char c : from) {
			if (!charArrayContains(union, c)) return false;
		}
		return true;
	}

	private char[] outputsArray() {
		char[] value = new char[outputs.size()];

		int i = 0;

		for(char c : outputs) {
			value[i] = c;
			i++;
		}

		return value;
	}

	private char[] concatenate (char[] a, char[] b) {
		int length = a.length + b.length;

		char[] concat = new char[length];

		int i = 0;

		for(int j = 0; j < a.length; j++) {
			concat[i] = a[j];
			i++;
		}

		for(int j = 0; j < b.length; j++) {
			concat[i] = b[j];
			i++;
		}

		return concat;
	}

	private boolean charArrayContains(char[] array, char element) {
		for(char c : array) {
			if (c == element) return true;
		}
		return false;
	}

	private void addToSet(FD fd) {
		for(char c: fd.to) {
			outputs.add(c);
		}
	}

}