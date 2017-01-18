package com.thathustudio.spage.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thathustudio.spage.R;
import com.thathustudio.spage.app.CustomApplication;
import com.thathustudio.spage.exception.SpageException;
import com.thathustudio.spage.model.Exercise;
import com.thathustudio.spage.model.User;
import com.thathustudio.spage.model.responses.EndPointResponse;
import com.thathustudio.spage.model.responses.LoginResponse;
import com.thathustudio.spage.model.responses.Task4ListResponse;
import com.thathustudio.spage.model.responses.Task4Response;
import com.thathustudio.spage.service.retrofit.TranslateRetrofitException;
import com.thathustudio.spage.utils.ShareReferrentHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private List<Call> calls;

    final int REQUEST_CODE = 1;

    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.etPass) EditText etPass;
    @BindView(R.id.btnSignIn) Button btnSignIn;
    @BindView(R.id.txtSignUp) TextView txtSignUp;
    @BindView(R.id.progressbarView) View progressbarView;

    @OnClick(R.id.btnSignIn)
    public void onClickedSignIn(){
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();

        User user = new User(email,pass);

        CustomApplication customApplication = (CustomApplication) getApplication();
        Call<Task4Response<User>> loginResponseCall = customApplication.getTask4Service().postLogin("application/json",user);
        loginResponseCall.enqueue(new LoginCallback(this));
        calls.add(loginResponseCall);
        progressbarView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.txtSignUp)
    public void onClickedSignUp(){
        // change register activity
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        //initData
        calls = new ArrayList<>();



    }

    @Override
    public void onPause() {
        super.onPause();

        for (Call call : calls) {
            call.cancel();
        }
        calls.clear();
    }

    public static class LoginCallback extends LoginActivityCallback<Task4Response<User>> {
        public LoginCallback(LoginActivity loginActivity) {
            super(loginActivity);
        }

        @Override
        public void onResponse(Call<Task4Response<User>> call, Response<Task4Response<User>> response) {
            LoginActivity loginActivity = weakReference.get();
            if (loginActivity != null) {
                SpageException spageException = TranslateRetrofitException.translateServiceException(call, response);

                try {
                    if (spageException != null) {
                        showToast(loginActivity.getApplicationContext(), spageException); // Fix warning
                        return;
                    }

                    //Log.e("Son","login: " +response.body().getResponse().toString());
                    //save data user

                    User user = response.body().getResponse();
                    ShareReferrentHelper.setCurrentUser(loginActivity.getApplicationContext(),user);
                    //change to home activity
                    Intent mainIntent = new Intent(loginActivity.getApplicationContext(), HomeActivity.class);
                    loginActivity.startActivity(mainIntent);
                    loginActivity.finish();

                } catch (Exception ex) {
                    Log.e("SPage", ex.getMessage());
                } finally {
                    loginActivity.calls.remove(call);
                    loginActivity.progressbarView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public static abstract class LoginActivityCallback<T extends EndPointResponse> implements Callback<T> {
        protected final WeakReference<LoginActivity> weakReference;

        public LoginActivityCallback(LoginActivity loginActivity) {
            weakReference = new WeakReference<>(loginActivity);
        }

        protected void showToast(Context context, Throwable throwable) {
            Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            LoginActivity loginActivity = weakReference.get();
            if (loginActivity != null) {
                try {
                    // Handle
                    showToast(loginActivity.getApplicationContext(), t);
                } catch (Exception ex) {
                    Log.e("SPage", ex.getMessage());
                } finally {
                    loginActivity.calls.remove(call);
                    loginActivity.progressbarView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

}
