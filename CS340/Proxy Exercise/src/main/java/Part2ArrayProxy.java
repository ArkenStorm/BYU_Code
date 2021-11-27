public class Part2ArrayProxy implements Part2Array2D {
    public Part2Implementer instance = null;
    private String loadFile;

    public Part2ArrayProxy(String fileName) {
        loadFile = fileName;
    }

    @Override
    public void setValue(int row, int col, int val) {
        if (instance == null) {
            instance = new Part2Implementer(loadFile);
        }
        instance.setValue(row, col, val);
    }

    @Override
    public int getValue(int row, int col) {
        if (instance == null) {
            instance = new Part2Implementer(loadFile);
        }
        return instance.getValue(row, col);
    }
}
