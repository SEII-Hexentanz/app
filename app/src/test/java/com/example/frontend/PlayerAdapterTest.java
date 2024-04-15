package com.example.frontend;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PlayerAdapterTest {

    private PlayerAdapter playerAdapter;

    @Mock
    private List<Player> mockedPlayerList;

    @Mock
    private PlayerViewHolder mockedViewHolder;

    @Mock
    private ViewGroup mockedParent;

    @Mock
    private LayoutInflater mockedLayoutInflater;

    @Mock
    private View mockedView;

    @Before
    public void setUp() {
        playerAdapter = new PlayerAdapter(mockedPlayerList);
    }

    @Test
    public void testOnCreateViewHolder() {
        when(mockedParent.getContext()).thenReturn(Mockito.mock(MainActivity.class));
        when(mockedLayoutInflater.inflate(anyInt(), eq(mockedParent), anyBoolean())).thenReturn(mockedView);
        when(mockedPlayerList.size()).thenReturn(2); // Assuming there are 2 players

        PlayerViewHolder viewHolder = playerAdapter.onCreateViewHolder(mockedParent, 0);

        assertNotNull(viewHolder);
    }

    @Test
    public void testOnBindViewHolder() {
        Player player1 = new Player();
        player1.setUsername("Maximus");
        player1.setImageResource(R.drawable.bluehat);

        when(mockedPlayerList.get(anyInt())).thenReturn(player1);
        when(mockedViewHolder.userName).thenReturn(Mockito.mock(TextView.class));
        when(mockedViewHolder.userImage).thenReturn(Mockito.mock(ImageView.class));

        playerAdapter.onBindViewHolder(mockedViewHolder, 0);

        verify(mockedViewHolder.userName).setText("Maximus");
        verify(mockedViewHolder.userImage).setImageResource(R.drawable.bluehat);
    }

    @Test
    public void testGetItemCount() {
        when(mockedPlayerList.size()).thenReturn(3); // Assuming there are 3 players

        assertEquals(3, playerAdapter.getItemCount());
    }
}
