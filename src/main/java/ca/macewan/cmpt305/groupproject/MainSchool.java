package ca.macewan.cmpt305.groupproject;

import java.util.Scanner;

public class MainSchool {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filename = "Edmonton_Public_School_Board.csv";
        String filename2 = "Edmonton_Neighbourhoods.csv";

        try{
            Schools schools = new Schools(filename);
            NeighbourhoodCatchments neighbourhoodCatchments = new NeighbourhoodCatchments(filename2);

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
            System.out.println("Enter neighbour Name: ");
            String neighbourId = scanner.nextLine();
            if (!neighbourId.isEmpty()){
                NeighbourhoodCatchment neighbourhoodCatchment = neighbourhoodCatchments.getNeighbourHoodCatchmentByName(neighbourId);

                if (neighbourhoodCatchment == null){
                    System.out.println("NO NEIGHBOURHOOD FOUND");
                    }
                else {
                    System.out.println("NEIGHBOURHOOD FOUND");
                    System.out.println(neighbourhoodCatchment.getNeighbourhood());
                    System.out.println(neighbourhoodCatchment.getCatchment());
                }
            }



        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
