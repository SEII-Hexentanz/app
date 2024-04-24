
package com.example.frontend;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class GameBoardFragment extends Fragment {
    private Button diceBtn;
    private FragmentContainerView fragmentContainerView;
    private TextView usernameTxt;
    private ImageView gameBoard;
    private float mScaleFactor;

    private ImageView btnGreenHome1;
    private ImageView btnGreenHome2;
    private ImageView btnGreenHome3;
    private ImageView btnGreenHome4;

    private ImageView btnRedHome1;
    private ImageView btnRedHome2;
    private ImageView btnRedHome3;
    private ImageView btnRedHome4;

    private ImageView btnBlueHome1;
    private ImageView btnBlueHome2;
    private ImageView btnBlueHome3;
    private ImageView btnBlueHome4;


    private ImageView btnLilaHome1;
    private ImageView btnLilaHome2;
    private ImageView btnLilaHome3;
    private ImageView btnLilaHome4;

    private ImageView btnRosaHome1;
    private ImageView btnRosaHome2;
    private ImageView btnRosaHome3;
    private ImageView btnRosaHome4;

    private ImageView btnYellowHome1;
    private ImageView btnYellowHome2;
    private ImageView btnYellowHome3;
    private ImageView btnYellowHome4;

    private ImageView btnGreenGoal1;
    private ImageView btnGreenGoal2;
    private ImageView btnGreenGoal3;
    private ImageView btnGreenGoal4;

    private ImageView btnRedGoal1;
    private ImageView btnRedGoal2;
    private ImageView btnRedGoal3;
    private ImageView btnRedGoal4;

    private ImageView btnBlueGoal1;
    private ImageView btnBlueGoal2;
    private ImageView btnBlueGoal3;
    private ImageView btnBlueGoal4;


    private ImageView btnLilaGoal1;
    private ImageView btnLilaGoal2;
    private ImageView btnLilaGoal3;
    private ImageView btnLilaGoal4;

    private ImageView btnRosaGoal1;
    private ImageView btnRosaGoal2;
    private ImageView btnRosaGoal3;
    private ImageView btnRosaGoal4;

    private ImageView btnYellowGoal1;
    private ImageView btnYellowGoal2;
    private ImageView btnYellowGoal3;
    private ImageView btnYellowGoal4;
    private ImageView boardField0;
    private ImageView boardField1;
    private ImageView boardField2;
    private ImageView boardField3;
    private ImageView boardField4;
    private ImageView boardField5;
    private ImageView boardField6;
    private ImageView boardField7;
    private ImageView boardField8;
    private ImageView boardField9;
    private ImageView boardField10;
    private ImageView boardField11;
    private ImageView boardField12;
    private ImageView boardField13;
    private ImageView boardField14;
    private ImageView boardField15;
    private ImageView boardField16;
    private ImageView boardField17;
    private ImageView boardField18;
    private ImageView boardField19;
    private ImageView boardField20;
    private ImageView boardField21;
    private ImageView boardField22;
    private ImageView boardField23;
    private ImageView boardField24;
    private ImageView boardField25;
    private ImageView boardField26;
    private ImageView boardField27;
    private ImageView boardField28;
    private ImageView boardField29;
    private ImageView boardField30;
    private ImageView boardField31;
    private ImageView boardField32;
    private ImageView boardField33;
    private ImageView boardField34;
    private ScaleGestureDetector scaleGestureDetector;

    public GameBoardFragment() {
        //leerer Konstruktor notwendig
    }


    public static GameBoardFragment newInstance(String username) {
        GameBoardFragment fragment = new GameBoardFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Display Gameboard only in Landscape Mode
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set screen orientation to landscape when GameBoardFragment is resumed
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Reset screen orientation to portrait when GameBoardFragment is paused
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_board, container, false);
        List<ImageView> imageViews = new ArrayList<>();
        findViews(view);
        setGameBoardUsername();
        onRollDiceClick();
        initializeGameBoard();
        scaleGestureDetector = new ScaleGestureDetector(requireContext(), new ScaleListener());
        initalizePlayerHomePositions(Game.INSTANCE.players());
       // displayImageViews(imageViews);


        return view;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            gameBoard.setScaleX(mScaleFactor);
            gameBoard.setScaleY(mScaleFactor);
            return true;
        }
    }


    private void setGameBoardUsername() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("username");
            usernameTxt.setText(name);
        }
    }


    private void onRollDiceClick() {
        diceBtn.setOnClickListener(view -> {
            fragmentContainerView.setVisibility(View.VISIBLE);
            showDiceFragment();
        });
    }

    private void findViews(View view) {
        diceBtn = view.findViewById(R.id.btn_rollDice);
        fragmentContainerView = view.findViewById(R.id.fragmentContainerDice);
        usernameTxt = view.findViewById(R.id.txtViewUsername);
        gameBoard = view.findViewById(R.id.gridLayoutGameBoard);

        btnGreenHome1 = view.findViewById(R.id.btnHomeGreen1);
        btnGreenHome2 = view.findViewById(R.id.btnHomeGreen2);
        btnGreenHome3 = view.findViewById(R.id.btnHomeGreen3);
        btnGreenHome4 = view.findViewById(R.id.btnHomeGreen4);

        btnRedHome1 = view.findViewById(R.id.btnHomeRed1);
        btnRedHome2 = view.findViewById(R.id.btnHomeRed2);
        btnRedHome3 = view.findViewById(R.id.btnHomeRed3);
        btnRedHome4 = view.findViewById(R.id.btnHomeRed4);

        btnBlueHome1 = view.findViewById(R.id.btnHomeBlue1);
        btnBlueHome2 = view.findViewById(R.id.btnHomeBlue2);
        btnBlueHome3 = view.findViewById(R.id.btnHomeBlue3);
        btnBlueHome4 = view.findViewById(R.id.btnHomeBlue4);

        btnLilaHome1 = view.findViewById(R.id.btnHomeLila1);
        btnLilaHome2 = view.findViewById(R.id.btnHomeLila2);
        btnLilaHome3 = view.findViewById(R.id.btnHomeLila3);
        btnLilaHome4 = view.findViewById(R.id.btnHomeLila4);

        btnRosaHome1 = view.findViewById(R.id.btnHomeRosa1);
        btnRosaHome2 = view.findViewById(R.id.btnHomeRosa2);
        btnRosaHome3 = view.findViewById(R.id.btnHomeRosa3);
        btnRosaHome4 = view.findViewById(R.id.btnHomeRosa4);

        btnYellowHome1 = view.findViewById(R.id.btnHomeYellow1);
        btnYellowHome2 = view.findViewById(R.id.btnHomeYellow2);
        btnYellowHome3 = view.findViewById(R.id.btnHomeYellow3);
        btnYellowHome4 = view.findViewById(R.id.btnHomeYellow4);



        btnGreenGoal1 = view.findViewById(R.id.btnGoalGreen1);
        btnGreenGoal2 = view.findViewById(R.id.btnGoalGreen2);
        btnGreenGoal3 = view.findViewById(R.id.btnGoalGreen3);
        btnGreenGoal4 = view.findViewById(R.id.btnGoalGreen4);

        btnRedGoal1 = view.findViewById(R.id.btnGoalRed1);
        btnRedGoal2 = view.findViewById(R.id.btnGoalRed2);
        btnRedGoal3 = view.findViewById(R.id.btnGoalRed3);
        btnRedGoal4 = view.findViewById(R.id.btnGoalRed4);

        btnBlueGoal1 = view.findViewById(R.id.btnGoalBlue1);
        btnBlueGoal2 = view.findViewById(R.id.btnGoalBlue2);
        btnBlueGoal3 = view.findViewById(R.id.btnGoalBlue3);
        btnBlueGoal4 = view.findViewById(R.id.btnGoalBlue4);

        btnLilaGoal1 = view.findViewById(R.id.btnGoalLila1);
        btnLilaGoal2 = view.findViewById(R.id.btnGoalLila2);
        btnLilaGoal3 = view.findViewById(R.id.btnGoalLila3);
        btnLilaGoal4 = view.findViewById(R.id.btnGoalLila4);

        btnRosaGoal1 = view.findViewById(R.id.btnGoalRosa1);
        btnRosaGoal2 = view.findViewById(R.id.btnGoalRosa2);
        btnRosaGoal3 = view.findViewById(R.id.btnGoalRosa3);
        btnRosaGoal4 = view.findViewById(R.id.btnGoalRosa4);

        btnYellowGoal1 = view.findViewById(R.id.btnGoalYellow1);
        btnYellowGoal2 = view.findViewById(R.id.btnGoalYellow2);
        btnYellowGoal3 = view.findViewById(R.id.btnGoalYellow3);
        btnYellowGoal4 = view.findViewById(R.id.btnGoalYellow4);

        boardField0= view.findViewById(R.id.gameboardpos0);
        boardField1= view.findViewById(R.id.gameboardpos1);
        boardField2= view.findViewById(R.id.gameboardpos2);
        boardField3= view.findViewById(R.id.gameboardpos3);
        boardField4= view.findViewById(R.id.gameboardpos4);
        boardField5= view.findViewById(R.id.gameboardpos5);
        boardField6= view.findViewById(R.id.gameboardpos6);
        boardField7= view.findViewById(R.id.gameboardpos7);
        boardField8= view.findViewById(R.id.gameboardpos8);
        boardField9= view.findViewById(R.id.gameboardpos9);
        boardField10= view.findViewById(R.id.gameboardpos10);
        boardField11= view.findViewById(R.id.gameboardpos11);
        boardField12= view.findViewById(R.id.gameboardpos12);
        boardField13= view.findViewById(R.id.gameboardpos13);
        boardField14= view.findViewById(R.id.gameboardpos14);
        boardField15= view.findViewById(R.id.gameboardpos15);
        boardField16= view.findViewById(R.id.gameboardpos16);
        boardField17= view.findViewById(R.id.gameboardpos17);
        boardField18= view.findViewById(R.id.gameboardpos18);
        boardField19= view.findViewById(R.id.gameboardpos19);
        boardField20= view.findViewById(R.id.gameboardpos20);
        boardField21= view.findViewById(R.id.gameboardpos21);
        boardField22= view.findViewById(R.id.gameboardpos22);
        boardField23= view.findViewById(R.id.gameboardpos23);
        boardField24= view.findViewById(R.id.gameboardpos24);
        boardField25= view.findViewById(R.id.gameboardpos25);
        boardField26= view.findViewById(R.id.gameboardpos26);
        boardField27= view.findViewById(R.id.gameboardpos27);
        boardField28= view.findViewById(R.id.gameboardpos28);
        boardField29= view.findViewById(R.id.gameboardpos29);
        boardField30= view.findViewById(R.id.gameboardpos30);
        boardField31= view.findViewById(R.id.gameboardpos31);
        boardField32= view.findViewById(R.id.gameboardpos32);
        boardField33= view.findViewById(R.id.gameboardpos33);
        boardField34= view.findViewById(R.id.gameboardpos34);
    }

    private void initalizePlayerHomePositions(SortedSet<at.aau.models.Player> players) {
        for (at.aau.models.Player player : players) {

            switch (player.color()) {
                case YELLOW -> {
                    btnYellowHome1.setImageResource(R.drawable.playericon);
                    btnYellowHome2.setImageResource(R.drawable.playericon);
                    btnYellowHome3.setImageResource(R.drawable.playericon);
                    btnYellowHome4.setImageResource(R.drawable.playericon);
                }

                case PINK -> {
                    btnRosaHome1.setImageResource(R.drawable.playericon);
                    btnRosaHome2.setImageResource(R.drawable.playericon);
                    btnRosaHome3.setImageResource(R.drawable.playericon);
                    btnRosaHome4.setImageResource(R.drawable.playericon);
                }
                case RED -> {
                    btnRosaHome1.setImageResource(R.drawable.playericon);
                    btnRosaHome2.setImageResource(R.drawable.playericon);
                    btnRosaHome3.setImageResource(R.drawable.playericon);
                    btnRosaHome4.setImageResource(R.drawable.playericon);
                }

                case GREEN -> {
                    btnGreenHome1.setImageResource(R.drawable.playericon);
                    btnGreenHome2.setImageResource(R.drawable.playericon);
                    btnGreenHome3.setImageResource(R.drawable.playericon);
                    btnGreenHome4.setImageResource(R.drawable.playericon);
                }
                case LIGHT_BLUE -> {
                    btnBlueHome1.setImageResource(R.drawable.playericon);
                    btnBlueHome2.setImageResource(R.drawable.playericon);
                    btnBlueHome3.setImageResource(R.drawable.playericon);
                    btnBlueHome4.setImageResource(R.drawable.playericon);
                }

                case DARK_BLUE -> {
                    btnLilaHome1.setImageResource(R.drawable.playericon);
                    btnLilaHome2.setImageResource(R.drawable.playericon);
                    btnLilaHome3.setImageResource(R.drawable.playericon);
                    btnLilaHome4.setImageResource(R.drawable.playericon);
                }
            }
        }
    }
    private void initializeGameBoard(){
        ArrayList<ImageView> listView = new ArrayList<>();
        boardField0.setImageResource(R.drawable.playericon);
        boardField1.setImageResource(R.drawable.playericon);
        boardField2.setImageResource(R.drawable.playericon);
        boardField3.setImageResource(R.drawable.playericon);
        boardField4.setImageResource(R.drawable.playericon);
        boardField5.setImageResource(R.drawable.playericon);
        boardField6.setImageResource(R.drawable.playericon);
        boardField7.setImageResource(R.drawable.playericon);
        boardField8.setImageResource(R.drawable.playericon);
        boardField9.setImageResource(R.drawable.playericon);
        boardField10.setImageResource(R.drawable.playericon);
        boardField11.setImageResource(R.drawable.playericon);
        boardField12.setImageResource(R.drawable.playericon);
        boardField13.setImageResource(R.drawable.playericon);
        boardField14.setImageResource(R.drawable.playericon);
        boardField15.setImageResource(R.drawable.playericon);
        boardField16.setImageResource(R.drawable.playericon);
        boardField17.setImageResource(R.drawable.playericon);
        boardField18.setImageResource(R.drawable.playericon);
        boardField19.setImageResource(R.drawable.playericon);
        boardField20.setImageResource(R.drawable.playericon);
        boardField21.setImageResource(R.drawable.playericon);
        boardField22.setImageResource(R.drawable.playericon);
        boardField23.setImageResource(R.drawable.playericon);
        boardField24.setImageResource(R.drawable.playericon);
        boardField25.setImageResource(R.drawable.playericon);
        boardField26.setImageResource(R.drawable.playericon);
        boardField27.setImageResource(R.drawable.playericon);
        boardField28.setImageResource(R.drawable.playericon);
        boardField29.setImageResource(R.drawable.playericon);
        boardField30.setImageResource(R.drawable.playericon);
        boardField31.setImageResource(R.drawable.playericon);
        boardField32.setImageResource(R.drawable.playericon);
        boardField33.setImageResource(R.drawable.playericon);
        boardField34.setImageResource(R.drawable.playericon);

        listView.add(boardField0);
        listView.add(boardField1);
        listView.add(boardField2);
        listView.add(boardField3);
        listView.add(boardField4);
        listView.add(boardField5);
        listView.add(boardField6);
        listView.add(boardField7);
        listView.add(boardField8);
        listView.add(boardField9);
        listView.add(boardField10);
        listView.add(boardField11);
        listView.add(boardField12);
        listView.add(boardField13);
        listView.add(boardField14);
        listView.add(boardField15);
        listView.add(boardField16);
        listView.add(boardField17);
        listView.add(boardField18);
        listView.add(boardField19);
        listView.add(boardField20);
        listView.add(boardField21);
        listView.add(boardField22);
        listView.add(boardField23);
        listView.add(boardField24);
        listView.add(boardField25);
        listView.add(boardField26);
        listView.add(boardField27);
        listView.add(boardField28);
        listView.add(boardField29);
        listView.add(boardField30);
        listView.add(boardField31);
        listView.add(boardField32);
        listView.add(boardField33);
        listView.add(boardField34);

    }



    private void showDiceFragment() {
        DiceFragment diceFragment = DiceFragment.newInstance();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.gridLayoutGameBoard, diceFragment);
        fragmentTransaction.commit();
    }
private List<ImageView> findImageViewByID(int count) {
    List<ImageView> imageViews = new ArrayList<>();
    Resources res = getResources();
    String packageName = requireContext().getPackageName();
    for (int i = 1; i <= count; i++) {
        int id = res.getIdentifier("gameboard" + i, "id", packageName);
        ImageView imageView = requireView().findViewById(id);
        imageViews.add(imageView);
    }
    return imageViews;
}

    private void displayImageViews(List<ImageView> imageViews) {
        for (ImageView imageView : imageViews) {
            // For example, you can set a drawable resource as the image
            imageView.setImageResource(R.drawable.herb);

        }
    }

/*
//necessary in Sprint 2
//EPIC method that will be used in END Game
    public void showWinner() {
        String winnerName="Max Mustermann";
        EngGameFragment endFragment = EngGameFragment.newInstance(winnerName);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView2, endFragment);
        fragmentTransaction.commit();
    }
*/


    }
