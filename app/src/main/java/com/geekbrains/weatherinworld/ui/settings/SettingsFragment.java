package com.geekbrains.weatherinworld.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.geekbrains.weatherinworld.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class SettingsFragment extends Fragment {
    private static final int RC_SIGN_IN = 40404;
    private static final String TAG = "GoogleAuth";
    private GoogleSignInClient googleSignInClient;
    private com.google.android.gms.common.SignInButton buttonSignIn;
    private Button btnSignOut;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(Objects.requireNonNull(getContext()), gso);
        initViews(view);


    }

    private void initViews(View view) {
        buttonSignIn = view.findViewById(R.id.sign_in_button);
        buttonSignIn.setOnClickListener(v -> signIn());
        btnSignOut = view.findViewById(R.id.sign_out_button);
        btnSignOut.setOnClickListener(v -> signOut());
    }

    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener((Activity) Objects.requireNonNull(getContext()), task -> enableSign());
    }

    private void enableSign(){
        buttonSignIn.setEnabled(true);
        btnSignOut.setEnabled(false);
    }

    private void disableSign(){
        buttonSignIn.setEnabled(false);
        btnSignOut.setEnabled(true);
    }


    @Override
    public void onStart() {
        super.onStart();
        enableSign();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(Objects.requireNonNull(getContext()));
        if (account != null) {
            disableSign();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // Когда сюда возвращается Task, результаты аутентификации уже
            // готовы
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

           disableSign();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

}