package ru.kpfu.itis.hyperbot.diagrams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GroupPointsDataParser {
    private String[] strNames;
    private int[][] values;

    public GroupPointsDataParser(String filename) throws FileNotFoundException, BadDataException {
        List<String> fileStrings = new ArrayList<String>();
        ArrayList<String[]> cells = new ArrayList<String[]>();
        ArrayList<String> names = new ArrayList<String>();
        Scanner sc = new Scanner(new File(filename), "utf-8");
        while(sc.hasNextLine()) {
            fileStrings.add(sc.nextLine().replaceAll("\"", ""));
        }
        sc.close();
        for(String s : fileStrings) {
            cells.add(s.split(","));
        }
        for(int i = 2; i < cells.size() && !cells.get(i)[0].isEmpty(); i++) {
            names.add(cells.get(i)[0]);
        }
        if(names.size() == 0) {
            throw new BadDataException("names not found");
        }
        strNames = new String[names.size()];
        names.toArray(strNames);

        values = new int[names.size()][1];
        for(int i = 2; i < 2 + names.size(); i++) {
            values[i-2][0] = (int)Double.parseDouble(cells.get(i)[2]);
        }
        if(values.length == 0 || values[0].length == 0) {
            throw new BadDataException("values not found");
        }

    }

    public String[] getNamesData() {
        return strNames;
    }

    public String[] getParamsData() {
        return new String[] {"Общий балл"};
    }

    public int[][] getValuesData() {
        return values;
    }
}
