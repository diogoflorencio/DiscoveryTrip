package com.example.diogo.discoverytrip;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PerfilFragment extends Fragment implements View.OnClickListener {
    public EditText nameVal_txt, emailVal_txt;
    private String name;
    private String email;
    private Button confirmarBtn;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "PerfilFragment onCreate");
        View rootView = inflater.inflate(R.layout.fragment_perfil, container, false);

        confirmarBtn = (Button) rootView.findViewById(R.id.pfConfirm_btn);
        confirmarBtn.setOnClickListener(this);

        name = getArguments().getString("googleName");
        email = getArguments().getString("googleEmail");

        return rootView;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pfConfirm_btn:
                backToHome();
                break;
        }
    }

    private void backToHome() {
        FragmentManager fragmentManager = getFragmentManager();
        HomeFragment fragment = new HomeFragment();

        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();
    }
}
