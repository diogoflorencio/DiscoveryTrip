package com.example.diogo.discoverytrip.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.diogo.discoverytrip.Exceptions.DataInputException;
import com.example.diogo.discoverytrip.R;

public class EventoCadastroFragment extends Fragment implements View.OnClickListener {

    public EventoCadastroFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "EventoCadastroFragment onCreate");
        View rootView = inflater.inflate(R.layout.fragment_evento_cadastro, container, false);

        rootView.findViewById(R.id.evConfirm_btn).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        Log.d("Logger", "EventoCadastroFragment onClick");
        switch (view.getId()) {
            case R.id.pfConfirm_btn:
                Log.d("Logger", "EventoCadastroFragment botao confirmar");
                try {
                    validateFields();
                    backToHome();
                } catch (DataInputException exception){
                    Toast.makeText(this.getActivity(),exception.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void backToHome() {
        Log.d("Logger", "EventoCadastroFragment backToHome");
        FragmentManager fragmentManager = getFragmentManager();
        HomeFragment fragment = new HomeFragment();

        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();
    }

    private void validateFields() throws DataInputException {
        Log.d("Logger", "EventoCadastroFragment validateFields");
    }
}
