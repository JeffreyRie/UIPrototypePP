package sample;

/**
 * This class represents a Map, to calculate paths with,
 * that contains Materials at specific locations.
 * @author Jeffrey Rietzler, Patrick Hanselmann
 * @version 0.2
 */
public class TerrainMap {
  private Material[][] terrain;

  /**
   * Constructor for class TerrainMap.
   * @param length fixed length of the map
   * @param height fixed height of the map
   */
  public TerrainMap(int length, int height) {
    terrain = new Material[length][height];
    fillDefault();
  }

  /**
   * Fills the TerrainMap with default values.
   */
  private void fillDefault() {
    for (int i = 0 ; i < terrain.length ; i++) {
      for (int j = 0 ; j < terrain[i].length ; j++) {
        terrain[i][j] = new Material("default", 1, false);
      }
    }
  }

  /**
   * Getter for the complete map.
   */
  public Material[][] getCompleteMap() {
    return terrain;
  }

  /**
   * Sets the map to the given new map
   * under the condition that the new map does not contain null element.
   * @param newMap the new map without null elements
   */
  public void setCompleteMap(Material[][] newMap) {
    for (int i = 0; i < newMap.length; i++) {
      for (int j = 0; j < newMap[i].length; j++) {
        if (newMap[i][j] == null) {
          throw new IllegalArgumentException();
        }
      }
    }
    this.terrain = newMap;
  }

  /**
   * Getter for a Material at a specific point on the Map.
   * @param point A Coordinate that describes the desired point on the map
   * @return The Material at the point point
   */
  public Material getMaterialAtPoint(Coordinate point) {
    if (pointInsideMap(point)) {
      return terrain[point.getXvalue()][point.getYvalue()];
    }
    throw new IllegalArgumentException();
  }

  /**
   * Sets a new Material at a specific point.
   * @param point A Coordinate that describes the desired point on the map
   * @param newMaterial the new Material at point point
   */
  public void setMaterialAtPoint(Coordinate point, Material newMaterial) {
    if (pointInsideMap(point) && newMaterial != null) {
      terrain[point.getXvalue()][point.getYvalue()] = newMaterial;
    } else {
      throw new IllegalArgumentException();
    }
  }

  /**
   * applies a multiplicative radial linear gradient,
   * beginning at the start point.
   * @param start origin of the radial gradient field
   * @return The modified TerrainMap
   */
  public TerrainMap addRadialGradient( Coordinate start) {
    TerrainMap out = new TerrainMap(0,0);
    out.setCompleteMap(this.getCompleteMap());

    for (int x = 0 ; x < out.getCompleteMap().length ; x++) {
      for (int y = 0 ; y < out.getCompleteMap()[x].length ; y++) {
        int distance = (int)Math.sqrt(Math.pow(start.getXvalue() - x,2)
                + Math.pow(start.getYvalue() - y, 2));
        Material mat = out.getMaterialAtPoint(new Coordinate(x,y));
        mat.setCosts(mat.getCosts() * distance);
        out.setMaterialAtPoint(new Coordinate(x,y), mat);
      }
    }
    return out;
  }

  private boolean pointInsideMap(Coordinate point) {
    return point.getXvalue() >= 0 && point.getYvalue() >= 0
        && point.getXvalue() < terrain.length && point.getYvalue() < terrain[0].length;
  }
}
