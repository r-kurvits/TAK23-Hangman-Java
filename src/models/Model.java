package models;

import models.datastructures.DataScore;

import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.*;

public class Model {
    private final String chooseCategory = "Kõik kategooriad";
    /**
     * See on vaikimisi andmebaasi fail kui käsurealt uut ei leotud. Andmebaasi tabelit nimed ja struktuurid peavad
     * samad olema, kuid andmed võivad erinevad olla.
     *  hangman_words_ee.db - Eestikeelsed sõnad, edetabel on tühi
     *  hangman_words_en.db - Inglisekeelsed sõnad, edetabel on tühi
     *  hangman_words_ee_test.db - Eestikeelsed sõnad, edetabel EI ole tühi
     */
    private String databaseFile = "hangman_words_ee_test.db";

    private String selectedCategory; // Vaikimisi valitud kategooria
    private String[] cmbCategories;

    private String imagesFolder = "images";
    private List<String> imageFiles = new ArrayList<>();

    private DefaultTableModel dtm;
    private List<DataScore> dataScores = new ArrayList<>();

    private String word;

    private List<String> userGuesses = new ArrayList<>();

    private String[] correctCharacters;

    private Integer wrongCount = 0;

    private List<String> wrongCharacters = new ArrayList<>();

    public Model(String dbName) {
        if (dbName != null ) {
            this.databaseFile = dbName;
        }
        new Database(this); // Loome andmebaasi ühenduse
        readImagesFolder();
        selectedCategory = chooseCategory; // Vaikimisi "Kõik kategooriad"
    }

    private void readImagesFolder() {
        File folder = new File(imagesFolder);
        File[] files = folder.listFiles();
        for (File file : Objects.requireNonNull(files)) {
            imageFiles.add(file.getAbsolutePath());
        }
        Collections.sort(imageFiles);
    }

    public void convertToCorrectCharacters(String word) {
        correctCharacters = new String[word.length()];
        for (int i = 0; i < word.length(); i++) {
            correctCharacters[i] = "_";
        }
    }

    public String arrayToString(String[] array) {
        return String.join(" ", array);
    }

    public String listToString(List<String> list) {
        return String.join(",", list);
    }

    /**
     * Rippmenüü esimene valik enne kategooriaid
     * @return teksti "Kõik kategooriad"
     */
    public String getChooseCategory() {
        return chooseCategory;
    }

    /**
     * Millise andmebaasiga on tegemist
     * @return andmebaasi failinimi
     */
    public String getDatabaseFile() {
        return databaseFile;
    }

    /**
     * Seadistab uue andmebaasi failinime, kui see saadi käsurealt
     * @param databaseFile uus andmebaasi failinimi
     */
    public void setDatabaseFile(String databaseFile) {
        this.databaseFile = databaseFile;
    }

    /**
     * Valitud kategoori
     * @return tagastab valitud kategooria
     */
    public String getSelectedCategory() {
        return selectedCategory;
    }

    /**
     * Seadistab valitud kategooria
     * @param selectedCategory uus valitud kategooria
     */
    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public String[] getCmbCategories() {
        return cmbCategories;
    }

    public void setCmbCategories(String[] cmbCategories) {
        this.cmbCategories = cmbCategories;
    }

    public List<String> getImageFiles() {
        return imageFiles;
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }

    public void setDtm(DefaultTableModel dtm) {
        this.dtm = dtm;
    }

    public List<DataScore> getDataScores() {
        return dataScores;
    }

    public void setDataScores(List<DataScore> dataScores) {
        this.dataScores = dataScores;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String[] getCorrectCharacters() {
        return correctCharacters;
    }

    public List<String> getUserGuesses() {
        return userGuesses;
    }

    public Integer getWrongCount() {
        return wrongCount;
    }

    public void setWrongCount(Integer wrongCount) {
        this.wrongCount = wrongCount;
    }

    public List<String> getWrongCharacters() {
        return wrongCharacters;
    }

    public void setWrongCharacters() {
        this.wrongCharacters = new ArrayList<>();
    }

    public void resetGameStats() {
        this.setWrongCount(0);
        this.setWrongCharacters();
    }
}
