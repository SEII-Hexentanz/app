package com.example.frontend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
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

    @BeforeEach
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
