import javax.swing.*;
public class Main {


    public static void start_ui() {
        SwingUtilities.invokeLater(() -> {
            UI ui = new UI();
            ui.setVisible(true);
        });
    }

    public static void main(String[] args) {
        start_ui();
    }
}
