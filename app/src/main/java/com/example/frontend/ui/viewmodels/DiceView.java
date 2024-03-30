package com.example.frontend.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.frontend.gamelogic.Dice;

public class DiceView extends ViewModel {

    private final MutableLiveData<Dice> dice = new MutableLiveData<>();
    private final MutableLiveData<Boolean> continuePressed = new MutableLiveData<>();
    public void setDices(Dice dice) {
        this.dice.setValue(dice);
    }
    public void setContinuePressed(Boolean continuePresses) {
        this.continuePressed.setValue(continuePresses);
    }
    public LiveData<Dice> getDicesData() {
        return this.dice;
    }
    public LiveData<Boolean> getContinuePressedData() {
        return this.continuePressed;
    }
}
