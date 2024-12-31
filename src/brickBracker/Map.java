package brickBracker;

import java.awt.*;

public class Map {
    public int[][] layout;
    public int brickW, brickH;

    public Map(int rows, int cols) {
        layout = new int[rows][cols];
        for (int[] row : layout) {
            java.util.Arrays.fill(row, 1);
        }
        brickW = 540 / cols;
        brickH = 150 / rows;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                if (layout[i][j] > 0) {
                    g.setColor(Color.white);
                    g.fillRect(j * brickW + 80, i * brickH + 50, brickW, brickH);

                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickW + 80, i * brickH + 50, brickW, brickH);
                }
            }
        }
    }

    public void setBrick(int value, int row, int col) {
        layout[row][col] = value;
    }
}
