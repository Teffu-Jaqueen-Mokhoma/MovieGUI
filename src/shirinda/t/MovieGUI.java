package shirinda.t;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MovieGUI extends JFrame {
    private JTextField titleField, yearField, genreField;
    private JTextArea outputArea;
    private JButton addButton, displayButton;
    private MovieLibrary movieLibrary;

    public MovieGUI() {
        super("Movie Library");
        movieLibrary = new MovieLibrary();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Movie"));

        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField(20);

        JLabel yearLabel = new JLabel("Year:");
        yearField = new JTextField(10);

        JLabel genreLabel = new JLabel("Genre:");
        genreField = new JTextField(15);

        inputPanel.add(titleLabel);
        inputPanel.add(titleField);
        inputPanel.add(yearLabel);
        inputPanel.add(yearField);
        inputPanel.add(genreLabel);
        inputPanel.add(genreField);

        addButton = new JButton("Add Movie");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMovie();
            }
        });

        inputPanel.add(addButton);

        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        displayButton = new JButton("Display Movies");
        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayMovies();
            }
        });
        mainPanel.add(displayButton, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addMovie() {
        String title = titleField.getText();
        int year = Integer.parseInt(yearField.getText());
        String genre = genreField.getText();

        Movie movie = new Movie(title, year, genre);
        movieLibrary.addMovie(movie);

        try {
            FileWriter writer = new FileWriter("movies.txt", true);
            writer.write(title + "," + year + "," + genre + "\n");
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        titleField.setText("");
        yearField.setText("");
        genreField.setText("");
    }

    private void displayMovies() {
        outputArea.setText("");
        for (Movie movie : movieLibrary.getMovies()) {
            outputArea.append(movie.getTitle() + " (" + movie.getReleaseYear() + ") - " + movie.getGenre() + "\n");
        }
    }

    public static void main(String[] args) {
        new MovieGUI();
    }
}

class Movie {
    private String title;
    private int releaseYear;
    private String genre;

    public Movie(String title, int releaseYear, String genre) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getGenre() {
        return genre;
    }
}

class MovieLibrary {
    private java.util.List<Movie> movies;

    public MovieLibrary() {
        movies = new java.util.ArrayList<>();
        loadMovies();
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public java.util.List<Movie> getMovies() {
        return movies;
    }

    private void loadMovies() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("movies.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String title = parts[0];
                int year = Integer.parseInt(parts[1]);
                String genre = parts[2];
                Movie movie = new Movie(title, year, genre);
                movies.add(movie);
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
