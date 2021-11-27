package ses;

import com.amazonaws.services.simpleemail.model.*;

import java.util.Collections;

public class EmailRequest {
    public String recipient;
    public String sender;
    public String subject;
    public String textBody;
    public String htmlBody;

    public SendEmailRequest getRequest() {
        SendEmailRequest request = new SendEmailRequest();
        request.setSource(sender);

        Destination destination = new Destination();
        destination.setToAddresses(Collections.singletonList(recipient));
        request.setDestination(destination);

        Message message = new Message();
        Content subjectContent = new Content(subject);
        message.setSubject(subjectContent);

        Body body = new Body();
        Content textContent = new Content(textBody);
        body.setText(textContent);

        Content htmlContent = new Content(htmlBody);
        body.setHtml(htmlContent);
        message.setBody(body);
        request.setMessage(message);

        return request;
    }
}
