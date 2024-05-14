package com.example.frontend;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RulesFragment extends Fragment {

    private Button returnButton; // returnBtn
    private TextView rulesText; // TextView for rules

    public RulesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rules, container, false);

        findViews(view);
        onReturnButtonClick();
        setRulesText();

        return view;
    }

    private void findViews(View view){
        returnButton = view.findViewById(R.id.returnButton);
        rulesText = view.findViewById(R.id.rulesText);
    }

    private void onReturnButtonClick(){
        returnButton.setOnClickListener(view -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        });
    }

    private void setRulesText() {
        rulesText.setText(R.string.hexentanz_rules);


    }
}
