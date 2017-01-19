package com.thathustudio.spage.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thathustudio.spage.R;
import com.thathustudio.spage.app.CustomApplication;
import com.thathustudio.spage.exception.SpageException;
import com.thathustudio.spage.model.User;
import com.thathustudio.spage.model.responses.EndPointResponse;
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

public class RegisterActivity extends AppCompatActivity {
    private List<Call> calls;
    User user = null;

    @BindView(R.id.etUserName) EditText etUserName;
    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.etPass) EditText etPass;
    @BindView(R.id.btnSingUp) Button btnSingUp;
    @BindView(R.id.progressbarView) View progressbarView;


    @OnClick(R.id.btnSingUp)
    public void onClickedSignIn(){
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();
        String username = etUserName.getText().toString();
        int role = 1;

        user = new User(-1,username,pass, email, role);

        CustomApplication customApplication = (CustomApplication) getApplication();
        Call<Task4Response<Integer>> registerResponseCall = customApplication.getTask4Service().registerUser("application/json", user);
        registerResponseCall.enqueue(new RegisterCallback(this));
        calls.add(registerResponseCall);
        progressbarView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        //initData
        calls = new ArrayList<>();

        //init Layout
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static class RegisterCallback extends RegisterActivityCallback<Task4Response<Integer>> {
        public RegisterCallback(RegisterActivity registerActivity) {
            super(registerActivity);
        }

        @Override
        public void onResponse(Call<Task4Response<Integer>> call, Response<Task4Response<Integer>> response) {
            RegisterActivity registerActivity = weakReference.get();
            if (registerActivity != null) {
                SpageException spageException = TranslateRetrofitException.translateServiceException(call, response);

                try {
                    if (spageException != null) {
                        showToast(registerActivity.getApplicationContext(), spageException); // Fix warning
                        return;
                    }

                    Log.e("Son","login: " +response.body().getResponse().toString());
                    //save data user

                    registerActivity.user.setId(response.body().getResponse());
                    ShareReferrentHelper.setCurrentUser(registerActivity.getApplicationContext(), registerActivity.user);
                    //change to home activity
                    Intent mainIntent = new Intent(registerActivity.getApplicationContext(), HomeActivity.class);
                    registerActivity.startActivity(mainIntent);
                    registerActivity.finish();

                } catch (Exception ex) {
                    Log.e("SPage", ex.getMessage());
                } finally {
                    registerActivity.calls.remove(call);
                    registerActivity.progressbarView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public static abstract class RegisterActivityCallback<T extends EndPointResponse> implements Callback<T> {
        protected final WeakReference<RegisterActivity> weakReference;

        public RegisterActivityCallback(RegisterActivity registerActivity) {
            weakReference = new WeakReference<>(registerActivity);
        }

        protected void showToast(Context context, Throwable throwable) {
            Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            RegisterActivity registerActivity = weakReference.get();
            if (registerActivity != null) {
                try {
                    // Handle
                    //showToast(registerActivity.getApplicationContext(), t);
                } catch (Exception ex) {
                    Log.e("SPage", ex.getMessage());
                } finally {
                    registerActivity.calls.remove(call);
                    registerActivity.progressbarView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
