import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Simulation extends JPanel implements ActionListener, KeyListener {
    private boolean paused = true;

    private final int FPS = 60;
    private final double TIME_SCALE = 1.0;

    private final ArrayList<Shape> shapes = new ArrayList<>();

    public Simulation() {
        Timer timer = new Timer(1000 / FPS, this);
        timer.start();

        setFocusable(true);
        addKeyListener(this);

        resetShapes();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Shape shape : shapes) {
            shape.draw(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double deltaTime = 1.0 / FPS * TIME_SCALE;

        if (!paused) simulate(deltaTime);

        repaint();
    }

    private void simulate(double deltaTime) {
        double timeLeft = deltaTime;
        while (timeLeft > 0) {
            Collision nextCollision = findNextCollision(timeLeft);
            if (nextCollision == null) {
                updateShapes(timeLeft);
                break;
            }

            System.out.println("Collision at " + nextCollision.getTime());

            updateShapes(nextCollision.getTime());
            timeLeft -= nextCollision.getTime();
            nextCollision.collide();

            nextCollision.getNode().setColliding(true);
            nextCollision.getConnector().setColliding(true);
        }
    }

    private Collision findNextCollision(double timeLeft) {
        Collision nextCollision = null;

        for (Shape shape1 : shapes) {
            for (Shape shape2 : shapes) {
                if (shape1 == shape2) {
                    continue;
                }
                
                Collision next = nextCollisionFromLists(shape1, shape2, timeLeft);

                nextCollision = nearestCollision(nextCollision, next);
            }
        }

        return nextCollision;
    }

    private Collision nearestCollision(Collision collision1, Collision collision2) {
        if (collision1 == null) {
            return collision2;
        }
        if (collision2 == null) {
            return collision1;
        }
        return collision1.getTime() < collision2.getTime() ? collision1 : collision2;
    }

    private Collision nextCollisionFromLists(Shape shape1, Shape shape2, double timeLeft) {
        Collision nextCollision = null;

        for (Node node : shape1.getNodes()) {
            for (Connector connector : shape2.getConnectors()) {
                Collision next = Collision.find(connector, node, timeLeft);

                nextCollision = nearestCollision(nextCollision, next);
            }
        }

        return nextCollision;
    }

    private void updateShapes(double timeLeft) {
        for (Shape shape : shapes) shape.update(timeLeft);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simulation");
        Simulation simulation = new Simulation();
        frame.add(simulation);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Handle key typed events
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                double deltaTime = 1.0 / FPS * TIME_SCALE;

                simulate(deltaTime);

                repaint();
                break;
            case KeyEvent.VK_P:
                paused = !paused;
                break;
            case KeyEvent.VK_R:
                resetShapes();
                break;
        }
    }

    private void resetShapes () {
        shapes.clear();

        Shape shape1 = new Shape(new Node[] {
                new Node(new double[] {650, 100}, new double[] {0, 0}, 1),
                new Node(new double[] {600, 200}, new double[] {0, 0}, 1),
                new Node(new double[] {700, 200}, new double[] {0, 1000}, 1),
        }, 1000);

        Shape shape2 = new Shape(new Node[] {
                new Node(new double[] {650, 700}, new double[] {0, 0}, 1),
                new Node(new double[] {750, 700}, new double[] {0, -600}, 1),
                new Node(new double[] {650, 800}, new double[] {0, 0}, 1),
        }, 1000);

        shapes.add(shape1);
        shapes.add(shape2);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}