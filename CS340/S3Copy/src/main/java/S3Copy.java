import com.amazonaws.services.elasticbeanstalk.model.SystemStatus;
import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.File;


public class S3Copy {

    public static void main(String[] args) {
        try {
            // Create AmazonS3 object for doing S3 operations
            AmazonS3 s3 = AmazonS3ClientBuilder
                    .standard()
                    .withRegion("us-east-2")
                    .build();

            // Write code to do the following:
            // 1. get name of file to be copied from the command line
            // 2. get name of S3 bucket from the command line
            // 3. upload file to the specified S3 bucket using the file name as the S3 key
            String bucketName = args[0];
            File file = new File(args[1]);
            s3.putObject(bucketName, file.getName(), file);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Usage: bucketname file");
        }
    }
}