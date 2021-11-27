package editor;

public class ImageProcessor {
    public static void invert(Image myImage) {
        int maxColorVal = myImage.getMaxColorVal();
        for (int row = 0; row < myImage.getHeight(); ++row) {
            for (int column = 0; column < myImage.getWidth(); ++ column) {
                Image.Pixel invert = myImage.getPixel(row, column);
                invert.setRed(maxColorVal - invert.getRed());
                invert.setGreen(maxColorVal - invert.getGreen());
                invert.setBlue(maxColorVal - invert.getBlue());
            }
        }
    }

    public static void grayscale(Image myImage) {
        for (int row = 0; row < myImage.getHeight(); ++row) {
            for (int column = 0; column < myImage.getWidth(); ++ column) {
                Image.Pixel grayscale = myImage.getPixel(row, column);
                int average = (grayscale.getRed() + grayscale.getGreen() + grayscale.getBlue()) / 3;
                grayscale.setRed(average);
                grayscale.setBlue(average);
                grayscale.setGreen(average);
            }
        }
    }

    public static void emboss(Image myImage) {
        for (int row = myImage.getHeight() - 1; row >= 0; --row) {
            for (int column = myImage.getWidth() - 1; column >= 0; --column) {
                if (row == 0 || column == 0) {
                    myImage.setPixel(row, column, new Image.Pixel(128, 128, 128));
                }
                else {
                    Image.Pixel pixel = myImage.getPixel(row, column);
                    Image.Pixel upperLeftPixel = myImage.getPixel(row - 1, column - 1);
                    int maxDiff = 0;
                    int redDiff = pixel.getRed() - upperLeftPixel.getRed();
                    int greenDiff = pixel.getGreen() - upperLeftPixel.getGreen();
                    int blueDiff = pixel.getBlue() - upperLeftPixel.getBlue();
                    if (Math.abs(redDiff) > Math.abs(maxDiff)) {
                        maxDiff = redDiff;
                    }
                    if (Math.abs(greenDiff) > Math.abs(maxDiff)) {
                        maxDiff = greenDiff;
                    }
                    if (Math.abs(blueDiff) > Math.abs(maxDiff)) {
                        maxDiff = blueDiff;
                    }
                    int newVal = 128 + maxDiff;
                    if (newVal < 0) {
                        newVal = 0;
                    }
                    else if (newVal > 255) {
                        newVal = 255;
                    }
                    pixel.setRed(newVal);
                    pixel.setGreen(newVal);
                    pixel.setBlue(newVal);
                }
            }
        }
    }

    public static void motionblur(Image myImage, int blurLength) {
        for (int row = 0; row < myImage.getHeight(); ++row) {
            for (int column = 0; column < myImage.getWidth(); ++ column) {
                int redSum = 0;
                int greenSum = 0;
                int blueSum = 0;
                int valueCount = 0;
                for (int i = 0; i < blurLength; ++i) {
                    if (column + i >= myImage.getWidth()) {
                        break;
                    }
                    ++valueCount;
                    redSum += myImage.getPixel(row, column + i).getRed();
                    greenSum += myImage.getPixel(row, column + i).getGreen();
                    blueSum += myImage.getPixel(row, column + i).getBlue();
                }
                Image.Pixel blur = new Image.Pixel(redSum / valueCount, greenSum / valueCount, blueSum / valueCount);
                myImage.setPixel(row, column, blur);
            }
        }
    }
}
