package com.example.tema3.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangePasswordFragment extends Fragment {
    private EditText emailEt;
    private Button changePasswordButton;
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
        view = inflater.inflate(R.layout.change_password_fragment, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        emailEt = view.findViewById(R.id.email);
        changePasswordButton = view.findViewById(R.id.change_password_button);
        progressDialog = new ProgressDialog(this.getActivity());
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        return view;
    }

    private void changePassword() {
        String email = emailEt.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailEt.setError(Constants.emailErrorMessage);
            return;
        }

        progressDialog.setMessage(Constants.progressDialogMessage);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(this.getActivity(), new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), Constants.succesfulChangePasswordMessage, Toast.LENGTH_SHORT).show();
                    activityFragmentCommunication.openLoginFragment();
                } else {
                    Toast.makeText(getActivity(), Constants.failedChangePasswordMessage, Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();
            }
        });
    }

}