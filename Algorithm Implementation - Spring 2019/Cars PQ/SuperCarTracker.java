public class SuperCarTracker
{
    private vinDLB allVINS = new vinDLB();
    private mmDLB allMMS = new mmDLB();
    private minHEAP price = new minHEAP(1);
    private minHEAP miles = new minHEAP(2);

    SuperCarTracker(){}

    public void add(Car car)
    {
        if (!allVINS.add(car, car.getVIN()))
        {
            System.out.println("Car could not be added.");
        }
        else
        {
            price.insert(car);
            miles.insert(car);
            minHEAP[] mmHeaps = allMMS.add(car);
            if (mmHeaps != null)
            {
                mmHeaps[0].insert(car);
                mmHeaps[1].insert(car);
            }
        }
    }

    public void changeColor(String vin, String change)
    {
        retrieve(vin).setColor(change);
    }

    public void changePrice(String vin, Double change)
    {
        retrieve(vin).setPrice(change);
        update(vin);
    }

    
    public void changeMileage(String vin, Double change)
    {
        retrieve(vin).setMiles(change);
        update(vin);
    }

    private void update(String vin)
    {
        Car car = retrieve(vin);
        price.change(car.index1);
        miles.change(car.index2);
        //allVINS.remove(car.getVIN());
        minHEAP[] mmHeaps = allMMS.search(car.getMake()+car.getModel());
        if (mmHeaps != null)
        {
            mmHeaps[0].change(car.index3);
            mmHeaps[1].change(car.index4);
        }
    }

    public Car retrieve(String vin)
    {
        return allVINS.search(vin);
    }

    public void remove(String vin)
    {
        Car car = retrieve(vin);

        //System.out.println("Car is " + car);
        //System.out.println("Price is " + price);
        if (car != null)
        {
            price.delete(car.index1);
            miles.delete(car.index2);
            allVINS.remove(car.getVIN());
            minHEAP[] mmHeaps = allMMS.search(car.getMake()+car.getModel());
            if (mmHeaps != null)
            {
                mmHeaps[0].delete(car.index3);
                mmHeaps[1].delete(car.index4);
            }
        }
        else
        {
            System.out.println("Could not removed");
        }
    }

    public Car getLowestPriceMM(String make, String model)
    {
        minHEAP[] mmHeaps = allMMS.search(make+model);
        if (mmHeaps == null)
        {
            return null;
        }
        return mmHeaps[0].min();
    }

    public Car getLowestMilesMM(String make, String model)
    {
        minHEAP[] mmHeaps = allMMS.search(make+model);
        if (mmHeaps == null)
        {
            return null;
        }
        return mmHeaps[1].min();
    }

    public Car getLowestPrice()
    {
        return price.min();
    }

    public Car getLowestMiles()
    {
        return miles.min();
    }
}