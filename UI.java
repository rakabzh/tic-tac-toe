import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;

public class UI extends JFrame {

    private int[][] board;
    private int player;
    private HashMap<Integer, Boolean> ais;
    private boolean done;
    private int game_number;

    private JLabel statusLabel;
    private JPanel boardPanel;
    private JPanel[][] cells; // Matrice 3x3
    private JButton newGameButton;
    private JButton quitButton;

    public UI() {
        this.board = State.empty_board();
        this.player = 1;
        this.done = false;
        this.ais = new HashMap<>();
        this.ais.put(1, false);
        this.ais.put(-1, false);
        this.game_number = 0;

        this.choose_player();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Morpion");
        this.setSize(300,400);
        this.setLocationRelativeTo(null);
    }

    private void choose_player(){
        getContentPane().removeAll();
        setLayout(null);

        JLabel title = new JLabel("Choisissez les joueur");
        title.setFont(new Font("Helvetica", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(50, 10, 200, 30);
        add(title);

        JComboBox player1 = new JComboBox<>();
        player1.addItemListener(itemEvent -> {
            ais.remove(1);
           if (itemEvent.getItem().equals("Humain")) {
               ais.put(1, false);
           }
           else {
               ais.put(1, true);
           }
        });
        player1.setBounds(130, 100, 100, 30);
        add(player1);

        JLabel label1 = new JLabel("Player 1 :");
        label1.setBounds(30, 100, 80, 30);
        add(label1);

        player1.addItem("Humain");
        player1.addItem("Robot");

        JComboBox player2 = new JComboBox<>();
        player2.addItemListener(itemEvent -> {
            ais.remove(-1);
            if (itemEvent.getItem().equals("Humain")) {
                ais.put(-1, false);
            }
            else {
                ais.put(-1, true);
            }
        });
        player2.setBounds(130, 150, 100, 30);
        add(player2);

        player2.addItem("Humain");
        player2.addItem("Robot");

        JLabel label2 = new JLabel("Player 2 :");
        label2.setBounds(30, 150, 80, 30);
        add(label2);

        JButton button = new JButton("Jouer");
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Helvetica", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> create_widgets());
        button.setBounds(75, 250, 100, 30);

        add(button);

        revalidate();
        repaint();
    }

    private void create_widgets() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.NORTH);

        boardPanel = new JPanel(new GridLayout(3, 3));
        cells = new JPanel[3][3];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                JPanel cell = new JPanel();
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                cell.setPreferredSize(new Dimension(100, 100));
                cell.setBackground(Color.WHITE);
                cell.setLayout(new BorderLayout());

                cells[x][y] = cell;
                boardPanel.add(cell);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        newGameButton = new JButton("Nouveau jeu");
        newGameButton.setFocusPainted(false);
        newGameButton.setBackground(Color.WHITE);
        newGameButton.setForeground(Color.BLACK);
        newGameButton.setFont(new Font("Helvetica", Font.BOLD, 16));
        newGameButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        newGameButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        newGameButton.addActionListener(e -> choose_player());
        buttonPanel.add(newGameButton);

        quitButton = new JButton("Quitter");
        quitButton.setFocusPainted(false);
        quitButton.setBackground(Color.WHITE);
        quitButton.setForeground(Color.BLACK);
        quitButton.setFont(new Font("Helvetica", Font.BOLD, 16));
        quitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        quitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        quitButton.addActionListener(e -> on_close());
        buttonPanel.add(quitButton);

        add(buttonPanel, BorderLayout.SOUTH);

        new_game();
        repaint();
    }

    private void on_player_change() {
        String name = "O";
        if (player == 1){
            name = "X";
        }
        String sort = "IA";
        if (!ais.get(player)){
            sort = "HUMAIN";
        }
        statusLabel.setText("À " + name + " de jouer [" + sort + "]");
        update_all_cells();
        if (ais.get(player)){
            Timer timer = new Timer(500, e -> play_ai(game_number));
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void play_ai(int game_number){
        if (this.game_number == game_number){
            List<Integer> coordonne = Solver.best_move(board, player);
            int x = coordonne.get(0);
            int y = coordonne.get(1);
            play(x,y);
        }
    }

    private void update_all_cells() {
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[0].length; y++) {
                update_cell(cells[x][y], x, y);
            }
        }
    }

    private void on_close() {
        this.dispose();
    }

    private void new_game() {
        player = 1;
        game_number++;
        board = State.empty_board();
        done = false;
        on_player_change();
    }

    private void play(int x, int y) {
        boolean win = State.play(board, x, y, player);
        
        if (win){
            on_win();
        } else if (State.is_board_full(board)) {
            on_draw();
        }
        else {
            player*=-1;
            on_player_change();
        }
    }

    private void on_win() {
        done = true;
        String name = "O";
        if (player==1){
            name = "X";
        }
        statusLabel.setText(name + " a gagné !");
        update_all_cells();
    }

    private void on_draw() {
        done = true;
        statusLabel.setText("Match nul !");
        update_all_cells();
    }

    private void update_cell(JPanel cell, int x, int y) {
        int value = State.token_at(board, x, y);

        cell.setBackground(Color.WHITE);
        for (MouseListener ml : cell.getMouseListeners()) {
            cell.removeMouseListener(ml);
        }
        for (MouseMotionListener mml : cell.getMouseMotionListeners()) {
            cell.removeMouseMotionListener(mml);
        }
        cell.setCursor(Cursor.getDefaultCursor());
        cell.removeAll();

        if (value == 0 && !done && !ais.get(player)){
            Cursor cursor;
            if (player == 1) {
                cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
            } else {
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            }

            cell.setCursor(cursor);
            cell.removeAll();
            cell.setLayout(new BorderLayout());

            cell.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    cell.setBackground(Color.LIGHT_GRAY);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    cell.setBackground(Color.WHITE);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        play(x, y);
                    }
                }
            });
        } else if (value == 1) {
            JLabel label = new JLabel("X", SwingConstants.CENTER);
            label.setFont(new Font("Helvetica", Font.BOLD, 32));
            label.setForeground(Color.BLUE);
            label.setBackground(Color.WHITE);
            label.setOpaque(true);

            cell.add(label, BorderLayout.CENTER);
        }
        else if (value == -1){
            JLabel label = new JLabel("O", SwingConstants.CENTER);
            label.setFont(new Font("Helvetica", Font.BOLD, 32));
            label.setForeground(Color.RED);
            label.setBackground(Color.WHITE);
            label.setOpaque(true);

            cell.add(label, BorderLayout.CENTER);
        }

        cell.revalidate();
        cell.repaint();
    }
}
