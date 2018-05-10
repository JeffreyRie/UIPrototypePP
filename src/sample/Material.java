package sample;

/**
 * This class represents a specific ground material.
 * @author Jeffrey Rietzler, Patrick Hanselmann
 * @version 0.1
 */
public class Material {

  private String name;
  private int costs;
  private boolean isObstacle;

  /**
   * Constructor for class Material.
   * @param name name of the material
   * @param costs integer costs to walk over this material
   * @param isObstacle indicates if the material shall be seen as a non-walkable material
   */
  public Material(String name, int costs, boolean isObstacle) {
    this.name = name;
    this.costs = costs;
    this.isObstacle = isObstacle;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCosts() {
    return costs;
  }

  public void setCosts(int costs) {
    this.costs = (costs >= 0 ? costs : 0);
  }

  public boolean isObstacle() {
    return isObstacle;
  }

  public void setObstacle(boolean obstacle) {
    isObstacle = obstacle;
  }
}
