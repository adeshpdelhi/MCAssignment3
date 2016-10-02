package com.creation.adesh.mcassignment3;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class DeleteUserFragment extends DialogFragment {

    private DeleteUserListener mListener;
    private EditText mName = null;

    public DeleteUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().setTitle("Delete user");
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_delete_user, container, false);
        mName = (EditText)v.findViewById(R.id.deletename);
        Button mSwitchUserButton = (Button)v.findViewById(R.id.deleteuserbutton);
        mSwitchUserButton.setOnClickListener(new View.OnClickListener(){
                                                 public void onClick(View v){
                                                     String name = mName.getText().toString();
                                                     mListener.deleteUser(name);
                                                     dismiss();
                                                 }
                                             }

        );
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DeleteUserListener) {
            mListener = (DeleteUserListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement DeleteUserListener");
        }
    }

    public interface DeleteUserListener {
        // TODO: Update argument type and name
        void deleteUser(String name);
    }
}
