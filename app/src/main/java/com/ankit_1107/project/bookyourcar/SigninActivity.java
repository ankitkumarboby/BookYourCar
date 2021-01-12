package com.ankit_1107.project.bookyourcar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

public class SigninActivity extends AppCompatActivity
        implements SignInFragment.OnSignInFragmentListener,
        SignUnFragment.OnSignUnFragmentListener {

    SignUnFragment signUnFragment=new SignUnFragment();
    SignInFragment signInFragment=new SignInFragment();

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseAuth.AuthStateListener authStateListener;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if(savedInstanceState == null) {
            fragmentTransaction.replace(R.id.sign_in_container, signInFragment);
            fragmentTransaction.commit();
        }
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    Intent intent = new Intent(SigninActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    public void onFragmentSignInInteraction() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right)
                .add(R.id.sign_in_container, signUnFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSignInButtonClickListener(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            firebaseUser=firebaseAuth.getCurrentUser();
                        }
                        else{
                            Toast.makeText(SigninActivity.this, "Wrong Password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onFragmentSignUpInteraction() {
        fragmentManager.popBackStack();
    }

    @Override
    public void onSignUpButtonPressedListener(final String name, String email, final String password, String confirmPassword, final String phoneNumber) {
        if (!validatePassword(password, confirmPassword)) {
            Toast.makeText(this, "password not matched", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            firebaseUser.updateProfile(changeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        loadUserInDb(password,phoneNumber);
                                    }
                                    else{
                                        Toast.makeText(SigninActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(SigninActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void loadUserInDb(String password,String phoneNumber){
        Person personSignUp = new Person(
                firebaseUser.getUid(),firebaseUser.getDisplayName(),firebaseUser.getEmail(),password,
                phoneNumber,null,Calendar.getInstance().getTimeInMillis(),new ArrayList<String>(),new ArrayList<String>()
        );

        db.collection("users").document(personSignUp.getUserId()).set(personSignUp);

    }

    public boolean validatePassword(@NotNull String password, String confirmPassword) {
        if (password.isEmpty() || confirmPassword.isEmpty()) return false;
        password = password.trim();
        confirmPassword = confirmPassword.trim();
        if (password.isEmpty() || confirmPassword.isEmpty()) return false;
        if (password.equals(confirmPassword)) return true;
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }


}