package com.example.tema3.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tema3.R;
import com.example.tema3.constants.Constants;
import com.example.tema3.interfaces.ActivityFragmentCommunication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
    private EditText emailEt, passwordEt;
    private Button loginButton;
    private TextView signUpTV;
    private TextView changePasswordTV;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private View view;
    private ActivityFragmentCommunication activityFragmentCommunication;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ActivityFragmentCommunication) {
            activityFragmentCommunication = (ActivityFragmentCommunication) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        emailEt = view.findViewById(R.id.email);
        passwordEt = view.findViewById(R.id.password);
        loginButton = view.findViewById(R.id.login_button);
        progressDialog = new ProgressDialog(this.getActivity());
        signUpTV = view.findViewById(R.id.sign_up_tv);
        changePasswordTV = view.findViewById(R.id.forgot_password_tv);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityFragmentCommunication.openSignUpFragment();
            }
        });
        changePasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityFragmentCommunication.openChangePasswordFragment();
            }
        });


        return view;
    }
    private void login(){
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEt.setError(Constants.emailErrorMessage);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEt.setError(Constants.passwordErrorMessage);
            return;
        }
        progressDialog.setMessage(Constants.progressDialogMessage);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), Constants.succesfulLoginMessage, Toast.LENGTH_SHORT).show();
                    activityFragmentCommunication.openDashboardFragment();
                } else {
                    Toast.makeText(getActivity(), Constants.failedLoginMessage, Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();
            }
        });
    }
}
