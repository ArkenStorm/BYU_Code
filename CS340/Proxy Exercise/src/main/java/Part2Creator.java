public class Part2Creator {
    public static void main(String[] args) {
        Part2Implementer array2D = new Part2Implementer(10,10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                array2D.setValue(i, j, i+j);
            }
        }
        array2D.save("out.dat");
    }
}
