package annotator;


import java.awt.Color;
import java.awt.Point;
import java.util.List;
public class ColoredLine {
    public List<Point> points;
    public Color col;
    public float thickness;

    public ColoredLine(List<Point> points, Color col, float thickness) {
        this.points = points;
        this.col = col;
        this.thickness = thickness;
    }
}
