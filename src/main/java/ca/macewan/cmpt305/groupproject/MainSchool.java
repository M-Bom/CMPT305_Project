package ca.macewan.cmpt305.groupproject;

import java.util.Scanner;

/**
 * This main is just to test Schools and Parks Methods
 * Can delete if you like at end
 * */
public class MainSchool {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filename = "Edmonton_Public_School_Board.csv";
        String filename2 = "Parks_20250326.csv";


        try{
            Schools schools = new Schools(filename);
            Parks parks = new Parks(filename2);
            System.out.println(schools.getAllSchoolTypes());
            System.out.println(schools.getSeniorSchools());
            System.out.println(parks.getAllCoordinates());
            System.out.println(parks.toString());

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
