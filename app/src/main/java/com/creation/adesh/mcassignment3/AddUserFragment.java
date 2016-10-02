package com.creation.adesh.mcassignment3;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class AddUserFragment extends DialogFragment {

    private AddUser mListener;
    private EditText mName = null;
    public AddUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().setTitle("Add a new user");
        View v =  inflater.inflate(R.layout.fragment_add_user, container, false);
        mName = (EditText)v.findViewById(R.id.name);
        Button mAddUserButton = (Button)v.findViewById(R.id.adduserbutton);
        mAddUserButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    String name = mName.getText().toString();
                    mListener.addUser(name);
                    dismiss();
                }
            }

        );
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddUser) {
            mListener = (AddUser) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement AddUser");
        }
    }

    public interface AddUser {
        // TODO: Update argument type and name
        void addUser(String name);
    }
}
