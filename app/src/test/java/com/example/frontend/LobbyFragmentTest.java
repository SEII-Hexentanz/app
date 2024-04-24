package com.example.frontend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Objects;


public class LobbyFragmentTest {

    private LobbyFragment lobbyFragment;

    @Mock
    private View mockedView;

    @Mock
    private Button mockedStartGameButton;

    @Mock
    private ImageButton mockedRetCreateLobbyButton;

    @Mock
    private Button mockedRulesButton;

    @Mock
    private RecyclerView mockedRecyclerView;

    @Mock
    private TextView mockedPlayerCountTextView;

    @BeforeEach
    public void setUp() {
        lobbyFragment = new LobbyFragment();
    }

    @Test
    public void testOnCreateView() {
        LayoutInflater mockedInflater = Mockito.mock(LayoutInflater.class);
        ViewGroup mockedContainer = Mockito.mock(ViewGroup.class);
        Bundle mockedSavedInstanceState = Mockito.mock(Bundle.class);
        Mockito.when(mockedInflater.inflate(Mockito.anyInt(), Mockito.any(ViewGroup.class), Mockito.anyBoolean())).thenReturn(mockedView);

        lobbyFragment.onCreateView(mockedInflater, mockedContainer, mockedSavedInstanceState);

        assertEquals(2, lobbyFragment.userList.size()); // Assuming dummy data provides 2 users
        verify(mockedRecyclerView).setAdapter(lobbyFragment.adapter);
        //verify(mockedRecyclerView).setLayoutManager(Mockito.any(LinearLayoutManager.class));
        verify(mockedPlayerCountTextView).setText("2"); // Assuming 2 users
    }

    @Test
    public void testOnReturnBtnClick() {
        lobbyFragment.retCreateLobby = mockedRetCreateLobbyButton;

        // Trigger click
        mockedRetCreateLobbyButton.performClick();

        FragmentManager mockedFragmentManager = Mockito.mock(FragmentManager.class);
        Mockito.when(lobbyFragment.getActivity()).thenReturn(Mockito.mock(MainActivity.class));
        Mockito.when(Objects.requireNonNull(lobbyFragment.getActivity()).getSupportFragmentManager()).thenReturn(mockedFragmentManager);

        // Verify popBackStack is called
        verify(mockedFragmentManager).popBackStack();
    }


}
