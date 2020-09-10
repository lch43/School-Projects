//Landon Higinbotham
//LCH43
import java.io.*;
import java.util.*;


public class CarTracker
{
    private vinDLB load;
    private mmDLB load2;
    private minHEAP load3;
    private Car load4;

    private static SuperCarTracker CarSystem = new SuperCarTracker();
    public static void main(String[]args)
    {
        loadIn();
        System.out.println("Welcome to CarTracker.java");
        boolean done = false;
        while (!done)
        {
            int i = 1;
            System.out.println("Enter "+i+++" to: Add a car");
            System.out.println("Enter "+i+++" to: Update a car");
            System.out.println("Enter "+i+++" to: Remove a specific car from consideration");
            System.out.println("Enter "+i+++" to: Retrieve the lowest price car");
            System.out.println("Enter "+i+++" to: Retrieve the lowest mileage car");
            System.out.println("Enter "+i+++" to: Retrieve the lowest price car by make and model");
            System.out.println("Enter "+i+++" to: Retrieve the lowest mileage car by make and model");
            System.out.println("Enter "+i+++" to: Safely exit program");
            Scanner input = new Scanner(System.in);
            String in = input.nextLine();

            if (in.equals("1"))
            {
                System.out.println("Enter the VIN.");
                String VIN = input.nextLine();
                
                System.out.println("Enter the make.");
                String make = input.nextLine();
                
                System.out.println("Enter the model.");
                String model = input.nextLine();
                
                System.out.println("Enter the price.");
                Double price = Double.parseDouble(input.nextLine());
                
                System.out.println("Enter the miles.");
                Double miles = Double.parseDouble(input.nextLine());
                
                System.out.println("Enter the color.");
                String color = input.nextLine();

                Car newCarro = new Car(VIN, make, model, price, miles, color);
                CarSystem.add(newCarro);
                
            }
            else if (in.equals("2"))
            {
                System.out.println("Enter the VIN.");
                String VIN = input.nextLine();
                int j = 1;
                System.out.println("What would you like to change?");
                System.out.println("Enter "+j+++" to: Change the price of the car");
                System.out.println("Enter "+j+++" to: Change the mileage of the car");
                System.out.println("Enter "+j+++" to: Change the color of the car");
                in = input.nextLine();
                if (in.equals("1"))
                {
                    System.out.println("Enter the new price.");
                    double p = Double.parseDouble(input.nextLine());
                    CarSystem.changePrice(VIN, p);
                }
                else if (in.equals("2"))
                {
                    System.out.println("Enter the new mileage.");
                    double p = Double.parseDouble(input.nextLine());
                    CarSystem.changeMileage(VIN, p);
                }
                else if (in.equals("3"))
                {
                    System.out.println("Enter the new color.");
                    String p = input.nextLine();
                    CarSystem.changeColor(VIN, p);
                }
                
            }
            else if (in.equals("3"))
            {
                System.out.println("Enter the VIN.");
                String VIN = input.nextLine();
                CarSystem.remove(VIN);
            }
            else if (in.equals("4"))
            {
                Car gotCar = CarSystem.getLowestPrice();
                if (gotCar == null)
                {
                    System.out.println("Could not retrieve lowest price.");
                }
                else
                {
                    System.out.println();
                    gotCar.printInfo();
                }
            }
            else if (in.equals("5"))
            {
                Car gotCar = CarSystem.getLowestMiles();
                if (gotCar == null)
                {
                    System.out.println("Could not retrieve lowest miles.");
                }
                else
                {
                    System.out.println();
                    gotCar.printInfo();
                }
            }
            else if (in.equals("6"))
            {
                System.out.println("Enter the make");
                String make = input.nextLine();
                System.out.println("Enter the make");
                String model = input.nextLine();
                Car gotCar = CarSystem.getLowestPriceMM(make, model);
                if (gotCar == null)
                {
                    System.out.println("Could not retrieve lowest price of "+make+" "+model);
                }
                else
                {
                    System.out.println();
                    gotCar.printInfo();
                }
            }
            else if (in.equals("7"))
            {
                System.out.println("Enter the make");
                String make = input.nextLine();
                System.out.println("Enter the make");
                String model = input.nextLine();
                Car gotCar = CarSystem.getLowestMilesMM(make, model);
                if (gotCar == null)
                {
                    System.out.println("Could not retrieve lowest price of "+make+" "+model);
                }
                else
                {
                    System.out.println();
                    gotCar.printInfo();
                }
            }
            else if (in.equals("8"))
            {
                done = true;
                input.close();
            }
            System.out.println();
        }
        System.out.println("Goodbye!");
    }

    public static void loadIn()
    {
        try
        {
            File carFile = new File("cars.txt");
            Scanner carFileIn = new Scanner(carFile);
            carFileIn.nextLine();
            while (carFileIn.hasNext())
            {
                String line = carFileIn.nextLine();
                String[] information = line.split(":");
                Car carro = new Car(information[0], information[1], information[2], Double.parseDouble(information[3]), Double.parseDouble(information[4]), information[5]);
                CarSystem.add(carro);
            }
        }
        catch(FileNotFoundException notFound)
        {

        }
    }
}