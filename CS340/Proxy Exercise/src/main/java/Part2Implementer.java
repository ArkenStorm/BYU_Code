import java.io.*;

public class Part2Implementer implements Part2Array2D {
    private int[][] array;

    public Part2Implementer(int rows, int cols) {
        array = new int[rows][cols];
    }

    public Part2Implementer(String fileName) {
        load(fileName);
    }

    @Override
    public void setValue(int row, int col, int val) {
        array[row][col] = val;
    }

    @Override
    public int getValue(int row, int col) {
        return array[row][col];
    }

    public void save(String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(array);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String fileName) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream iis = new ObjectInputStream(fis);
            array = (int[][]) iis.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
