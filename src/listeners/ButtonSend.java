package listeners;

import models.Database;
import models.Model;
import models.datastructures.DataScore;
import views.View;
import views.panels.LeaderBoard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ButtonSend implements ActionListener {
    private Model model;
    private View view;
    public ButtonSend(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String userInput = view.getGameBoard().getTxtChar().getText();
        view.getGameBoard().getTxtChar().setText("");
        view.getGameBoard().getTxtChar().grabFocus();
        this.checkUserInput(userInput);
        view.getGameBoard().getLblResult().setText(model.arrayToString(model.getCorrectCharacters()).toUpperCase());
        view.getGameBoard().getLblError().setText("Vigased tähed: " + model.listToString(model.getWrongCharacters()).toUpperCase());
        gameOver();
    }

    private void checkUserInput(String text) {
        if (text == null || text.isEmpty()) {
            view.displayMessage("Palun sisesta täht");
            return;
        }
        String word = model.getWord();
        String guess = text.trim().toLowerCase();
        String[] correctLetters = model.getCorrectCharacters();
        List<String> wrongLetters = model.getWrongCharacters();
        model.getUserGuesses().add(guess);
        int wrongCount = model.getWrongCount();
        char[] wordLetters = word.toCharArray();
        boolean found = false;

        Pattern pattern = Pattern.compile("^[a-zA-ZäöõüÄÖÕÜ]+$");
        Matcher matcher = pattern.matcher(guess);

        if (!matcher.matches()) {
            view.displayMessage("Vale sisestus! Lubatud on sisestada vaid tähti!");
            return;
        }

        for (int i = 0; i < wordLetters.length; i++) {
            if (guess.equals(String.valueOf(wordLetters[i]).toLowerCase())) {
                correctLetters[i] = guess;
                found = true;
            }
        }
        if (!found) {
            wrongCount++;
            model.setWrongCount(wrongCount);
            view.changeImage(wrongCount);
            if (!wrongLetters.contains(guess)) {
                wrongLetters.add(guess);
            }
        }
    }

    private void gameOver() {
        boolean gameOver = false;
        boolean gameWon = false;
        String word = model.getWord().toLowerCase();
        String correctLetters = model.arrayToString(model.getCorrectCharacters()).toLowerCase().replace(" ", "");
        if (model.getWrongCount() == 11) {
            gameOver = true;
        }
        if (word.equals(correctLetters)) {
            gameWon = true;
        }
        if (gameOver) {
            view.showButtons();
            if(view.getGameTimer().isRunning()) {
                view.getGameTimer().stopTime();
                view.getGameTimer().setRunning(false);
                view.gameOver();
            }

        }
        if (gameWon) {
            view.showButtons();
            if(view.getGameTimer().isRunning()) {
                view.getGameTimer().stopTime();
                view.getGameTimer().setRunning(false);
            }
            String name = view.gameWonTab();
            if (name != null && !name.isEmpty()) {
                new Database(model).saveScore(name, view.getGameTimer().getPlayedTimeInSeconds());
                new Database(model).selectScores();
                view.clearScoresTable();
                view.updateScoresTable();
            }
        }
    }
}
