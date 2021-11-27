package editor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ImageEditor {
    public static void main(String[] args) {
        try (FileReader reader = new FileReader(args[0]); FileWriter writer = new FileWriter(args[1])) {
            Image myImage = PPMParser.parse(reader);
            switch(args[2]) {
                case "invert":
                    ImageProcessor.invert(myImage);
                    break;
                case "grayscale":
                    ImageProcessor.grayscale(myImage);
                    break;
                case "emboss":
                    ImageProcessor.emboss(myImage);
                    break;
                case "motionblur":
                    int blurLength = Integer.parseInt(args[3]);
                    ImageProcessor.motionblur(myImage, blurLength);
                    break;
            }
            writer.append(PPMParser.encodePPM(myImage));
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
        }
    }
}