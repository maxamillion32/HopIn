/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import sk.maverick.harsha.hopin.Http.HttpManager;
import sk.maverick.harsha.hopin.Http.RequestParams;
import sk.maverick.harsha.hopin.Util.RegexValidator;

public class SignUp extends AppCompatActivity {

    protected final static String TAG = "LoginActivity";
    private EditText username, phone, password, repassword;
    private Button register;
    private TextView signup,oldUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
    }

    private void init(){
        username    = (EditText) findViewById(R.id.sign_up_username_et);
        phone       = (EditText) findViewById(R.id.sign_up_phone_et);
        password    = (EditText) findViewById(R.id.sign_up_password_et);
        repassword  = (EditText) findViewById(R.id.sign_up_repassword_et);
        oldUser     = (TextView) findViewById(R.id.sign_up_old_user_tv);
        signup      = (TextView) findViewById(R.id.sign_up_text_view);
        register    = (Button) findViewById(R.id.sign_up_register_btn);

        oldUser.setPaintFlags(oldUser.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Linkify.addLinks(oldUser, Linkify.ALL);

        Typeface roboto_light = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        username.setTypeface(roboto_light);
        phone.setTypeface(roboto_light);
        password.setTypeface(roboto_light);
        repassword.setTypeface(roboto_light);

        Typeface roboto_thin = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");
        signup.setTypeface(roboto_thin);
        oldUser.setTypeface(roboto_thin);

    }

    public void backToLogin(View view){
        finish();
    }

    public void register(View view){

        String username_text, phone_text, pass_text, repass_text;

        username_text   = username.getText().toString();
        phone_text      = phone.getText().toString();
        pass_text       = password.getText().toString();
        repass_text     = repassword.getText().toString();

        boolean validUsername = RegexValidator.validateName(username_text);
        boolean validPhone = RegexValidator.validatePhoneNumber(phone_text);
        boolean validPassword = RegexValidator.validatePassword(pass_text);
        boolean passwordsMatch = pass_text.equals(repass_text);


        if(!validUsername){
            username.setError("Invalid UserName");
        }

        if(!validPhone){
            phone.setError("Invalid Phone number");
        }

        if(!validPassword){
            password.setError("Invalid");
        }
        if(!passwordsMatch){
            repassword.setError("Doesn't Match");
        }


        if(validUsername && validPassword && validPhone && passwordsMatch){
            // TODO
            //call async task to register the user
            RequestParams request = new RequestParams();
            request.setUri("http://localhost:3000/register");
            request.setParam("username", username_text);
            request.setParam("password", pass_text);
            request.setParam("phonenumber", phone_text);
            Log.v(TAG, "Request being sent with params " + request.getUri() + request.getParams().toString());
            new AsyncSignUp().execute(request);
        }

    }

    private class AsyncSignUp extends AsyncTask<RequestParams, Void, String>{

        protected String doInBackground(RequestParams... params) {

            String response = null;
            try {
                 response = HttpManager.postData(params[0]);

            } catch (IOException e) {
                e.printStackTrace();
            }


            Log.v(TAG, "Do background, async call goes to the server");
            return response;
        }

        protected void onPostExecute(String result) {

            Log.v(TAG, "Result is "+ result);
        }
    }

}
