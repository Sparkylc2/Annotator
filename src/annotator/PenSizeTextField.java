package annotator;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PenSizeTextField extends JTextField {
    private final Color backgroundColor;

    private final Dimension dimension = new Dimension(150, 30);
    public PenSizeTextField(Color backgroundColor) {
        super("1");
        this.backgroundColor = backgroundColor;

        setBackground(backgroundColor);
        setForeground(Color.WHITE);
        try {
            float fontSize = 12;
            File file = new File(System.getProperty("user.dir") + "/src/annotator/fonts/Inter-Regular.ttf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(fontSize);
            setFont(font);
        } catch(FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setEditable(false);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.dispose();


        Graphics2D g2d = (Graphics2D) g.create();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());

            String info = getString();
            FontMetrics metrics = g2d.getFontMetrics();
            int textWidth = metrics.stringWidth(info);
            int textHeight = metrics.getHeight();

            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() - textHeight) / 2 + metrics.getAscent();
            g2d.setColor(getForeground());
            g2d.drawString(info, x, y);
        } finally {
            g2d.dispose();
        }
    }

    private static String getString() {
        String info;
        if(Main.Annotator.pen1.isSelected() || Main.Annotator.pen2.isSelected()) {
            info = String.format("Stroke: %.2f", (float) Main.Annotator.transparentDrawingPanel.currentThickness);
        } else if(Main.Annotator.transparentDrawingPanel.eraserActive) {
            info = String.format("Eraser: %.2f", (float) Main.Annotator.transparentDrawingPanel.eraserSize);
        } else {
            if(Main.Annotator.rectangle.isSelected()) {
                info = String.format("Stroke: %.2f", (float) Main.Annotator.transparentDrawingPanel.rectangleThickness);
            } else if(Main.Annotator.circle.isSelected()){
                info = String.format("Stroke: %.2f", (float) Main.Annotator.transparentDrawingPanel.circleThickness);
            } else {
                info = String.format("Stroke: %.2f", (float) Main.Annotator.transparentDrawingPanel.currentThickness);
            }
        }
        return info;
    }

    @Override
    public void setOpaque(boolean isOpaque) {
        super.setOpaque(false);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(200, 30);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(100, 30);
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(150, 30);
    }


}
