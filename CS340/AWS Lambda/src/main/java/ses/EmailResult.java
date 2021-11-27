package ses;

import com.amazonaws.services.simpleemail.model.SendEmailResult;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmailResult {
    String message;
    String timestamp;

    public void setResult(SendEmailResult result) {
        message = result.getMessageId();
        timestamp = new Date().toString();
    }
}
