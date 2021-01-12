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

public class SignInFragment extends Fragment {
    private OnSignInFragmentListener listener;
    EditText emailEditText,passwordEditText;
    Button buttonSignIn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in,container,false);
        TextView goToRegisterView = view.findViewById(R.id.gotoRegister);
        goToRegisterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFragmentSignInInteraction();
            }
        });
        emailEditText = view.findViewById(R.id.inputEmailSignIn);
        passwordEditText = view.findViewById(R.id.inputPasswordSignIn);
        buttonSignIn = view.findViewById(R.id.btnLogin);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateSignIn()) return;

                listener.onSignInButtonClickListener(
                        emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim()
                );
            }
        });

        return view;
    }

    private boolean validateSignIn(){
        boolean res = true;
        if(emailEditText.getText().toString().trim().equalsIgnoreCase("")){
            emailEditText.setError("This field can not be blank");
            res=false;
        }
        if(passwordEditText.getText().toString().trim().equalsIgnoreCase("")){
            passwordEditText.setError("This field can not be blank");
            res=false;
        }
        return res;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnSignInFragmentListener){
            listener = (OnSignInFragmentListener) context;
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

    public interface OnSignInFragmentListener{
        void onFragmentSignInInteraction();
        void onSignInButtonClickListener(String email,String password);
    }
}
