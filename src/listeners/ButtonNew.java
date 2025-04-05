package listeners;

import models.Database;
import models.Model;
import views.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class ButtonNew implements ActionListener {
    private Model model;
    private View view;
    public ButtonNew(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        view.hideButtons();
        if(!view.getGameTimer().isRunning()) {
            model.resetGameStats();
            view.getGameBoard().getLblError().setText("Vigased tähed:" + model.listToString(model.getWrongCharacters()).toUpperCase());
            view.getGameTimer().setSeconds(0);
            view.getGameTimer().setMinutes(0);
            view.getGameTimer().setRunning(true);
            view.getGameTimer().startTime();
            new Database(model).selectRandomWord();
            String guessWord = model.arrayToString(model.getCorrectCharacters());
            view.changeImage(0);
            view.getGameBoard().getLblResult().setText(guessWord);
        } else {
            view.getGameTimer().stopTime();
            view.getGameTimer().setRunning(false);
        }
    }
}
