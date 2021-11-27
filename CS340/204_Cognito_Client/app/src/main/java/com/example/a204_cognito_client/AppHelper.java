package com.example.a204_cognito_client;

import android.content.Context;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

/**
 * This class is to help set attributes for a user pool
 * and create an instance of CognitoUserPool
 */
public class AppHelper {

    private static AppHelper sharedInstance = null;

    private AppHelper() { }

    public static AppHelper getInstance() {
        if (sharedInstance == null)
            sharedInstance = new AppHelper();

        return sharedInstance;
    }

    private CognitoUserPool userPool;

    /**
     * Add your pool id here
     */
    //TODO: Set your pool id here (find this by going under general settings in your user pool)
    private final String userPoolId = "us-east-2_xvRlfSQ3O";

    /**
     * Add your app id here
     */
    //TODO: Set your client id here (find this by going under app clients in your user pool)
    private final String clientId = "4h4pok1jgg5vq73o4go3l71seu";

    /**
     * Add your client secret here
     */
    //TODO: Set your client secret here (find this by going under app clients and INTO show Details in your user pool)
    private final String clientSecret = "1hukl4ipcbqvlbhaculcpr0n2e4pe0vhp4ep7c3cnuphsgi1cd1g";

    /**
     * Set Your User Pools region here
     * e.g. if your user pools are in US East (N Virginia) then set cognitoRegion = Regions.US_EAST_1.
     */
    //TODO: Set your cognito region here by looking at which AWS region your user pool is in and using that
    private final Regions cognitoRegion = Regions.US_EAST_2;


    public void init(Context context) {
        // setup AWS service configuration. Choosing default configuration
        ClientConfiguration clientConfiguration = new ClientConfiguration();

        // Create a CognitoUserPool object to refer to your user pool
        userPool = new CognitoUserPool(context, userPoolId, clientId, clientSecret, clientConfiguration, cognitoRegion);
    }

    public CognitoUserPool getUserPool() {
        return userPool;
    }


}
