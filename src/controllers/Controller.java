package controllers;

import listeners.ButtonCancel;
import listeners.ButtonNew;
import listeners.ComboboxChange;
import models.Model;
import views.View;

public class Controller {
    public Controller(Model model, View view) {

        view.getSettings().getCmbCategory().addItemListener(new ComboboxChange(model));

        view.getSettings().getBtnNewGame().addActionListener(new ButtonNew(model, view));

        view.getGameBoard().getBtnCancel().addActionListener(new ButtonCancel(model, view));
    }
}
