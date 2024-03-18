package annotator;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.*;
import java.awt.Point;
public class TransparentDrawingPanel extends JPanel {

    public Point eraserRectanglePosition = new Point(0, 0);
    public Color eraserIconFillColor = new Color(255, 255, 255, 121);
    public Color eraserIconStrokeColor = new Color(0, 0, 0, 180);


    public Color Pen1Color = new Color(0, 0, 0);
    public Color Pen2Color = new Color(255, 0, 0);

    public Color RectangleColor = new Color(0, 0, 0);
    public Color CircleColor = new Color(0, 0, 0);


    public float rectangleThickness = 3;
    public float circleThickness = 3;

    public boolean eraserActive = false;

    public int eraserSize = 20;
    public float currentThickness = 3;


    public boolean wasShiftDownLastFrame = false;
    public boolean wasAltDownLastFrame = false;
    public boolean modifierStateChange = false;


    public final List<ColoredShape> shapes = new ArrayList<>();
    public final List<ColoredLine> lines = new ArrayList<>();

    public ColoredShape currentShape = null;
    public List<Point> currentLine = new ArrayList<>();

    public Point mouseDownPoint = new Point(0, 0);



    Point mousePoint = new Point(0, 0);
    JButton transparentDrawingFrameHitRegButton;

    TransparentDrawingPanel() {
        setLayout(null);
        setOpaque(false);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    mouseDownPoint = e.getPoint();
                    if(Main.Annotator.pen1.isSelected() || Main.Annotator.pen2.isSelected()) {
                        currentLine = new ArrayList<>();
                        currentLine.add(e.getPoint());
                        Color col;
                        if(Main.Annotator.pen1.isSelected()) {
                            col = Pen1Color;
                        } else if(Main.Annotator.pen2.isSelected()) {
                            col = Pen2Color;
                        } else {
                            col = Color.BLACK;
                        }
                        lines.add(new ColoredLine(currentLine, col, currentThickness));
                    } else if(Main.Annotator.rectangle.isSelected() || Main.Annotator.circle.isSelected()) {
                        Color shapeCol = null;
                        float thickness = 0;
                        if(Main.Annotator.rectangle.isSelected()) {
                            shapeCol = RectangleColor;
                            thickness = rectangleThickness;
                        } else if(Main.Annotator.circle.isSelected()) {
                            shapeCol = CircleColor;
                            thickness = circleThickness;
                        }

                        currentShape = new ColoredShape(e.getPoint(), thickness, shapeCol, Main.Annotator.rectangle.isSelected());
                        shapes.add(currentShape);
                    }

                    wasShiftDownLastFrame = e.isShiftDown();
                    wasAltDownLastFrame = e.isAltDown();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK) {
                    Main.Annotator.mousePosition = e.getPoint();
                    if(Main.Annotator.pen1.isSelected() || Main.Annotator.pen2.isSelected()) {
                        if(wasShiftDownLastFrame != e.isShiftDown() || wasAltDownLastFrame != e.isAltDown()) {
                            mouseDownPoint = currentLine.get(currentLine.size() - 1);
                            wasShiftDownLastFrame = e.isShiftDown();
                            wasAltDownLastFrame = e.isAltDown();
                            modifierStateChange = true;
                        }
                        if(modifierStateChange) {
                            return;
                        }
                        Point newPoint = null;

                        if(e.isAltDown() && e.isShiftDown()) {
                            Point diff = new Point(e.getPoint().x - mouseDownPoint.x, e.getPoint().y - mouseDownPoint.y);
                            int maxMag = Math.max(Math.abs(diff.x), Math.abs(diff.y));
                            newPoint = new Point(mouseDownPoint.x + (diff.x > 0 ? maxMag : -maxMag), mouseDownPoint.y + (diff.y > 0 ? maxMag : -maxMag));
                        } else if(e.isAltDown()) {
                            newPoint = new Point(mouseDownPoint.x, e.getPoint().y);
                        } else if(e.isShiftDown()) {
                            newPoint = new Point( e.getPoint().x, mouseDownPoint.y);
                        } else {
                            newPoint = e.getPoint();
                        }

                        if (currentLine != null && newPoint != null) {
                            currentLine.add(newPoint);
                        }
                    } else if(Main.Annotator.rectangle.isSelected() || Main.Annotator.circle.isSelected()) {
                        if(e.isAltDown() && e.isShiftDown()) {
                            Point diff = new Point(e.getPoint().x - currentShape.initPos.x, e.getPoint().y - currentShape.initPos.y);
                            int maxMag = Math.max(Math.abs(diff.x), Math.abs(diff.y));
                            currentShape.width = maxMag;
                            currentShape.height = maxMag;
                        } else if(e.isAltDown()) {
                            currentShape.height = (e.getPoint().y - currentShape.initPos.y);
                        } else if(e.isShiftDown()) {
                            currentShape.width = (e.getPoint().x - currentShape.initPos.x);
                        } else {
                            currentShape.width = (e.getPoint().x - currentShape.initPos.x);
                            currentShape.height = (e.getPoint().y - currentShape.initPos.y);
                        }
                    }
                }

                if(eraserActive) {
                    Main.Annotator.mousePosition = e.getPoint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    currentLine = null;
                    currentShape = null;
                    modifierStateChange = false;
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if(eraserActive) {
                    Main.Annotator.mousePosition = e.getPoint();
                    repaint();
                }
            }


        };


        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);


        transparentDrawingFrameHitRegButton = new JButton("agh");
        transparentDrawingFrameHitRegButton.setPreferredSize(new Dimension(Main.Annotator.transparentDrawingPanelWidth, Main.Annotator.transparentDrawingPanelHeight));

        add(transparentDrawingFrameHitRegButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<ColoredLine> coloredLinesSnapshot = new ArrayList<>(lines);

        for(ColoredLine coloredLine : coloredLinesSnapshot) {
            g2d.setColor(coloredLine.col);
            g2d.setStroke(new BasicStroke(coloredLine.thickness));

            GeneralPath path = new GeneralPath();
            if (!coloredLine.points.isEmpty()) {
                Point firstPoint = coloredLine.points.get(0);
                path.moveTo(firstPoint.x, firstPoint.y);

                for(int i = 1; i < coloredLine.points.size(); i++) {
                    Point p = coloredLine.points.get(i);
                    path.lineTo(p.x, p.y);
                }
            }

            g2d.draw(path);
        }

        for(ColoredShape shape : shapes) {
            g2d.setColor(shape.col);
            g2d.setStroke(new BasicStroke(shape.thickness));
            if(shape.isRectangle) {
                g2d.drawRect(shape.initPos.x, shape.initPos.y, shape.width, shape.height);
            } else {
                g2d.drawOval(shape.initPos.x, shape.initPos.y, shape.width, shape.height);
            }
        }

        if(eraserActive) {
            g2d.setColor(eraserIconStrokeColor);
            g2d.setStroke(new BasicStroke(5));
            g2d.drawOval(Main.Annotator.mousePosition.x - eraserSize / 2, Main.Annotator.mousePosition.y - eraserSize / 2, eraserSize, eraserSize);

            g2d.setColor(eraserIconFillColor);
            g2d.fillOval(Main.Annotator.mousePosition.x - eraserSize / 2, Main.Annotator.mousePosition.y - eraserSize / 2, eraserSize, eraserSize);
        }

    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Main.Annotator.transparentDrawingPanelWidth, Main.Annotator.transparentDrawingPanelHeight);
    }


    public void clear() {
        lines.clear();
        shapes.clear();
    }
}

