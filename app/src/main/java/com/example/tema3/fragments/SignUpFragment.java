package com.example.tema3.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

public class SignUpFragment extends Fragment {
    private EditText emailEt, passwordEt, confirmPasswordEt;
    private Button signUpButton;
    private TextView signInTV;
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
        view = inflater.inflate(R.layout.sign_up_fragment, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        emailEt = view.findViewById(R.id.email);
        passwordEt = view.findViewById(R.id.password);
        confirmPasswordEt = view.findViewById(R.id.confirm_password);
        signUpButton = view.findViewById(R.id.sign_up_button);
        progressDialog = new ProgressDialog(this.getActivity());
        signInTV = view.findViewById(R.id.sign_in_tv);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        signInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityFragmentCommunication.openSignUpFragment();
            }
        });

        return view;
    }

    private void register() {
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        String confirmPassword = confirmPasswordEt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEt.setError(Constants.emailErrorMessage);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEt.setError(Constants.passwordErrorMessage);
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            passwordEt.setError(Constants.confirmPasswordErrorMessage);
            return;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordEt.setError(Constants.passwordsNotMatchingErrorMessage);
        }
        if (!isValidEmail(email)) {
            emailEt.setError(Constants.invalidEmailErrorMessage);
        }
        if (password.length() < 4) {
            passwordEt.setError(Constants.passwordLengthErrorMessage);
        }
        progressDialog.setMessage(Constants.progressDialogMessage);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), Constants.succesfulRegisterMessage, Toast.LENGTH_SHORT).show();
                    activityFragmentCommunication.openDashboardFragment();
                } else {
                    Toast.makeText(getActivity(), Constants.failedRegisterMessage, Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();
            }
        });
    }

    private Boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
