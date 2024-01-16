package hr.fer.zemris.java.gui.charts;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Class representing and holding bar chart data
 */
public class BarChart {

    /**
     * List of pairs (x,y)
     */
    private final List<XYValue> values;

    /**
     * Description of x axis
     */
    private final String xInfo;

    /**
     * Description of y axis
     */
    private final String yInfo;

    /**
     * Minimum y value
     */
    private final int yMin;

    /**
     * Maximum y value
     */
    private final int yMax;

    /**
     * Distance between two y values
     */
    private final int deltaY;

    public BarChart(List<XYValue> values, String xInfo, String yInfo, int yMin, int yMax, int deltaY) {

        if (yMin < 0) {
            throw new IllegalArgumentException("yMin must be greater than 0");
        }
        if(yMax < yMin) {
            throw new IllegalArgumentException("yMax must be greater than yMin");
        }

        for(XYValue value : values) {
            if(value.getY() < yMin) {
                throw new IllegalArgumentException("Y value must be greater than yMin");
            }
        }

        this.values = values;
        this.xInfo = xInfo;
        this.yInfo = yInfo;
        this.yMin = yMin;
        this.yMax = yMax;
        this.deltaY = deltaY;
    }

    public static BarChart parse(String path) {

        try(BufferedReader br = Files.newBufferedReader(Path.of(path), Charset.defaultCharset());){

            String xInfo = br.readLine();
            String yInfo = br.readLine();
            String[] values = br.readLine().split("\\s+");
            int yMin = Integer.parseInt(br.readLine());
            int yMax = Integer.parseInt(br.readLine());
            int deltaY = Integer.parseInt(br.readLine());

            List<XYValue> xyValues = Stream.of(values).map(s -> s.split(",")).map(s -> new XYValue(s[0], s[1])).toList();

            return new BarChart(xyValues, xInfo, yInfo, yMin, yMax, deltaY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public List<XYValue> getValues() {
        return values;
    }

    public String getxInfo() {
        return xInfo;
    }

    public String getyInfo() {
        return yInfo;
    }

    public int getyMin() {
        return yMin;
    }

    public int getyMax() {
        return yMax;
    }

    public int getDeltaY() {
        return deltaY;
    }
}

