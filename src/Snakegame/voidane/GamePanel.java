package Snakegame.voidane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    // The size of the program
    static final int SCEEN_WIDTH = 600;
    static final int SCEEN_HEIGHT = 600;

    // The size of the player
    static final int UNIT_SIZE = 25;

    // The max snake size
    static final int GAME_UNITS = (SCEEN_WIDTH * SCEEN_HEIGHT) / UNIT_SIZE;

    // Delay, Higher is slower, lower is faster
    static final int DELAY = 75;

    // The color of the snake when it eats more apples
    static int FADE = 0;

    // Stores all snake values from the x axis and y axis.
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];

    // Beginning number of body parts.
    int bodyParts = 6;

    // Amount of apples the player has eaten
    int applesEaten;

    // The random cordinate the apple will appear at
    int appleX;
    int appleY;

    // The direction of the snake
    char direction = 'R';

    // Is the game running
    boolean running = false;

    Timer timer;
    Random random;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCEEN_WIDTH, SCEEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (!running) {
            gameOver(g);
            return;
        }

        for (int i = 0; i < SCEEN_HEIGHT / UNIT_SIZE ; i++ ) {
            g.drawLine(i*UNIT_SIZE, 0,i*UNIT_SIZE, SCEEN_HEIGHT);
            g.drawLine(0, i*UNIT_SIZE, SCEEN_WIDTH, i*UNIT_SIZE);
        }
        g.setColor(Color.RED);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        for ( int i = 0 ; i < bodyParts ; i++ ) {
            if ( i == 0 ) {
                g.setColor(new Color(0,255,0));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
            else {
                g.setColor(new Color(45 + FADE,180 - FADE,0));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                g.setColor(new Color(FADE,255 - FADE,0));
                g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);
            }
        }
        g.setColor(Color.CYAN);
        g.setFont(new Font("Ink Free", Font.BOLD, 25));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCEEN_WIDTH - metrics.stringWidth(("Score: " + applesEaten))) / 2,
                g.getFont().getSize());
    }

    public void newApple() {
        appleX = random.nextInt( (int) (SCEEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt( (int) (SCEEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts ; i > 0 ; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;

        }
    }

    public void checkApple() {

        if (appleX == x[0] && appleY == y[0]) {
            bodyParts++;
            applesEaten++;
            if (FADE < 180)
                FADE += 2;
            newApple();
        }

    }

    public void checkCollisions() {

        for ( int i = bodyParts ; i > 0 ; i-- ) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        if (x[0] < 0 || x[0] > SCEEN_WIDTH-1 || y[0] < 0 || y[0] > SCEEN_HEIGHT-1) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }

    }

    public void gameOver(Graphics g) {
        g.setColor(Color.CYAN);
        g.setFont(new Font("Ink Free", Font.BOLD, 25));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCEEN_WIDTH - metrics1.stringWidth(("Score: " + applesEaten))) / 2,
                (SCEEN_HEIGHT / 2 )+ g.getFont().getSize());

        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCEEN_WIDTH - metrics2.stringWidth(("Game Over"))) / 2, SCEEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                break;

                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
