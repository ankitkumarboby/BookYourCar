package com.ankit_1107.project.bookyourcar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignUnFragment extends Fragment {
    private OnSignUnFragmentListener listener;
    EditText nameEditText,emailEditText,passwordEditText,confirmPasswordEditText,phoneNumberEditText;
    Button buttonSignUp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up,container,false);
        TextView goToLoginView = view.findViewById(R.id.gotoLogin);
        goToLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFragmentSignUpInteraction();
            }
        });

        nameEditText = view.findViewById(R.id.inputNameSignUp);
        emailEditText = view.findViewById(R.id.inputEmailSignUp);
        passwordEditText = view.findViewById(R.id.inputPasswordSignUp);
        confirmPasswordEditText = view.findViewById(R.id.inputConfirmPasswordSignUp);
        phoneNumberEditText = view.findViewById(R.id.inputPhoneNumberSignUp);
        buttonSignUp = view.findViewById(R.id.btnSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateSignUp()) return;

                listener.onSignUpButtonPressedListener(nameEditText.getText().toString().trim(),
                        emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim(),
                        confirmPasswordEditText.getText().toString().trim(),
                        phoneNumberEditText.getText().toString().trim());
            }
        });

        return view;
    }

    private boolean validateSignUp(){
        boolean res = true;
        if(emailEditText.getText().toString().trim().equalsIgnoreCase("")){
            emailEditText.setError("This field can not be blank");
            res=false;
        }
        if(passwordEditText.getText().toString().trim().equalsIgnoreCase("")){
            passwordEditText.setError("This field can not be blank");
            res=false;
        }
        if(confirmPasswordEditText.getText().toString().trim().equalsIgnoreCase("")){
            confirmPasswordEditText.setError("This field can not be blank");
            res=false;
        }
        if(nameEditText.getText().toString().trim().equalsIgnoreCase("")){
            nameEditText.setError("This field can not be blank");
            res=false;
        }
        if(phoneNumberEditText.getText().toString().trim().equalsIgnoreCase("")){
            phoneNumberEditText.setError("This field can not be blank");
            res=false;
        }
        return res;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnSignUnFragmentListener){
            listener = (OnSignUnFragmentListener) context;
        }
        else{
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    public interface OnSignUnFragmentListener{
        void onFragmentSignUpInteraction();
        void onSignUpButtonPressedListener(String name,String email,String password,String confirmPassword,String phoneNumber);
    }
}