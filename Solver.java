import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Solver {
    public static List<Integer> random_move(int[][] board, int player) {
        List<List<Integer>> free = State.free_cells(board);
        Random random = new Random();
        return free.get(random.nextInt(0, free.size() - 1));
    }

    public static List<Integer> best_move(int[][] board, int player) {
        return minimax(board, player).getValue();
    }

    private static SimpleEntry<Integer, List<Integer>> minimax(int[][] board, int player) {
        List<Integer> best_move = null;
        int best_value = 0;
        int value = 0;

        List<List<Integer>> list = State.free_cells(board);
        for (int i = 0; i < list.size(); i++) {
            int x = list.get(i).get(0);
            int y = list.get(i).get(1);

            if (State.play(board, x, y, player)) {
                value = player;
            } else {
                value = minimax(board, -player).getKey();
            }
            State.unplay(board, x, y);

            if (best_move == null || player * value > player * best_value) {
                best_move = Arrays.asList(x, y);
                best_value = value;
            }
        }
        return new SimpleEntry<>(best_value, best_move);
    }
}
