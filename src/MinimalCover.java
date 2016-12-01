/*
 * Caluclate the minimal cover of a set of functional dependencies
 *
 * @author Victor Velechovsky
 * @version 1.0
*/
import java.lang.reflect.Array;
import java.util.*;

public class MinimalCover {

    FGraph graph;

    Set<FD> G;
    Set<FD> F;

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

        MinimalCover calcMinimalCover = new MinimalCover(graph);
        Set<FD> minimalCover = calcMinimalCover.getMinimalCover();

        for(FD fd : minimalCover) {
            System.out.println(fd.toString());
        }
    }

    /*
    public static void main(String [] args) {
        char[] attributes = {'A', 'B', 'C', 'D', 'E', 'F'};

        FD[] functionalDependencies = new FD[6];

        functionalDependencies[0] = new FD(new char[]{'A', 'B'}, new char[]{'D'});
        functionalDependencies[1] = new FD(new char[]{'B'}, new char[]{'C'});
        functionalDependencies[2] = new FD(new char[]{'A', 'E'}, new char[]{'B'});
        functionalDependencies[3] = new FD(new char[]{'A'}, new char[]{'D'});
        functionalDependencies[4] = new FD(new char[]{'D'}, new char[]{'E'});
        functionalDependencies[5] = new FD(new char[]{'D'}, new char[]{'F'});

        FGraph graph = new FGraph(functionalDependencies, attributes);

        MinimalCover calcMinimalCover = new MinimalCover(graph);
        Set<FD> minimalCover = calcMinimalCover.getMinimalCover();

        for(FD fd : minimalCover) {
            System.out.println(fd.toString());
        }
    }
    */

    /*
     * Calculate the minimal cover of an F.D. graph
     *
     * @param graph - Contains the set of attributes and list of F.D.'s
    */
    public MinimalCover(FGraph graph) {
        this.graph = graph;

        G = new TreeSet<>();
        F = new TreeSet<>();

        // Convert G to singeltons
        for(FD fd : graph.dependencies) {
            F.add(fd);

            for(FD fd_i : singletons(fd)) {
                G.add(fd_i);
            }
        }

        // Eliminate redundant RHS's
        for(FD fd : G) {
            FD[] gMinus = gMinus(fd);

            Closure calcClosureOne = new Closure(new FGraph(gMinus, graph.attributes), fd.from);
            Set<Character> closureOne = calcClosureOne.getClosure();

            if(contains(closureOne, fd.to[0])) {
                G = toSet(gMinus);
            }
        }

        // Eliminate redundant LHS's
        for(FD fd : G) {
            if(fd.from.length > 1) {
                for(char B : fd.from) {
                    char[] xMinusB = xMinusB(fd, B);

                    FD xMinusBFD = new FD(xMinusB, fd.to);

                    FD[] gMinus = gMinus(fd);

                    FD[] union = new FD[gMinus.length + 1];

                    for(int i = 0; i < gMinus.length; i++) {
                        union[i] = gMinus[i];
                    }

                    union[union.length - 1] = xMinusBFD;

                    FGraph unionGraph = new FGraph(union, graph.attributes);

                    Closure calcUnionClosure = new Closure(unionGraph, xMinusB);
                    Set<Character> unionClosure = calcUnionClosure.getClosure();

                    if(contains(unionClosure, B) && fd.from.length > 1) {
                        fd.from = xMinusB;
                    }
                }
            }

        }

    }

    /*
     * Returns the set of MINIMAL F.D.'s
     *
     * @return minimal set of F.D.'s
    */
    public Set<FD> getMinimalCover() {
        return G;
    }

    // Remove b from RHS of fd
    private char[] xMinusB(FD fd, char b) {
        ArrayList<Character> from = new ArrayList<>();

        for(char c : fd.from) {
            if(c != b) {
                from.add(c);
            }
        }

        char[] fromChar = new char[from.size()];

        for(int i = 0; i < from.size(); i++) {
            fromChar[i] = from.get(i);
        }

        return fromChar;

    }

    // Check if set contains the character c
    private boolean contains(Set<Character> set, Character c) {
        for (Character chr : set) {
            if(chr == c) return true;
        }

        return false;
    }

    // Convert an array of FD's to a set
    private Set<FD> toSet(FD[] deps) {
        Set<FD> fdSet = new TreeSet<>();

        for(FD fd : deps) fdSet.add(fd);

        return fdSet;
    }

    // Split the RHS of an fd into an array of singletons
    private FD[] singletons(FD fd) {
        FD[] singletons = new FD[fd.to.length];

        int i = 0;

        for(char c : fd.to) {
            char[] newTo = {c};
            singletons[i] = new FD(fd.from, newTo);

            i++;
        }

        return singletons;
    }

    // Return the graph G with the FD fd removed
    private FD[] gMinus(FD fd) {
        Set<FD> gMinus = new TreeSet<>();

        for (FD f : G) {
            if (f != fd) gMinus.add(f);
        }

        FD[] gMinusArray = new FD[gMinus.size()];

        int i = 0;

        for (FD f : gMinus) {
            gMinusArray[i] = f;
            i++;
        }

        return gMinusArray;
    }

}