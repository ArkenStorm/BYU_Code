package editor;

public class Image {
    private int width;
    private int height;
    private int maxColorVal;
    private Pixel[][] imgData;

    public Image(int width, int height, int maxColorVal) {
        this.width = width;
        this.height = height;
        this.maxColorVal = maxColorVal;
        this.imgData = new Pixel[height][width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxColorVal() {
        return maxColorVal;
    }

    public Pixel getPixel(int row, int column) {
        return imgData[row][column];
    }

    public void setPixel(int row, int column, Pixel pixel) {
        imgData[row][column] = pixel;
    }

    public static class Pixel {
        private int red;
        private int green;
        private int blue;

        public Pixel(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public int getRed() {
            return red;
        }

        public void setRed(int red) {
            this.red = red;
        }

        public int getGreen() {
            return green;
        }

        public void setGreen(int green) {
            this.green = green;
        }

        public int getBlue() {
            return blue;
        }

        public void setBlue(int blue) {
            this.blue = blue;
        }
    }
}
