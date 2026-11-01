package main.java.variant1.smarthomedevices.model;

import variant1.smarthomedevices.service.ICoffeeBrewable;
import variant1.smarthomedevices.service.IPowerable;

public class SmartCoffeeMaker implements IPowerable, ICoffeeBrewable {
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