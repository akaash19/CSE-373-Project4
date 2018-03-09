package misc.graphs;

import datastructures.concrete.*;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.*;
import misc.exceptions.NoPathExistsException;

import static misc.Searcher.topKSort;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends Edge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated then usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've contrained Graph
    //   so that E *must* always be an instance of Edge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the Edge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.

    private IDictionary<V, IList<E>> adjacencyList;
    private IDisjointSet<V> vertexMSTs;
    private IList<V> verticesList;
    private IList<E> edgesList;
    private int totalVertices;
    private int totalEdges;

    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException  if any of the edges have a negative weight
     * @throws IllegalArgumentException  if one of the edges connects to a vertex not
     *                                   present in the 'vertices' list
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        this.vertexMSTs = new ArrayDisjointSet<>();

        this.totalVertices = 0;
        this.adjacencyList = new ChainedHashDictionary<>();
        this.verticesList = new DoubleLinkedList<>();
        this.edgesList = new DoubleLinkedList<>();
        for (V vertex : vertices) {
            this.adjacencyList.put(vertex, new DoubleLinkedList<>());
            this.verticesList.add(vertex);
            this.totalVertices++;
        }

        this.totalEdges = 0;
        for (E edge : edges) {
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException("edges cannot have a negative weight");
            }
            V vertex1 = edge.getVertex1();
            V vertex2 = edge.getVertex2();
            if (!vertices.contains(vertex1) || !vertices.contains(vertex2)) {
                throw new IllegalArgumentException("one of the edges connects to a vertex not present in the " +
                        "'vertices' lists");
            }
            this.edgesList.add(edge);
            this.totalEdges++;
            IList<E> edges1 = this.adjacencyList.get(vertex1);
            edges1.add(edge);
            this.adjacencyList.put(vertex1, edges1);
            IList<E> edges2 = this.adjacencyList.get(vertex2);
            edges2.add(edge);
            this.adjacencyList.put(vertex2, edges2);
        }
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return this.totalVertices;
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return this.totalEdges;
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        /* Kruskal's Algorithm:

          def kruskal():
               mst = new SomeSet<Edge>()
               for (v : vertices):
                   makeMST(v) // stores v as a MST containing 1 node
               sort edges in ascending order by their weight
               for (edge : edges):
                   if findMST(edge.src) != findMST(edge.dst)
                       // finds MST that vertex part of
                       union(edge.src, edge.dst) // combines the two MSTs of the two vertices, using edge
                       mst.add(edge)
               return mst
        */

        ISet<E> mst = new ChainedHashSet<>();
        for (V vertex : this.verticesList) {
            makeMST(vertex);
        }
        IList<E> sortedEdges = sortEdges();
        for (E edge : sortedEdges) {
            V vertex1 = edge.getVertex1();
            V vertex2 = edge.getVertex2();
            if (findMST(vertex1) != findMST(vertex2)) {
                union(vertex1, vertex2);
                mst.add(edge);
            }
        }
        return mst;
    }

    private IList<E> sortEdges() {
        return topKSort(this.totalEdges, this.edgesList);
    }

    private void makeMST(V vertex) {
        this.vertexMSTs.makeSet(vertex);
    }

    private int findMST(V vertex) {
        return this.vertexMSTs.findSet(vertex);
    }

    private void union(V vertex1, V vertex2) {
        this.vertexMSTs.union(vertex1, vertex2);
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     */
    public IList<E> findShortestPathBetween(V start, V end) {
        /* Dijkstra's Algorithm:

         //Another impl: after implementing decreasePriority
         def dijkstra(start):
            backpointers = empty Dictionary of vertex to vertex
            costs = empty Dictionary of vertex to double

            heap = new Heap<Node with cost>();
            for (v : vertices):
                heap.put([v, infinity])
                costs.put(v, infinity)

            heap.decreasePriority([start, 0])
            costs.put(start, 0)

            while (heap is not empty):
                current, currentCost = heap.removeMin()

            for (edge : current.getOutEdges()):
                newCost = currentCost + edge.cost
                if (newCost < cost.get(edge.dest)):
                    cost.put(edge.dest, newCost)
                    heap.decreaseKey([edge.dest, newCost])
                    backpointers.put(edge.dest, current)

         return backpointers dictionary
         */

        IPriorityQueue<VertexDistance> minDistance = new ArrayHeap<>();
        ISet<V> unvisitedNodes = new ChainedHashSet<>();
        IDictionary<V, E> backEdges= new ChainedHashDictionary<>();
        IDictionary<V, VertexDistance> distances = new ChainedHashDictionary<>();
        IList<E> pathEdges = new DoubleLinkedList<>();

        if (start.equals(end)) {
            return pathEdges;
        }

        for (V vertex : this.verticesList) {
            VertexDistance vertexDistance = new VertexDistance(vertex, Double.POSITIVE_INFINITY);
            if (vertex.equals(start)) {
                vertexDistance.setDistance(0.0);
            }
            minDistance.insert(vertexDistance);
            distances.put(vertex, vertexDistance);
            unvisitedNodes.add(vertex);
        }

        while (!unvisitedNodes.isEmpty()) {
            VertexDistance current = minDistance.removeMin();
            V currentVertex = current.getVertex();
            if (end.equals(currentVertex)) {

                while (!start.equals(currentVertex)) {
                    E edge = backEdges.get(currentVertex);

                    pathEdges.insert(0, edge);

                    currentVertex = edge.getOtherVertex(currentVertex);
                }

                return pathEdges;
            }

            unvisitedNodes.remove(currentVertex);

            for (E edge : this.adjacencyList.get(currentVertex)) {
                V nextVertex = edge.getOtherVertex(currentVertex);
                VertexDistance next = distances.get(nextVertex);

                if (unvisitedNodes.contains(nextVertex)) {
                    Double nextDistance = next.getDistance();
                    Double newDistance = current.getDistance() + edge.getWeight();

                    if (newDistance.compareTo(nextDistance) < 0) {
                        minDistance.remove(next);
                        next.setDistance(newDistance);
                        minDistance.insert(next);

                        backEdges.put(nextVertex, edge);
                    }
                }
            }
        }
        throw new NoPathExistsException("no path from the start to the end exists");

    }


    private class VertexDistance implements Comparable<VertexDistance> {
        private Double distance;
        private V vertex;

        public VertexDistance(V vertex, Double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        public V getVertex() {
            return this.vertex;
        }

        public Double getDistance() {
            return this.distance;
        }

        public void setDistance(Double newDistance) {
            this.distance = newDistance;
        }

        @Override
        public int compareTo(VertexDistance o) {
            return (int) (this.distance - o.distance);
        }
    }
}
