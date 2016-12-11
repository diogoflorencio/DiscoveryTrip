package com.example.diogo.discoverytrip;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PerfilFragment extends Fragment {
    public TextView nameVal_txt;
    public TextView emailVal_txt;
    private String name;
    private String email;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_perfil, container, false);

        name = getArguments().getString("googleName");
        email = getArguments().getString("googleEmail");

        nameVal_txt = (TextView) rootView.findViewById(R.id.nameVal_txt);
        emailVal_txt = (TextView) rootView.findViewById(R.id.emailVal_txt);

        nameVal_txt.setText("" + name);
        emailVal_txt.setText("" + email);

        return rootView;
    }
}
