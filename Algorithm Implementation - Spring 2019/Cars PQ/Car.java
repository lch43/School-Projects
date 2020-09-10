public class Car
    {
        private String VIN = "";
        private String make = "";
        private String model = "";
        private Double price = 0.0;
        private Double miles = 0.0;
        private String color = "";

        int index1;
        int index2;
        int index3;
        int index4;
        
    
        Car()
        {
    
        }
    
        Car(String v, String ma, String mo, Double p, Double mi, String c)
        {
            setVIN(v);
            setMake(ma);
            setModel(mo);
            setPrice(p);
            setMiles(mi);
            setColor(c);
        }
    
        public void setVIN(String v) {
            VIN = v;
        }
        
        public void setMake(String ma) {
            make = ma;
        }
        
        public void setModel(String mo) {
            model = mo;
        }
        
        public void setPrice(Double p) {
            price = p;
        }
    
        public void setMiles(Double mi) {
            miles = mi;
        }
    
        public void setColor(String c) {
            color = c;
        }
    
        public String getVIN() {
            return VIN;
        }
    
        public String getMake() {
            return make;
        }
    
        public String getModel() {
            return model;
        }
    
        public Double getPrice() {
            return price;
        }
    
        public Double getMiles() {
            return miles;
        }
    
        public String getColor() {
            return color;
        }

        public void printInfo()
        {
            System.out.println("VIN:"+VIN);
            System.out.println("Make:"+make);
            System.out.println("Model:"+model);
            System.out.println("Price:"+price);
            System.out.println("Miles:"+miles);
            System.out.println("Color:"+color);
        }

        public int comparePrice(Car other){
            if (this.price > other.getPrice())
            {
                return 1;
            }
            else if (this.price < other.getPrice())
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }

        public int compareMiles(Car other){
            if (this.miles > other.getMiles())
            {
                return 1;
            }
            else if (this.miles < other.getMiles())
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }
            
    }