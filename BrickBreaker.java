// package brick-breaker-game;
//  First Task Project
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BrickBreaker extends JFrame implements KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 20;
    private static final int BALL_DIAMETER = 20;
    private static final int BRICK_WIDTH = 80;
    private static final int BRICK_HEIGHT = 30;
    private static final int NUM_ROWS = 5;
    private static final int NUM_COLS = 10;
    private static final int FPS = 25;

    private boolean running = true;
    private boolean gameOver = false;
    private int paddleX = WIDTH / 2 - PADDLE_WIDTH / 2;
    private int ballX = WIDTH / 2 - BALL_DIAMETER / 2;
    private int ballY = HEIGHT / 2 - BALL_DIAMETER / 2;
    private int ballSpeedX = 5;
    private int ballSpeedY = 5;
    private boolean[] bricks;
    private int score = 0;

    public BrickBreaker() {
        setTitle("Brick Breaker");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addKeyListener(this);

        bricks = new boolean[NUM_ROWS * NUM_COLS];
        for (int i = 0; i < bricks.length; i++) {
            bricks[i] = true;
        }

        Timer timer = new Timer(1000 / FPS, e -> {
            if (!gameOver) {
                update();
                repaint();
            }
        });
        timer.start();
    }

    private void update() {
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        // Check collision with walls
        if (ballX <= 0 || ballX >= WIDTH - BALL_DIAMETER) {
            ballSpeedX *= -1;
        }
        if (ballY <= 0) {
            ballSpeedY *= -1;
        }

        // Check collision with paddle
        if (ballY + BALL_DIAMETER >= HEIGHT - PADDLE_HEIGHT && ballX + BALL_DIAMETER >= paddleX && ballX <= paddleX + PADDLE_WIDTH) {
            ballSpeedY *= -1;
        }

        // Check collision with bricks
        for (int i = 0; i < bricks.length; i++) {
            if (bricks[i]) {
                int row = i / NUM_COLS;
                int col = i % NUM_COLS;
                int brickX = col * BRICK_WIDTH;
                int brickY = row * BRICK_HEIGHT;

                if (ballX + BALL_DIAMETER >= brickX && ballX <= brickX + BRICK_WIDTH &&
                        ballY + BALL_DIAMETER >= brickY && ballY <= brickY + BRICK_HEIGHT) {
                    bricks[i] = false;
                    ballSpeedY *= -1;
                    score++;
                    if (score == bricks.length) {
                        gameOver = true;
                    }
                    break;
                }
            }
        }

        // Check game over
        if (ballY >= HEIGHT) {
            gameOver = true;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw paddle
        g.setColor(Color.WHITE);
        g.fillRect(paddleX, HEIGHT - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Draw ball
        g.setColor(Color.RED);
        g.fillOval(ballX, ballY, BALL_DIAMETER, BALL_DIAMETER);

        // Draw bricks
        g.setColor(Color.GREEN);
        for (int i = 0; i < bricks.length; i++) {
            if (bricks[i]) {
                int row = i / NUM_COLS;
                int col = i % NUM_COLS;
                int brickX = col * BRICK_WIDTH;
                int brickY = row * BRICK_HEIGHT;
                g.fillRect(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT);
            }
        }

        // Draw score
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);

        // Draw game over message
        if (gameOver) {
            g.drawString("Game Over!", WIDTH / 2 - 50, HEIGHT / 2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            paddleX -= 20;
            if (paddleX < 0) {
                paddleX = 0;
            }
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            paddleX += 20;
            if (paddleX > WIDTH - PADDLE_WIDTH) {
                paddleX = WIDTH - PADDLE_WIDTH;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BrickBreaker game = new BrickBreaker();
            game.setVisible(true);
        });
    }
}
