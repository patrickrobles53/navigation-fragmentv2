package admin4.techelm.com.techelmtechnologies.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import admin4.techelm.com.techelmtechnologies.db.UserDBUtil;
import admin4.techelm.com.techelmtechnologies.json.JSONHelper;
import admin4.techelm.com.techelmtechnologies.model.UserLoginWrapper;
import admin4.techelm.com.techelmtechnologies.webservice.WebServiceRequest;
import admin4.techelm.com.techelmtechnologies.webservice.command.GetCommand;
import admin4.techelm.com.techelmtechnologies.webservice.interfaces.OnServiceListener;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebServiceInfo;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class LoginActivityAuthenticationTask_OLD extends AsyncTask<Void, Void, Boolean> {

    public static final String TAG = LoginActivityAuthenticationTask_OLD.class.getSimpleName();
    public static final String LOGIN_URL =
            "http://enercon714.firstcomdemolinks.com/sampleREST/simple-codeigniter-rest-api-master/index.php/auth/user";

    private List<String> USER_CREDENTIALS = null;
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world", "@dev:password:"
    };

    private final String mEmail;
    private final String mPassword;
    private int passwordFlag = 0;
    private int usernameFlag = 0;
    private int test = 0;

    private CallbackInterface mCallback;
    private LoginActivityAuthenticationTask_OLD mAuthTask = null;
    private Context mContext; // Not really useful in here

    // POST Command for User Login Authentication
    private GetCommand postCommand;

    LoginActivityAuthenticationTask_OLD(String email, String password, Context context) {
        mEmail = email;
        mPassword = password;

        // .. Attach the interface
        mContext = context;
        try {
            mCallback = (CallbackInterface) context;
        } catch (ClassCastException ex) {
            Log.e("TASK", "Must implement the LoginActivityAuthenticationTask in the Activity", ex);
        }
        System.gc();
    }

    public interface CallbackInterface {
        /**
         * Callback invoked when clicked and onProgress
         */
        void onHandleSelection(int position, UserLoginWrapper user, int mode);
        void onHandleShowProgessLogin(boolean taskStatus);
        void onHandleAuthTask(LoginActivityAuthenticationTask_OLD mAuthTask);
        void onHandleEmailError(String emailError);
        void onHandlePasswordError(String passwordError);
        void onHandleSuccessLogin(UserLoginWrapper user);
        void onHandleShowDetails(String details);
    }

    /**
     * Get User Credentials from DB
     *
     * @return List of Credentials
     */
    private List<String> getUserCredentials() {
        UserDBUtil db = new UserDBUtil(mContext);
        db.open();

        List<String> userCredential = db.getUserCredentials();

        db.close();
        return userCredential;
    }

    private void parseJSON(String JSONResult) {
        try {
            JSONObject json = new JSONObject(JSONResult);
            String str = "";

            JSONArray articles = json.getJSONArray("UserCredentials");
            // {"status":200,"message":"Successfully login.","id":"2","password":"password","username":"@dev"}
            // str += "articles length = " + json.getJSONArray("id").length();
            str += "\n--------\n";
            // str += "names: " + articles.getJSONObject(0).names();
            str += "\n--------\n";
            str += "name: " + articles.getJSONObject(0).getString("username");
            str += "\n--------\n";
            str += "password: " + articles.getJSONObject(0).getString("password");
            str += "\n--------\n";
            str += "id: " + articles.getJSONObject(0).getString("id");
            str += "\n--------\n";
            str += "Message: " + articles.getJSONObject(0).getString("message");

            Log.e(TAG, "parseJSON: " + str);
            //etResponse.setText(json.toString(1));
            // mCallback.onHandleShowDetails(str);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            mCallback.onHandleShowDetails(e.toString());
        }
    }

    // TODO: Network API activity
    public void postLogin(String email, String password) {
        /*web info*/
        WebServiceInfo webServiceInfo = new WebServiceInfo();
        // String url = "http://jsonplaceholder.typicode.com/posts";
         String url = "http://enercon714.firstcomdemolinks.com/sampleREST/simple-codeigniter-rest-api-master/index.php/auth/user?user=@dev&password=password";
        //String url = "http://enercon714.firstcomdemolinks.com/sampleREST/simple-codeigniter-rest-api-master/index.php/auth/user";
        webServiceInfo.setUrl(url);

        /*add parameter*/
        //webServiceInfo.addParam("user", email);
        //webServiceInfo.addParam("password", password);
        // webServiceInfo.addParam("userId", "2");

        /*postStartDate command*/
        postCommand = new GetCommand(webServiceInfo);

        //mCallback.onHandleShowDetails("2");
        /*request*/
        WebServiceRequest webServiceRequest = new WebServiceRequest(postCommand);
        webServiceRequest.execute();
        webServiceRequest.setOnServiceListener(new OnServiceListener() {
            @Override
            public void onServiceCallback(WebResponse response) {
                Log.e(TAG, "WebResponse: " + response.getStringResponse());
                // textView23.setText(response.getStringResponse());
                // USER_CREDENTIALS = response.getStringResponse();
                //mCallback.onHandleShowDetails("3");
                parseJSON(response.getStringResponse());
            }
        });
    }

    /*@Override
    protected void onPreExecute() {
        super.onPreExecute();
        //mCallback.onHandleShowDetails("1");
        postLogin(mEmail, mPassword);
    }*/

    @Override
    protected Boolean doInBackground(Void... params) {

        // Set up Login Credentials
        StringBuilder loginUser = new StringBuilder();
        loginUser.append(LOGIN_URL);
        loginUser.append("?user=" + mEmail);
        loginUser.append("&password=" + mPassword);

        new JSONHelper().GET(loginUser.toString());

        try {
            if (USER_CREDENTIALS == null) { /**GET USER DETAILS */
                USER_CREDENTIALS = getUserCredentials();
                Log.v("USER_CREDENTIALS", USER_CREDENTIALS.toString() + " Credentials is NULL");
            } else {
                // Simulate network access.
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /** Check if user login input exists on the list of credentials USER_CREDENTIALS */
        for (String credential : USER_CREDENTIALS) {
            String[] pieces = credential.split(":");
            if (pieces[0].equals(mEmail)) { // Account exists, return true if the password matches.
                usernameFlag = 1;
                if (pieces[1].equals(mPassword)) {
                    passwordFlag = 1;
                    return true;
                } else {
                    passwordFlag = 2;
                    usernameFlag = 2;
                    return false;
                }
            }
        }

        usernameFlag = 1;
        return false;

        // TO DO: register the new account here. But since it was said that it is happening on the back end, ignore this
        // return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        mCallback.onHandleAuthTask(null); // mAuthTask = null;
        mCallback.onHandleShowProgessLogin(false); // showProgress(false);

        if (success) {
            // finish();
            mCallback.onHandleSuccessLogin(null);
            System.out.println("onPostExecute: OK to Login");
        } else {
            if (usernameFlag == 1) {
                mCallback.onHandleEmailError("This email address is invalid");
            } else if (usernameFlag == 2 && passwordFlag == 2) {
                mCallback.onHandlePasswordError("This password is incorrect");
            }
        }

        if (mCallback != null) {
            mCallback.onHandleSelection(1, null, 2);
        }
    }

    @Override
    protected void onCancelled() {
        mCallback.onHandleAuthTask(null);
        mCallback.onHandleShowProgessLogin(false);
    }

}


