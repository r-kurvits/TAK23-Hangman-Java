package controllers;

import listeners.ButtonCancel;
import listeners.ButtonNew;
import listeners.ButtonSend;
import listeners.ComboboxChange;
import models.Model;
import views.View;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller {
    public Controller(Model model, View view) {

        view.getSettings().getCmbCategory().addItemListener(new ComboboxChange(model));

        view.getSettings().getBtnNewGame().addActionListener(new ButtonNew(model, view));

        view.getGameBoard().getBtnCancel().addActionListener(new ButtonCancel(model, view));

        view.getGameBoard().getBtnSend().addActionListener(new ButtonSend(model, view));

        view.getGameBoard().getTxtChar().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    new ButtonSend(model, view).actionPerformed(null); // Simulate button click
                }
            }
        });
    }
}
