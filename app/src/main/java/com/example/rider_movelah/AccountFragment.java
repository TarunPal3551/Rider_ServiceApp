package com.example.rider_movelah;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    public AccountFragment() {
        // Required empty public constructor
    }
    ImageView logoutButton;
    TextView textViewEmail, textViewMobileNumber, textViewName;
    Preferences preferences;
    TextView textViewLogout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        preferences = new Preferences(getContext());
        logoutButton = (ImageView) view.findViewById(R.id.logout);
        textViewEmail = (TextView) view.findViewById(R.id.email);
        textViewMobileNumber = (TextView) view.findViewById(R.id.contact);
        textViewName = (TextView) view.findViewById(R.id.username);
        textViewLogout=(TextView)view.findViewById(R.id.edit);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.setUserLoggedIn(false);
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();



            }
        });
        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.setUserLoggedIn(false);
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
        textViewName.setText(new Preferences(getContext()).getPref_username());
        textViewEmail.setText(new Preferences(getContext()).getPref_emailid());
        textViewMobileNumber.setText(new Preferences(getContext()).getPref_phonenum());
        return view;
    }
}
