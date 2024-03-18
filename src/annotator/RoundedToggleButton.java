package annotator;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class RoundedToggleButton  extends JToggleButton {
    private final Color backgroundColor;
    private final Color foregroundColor;
    private final Color selectedColor;
    private Font font;
    private final int cornerRadius;

    public RoundedToggleButton(String text, Color backgroundColor, Color foregroundColor, Color selectedColor, Color borderColor, int cornerRadius) {
        super(text);
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.selectedColor = selectedColor;

        this.cornerRadius = cornerRadius;

        setOpaque(false);
        setContentAreaFilled(false);
        setForeground(foregroundColor);

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


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
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw background
        Color fill = isSelected() ? selectedColor : backgroundColor;
        g2d.setColor(fill);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics(getFont());
        int x = (width - metrics.stringWidth(getText())) / 2;
        int y = ((height - metrics.getHeight()) / 2) + metrics.getAscent();
        g2d.setColor(foregroundColor);
        g2d.drawString(getText(), x, y);

        g2d.dispose();
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 30);
    }
}

