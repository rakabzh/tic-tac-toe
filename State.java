import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class State {

    public State() {
    }

    public static int[][] empty_board(){
        return new int[3][3];
    }

    public static int token_at(int[][] board, int x, int y){
        return board[x][y];
    }
    public static boolean is_board_full(int[][] board) {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board[x][y] == 0){
                    return false;
                }
            }
        }
        return true;
    }

    public static ArrayList<List<Integer>> free_cells(int[][] board){
        ArrayList<List<Integer>> list = new ArrayList<>();
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board[x][y] == 0) {
                    list.add(Arrays.asList(x,y));
                }
            }
        }
        return list;
    }

    public static boolean play(int[][] board, int x, int y, int player){
        board[x][y] = player;
        int sum_colonne = board[x][y]+board[(x+1)%3][y]+board[(x+2)%3][y];
        int sum_ligne = board[x][y]+board[x][(y+1)%3]+board[x][(y+2)%3];
        if (Math.abs(board[0][0]+board[1][1]+board[2][2]) == 3 || Math.abs(board[0][2]+board[1][1]+board[2][0]) == 3){
            return true;
        }
        if (Math.abs(sum_colonne) == 3 || Math.abs(sum_ligne)==3) {
            return true;
        }
        return false;
    }

    public static int[][] unplay(int[][] board, int x, int y){
        board[x][y] = 0;
        return board;
    }
}
