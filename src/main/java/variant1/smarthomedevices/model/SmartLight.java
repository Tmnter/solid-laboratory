package main.java.variant1.smarthomedevices.model;

import variant1.smarthomedevices.service.IDimmable;
import variant1.smarthomedevices.service.IPowerable;

public class SmartLight implements IPowerable, IDimmable {
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