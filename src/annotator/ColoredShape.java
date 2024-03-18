package annotator;


import java.awt.Color;

import java.awt.Point;

public class ColoredShape {

    public Point initPos;
    public float thickness;
    public Color col;

    public int width = 0;
    public int height = 0;
    public boolean isRectangle;

    public ColoredShape(Point initPos, float thickness, Color col, boolean isRectangle) {
        this.initPos = initPos;
        this.thickness = thickness;
        this.col = col;
        this.isRectangle = isRectangle;
    }
}


