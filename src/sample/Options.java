package sample;

/**
 * Enum to specify which path shall be calculated.
 * @author Patrick Hanselmann, Jeffrey Rietzler
 */
public enum Options {
    /**
     *  option to calculate the path with the least costs.
     */
    COSTS_ONLY,

    /**
     * option to calculate the optimum path considering
     * the optimal costs and optimal numbers of visited coordinates.
     */
    COSTS_AND_NUMBER_POINTS
}
