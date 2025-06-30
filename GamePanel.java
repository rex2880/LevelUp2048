import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Stack;

public class GamePanel extends JPanel {
    private int SIZE;
    private static final int TILE_SIZE = 100;
    private static final int PADDING = 16;
    private static final Font FONT = new Font("Comic Sans MS", Font.BOLD, 32);
    private int[][] board;
    private Stack<int[][]> history = new Stack<>();
    private boolean moved;
    private int score = 0;
    private Random random = new Random();

    public GamePanel(int size) {
        this.SIZE = size;
        int totalSize = SIZE * (TILE_SIZE + PADDING) + PADDING;
        setPreferredSize(new Dimension(totalSize, totalSize + 60));
        setBackground(new Color(250, 248, 239));
        board = new int[SIZE][SIZE];

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                moved = false;
                if (e.getKeyCode() == KeyEvent.VK_U) {
                    undo();
                    return;
                }
                saveHistory();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP, KeyEvent.VK_W -> moveUp();
                    case KeyEvent.VK_DOWN, KeyEvent.VK_S -> moveDown();
                    case KeyEvent.VK_LEFT, KeyEvent.VK_A -> moveLeft();
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> moveRight();
                }
                if (moved) {
                    addRandomTile();
                    repaint();
                    checkGameState();
                }
            }
        });

        addRandomTile();
        addRandomTile();
    }

    private void saveHistory() {
        int[][] snapshot = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
            System.arraycopy(board[i], 0, snapshot[i], 0, SIZE);
        history.push(snapshot);
    }

    private void undo() {
        if (!history.isEmpty()) {
            board = history.pop();
            repaint();
        }
    }

    private void addRandomTile() {
        int empty = 0;
        for (int[] row : board)
            for (int tile : row)
                if (tile == 0) empty++;
        if (empty == 0) return;

        int pos = random.nextInt(empty);
        int value = random.nextDouble() < 0.9 ? 2 : 4;
        int count = 0;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (board[i][j] == 0)
                    if (count++ == pos) {
                        board[i][j] = value;
                        return;
                    }
    }

    private void moveUp() {
        for (int col = 0; col < SIZE; col++) {
            int[] temp = new int[SIZE];
            int idx = 0;
            for (int row = 0; row < SIZE; row++)
                if (board[row][col] != 0) {
                    if (idx > 0 && temp[idx - 1] == board[row][col]) {
                        temp[idx - 1] *= 2;
                        score += temp[idx - 1];
                        moved = true;
                    } else {
                        temp[idx++] = board[row][col];
                        moved |= (row != idx - 1);
                    }
                }
            for (int row = 0; row < SIZE; row++) board[row][col] = temp[row];
        }
    }

    private void moveDown() {
        for (int col = 0; col < SIZE; col++) {
            int[] temp = new int[SIZE];
            int idx = SIZE - 1;
            for (int row = SIZE - 1; row >= 0; row--)
                if (board[row][col] != 0) {
                    if (idx < SIZE - 1 && temp[idx + 1] == board[row][col]) {
                        temp[idx + 1] *= 2;
                        score += temp[idx + 1];
                        moved = true;
                    } else {
                        temp[idx--] = board[row][col];
                        moved |= (row != idx + 1);
                    }
                }
            for (int row = 0; row < SIZE; row++) board[row][col] = temp[row];
        }
    }

    private void moveLeft() {
        for (int row = 0; row < SIZE; row++) {
            int[] temp = new int[SIZE];
            int idx = 0;
            for (int col = 0; col < SIZE; col++)
                if (board[row][col] != 0) {
                    if (idx > 0 && temp[idx - 1] == board[row][col]) {
                        temp[idx - 1] *= 2;
                        score += temp[idx - 1];
                        moved = true;
                    } else {
                        temp[idx++] = board[row][col];
                        moved |= (col != idx - 1);
                    }
                }
            System.arraycopy(temp, 0, board[row], 0, SIZE);
        }
    }

    private void moveRight() {
        for (int row = 0; row < SIZE; row++) {
            int[] temp = new int[SIZE];
            int idx = SIZE - 1;
            for (int col = SIZE - 1; col >= 0; col--)
                if (board[row][col] != 0) {
                    if (idx < SIZE - 1 && temp[idx + 1] == board[row][col]) {
                        temp[idx + 1] *= 2;
                        score += temp[idx + 1];
                        moved = true;
                    } else {
                        temp[idx--] = board[row][col];
                        moved |= (col != idx + 1);
                    }
                }
            System.arraycopy(temp, 0, board[row], 0, SIZE);
        }
    }

    private void checkGameState() {
        for (int[] row : board)
            for (int val : row)
                if (val == 2048) {
                    JOptionPane.showMessageDialog(this, "🎉 You Win! Score: " + score);
                    return;
                }
        if (isGameOver()) {
            JOptionPane.showMessageDialog(this, "Game Over! Score: " + score);
        }
    }

    private boolean isGameOver() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (board[i][j] == 0) return false;

        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE - 1; j++)
                if (board[i][j] == board[i][j + 1]) return false;
        for (int j = 0; j < SIZE; j++)
            for (int i = 0; i < SIZE - 1; i++)
                if (board[i][j] == board[i + 1][j]) return false;

        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(FONT);
        g.setColor(new Color(119, 110, 101));
        g.drawString("Score: " + score + "    (Press U to Undo)", 20, 40);
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                drawTile(g, board[i][j], j * (TILE_SIZE + PADDING) + PADDING,
                        i * (TILE_SIZE + PADDING) + PADDING + 50);
    }

    private void drawTile(Graphics g, int val, int x, int y) {
        Color tileColor = switch (val) {
            case 0 -> new Color(205, 193, 180);
            case 2 -> new Color(238, 228, 218);
            case 4 -> new Color(237, 224, 200);
            case 8 -> new Color(242, 177, 121);
            case 16 -> new Color(245, 149, 99);
            case 32 -> new Color(246, 124, 95);
            case 64 -> new Color(246, 94, 59);
            case 128 -> new Color(237, 207, 114);
            case 256 -> new Color(237, 204, 97);
            case 512 -> new Color(237, 200, 80);
            case 1024 -> new Color(237, 197, 63);
            case 2048 -> new Color(237, 194, 46);
            default -> new Color(60, 58, 50);
        };
        g.setColor(tileColor);
        g.fillRoundRect(x, y, TILE_SIZE, TILE_SIZE, 20, 20);
        if (val != 0) {
            g.setColor(val <= 4 ? new Color(119, 110, 101) : Color.WHITE);
            g.setFont(FONT);
            String text = String.valueOf(val);
            FontMetrics fm = g.getFontMetrics();
            g.drawString(text, x + (TILE_SIZE - fm.stringWidth(text)) / 2,
                    y + (TILE_SIZE + fm.getAscent()) / 2 - 5);
        }
    }
}

