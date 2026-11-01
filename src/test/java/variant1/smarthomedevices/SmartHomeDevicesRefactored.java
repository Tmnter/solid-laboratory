package variant1.smarthomedevices;

// Опис предметної області: Екосистема пристроїв розумного дому після рефакторингу (ISP).

public class SmartHomeDevicesRefactored {

    // 1. Базовий інтерфейс живлення, який потрібен абсолютно всім пристроям
    public interface IPowerable {
        void powerOn();
        void powerOff();
    }

    // 2. Інтерфейс для пристроїв, що керують температурою
    public interface ITemperatureControllable {
        void setTemperature(double temp);
    }

    // 3. Інтерфейс для пристроїв із можливістю регулювання яскравості
    public interface IDimmable {
        void setBrightness(int level);
    }

    // 4. Інтерфейс для пристроїв, що мають функцію приготування кави
    public interface ICoffeeBrewable {
        void brewCoffee();
    }

    // Термостат реалізує тільки живлення та температуру
    public static class SmartThermostat implements IPowerable, ITemperatureControllable {
        @Override 
        public void powerOn() { 
            System.out.println("Thermostat powered on."); 
        }
        
        @Override 
        public void powerOff() { 
            System.out.println("Thermostat powered off."); 
        }
        
        @Override 
        public void setTemperature(double temp) { 
            System.out.println("Temperature set to " + temp + "°C."); 
        }
    }

    // Розумна лампочка реалізує живлення та яскравість
    public static class SmartLight implements IPowerable, IDimmable {
        @Override 
        public void powerOn() { 
            System.out.println("Light turned on."); 
        }
        
        @Override 
        public void powerOff() { 
            System.out.println("Light turned off."); 
        }
        
        @Override 
        public void setBrightness(int level) { 
            System.out.println("Brightness set to " + level + "%."); 
        }
    }

    // Кавоварка реалізує живлення та приготування кави
    public static class SmartCoffeeMaker implements IPowerable, ICoffeeBrewable {
        @Override 
        public void powerOn() { 
            System.out.println("Coffee maker powered on."); 
        }
        
        @Override 
        public void powerOff() { 
            System.out.println("Coffee maker powered off."); 
        }
        
        @Override 
        public void brewCoffee() { 
            System.out.println("Brewing fresh coffee..."); 
        }
    }
}