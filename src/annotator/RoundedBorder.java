package annotator;

import javax.swing.border.AbstractBorder;
import java.awt.*;
public class RoundedBorder extends AbstractBorder {
    private Color col;
    private int thickness;
    private int radius;

    public RoundedBorder(Color col, int thickness, int radius) {
        this.col = col;
        this.thickness = thickness;
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(col);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawRoundRect(x, y, width - thickness, height - thickness, radius, radius);
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }
}
