package annotator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class RoundedButton extends JButton {
    private final Color backgroundColor;
    private final Color selectedColor;
    private final Color foregroundColor;

    private Font font;

    private float fontSize = 12;
    private final int cornerRadius;

    public RoundedButton(String text, Color backgroundColor, Color foregroundColor, Color selectedColor, int cornerRadius) {
        super(text);
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.selectedColor = selectedColor;

        this.cornerRadius = cornerRadius;

        setOpaque(false);
        setContentAreaFilled(false);
        setForeground(foregroundColor);

        setBorder(new EmptyBorder(10, 10, 10, 10));


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

        if(getModel().isPressed()) {
            g2d.setColor(selectedColor);
        } else {
            g2d.setColor(backgroundColor);
        }

        g2d.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);

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
        // Provide a preferred size hint that fits the button's content and padding
        return new Dimension(100, 30); // Example, adjust as needed
    }
}

