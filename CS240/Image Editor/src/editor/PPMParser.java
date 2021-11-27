package editor;

import java.io.FileReader;
import java.util.Scanner;

public class PPMParser {
    public static Image parse(FileReader reader) {
        Scanner parser = new Scanner(reader).useDelimiter("((#[^\\n]*\\n)|(\\s+))+");
        parser.next("P3");
        int width = parser.nextInt();
        int height = parser.nextInt();
        int maxColorVal = parser.nextInt();
        Image myImage = new Image(width, height, maxColorVal);
        for (int row = 0; row < height; ++row) {
            for (int column = 0; column < width; ++column) {
                int R = parser.nextInt();
                int G = parser.nextInt();
                int B = parser.nextInt();
                myImage.setPixel(row, column, new Image.Pixel(R, G, B));
            }
        }
        return myImage;
    }

    public static StringBuilder encodePPM(Image myImage) {
        StringBuilder builder = new StringBuilder("P3\n");
        builder.append(myImage.getWidth() + " " + myImage.getHeight() + "\n");
        builder.append(myImage.getMaxColorVal() + "\n");
        for (int row = 0; row < myImage.getHeight(); ++row) {
            for (int column = 0; column < myImage.getWidth(); ++column) {
                int R = myImage.getPixel(row, column).getRed();
                int G = myImage.getPixel(row, column).getGreen();
                int B = myImage.getPixel(row, column).getBlue();

                builder.append(R + " " + G + " " + B + "\t");
            }
        }
        return builder;
    }
}
