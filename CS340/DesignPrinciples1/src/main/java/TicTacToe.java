/*1. What design principles does this code violate?
    It is not using high quality abstraction, and things are too visible
    Game doesn't describe the class well.

2. Refactor the code to improve its design.
    a "board" could probably be its own class
 */


//Game.java

public class TicTacToe {
    private StringBuffer board;

    public TicTacToe(String s) {board = new StringBuffer(s);}

    public TicTacToe(StringBuffer s, int position, char player) {
        board = new StringBuffer();
        board.append(s);
        board.setCharAt(position, player);
    }

    public int move(char player) {
        for (int i = 0; i < 9; i++) {
            if (board.charAt(i) == '-') {
                TicTacToe game = play(i, player);
                if (game.winner() == player)
                    return i;
            }
        }

        for (int i = 0; i < 9; i++) {
            if (board.charAt(i) == '-')
                return i;
        }
        return -1;
    }

    private TicTacToe play(int i, char player) {
        return new TicTacToe(this.board, i, player);
    }

    public char winner() {
        if (board.charAt(0) != '-'
                && board.charAt(0) == board.charAt(1)
                && board.charAt(1) == board.charAt(2))
            return board.charAt(0);
        if (board.charAt(3) != '-'
                && board.charAt(3) == board.charAt(4)
                && board.charAt(4) == board.charAt(5))
            return board.charAt(3);
        if (board.charAt(6) != '-'
                && board.charAt(6) == board.charAt(7)
                && board.charAt(7) == board.charAt(8))
            return board.charAt(6);
        return '-';
    }
}

//TicTacToeTest.java

        import junit.framework.*;

public class TicTacToeTest extends TestCase {

    public TicTacToeTest(String s) {super(s);}

    public void testDefaultMove() {
        TicTacToe game = new TicTacToe("XOXOX-OXO");
        assertEquals(5, game.move('X'));

        game = new TicTacToe("XOXOXOOX-");
        assertEquals(8, game.move('O'));

        game = new TicTacToe("---------");
        assertEquals(0, game.move('X'));

        game = new TicTacToe("XXXXXXXXX");
        assertEquals(-1, game.move('X'));
    }

    public void testFindWinningMove() {
        TicTacToe game = new TicTacToe("XO-XX-OOX");
        assertEquals(5, game.move('X'));
    }
    public void testWinConditions() {
        TicTacToe game = new TicTacToe("---XXX---");
        assertEquals('X', game.winner());
    }
}

