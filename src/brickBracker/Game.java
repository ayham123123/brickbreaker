package brickBracker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class Game extends JPanel implements KeyListener, ActionListener {
    private boolean playing = false, difficultySet = false;
    private int score = 0, bricksLeft = 21, delay = 8, paddleX = 310, ballX = 120, ballY = 350, ballDirX, ballDirY;
    private Timer timer;
    private Map map;

    public Game() {
        map = new Map(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    private void ballDir() {
        Random rand = new Random();
        ballDirX = rand.nextInt(3) + 1;
        ballDirX *= rand.nextBoolean() ? 1 : -1;
        ballDirY = rand.nextInt(3) + 1;
        ballDirY *= rand.nextBoolean() ? 1 : -1;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        if (!difficultySet) {
            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Select Difficulty", 230, 200);
            g.setFont(new Font("serif", Font.PLAIN, 20));
            g.drawString("1: Easy", 290, 250);
            g.drawString("2: Medium", 290, 300);
            g.drawString("3: Hard", 290, 350);
            return;
        }

        map.draw((Graphics2D) g);

        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: " + score, 550, 30);

        g.setColor(Color.green);
        g.fillRect(paddleX, 550, 100, 8);

        g.setColor(Color.white);
        g.fillOval(ballX, ballY, 20, 20);

        if (bricksLeft <= 0) {
            endGame(g, "You Win! Score: " + score);
        } else if (ballY > 570) {
            endGame(g, "Game Over! Score: " + score);
        }

        g.dispose();
    }

    private void endGame(Graphics g, String message) {
        playing = false;
        ballDirX = ballDirY = 0;
        g.setColor(Color.red);
        g.setFont(new Font("serif", Font.BOLD, 30));
        g.drawString(message, 200, 300);
        g.setFont(new Font("serif", Font.PLAIN, 20));
        g.drawString("Press Enter to Restart", 230, 350);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (playing) {
            if (new Rectangle(ballX, ballY, 20, 20).intersects(new Rectangle(paddleX, 550, 100, 8))) {
                ballDirY = -ballDirY;
            }

            for (int i = 0; i < map.layout.length; i++) {
                for (int j = 0; j < map.layout[0].length; j++) {
                    if (map.layout[i][j] > 0) {
                        int brickX = j * map.brickW + 80;
                        int brickY = i * map.brickH + 50;
                        Rectangle brickRect = new Rectangle(brickX, brickY, map.brickW, map.brickH);

                        if (new Rectangle(ballX, ballY, 20, 20).intersects(brickRect)) {
                            map.setBrick(0, i, j);
                            bricksLeft--;
                            score += 5;
                            if (ballX + 19 <= brickRect.x || ballX + 1 >= brickRect.x + map.brickW) {
                                ballDirX = -ballDirX;
                            } else {
                                ballDirY = -ballDirY;
                            }
                        }
                    }
                }
            }

            ballX += ballDirX;
            ballY += ballDirY;

            if (ballX < 0 || ballX > 670) ballDirX = -ballDirX;
            if (ballY < 0) ballDirY = -ballDirY;
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!difficultySet) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_1: // Easy
                    delay = 15;
                    break;
                case KeyEvent.VK_2: // Medium
                    delay = 6;
                    break;
                case KeyEvent.VK_3: // Hard
                    delay = 3;
                    break;
                default:
                    return;
            }
            difficultySet = true;
            timer.setDelay(delay);
            ballDir();
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT && paddleX < 600) {
            movePaddle(20);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && paddleX > 10) {
            movePaddle(-20);
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER && !playing) {
            resetGame();
        }
    }

    private void movePaddle(int offset) {
        playing = true;
        paddleX += offset;
    }

    private void resetGame() {
        playing = true;
        ballX = 120;
        ballY = 350;
        paddleX = 310;
        score = 0;
        bricksLeft = 21;
        map = new Map(3, 7);
        difficultySet = false;
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
