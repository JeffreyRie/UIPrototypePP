package sample;


import java.util.*;

/**
 * Implementation of the pathfinder interface.
 * Calculates the path based on a dijkstra algorithm.
 * @author Patrick Hanselmann, Jeffrey Rietzler
 * @version 1.0
 */
public class PathFinderAlgorithm implements PathFinder {

  // Graph to save the 2D Relation of all nodes
  private Node[][] graph;
  // Map with all nodes that were not finally used by the algorithm
  // Saved with their current costs to reach this node, as key
  private TreeMap<Integer, List<Node>> notFinishedNodes;
  // List of all finally used Nodes. These are not used in the algorithm anymore
  private List<Node> finishedNodes;

  /**
   * Constructor for class PathFindingAlgorithm.
   */
  public PathFinderAlgorithm() {
    notFinishedNodes = new TreeMap<>();
    finishedNodes = new LinkedList<>();
  }

  @Override
  public Queue<Coordinate> getPath(TerrainMap terrainMap, Coordinate startingPoint,
                                   Coordinate endingPoint, Options option)
          throws Exception {
    TerrainMap terrainMapTest = new TerrainMap(2,3);
    terrainMapTest.setCompleteMap(terrainMap.getCompleteMap());
    if (terrainMapTest.getMaterialAtPoint(startingPoint).isObstacle()
            || terrainMapTest.getMaterialAtPoint(endingPoint).isObstacle()) {
      throw new Exception();
    }

    if (option == Options.COSTS_AND_NUMBER_POINTS) {
      terrainMapTest = terrainMapTest.addRadialGradient(startingPoint);
    }

    //switch starting and ending point
    Coordinate temp = startingPoint;
    startingPoint = endingPoint;
    endingPoint = temp;
    //Queue with the coordinates from the starting to ending point
    Queue<Coordinate> path = new LinkedList<>();
    //Initialisation of all needed data fields
    initialise(terrainMapTest.getCompleteMap().length, terrainMapTest.getCompleteMap()[0].length);
    //get the starting point
    Node currentNode = graph[startingPoint.getXvalue()][startingPoint.getYvalue()];
    // set costs for starting point to zero
    currentNode.setSumCostsToReachThisPoint(0);
    // add starting point to finished nodes
    finishedNodes.add(currentNode);

    boolean finished = false;
    // Dijkstra-Algorithm
    do {
      // get all neighbour points that are not in the finished List
      List<Node> neighbours = getUnFinishedNeighbourNodes(currentNode,terrainMapTest);

      for (Node node : neighbours) {
        Coordinate neighbourCoord = new Coordinate(node.getXvalue(), node.getYvalue());
        // calculate the costs to get to a neighbour node
        int costsToNeighbour = currentNode.getSumCostsToReachThisPoint()
                + terrainMapTest.getMaterialAtPoint(neighbourCoord).getCosts();
        if (costsToNeighbour < node.getSumCostsToReachThisPoint()) {
          // set the lower costs
          node.setSumCostsToReachThisPoint(costsToNeighbour);
          // set currentNode as new predecessor
          node.setPredecessor(currentNode);
          // add the node to the notFinished Map
          addNodeToList(node);
        }
      }

      if (notFinishedNodes.isEmpty()) {
        finished = true;
      } else {
        currentNode = getNodeWithLeastCosts();
        // add currentNode to finished List
        finishedNodes.add(currentNode);
      }
    }
    while (!finished);
    //get path
    Node point = graph[endingPoint.getXvalue()][endingPoint.getYvalue()];
    // No path found
    if (point.predecessor == null) {
      return path;
    }
    while (point != null) {
      path.add(new Coordinate(point.getXvalue(), point.getYvalue()));
      point = point.getPredecessor();
    }
    return path;
  }

  @Override
  public Queue<Coordinate> getPath(TerrainMap terrainMap, Coordinate startingPoint,
                                   Coordinate endingPoint, List<Coordinate> intermediatePoints,
                                   Options option) throws Exception {
    List<Coordinate> tempPath = new LinkedList<>();
    //if there is no intermediate point
    if (intermediatePoints.isEmpty()) {
      //calculate the path from the starting to the ending point
      return getPath(terrainMap,startingPoint,endingPoint,option);
    }
    tempPath.addAll(getPath(terrainMap,startingPoint,intermediatePoints.get(0),option));
    //if there is more than one intermediate point
    if (intermediatePoints.size() > 1) {
      // iterate over all of them
      for (int i = 0; i < intermediatePoints.size() - 1 ; i++) {
        Queue<Coordinate> coordinates = getPath(terrainMap,intermediatePoints.get(i),
                intermediatePoints.get(i + 1),option);
        //remove the first coordinate because it is already in the path list
        coordinates.remove();
        tempPath.addAll(coordinates);
      }
    }
    Queue<Coordinate> lastPath = getPath(terrainMap,
        intermediatePoints.get(intermediatePoints.size() - 1), endingPoint,option);
    lastPath.remove();
    tempPath.addAll(lastPath);
    //Queue with the coordinates from the starting to ending point over intermediate points
    return new LinkedList<>(tempPath);
  }

  /**
   * Adds a Node to the notFinished Map.
   * if the Node is already in the finished List it gets deleted and
   * added with its new costs as key
   * @param node the node to be added
   */
  private void addNodeToList(Node node) {
    // look up if the Node is already in the unfinished Map
    TreeMap<Integer, List<Node>> tempNodes = new TreeMap<>(notFinishedNodes);
    for (Integer key : tempNodes.keySet()) {
      List<Node> nodes = tempNodes.get(key);
      for (Node savedNode: nodes) {
        // if it is
        if (savedNode.equals(node)) {
          // remove it
          notFinishedNodes.get(key).remove(node);
          // if the list it was saved in is now empty
          if (notFinishedNodes.get(key).isEmpty()) {
            //remove it
            notFinishedNodes.remove(key);
          }

        }
      }
    }
    int costs = node.getSumCostsToReachThisPoint();
    //if Map already contains new key of the Node
    if (notFinishedNodes.containsKey(costs)) {
      //add Node to the List with this key
      notFinishedNodes.get(costs).add(node);
    }  else {
      // create new list with Node and add it to map
      List<Node> list = new LinkedList<>();
      list.add(node);
      notFinishedNodes.put(costs, list);
    }
  }

  /**
   * returns the Node with the least costs.
   */
  private Node getNodeWithLeastCosts() {
    //remove entry at first key in map and return it
    Node out = notFinishedNodes.get(notFinishedNodes.firstKey()).remove(0);
    // if the list the Node was in is now empty then delete it
    if (notFinishedNodes.get(notFinishedNodes.firstKey()).isEmpty()) {
      notFinishedNodes.remove(notFinishedNodes.firstKey());
    }
    return out;
  }

  /**
   * return all neighbour nodes of current node that are not in the finished List.
   * @param currentNode the currently observed node
   */
  private List<Node> getUnFinishedNeighbourNodes(Node currentNode, TerrainMap terrainMap) {
    int xvalue = currentNode.getXvalue();
    int yvalue = currentNode.getYvalue();
    List<Node> neighbours = new LinkedList<>();
    if (xvalue - 1 >= 0) {
      neighbours.add(graph[xvalue - 1][yvalue]);
    }
    if (xvalue + 1 < graph.length) {
      neighbours.add(graph[xvalue + 1][yvalue]);
    }
    if (yvalue - 1 >= 0) {
      neighbours.add(graph[xvalue][yvalue - 1]);
    }
    if (yvalue + 1 < graph[0].length) {
      neighbours.add(graph[xvalue][yvalue + 1]);
    }
    Iterator<Node> it = neighbours.iterator();
    while (it.hasNext()) {
      Node nextNode = it.next();
      boolean isObstacle = terrainMap.getMaterialAtPoint(
              new Coordinate(nextNode.getXvalue(),nextNode.getYvalue())).isObstacle();
      if (finishedNodes.contains(nextNode) || isObstacle) {
        it.remove();
      }
    }
    return neighbours;
  }

  /**
   * initializer for all needed data types.
   * @param xlength length in x-direction of the used TerrainMap
   * @param ylength length in y-direction of the used TerrainMap
   */
  private void initialise(int xlength, int ylength) {
    graph = new Node[xlength][ylength];
    for (int x = 0 ; x < graph.length ; x++) {
      for (int y = 0; y < graph[x].length ; y++) {
        graph[x][y] = new Node(x,y);
      }
    }
    notFinishedNodes = new TreeMap<>();
    finishedNodes = new LinkedList<>();
  }


  /**
   * Represents a node in a graph.
   */
  private class Node {
    private Node predecessor;
    // costs to reach this point from a starting point
    private int sumCostsToReachThisPoint;
    private final int xvalue;
    private final int yvalue;

    private Node(int xvalue, int yvalue) {
      this.xvalue = xvalue;
      this.yvalue = yvalue;
      predecessor = null;
      sumCostsToReachThisPoint = Integer.MAX_VALUE - 1;
    }

    private void setPredecessor(Node predecessor) {
      this.predecessor = predecessor;
    }

    private Node getPredecessor() {
      return predecessor;
    }

    private void setSumCostsToReachThisPoint(int costs) {
      this.sumCostsToReachThisPoint = costs;
    }

    private int getSumCostsToReachThisPoint() {
      return sumCostsToReachThisPoint;
    }

    private int getXvalue() {
      return xvalue;
    }

    private int getYvalue() {
      return yvalue;
    }
  }
}
