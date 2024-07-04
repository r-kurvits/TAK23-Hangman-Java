package views.panels;

import models.Database;
import models.Model;
import views.View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * See on edetabeli klass. See näitab andmebaasist loetud edetabelit. Seda ei saa mängimise ajal
 * vaadata.
 */
public class LeaderBoard extends JPanel {
    /**
     * Klassisisene mudel, mille väärtus saadakse View konstruktorist ja loodud MainApp-is
     */
    private final Model model;
    /**
     * Klassisisene vaade, mille väärtus saadakse otse View-st
     */
    private final View view;
    private String[] heading = new String[]{"Kuupäev", "Nimi", "Sõna", "Tähed", "Mänguaeg"};
    private DefaultTableModel dtm = new DefaultTableModel(heading, 0);
    private JTable table = new JTable(dtm);

    public LeaderBoard(Model model, View view) {
        this.model = model;
        this.view = view;

        setLayout(new BorderLayout());

        setBackground(new Color(250, 150, 215)); // Leaderboard paneeli taustavärv

        setBorder(new EmptyBorder(5, 5, 5, 5));

        model.setDtm(dtm);

        createLeaderBoard();
    }

    private void createLeaderBoard() {
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);
        table.getColumnModel().getColumn(0).setPreferredWidth(110);
        table.setDefaultEditor(Object.class, null);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);

        new Database(model).selectScores();
        if(!model.getDataScores().isEmpty()) {
            view.updateScoresTable();
        } else {
            JOptionPane.showMessageDialog(view, "Esmalt tuleb mängida! ");
        }
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }
}
