package sample;

import java.util.List;
import java.util.Queue;

/**
 * Service to calculate the path of a map with different terrains.
 * @author Patrick Hanselmann, Jeffrey Manuel Rietzler
 * @version 0.1
 */
public interface PathFinder {

  /**
   * calculates the path between the starting and end point.
   * @param terrainMap the map of the terrain to calculate the path
   * @param startingPoint the starting point of the path
   * @param endingPoint the ending point of the path
   * @param option option to specify on which basis is used to calculate the path
   * @return the calculated path from the starting to the end point
   */
  Queue<Coordinate> getPath(TerrainMap terrainMap, Coordinate startingPoint, Coordinate endingPoint,
                            Options option)
                            throws Exception;

  /**
   * calculates the path between the starting and end point over intermediate points.
   * @param terrainMap the map of the terrain to calculate the path
   * @param startingPoint the starting point of the path
   * @param endingPoint the ending point of the path
   * @param intermediatePoints the points that should be visited
   * @param option option to specify on which basis is used to calculate the path
   * @return the calculated path from the starting to the end point over intermediate points
   */
  Queue<Coordinate> getPath(TerrainMap terrainMap, Coordinate startingPoint, Coordinate endingPoint,
                            List<Coordinate> intermediatePoints, Options option)
                            throws Exception;
}


