package models;

import helpers.RealTimer;
import models.datastructures.DataScore;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * See klass tegeleb andmebaasi ühenduse ja "igasuguste" päringutega tabelitest.
 * Alguses on ainult ühenduse jaoks funktsionaalsus
 */
public class Database {
    /**
     * Algselt ühendust pole
     */
    private Connection connection = null;
    /**
     * Andmebaasi ühenduse string
     */
    private String databaseUrl;
    /**
     * Loodud mudel
     */
    private Model model;

    /**
     * Klassi andmebaas konstruktor
     * @param model loodud mudel
     */
    public Database(Model model) {
        this.model = model;
        this.databaseUrl = "jdbc:sqlite:" + model.getDatabaseFile();
        this.selectUniqueCategories();
    }

    /**
     * Loob andmebaasiga ühenduse
     * @return andmebaasi ühenduse
     */
    private Connection dbConnection() throws SQLException {
        // https://stackoverflow.com/questions/13891006/
        if(connection != null) {
            connection.close();
        }
        connection = DriverManager.getConnection(databaseUrl);
        return connection;
    }

    private void selectUniqueCategories() {
        String sql = "SELECT DISTINCT(category) as category FROM words ORDER BY category;";
        List<String> categories = new ArrayList<>();

        try {
            Connection connection = this.dbConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String category = rs.getString("category");
                categories.add(category);
            }
            categories.add(0, model.getChooseCategory());
            String[] result = categories.toArray(new String[0]);
            model.setCmbCategories(result);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectScores() {
        String sql = "SELECT * FROM scores ORDER BY gametime, playertime DESC, playername;";
        List<DataScore> data = new ArrayList<>();
        try {
            Connection connection = this.dbConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            model.getDataScores().clear();
            while(rs.next()) {
                String datetime = rs.getString("playertime");
                LocalDateTime playerTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String playerName = rs.getString("playername");
                String guessWord = rs.getString("guessword");
                String wrongChar = rs.getString("wrongcharacters");
                int timeSeconds = rs.getInt("gametime");
                data.add(new DataScore(playerTime, playerName, guessWord, wrongChar, timeSeconds));
            }
            model.setDataScores(data);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectRandomWord() {
        String selectedCategory = model.getSelectedCategory();
        String sql;
        if (selectedCategory.equals("Kõik kategooriad")) {
            sql = "SELECT word FROM words ORDER BY RANDOM () LIMIT 1;";
        } else {
            sql = "SELECT word FROM words WHERE category = '" + selectedCategory.replace("'", "''") + "' ORDER BY RANDOM() LIMIT 1;";
        }
        try {
            Connection connection = this.dbConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String word = rs.getString("word");
                model.setWord(word);
                model.convertToCorrectCharacters(word);
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveScore(String name, int gameTime) {
        String sql = "INSERT INTO scores (playertime, playername, guessword, wrongcharacters, gametime) VALUES (?, ?, ?, ?, ?)";
        String word = model.getWord();
        String wrongCharacters = model.listToString(model.getWrongCharacters()).toUpperCase();
        String playerTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try {
            Connection connection = this.dbConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, playerTime);
            stmt.setString(2, name);
            stmt.setString(3, word);
            stmt.setString(4, wrongCharacters);
            stmt.setInt(5, gameTime);
            stmt.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
