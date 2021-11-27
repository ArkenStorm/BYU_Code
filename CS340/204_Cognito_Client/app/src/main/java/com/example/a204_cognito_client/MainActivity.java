package com.example.a204_cognito_client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText givenName;
    private EditText email;
    private EditText confirmationCode;

    private Button signUpButton;
    private Button signInButton;
    private Button confirmButton;

    private Context context;

    private String usernameText;
    private String passwordText;
    private String givenNameText;
    private String emailText;
    private String confirmationCodeText;

    private AppHelper appHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        givenName = (EditText) findViewById(R.id.givenNameEditText);
        email = (EditText) findViewById(R.id.emailEditText);
        confirmationCode = (EditText) findViewById(R.id.confirmCodeEditText);

        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        givenName.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);
        confirmationCode.addTextChangedListener(textWatcher);

        signUpButton = (Button) findViewById(R.id.signUpButton);
        signInButton = (Button) findViewById(R.id.signInButton);
        confirmButton = (Button) findViewById(R.id.confirmButton);

        appHelper = AppHelper.getInstance();
        appHelper.init(getApplicationContext());

        context = this;

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Create a CognitoUserAttributes object

                //TODO: Add user attributes to the object created

                //TODO: Call respective method on the user pool (from AppHelper.java)
                // Create a CognitoUserAttributes object and add user attributes
                CognitoUserAttributes userAttributes = new CognitoUserAttributes();

                // Add the user attributes. Attributes are added as key-value pairs
                // Adding user's given name.
                // Note that the key is "given_name" which is the OIDC claim for given name
                userAttributes.addAttribute("given_name", givenNameText);

                // Adding user's email address
                userAttributes.addAttribute("email", emailText);

                AppHelper.getInstance().getUserPool().signUpInBackground(usernameText, passwordText, userAttributes, null, signupCallback);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Call respective method on the user pool (from AppHelper.java)
                AppHelper.getInstance().getUserPool().getUser(usernameText).getSessionInBackground(authenticationHandler);


            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Call respective method on the user pool (from AppHelper.java)
                AppHelper.getInstance().getUserPool().getUser(usernameText).confirmSignUpInBackground(confirmationCodeText, false, confHandler);

            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            usernameText = username.getText().toString();
            passwordText = password.getText().toString();
            givenNameText = givenName.getText().toString();
            emailText = email.getText().toString();
            confirmationCodeText = confirmationCode.getText().toString();
        }
    };

    //TODO: Call this method once the login was successful
    public void startLoggedInActivity() {
        Intent intent = new Intent(this, LoggedInActivity.class);
        startActivity(intent);
    }

    //TODO: Import these handlers from AWS and then allow it to generate the necessary methods
    SignUpHandler signupCallback = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, SignUpResult signUpResult) {
            // Sign-up was successful

            // Check if this user (cognitoUser) has to be confirmed
            if(!signUpResult.isUserConfirmed()) {
                Toast toast = Toast.makeText(getApplicationContext(), "Check your email for confirmation code.", Toast.LENGTH_LONG);
                toast.show();
            }
            else {
                startLoggedInActivity();
            }
        }

        @Override
        public void onFailure(Exception exception) {
            Toast toast = Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    };

    GenericHandler confHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            startLoggedInActivity();
        }

        @Override
        public void onFailure(Exception exception) {
            Toast toast = Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    };

    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
            startLoggedInActivity();
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
            // The API needs user sign-in credentials to continue
            AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, passwordText, null);

            // Pass the user sign-in credentials to the continuation
            authenticationContinuation.setAuthenticationDetails(authenticationDetails);

            // Allow the sign-in to continue
            authenticationContinuation.continueTask();
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
            continuation.continueTask();
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {

        }

        @Override
        public void onFailure(Exception exception) {

        }
    };
}

