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

import com.creation.adesh.mcassignment3.R;


public class SwitchUserFragment extends DialogFragment {

    private SwitchUserListener mListener;
    private EditText mName = null;
    public SwitchUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Switch user");
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_switch_user, container, false);
        mName = (EditText)v.findViewById(R.id.switchname);
        Button mSwitchUserButton = (Button)v.findViewById(R.id.switchuserbutton);
        mSwitchUserButton.setOnClickListener(new View.OnClickListener(){
                                              public void onClick(View v){
                                                  String name = mName.getText().toString();
                                                  mListener.switchUser(name);
                                                  dismiss();
                                              }
                                          }

        );
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SwitchUserListener) {
            mListener = (SwitchUserListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SwitchUserListener");
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface SwitchUserListener {
        void switchUser(String name);
    }
}
