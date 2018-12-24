package ru.kpfu.itis.hyperbot.diagrams;

import com.yabcompany.GraphPrinter.GraphPrinter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class Application {

    private static final String DEFAULT_FILENAME = "Points.csv";

    public void run() {
        Scanner sc = new Scanner(System.in);
        String input = "";

        System.out.println("Welcome to group points diagram builder");
        GraphPrinter diagramBuilder = null;
        try {
            diagramBuilder = new GraphPrinter("config.properties","");
        } catch (IOException e) {
            System.out.println("Error: config file not found");
            System.out.println("Put config.properties into the program directory and restart the program");
            System.exit(1);
        } catch (NumberFormatException ex) {
            System.out.println("Error: config file damaged");
            System.out.println("Put correct config.properties into the program directory and restart the program");
            System.exit(1);
        }
        GroupPointsDataParser groupPointsDataParser = null;

        boolean askAgain = true;
        while (askAgain) {
            System.out.println("Type the location of group points data file");
            System.out.println("Or type \"load\" to load the file from the internet");
            String filename;
            input = sc.nextLine();
            if (input.equals("load")) {
                filename = DEFAULT_FILENAME;
                String url = "https://docs.google.com/spreadsheets/d/1vbSDj_bBaAmcD60p3SXSf9Zmb_7-2gHLWAehw8jIKfg/gviz/tq?tqx=out:csv&sheet=%D0%91%D0%B0%D0%BB%D0%BB%D1%8B%20%D0%B7%D0%B0%20%D1%81%D0%B5%D0%BC%D0%B5%D1%81%D1%82%D1%80";
                System.out.println("Downloading file...");
                try {
                    InputStream in = new URL(url).openStream();
                    Files.copy(in, Paths.get(filename), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File downloaded");
                } catch (Exception ex) {
                    System.out.println("Error: can't download file");
                    System.out.println("Check your internet connection and restart the program");
                    System.out.println("Old downloaded file will be used, if it is available");
                }
            } else {
                filename = input;
            }
            try {
                groupPointsDataParser = new GroupPointsDataParser(filename);
                askAgain = false;
            } catch (FileNotFoundException e) {
                System.out.println("Error: data file not found");
            } catch (BadDataException e) {
                System.out.println("Error: file is damaged, empty or has wrong format");
            }
        }

        String[] names = groupPointsDataParser.getNamesData();
        String[] params = groupPointsDataParser.getParamsData();
        int[][] values = groupPointsDataParser.getValuesData();


        boolean show = false;
        boolean save = false;
        askAgain = true;
        while(askAgain) {
            System.out.println("Type \"show\" to show the diagram, \"save\" to save it as picture and \"show&save\" to do both");
            input = sc.nextLine();
            switch (input) {
                case "show" :
                    show = true;
                    askAgain = false;
                    break;
                case "save":
                    save = true;
                    askAgain = false;
                    break;
                case "show&save":
                    show = true;
                    save = true;
                    askAgain = false;
                    break;
                default:
                    System.out.println("Wrong command");
                    break;
            }
        }

        BufferedImage bufferedImage =  diagramBuilder.horizColumnGraph("Информатика J804",names, params, values );
        if(save) {
            System.out.println("Type the name of file to be saved");
            input = sc.nextLine();
            try {
                diagramBuilder.saveToImage(bufferedImage, input, "png");
                System.out.println("Saved successfully");
            } catch (IOException e) {
                System.out.println("Error: can't save file");
                System.out.println("Check write permissions and destination directories");
            }
        }
        if(show) {
            DiagramWindow window = new DiagramWindow(bufferedImage);
            System.out.println("Diagram window is opened");
        }
    }
}
