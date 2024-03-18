package annotator;


import javax.swing.JToolBar;
import java.awt.*;

public class RoundedJToolBar extends JToolBar {
    private final Color backgroundColor = new Color(34, 35, 36, 255);
    private final Color borderColor = new Color(44, 44, 47, 255);
    private final int cornerRadius = 15;

    private final int toolbarHeight = 50;

    private final int toolbarWidth;
    public RoundedJToolBar() {
        super();
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        if(Main.Annotator.MacOS) {
            this.toolbarWidth = (int)(1.5 * Main.Annotator.screenWidth / 2f);
        } else {
            this.toolbarWidth = (int)(Main.Annotator.screenWidth /2f);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(0, 0, toolbarWidth, toolbarHeight, cornerRadius, cornerRadius);
        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(toolbarWidth, toolbarHeight);
    }


    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(this.borderColor);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(0, 0, toolbarWidth, toolbarHeight, cornerRadius, cornerRadius);
        g2d.dispose();
    }

}
