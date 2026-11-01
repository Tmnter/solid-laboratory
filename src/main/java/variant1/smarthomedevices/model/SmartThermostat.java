package main.java.variant1.smarthomedevices.model;

import variant1.smarthomedevices.service.IPowerable;
import variant1.smarthomedevices.service.ITemperatureControllable;

public class SmartThermostat implements IPowerable, ITemperatureControllable {
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