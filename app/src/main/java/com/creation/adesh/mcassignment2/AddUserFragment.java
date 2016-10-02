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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
            throw new RuntimeException(context.toString()
                    + " must implement AddUser");
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface AddUser {
        // TODO: Update argument type and name
        void addUser(String name);
    }
}
