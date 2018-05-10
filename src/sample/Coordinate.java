package sample;

/**
 * Class for the coordinate of a point.
 * @author Patrick Hanselmann, Jeffrey Manuel Rietzler
 * @version 0.1
 */
public class Coordinate {
  //the xvalue-coordinate of the point
  private int xvalue;
  //the yvalue-coordinate of the point
  private int yvalue;

  /**
   * Constructor for the class Coordinate.
   * @param xvalue the xvalue-coordinate for the point
   * @param yvalue the yvalue-coordinate for the point
   */
  public Coordinate(int xvalue, int yvalue) {
    this.xvalue = xvalue;
    this.yvalue = yvalue;
  }

  public int getXvalue() {
    return xvalue;
  }

  public void setXvalue(int xvalue) {
    this.xvalue = xvalue;
  }

  public int getYvalue() {
    return yvalue;
  }

  public void setYvalue(int yvalue) {
    this.yvalue = yvalue;
  }

  @Override
  public String toString() {
    return this.xvalue + ", " + this.yvalue;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Coordinate) {
      Coordinate other = (Coordinate) obj;
      return this.xvalue == other.xvalue && this.yvalue == other.yvalue;
    }
    return false;
  }
}
