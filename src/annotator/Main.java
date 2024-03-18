package annotator;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.*;
import com.bric.colorpicker.ColorPickerDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        Annotator.setup();
        Annotator.draw();
    }

    public static class Annotator {
        public static int screenWidth;
        public static int screenHeight;
        public static Point dragStartPoint = null;
        public static boolean isRightMouseButtonDragging = false;
        public static boolean isLeftMouseButtonDragging = false;
        public static boolean isMiddleMouseButtonDragging = false;
        public static RoundedToggleButton previousToggleButton;
        public static int penSelectedID = 0;
        public static Point mousePosition = new Point(0, 0);
        public static JFrame transparentDrawingFrame;
        public static JFrame parentFrame;
        public static TransparentDrawingPanel transparentDrawingPanel;
        public static RoundedJToolBar toolbar;
        public static ButtonGroup hotbarButtonGroup;
        public static RoundedToggleButton pen1;
        public static RoundedToggleButton pen2;
        public static RoundedToggleButton rectangle;
        public static RoundedToggleButton circle;
        public static RoundedToggleButton eraserButton;
        public static RoundedToggleButton selectColorButton;
        public static boolean MacOS = System.getProperty("os.name").toLowerCase().contains("mac");
        public static int transparentDrawingPanelWidth;
        public static int transparentDrawingPanelHeight;

        public static void setup() {
            initScreen();
            initPanelVariables();
            initParentFrame();
            initTransparentDrawingFrame();

            initGlobalListeners();
        }

        public static void draw() {
            while (true) {
                transparentDrawingPanel.repaint();
            }
        }

        public static void initScreen() {
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            screenWidth = gd.getDisplayMode().getWidth();
            screenHeight = gd.getDisplayMode().getHeight();
        }

        public static void initGlobalListeners() {
            try {
                GlobalScreen.registerNativeHook();
            } catch (Exception e) {
                e.printStackTrace();
            }

            GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
                public void nativeKeyPressed(NativeKeyEvent e) {
                    keyPress(e.getKeyCode(), e);
                }

                public void nativeKeyReleased(NativeKeyEvent e) {
                    keyRelease(e);
                }

            });

            GlobalScreen.addNativeMouseListener(new NativeMouseListener() {

                public void nativeMousePressed(NativeMouseEvent e) {
                    mousePressedHandler(e);
                }


                public void nativeMouseReleased(NativeMouseEvent e) {
                    mouseReleasedHandler(e);
                }


                public void nativeMouseClicked(NativeMouseEvent e) {

                }
            });

            GlobalScreen.addNativeMouseWheelListener(new NativeMouseWheelListener() {

                public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
                    mouseScrollHandler(e);
                }
            });

            GlobalScreen.addNativeMouseMotionListener(new NativeMouseMotionListener() {

                public void nativeMouseDragged(NativeMouseEvent e) {
                    mouseDragHandler(e);
                }


                public void nativeMouseMoved(NativeMouseEvent e) {
                    mouseMovedHandler(e);
                }
            });
        }

        public static void initPanelVariables() {
            if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                transparentDrawingPanelWidth = screenWidth;
                transparentDrawingPanelHeight = screenWidth - 125;
            } else {
                transparentDrawingPanelWidth = screenWidth;
                transparentDrawingPanelHeight = screenHeight;
            }
        }

        public static void initParentFrame() {
            parentFrame = new JFrame("Annotator");
            parentFrame.setVisible(false);
            parentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            parentFrame.removeNotify();
            parentFrame.setUndecorated(true);
            parentFrame.setOpacity(0f);
            parentFrame.setLayout(null);
        }


        public static void initTransparentDrawingFrame() {
                    transparentDrawingFrame = new JFrame("Transparent Drawing Frame");
                    transparentDrawingFrame.setAlwaysOnTop(true);
                    transparentDrawingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    transparentDrawingFrame.setUndecorated(true);
                    transparentDrawingFrame.setBackground(new Color(255, 255, 255, 1));

                    transparentDrawingPanel = new TransparentDrawingPanel();
                    transparentDrawingPanel.setPreferredSize(new Dimension(transparentDrawingPanelWidth, transparentDrawingPanelHeight));
                    transparentDrawingPanel.setLayout(new BorderLayout());
                    transparentDrawingFrame.add(transparentDrawingPanel);

                    toolbar = new RoundedJToolBar();
                    toolbar.setFloatable(false);


                    Color toggleBackgroundColor = new Color(55, 55, 55, 255);
                    Color toggleForegroundColor = new Color(255, 255, 255, 255);
                    Color toggleBackgroundSelectedColor = new Color(82, 82, 82, 255);
                    Color toggleBorderColor = new Color(44, 44, 47, 255);

                    int toggleCornerRadius = 15;
                    int toggleBorderThickness = 0;


                    hotbarButtonGroup = new ButtonGroup();

                    pen1 = new RoundedToggleButton("Pen 1",
                            toggleBackgroundColor,
                            toggleForegroundColor,
                            toggleBackgroundSelectedColor,
                            toggleBorderColor,
                            toggleCornerRadius);

                    pen1.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            setCurrentActiveButton();
                        }
                    });

                    pen2 = new RoundedToggleButton("Pen 2",
                            toggleBackgroundColor,
                            toggleForegroundColor,
                            toggleBackgroundSelectedColor,
                            toggleBorderColor,
                            toggleCornerRadius);
                    pen2.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            setCurrentActiveButton();
                        }
                    });

                    rectangle = new RoundedToggleButton("Rectangle",
                            toggleBackgroundColor,
                            toggleForegroundColor,
                            toggleBackgroundSelectedColor,
                            toggleBorderColor,
                            toggleCornerRadius);
                    rectangle.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (rectangle.isSelected()) {
                                //
                            } else {
                                //
                            }
                            setCurrentActiveButton();
                        }
                    });

                    circle = new RoundedToggleButton("Circle",
                            toggleBackgroundColor,
                            toggleForegroundColor,
                            toggleBackgroundSelectedColor,
                            toggleBorderColor,
                            toggleCornerRadius);
                    circle.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (circle.isSelected()) {

                            } else {
                                //
                            }
                            setCurrentActiveButton();
                        }
                    });


                    eraserButton = new RoundedToggleButton("Eraser",
                            toggleBackgroundColor,
                            toggleForegroundColor,
                            toggleBackgroundSelectedColor,
                            toggleBorderColor,
                            toggleCornerRadius);
                    eraserButton.addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent e) {
                            if (e.getStateChange() == ItemEvent.SELECTED) {
                                transparentDrawingPanel.eraserActive = true;
                            } else {
                                transparentDrawingPanel.eraserActive = false;
                            }
                            setCurrentActiveButton();
                        }
                    });

                    selectColorButton = new RoundedToggleButton("Select Color",
                            toggleBackgroundColor,
                            toggleForegroundColor,
                            toggleBackgroundSelectedColor,
                            toggleBorderColor,
                            toggleCornerRadius);
                    selectColorButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (selectColorButton.isSelected()) {
                                transparentDrawingFrame.setAlwaysOnTop(false);

                                if (pen1.isSelected()) {
                                    transparentDrawingPanel.Pen1Color = ColorPickerDialog.showDialog(transparentDrawingFrame, "Pen 1 Color", transparentDrawingPanel.Pen1Color, false);
                                } else if (pen2.isSelected()) {
                                    transparentDrawingPanel.Pen2Color = ColorPickerDialog.showDialog(transparentDrawingFrame, "Pen 2 Color", transparentDrawingPanel.Pen2Color, false);
                                } else if (rectangle.isSelected()) {
                                    transparentDrawingPanel.RectangleColor = ColorPickerDialog.showDialog(transparentDrawingFrame, "Rectangle Color", transparentDrawingPanel.RectangleColor, false);
                                } else if (circle.isSelected()) {
                                    transparentDrawingPanel.CircleColor = ColorPickerDialog.showDialog(transparentDrawingFrame, "Circle Color", transparentDrawingPanel.CircleColor, false);
                                }
                                selectColorButton.setSelected(false);
                                transparentDrawingFrame.setAlwaysOnTop(false);
                            }
                        }
                    });

                    PenSizeTextField penSizeTextField = new PenSizeTextField(toggleBackgroundColor);

                    RoundedButton clearButton = new RoundedButton("Clear",
                            toggleBackgroundColor,
                            toggleForegroundColor,
                            toggleBackgroundSelectedColor,
                            toggleCornerRadius);
                    clearButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            transparentDrawingPanel.clear();
                        }
                    });

                    hotbarButtonGroup.add(pen1);
                    hotbarButtonGroup.add(pen2);
                    hotbarButtonGroup.add(rectangle);
                    hotbarButtonGroup.add(circle);
                    hotbarButtonGroup.add(eraserButton);


                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.anchor = GridBagConstraints.CENTER;
                    gbc.insets = new Insets(0, 10, 0, 10);

                    Dimension buttonSize = new Dimension(100, 30);
                    pen1.setPreferredSize(buttonSize);
                    pen1.setMaximumSize(buttonSize);
                    pen2.setPreferredSize(buttonSize);
                    pen2.setMaximumSize(buttonSize);
                    rectangle.setPreferredSize(buttonSize);
                    rectangle.setMaximumSize(buttonSize);
                    circle.setPreferredSize(buttonSize);
                    circle.setMaximumSize(buttonSize);
                    selectColorButton.setPreferredSize(new Dimension(50, 30));
                    selectColorButton.setMaximumSize(new Dimension(50, 30));
                    clearButton.setPreferredSize(buttonSize);
                    clearButton.setMaximumSize(buttonSize);
                    eraserButton.setPreferredSize(buttonSize);
                    eraserButton.setMaximumSize(buttonSize);
                    penSizeTextField.setPreferredSize(new Dimension(150, 30));
                    penSizeTextField.setMaximumSize(new Dimension(150, 30));

                    JPanel colorButtonsPanel = new JPanel();
                    colorButtonsPanel.setLayout(new GridBagLayout());
                    colorButtonsPanel.setOpaque(false);
                    colorButtonsPanel.add(pen1, gbc);
                    colorButtonsPanel.add(pen2, gbc);
                    colorButtonsPanel.add(rectangle, gbc);
                    colorButtonsPanel.add(circle, gbc);
                    colorButtonsPanel.add(eraserButton, gbc);
                    colorButtonsPanel.add(selectColorButton, gbc);

                    JPanel clearButtonPanel = new JPanel();
                    clearButtonPanel.setLayout(new GridBagLayout());
                    clearButtonPanel.setOpaque(false);
                    clearButtonPanel.add(clearButton, gbc);

                    JPanel penSizeTextFieldPanel = new JPanel();
                    penSizeTextFieldPanel.setLayout(new GridBagLayout());
                    penSizeTextFieldPanel.setOpaque(false);
                    penSizeTextFieldPanel.add(penSizeTextField, gbc);

                    toolbar.setLayout(new BorderLayout());
                    toolbar.add(colorButtonsPanel, BorderLayout.WEST);
                    toolbar.add(penSizeTextFieldPanel, BorderLayout.CENTER);
                    toolbar.add(clearButtonPanel, BorderLayout.EAST);


                    JPanel panel = new JPanel();
                    panel.setOpaque(false);
                    panel.add(toolbar);
                    transparentDrawingPanel.add(panel, BorderLayout.NORTH);

                    transparentDrawingFrame.pack();
                    transparentDrawingFrame.setLocationRelativeTo(null);
                    transparentDrawingFrame.setVisible(true);

                }


        public static void stop() {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public static void keyPress(int keyCode, NativeKeyEvent e) {

    /*
    -------------------- Switching between drawing frame focus and unfocus -------------------------
    */
            if ((e.getModifiers() & NativeKeyEvent.CTRL_MASK) != 0 && keyCode == NativeKeyEvent.VC_SPACE) {
                transparentDrawingFrame.setAlwaysOnTop(true);

                if (MacOS) {
                    if (transparentDrawingFrame.isVisible()) {
                        transparentDrawingFrame.setVisible(false);
                    } else {
                        transparentDrawingFrame.setVisible(true);
                        transparentDrawingFrame.requestFocus();
                        transparentDrawingFrame.toFront();
                    }
                } else {
                    if (transparentDrawingFrame.isFocused()) {
                        transparentDrawingFrame.setExtendedState(JFrame.ICONIFIED);
                    } else {
                        transparentDrawingFrame.setExtendedState(JFrame.NORMAL);
                        transparentDrawingFrame.requestFocus();
                        transparentDrawingFrame.toFront();
                    }
                }
            }




    /*
    -------------------- Check for window focus -------------------------
    */
            if (!transparentDrawingFrame.isFocused()) {
                return;
            }

    /*
    -------------------- Clearing the drawing -------------------------
    */
            if (keyCode == NativeKeyEvent.VC_R) {
                transparentDrawingPanel.clear();
            }


    /*
    -------------------- Exiting the program -------------------------
    */
            if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
                stop();
                System.exit(0);
            }

    /*
    -------------------- Changing the pen color -------------------------
    */
            if (e.getKeyCode() == NativeKeyEvent.VC_1) {
                pen1.setSelected(true);
            } else if (e.getKeyCode() == NativeKeyEvent.VC_2) {
                pen2.setSelected(true);
            } else if (e.getKeyCode() == NativeKeyEvent.VC_3) {
                rectangle.setSelected(true);
            } else if (e.getKeyCode() == NativeKeyEvent.VC_4) {
                circle.setSelected(true);
            } else if (e.getKeyCode() == NativeKeyEvent.VC_5) {
                eraserButton.setSelected(true);
                transparentDrawingPanel.eraserActive = true;
            }

            if (e.getKeyCode() == NativeKeyEvent.VC_Q) {
                if (penSelectedID > 0) {
                    penSelectedID--;
                } else {
                    penSelectedID = 4;
                }
                handleQESwitching();
            }

            if (e.getKeyCode() == NativeKeyEvent.VC_E) {
                if (penSelectedID < 4) {
                    penSelectedID++;
                } else {
                    penSelectedID = 0;
                }
                handleQESwitching();

            }

    /*
    -------------------- Tab Switching -------------------------
    */
        }

        public static void keyRelease(NativeKeyEvent e) {
        }


        public static void mouseDragHandler(NativeMouseEvent e) {
            if (!transparentDrawingFrame.isFocused()) {
                return;
            }

            if (isMiddleMouseButtonDragging && dragStartPoint != null) {
                int dx = e.getX() - dragStartPoint.x;
                int dy = e.getY() - dragStartPoint.y;

                for (ColoredLine line : transparentDrawingPanel.lines) {
                    for (Point p : line.points) {
                        p.x += dx;
                        p.y += dy;
                    }
                }
                dragStartPoint = e.getPoint();
            } else if (isRightMouseButtonDragging || transparentDrawingPanel.eraserActive) {
                erasePoints(e);
            }
        }


        public static void mouseScrollHandler(NativeMouseWheelEvent e) {
            if (!transparentDrawingFrame.isFocused()) {
                return;
            }

            if (!(isLeftMouseButtonDragging || isMiddleMouseButtonDragging)) {
                if (e.getWheelRotation() > 0) {
                    if (isRightMouseButtonDragging || transparentDrawingPanel.eraserActive) {
                        transparentDrawingPanel.eraserSize = Math.min(Math.max(transparentDrawingPanel.eraserSize - Math.abs(e.getWheelRotation()), 1), 100);
                    } else if (pen1.isSelected() || pen2.isSelected()) {
                        transparentDrawingPanel.currentThickness = Math.min(Math.max(transparentDrawingPanel.currentThickness - Math.abs(e.getWheelRotation()), 1), 30);
                    } else if (rectangle.isSelected()) {
                        transparentDrawingPanel.rectangleThickness = Math.min(Math.max(transparentDrawingPanel.rectangleThickness - Math.abs(e.getWheelRotation()), 1), 30);
                        if (transparentDrawingPanel.currentShape != null) {
                            transparentDrawingPanel.currentShape.thickness = transparentDrawingPanel.rectangleThickness;
                        }
                    } else if (circle.isSelected()) {
                        transparentDrawingPanel.circleThickness = Math.min(Math.max(transparentDrawingPanel.circleThickness - Math.abs(e.getWheelRotation()), 1), 30);
                        if (transparentDrawingPanel.currentShape != null) {
                            transparentDrawingPanel.currentShape.thickness = transparentDrawingPanel.circleThickness;
                        }
                    }
                } else {
                    if (isRightMouseButtonDragging || transparentDrawingPanel.eraserActive) {
                        transparentDrawingPanel.eraserSize = Math.min(Math.max(transparentDrawingPanel.eraserSize + Math.abs(e.getWheelRotation()), 1), 100);
                    } else if (pen1.isSelected() || pen2.isSelected()) {
                        transparentDrawingPanel.currentThickness = Math.min(Math.max(transparentDrawingPanel.currentThickness + Math.abs(e.getWheelRotation()), 1), 30);
                    } else if (rectangle.isSelected()) {
                        transparentDrawingPanel.rectangleThickness = Math.min(Math.max(transparentDrawingPanel.rectangleThickness + Math.abs(e.getWheelRotation()), 1), 30);
                        if (transparentDrawingPanel.currentShape != null) {
                            transparentDrawingPanel.currentShape.thickness = transparentDrawingPanel.rectangleThickness;
                        }
                    } else if (circle.isSelected()) {
                        transparentDrawingPanel.circleThickness = Math.min(Math.max(transparentDrawingPanel.circleThickness + Math.abs(e.getWheelRotation()), 1), 30);
                        if (transparentDrawingPanel.currentShape != null) {
                            transparentDrawingPanel.currentShape.thickness = transparentDrawingPanel.circleThickness;
                        }
                    }
                }
            }
        }

        public static void mousePressedHandler(NativeMouseEvent e) {
            if (e.getButton() == NativeMouseEvent.BUTTON2) {
                isRightMouseButtonDragging = true;
                eraserButton.setSelected(true);
            } else if (e.getButton() == NativeMouseEvent.BUTTON1) {
                isLeftMouseButtonDragging = true;
            } else if (e.getButton() == NativeMouseEvent.BUTTON3) {
                isMiddleMouseButtonDragging = true;
                dragStartPoint = e.getPoint();
            }
        }

        public static void mouseReleasedHandler(NativeMouseEvent e) {
            setCurrentActiveButton();
            if (e.getButton() == NativeMouseEvent.BUTTON2) {
                isRightMouseButtonDragging = false;
                transparentDrawingPanel.eraserActive = false;
                revertUIState();
            } else if (e.getButton() == NativeMouseEvent.BUTTON1) {
                isLeftMouseButtonDragging = false;
            } else if (e.getButton() == NativeMouseEvent.BUTTON3) {
                isMiddleMouseButtonDragging = false;
                dragStartPoint = null;
            }
        }

        public static void mouseMovedHandler(NativeMouseEvent e) {
            if (!transparentDrawingFrame.isFocused()) {
                return;
            }
        }

        public static void erasePoints(NativeMouseEvent e) {
            int eraserSize = transparentDrawingPanel.eraserSize;
            Ellipse2D.Double eraserArea = new Ellipse2D.Double(mousePosition.x - eraserSize / 2, mousePosition.y - eraserSize / 2, eraserSize, eraserSize);

            java.util.List<ColoredLine> newLines = new ArrayList<>();

            for (ColoredLine line : transparentDrawingPanel.lines) {
                java.util.List<Point> linePoints = line.points;
                List<Point> newLine = new ArrayList<>();
                boolean lineSplit = false;

                for (Point p : linePoints) {
                    if (!eraserArea.contains(p)) {
                        newLine.add(p);
                    } else {
                        if (!newLine.isEmpty()) {
                            newLines.add(new ColoredLine(new ArrayList<>(newLine), line.col, line.thickness));
                            newLine.clear();
                            lineSplit = true;
                        }
                    }
                }

                if (!newLine.isEmpty()) {
                    newLines.add(new ColoredLine(newLine, line.col, line.thickness));
                } else if (!lineSplit) {
                    newLines.add(line);
                }
            }

            for (ColoredShape shape : new ArrayList<>(transparentDrawingPanel.shapes)) {
                if (eraserArea.intersects(shape.initPos.x, shape.initPos.y, shape.width, shape.height)) {
                    transparentDrawingPanel.shapes.remove(shape);
                }
            }

            transparentDrawingPanel.lines.clear();
            transparentDrawingPanel.lines.addAll(newLines);

        }


        public static void revertUIState() {
            if (previousToggleButton == pen1) {
                pen1.setSelected(true);
            } else if (previousToggleButton == pen2) {
                pen2.setSelected(true);
            } else if (previousToggleButton == rectangle) {
                rectangle.setSelected(true);
            } else if (previousToggleButton == circle) {
                circle.setSelected(true);
            }
            transparentDrawingFrame.repaint();
        }


        public static void handleQESwitching() {
            switch (penSelectedID) {
                case 0:
                    pen1.setSelected(true);
                    break;
                case 1:
                    pen2.setSelected(true);
                    break;
                case 2:
                    rectangle.setSelected(true);
                    break;
                case 3:
                    circle.setSelected(true);
                    break;
                case 4:
                    eraserButton.setSelected(true);
                    transparentDrawingPanel.eraserActive = true;
                    break;
                default:
                    pen1.setSelected(true);
                    transparentDrawingPanel.eraserActive = false;
                    break;
            }
        }


        public static void setCurrentActiveButton() {

            if (previousToggleButton == null) {
                previousToggleButton = pen1;
            }

            if (pen1.isSelected()) {
                previousToggleButton = pen1;
            } else if (pen2.isSelected()) {
                previousToggleButton = pen2;
            } else if (rectangle.isSelected()) {
                previousToggleButton = rectangle;
            } else if (circle.isSelected()) {
                previousToggleButton = circle;
            }
        }

        public Point getDragStartPoint() {
            return dragStartPoint;
        }

        public void setDragStartPoint(Point dragStartPoint) {
            Annotator.dragStartPoint = dragStartPoint;
        }

        public boolean isRightMouseButtonDragging() {
            return isRightMouseButtonDragging;
        }

        public void setRightMouseButtonDragging(boolean rightMouseButtonDragging) {
            isRightMouseButtonDragging = rightMouseButtonDragging;
        }

        public boolean isLeftMouseButtonDragging() {
            return isLeftMouseButtonDragging;
        }

        public void setLeftMouseButtonDragging(boolean leftMouseButtonDragging) {
            isLeftMouseButtonDragging = leftMouseButtonDragging;
        }

        public boolean isMiddleMouseButtonDragging() {
            return isMiddleMouseButtonDragging;
        }

        public void setMiddleMouseButtonDragging(boolean middleMouseButtonDragging) {
            isMiddleMouseButtonDragging = middleMouseButtonDragging;
        }

        public RoundedToggleButton getPreviousToggleButton() {
            return previousToggleButton;
        }

        public void setPreviousToggleButton(RoundedToggleButton previousToggleButton) {
            Annotator.previousToggleButton = previousToggleButton;
        }

        public int getPenSelectedID() {
            return penSelectedID;
        }

        public void setPenSelectedID(int penSelectedID) {
            Annotator.penSelectedID = penSelectedID;
        }

        public Point getMousePosition() {
            return mousePosition;
        }

        public void setMousePosition(Point mousePosition) {
            Annotator.mousePosition = mousePosition;
        }

        public JFrame getTransparentDrawingFrame() {
            return transparentDrawingFrame;
        }

        public void setTransparentDrawingFrame(JFrame transparentDrawingFrame) {
            Annotator.transparentDrawingFrame = transparentDrawingFrame;
        }

        public JFrame getParentFrame() {
            return parentFrame;
        }

        public void setParentFrame(JFrame parentFrame) {
            Annotator.parentFrame = parentFrame;
        }

        public TransparentDrawingPanel getTransparentDrawingPanel() {
            return transparentDrawingPanel;
        }

        public void setTransparentDrawingPanel(TransparentDrawingPanel transparentDrawingPanel) {
            Annotator.transparentDrawingPanel = transparentDrawingPanel;
        }

        public RoundedJToolBar getToolbar() {
            return toolbar;
        }

        public void setToolbar(RoundedJToolBar toolbar) {
            Annotator.toolbar = toolbar;
        }

        public ButtonGroup getHotbarButtonGroup() {
            return hotbarButtonGroup;
        }

        public void setHotbarButtonGroup(ButtonGroup hotbarButtonGroup) {
            Annotator.hotbarButtonGroup = hotbarButtonGroup;
        }

        public RoundedToggleButton getPen1() {
            return pen1;
        }

        public void setPen1(RoundedToggleButton pen1) {
            Annotator.pen1 = pen1;
        }

        public RoundedToggleButton getPen2() {
            return pen2;
        }

        public void setPen2(RoundedToggleButton pen2) {
            Annotator.pen2 = pen2;
        }

        public RoundedToggleButton getRectangle() {
            return rectangle;
        }

        public void setRectangle(RoundedToggleButton rectangle) {
            Annotator.rectangle = rectangle;
        }

        public RoundedToggleButton getCircle() {
            return circle;
        }

        public void setCircle(RoundedToggleButton circle) {
            Annotator.circle = circle;
        }

        public RoundedToggleButton getEraserButton() {
            return eraserButton;
        }

        public void setEraserButton(RoundedToggleButton eraserButton) {
            Annotator.eraserButton = eraserButton;
        }

        public RoundedToggleButton getSelectColorButton() {
            return selectColorButton;
        }

        public void setSelectColorButton(RoundedToggleButton selectColorButton) {
            Annotator.selectColorButton = selectColorButton;
        }

        public boolean isMacOS() {
            return MacOS;
        }

        public void setMacOS(boolean macOS) {
            MacOS = macOS;
        }

        public int getTransparentDrawingPanelWidth() {
            return transparentDrawingPanelWidth;
        }

        public void setTransparentDrawingPanelWidth(int transparentDrawingPanelWidth) {
            Annotator.transparentDrawingPanelWidth = transparentDrawingPanelWidth;
        }

        public int getTransparentDrawingPanelHeight() {
            return transparentDrawingPanelHeight;
        }

        public void setTransparentDrawingPanelHeight(int transparentDrawingPanelHeight) {
            Annotator.transparentDrawingPanelHeight = transparentDrawingPanelHeight;
        }
    }
}
