import javax.swing.*;
import java.awt.*;

public class LevelUp2048 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Level Up 2048");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.setLayout(null);
            frame.getContentPane().setBackground(new Color(250, 248, 239));

            JLabel title = new JLabel("Level Up 2048", SwingConstants.CENTER);
            title.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
            title.setBounds(50, 30, 300, 50);
            title.setForeground(new Color(119, 110, 101));
            frame.add(title);

            JButton easyBtn = new JButton("Easy (4x4)");
            JButton mediumBtn = new JButton("Medium (5x5)");
            JButton hardBtn = new JButton("Hard (6x6)");

            Font btnFont = new Font("Comic Sans MS", Font.BOLD, 18);
            easyBtn.setFont(btnFont);
            mediumBtn.setFont(btnFont);
            hardBtn.setFont(btnFont);

            easyBtn.setBounds(100, 100, 200, 50);
            mediumBtn.setBounds(100, 170, 200, 50);
            hardBtn.setBounds(100, 240, 200, 50);

            easyBtn.addActionListener(e -> startGame(frame, 4));
            mediumBtn.addActionListener(e -> startGame(frame, 5));
            hardBtn.addActionListener(e -> startGame(frame, 6));

            frame.add(easyBtn);
            frame.add(mediumBtn);
            frame.add(hardBtn);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static void startGame(JFrame frame, int size) {
        frame.dispose();
        JFrame gameFrame = new JFrame("Level Up 2048");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GamePanel panel = new GamePanel(size);
        gameFrame.add(panel);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
        panel.requestFocusInWindow();
    }
}

