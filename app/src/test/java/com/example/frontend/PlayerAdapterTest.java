package com.example.frontend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PlayerAdapterTest {

    private PlayerAdapter playerAdapter;

    @Mock
    private List<Player> mockedPlayerList;

    @BeforeEach
    public void setUp() {
        playerAdapter = new PlayerAdapter(mockedPlayerList);
    }
    @Test
    public void testGetItemCount() {
        when(mockedPlayerList.size()).thenReturn(3); // Assuming there are 3 players

        assertEquals(3, playerAdapter.getItemCount());
    }
}
