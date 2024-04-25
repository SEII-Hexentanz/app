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

    private Button returnButton; // Button zum Zurückkehren
    private TextView rulesText; // TextView für die Spielregeln

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

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        });
    }

    private void setRulesText() {
        rulesText.setText("\"Hexentanz\" ist ein Brettspiel, bei dem die Spieler versuchen, ihre Hexenfiguren einmal um das Spielfeld zu bewegen und sicher in ihr Zuhause zu bringen. Zu Beginn des Spiels werden alle Hexen verdeckt, sodass ihre wahre Identität nur dem jeweiligen Spieler bekannt ist. Spieler bewegen ihre Figuren basierend auf Würfelwürfen, und es gibt spezielle Felder, die Figuren blockieren oder zurückwerfen können. Eine zentrale Herausforderung des Spiels ist es, sich zu merken, welche verdeckte Figur zu welchem Spieler gehört, da Figuren im Verlauf des Spiels oft verwechselt werden. Ziel ist es, als Erster alle eigenen Hexen ins Ziel zu bringen, wobei geschickte Täuschung und gutes Gedächtnis entscheidend sind.");
    }
}
