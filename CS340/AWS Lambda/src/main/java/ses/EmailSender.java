package ses;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.simpleemail.*;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.regions.Regions;

public class EmailSender {
    public EmailResult handleRequest(EmailRequest request, Context context) {
        EmailResult emailResult = new EmailResult();
        SendEmailResult result;

        LambdaLogger logger = context.getLogger();
        logger.log("Entering send_email");

        try {
            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()

                            // Replace US_WEST_2 with the AWS Region you're using for
                            // Amazon SES.
                            .withRegion(Regions.US_EAST_1).build();

            // TODO:
            // Use the AmazonSimpleEmailService object to send an email message
            // using the values in the EmailRequest parameter object
            result = client.sendEmail(request.getRequest());

            logger.log("Email sent!");
        } catch (Exception ex) {
            logger.log("The email was not sent. Error message: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
        finally {
            logger.log("Leaving send_email");
        }

        // TODO:
        // Return EmailResult
        emailResult.setResult(result);
        return emailResult;
    }

}