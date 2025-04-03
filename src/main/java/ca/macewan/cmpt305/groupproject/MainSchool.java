package ca.macewan.cmpt305.groupproject;

import java.util.Scanner;

public class MainSchool {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filename = "Edmonton_Public_School_Board.csv";


        try{
            Schools schools = new Schools(filename);
            System.out.println(schools.getAllSchoolTypes());
            System.out.println(schools.getSeniorSchools());
            //System.out.println(schools.getAllCoordinates());
            System.out.println("Enter school id: ");
            String targetId = scanner.nextLine();
            if (!targetId.isEmpty()){
                School schoolData = schools.getSchoolByID(targetId);
                if (schoolData == null){
                    System.out.println("NO SCHOOL FOUND");
                }
                // if there is a property assessment object
                else {
                    System.out.println("SCHOOL FOUND");
                    System.out.println(schoolData);
                    System.out.println(schoolData.getCatchment());
                }

            }


        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
