package com.pi4j.example;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;

public class StepMotorApp {

    public static void main(String[] args) {

        StepMotorApp stepMotorApp = new StepMotorApp();
        try {
            stepMotorApp.execute(Pi4J.newAutoContext());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public void execute(Context pi4j) throws Exception{
        // Initialize step motor with default configuration for CrowPi
        final var stepMotor = new StepMotorComponent(pi4j);

        // 'Warn' the user about the upcoming action
        System.out.println("Watch your step motor closely, it will start moving in 5 seconds...");
        Thread.sleep(5000);

        // Turn the motor 50 times forward and backward, resulting in the same position
        System.out.println("Moving 50 steps forward...");
        stepMotor.turnForward(50);
         Thread.sleep(1000);

        System.out.println("...and moving 50 steps backward...");
        stepMotor.turnBackward(50);

        System.out.println("...and we are back at our previous position!");
         Thread.sleep(1000);

        // Simulate a sweeping motion (forward -> backward + backward -> forward)
        for (int i = 5; i > 0; i--) {
            System.out.println("Sweep sweep sweep... just " + i + " more times");

            // Start by rotating 90 degrees forward...
            stepMotor.turnDegrees(90);

            // ...then rotating 180 degrees backwards to the other side...
            // note: a negative amount of degrees means going backward
            stepMotor.turnDegrees(-180);

            // ...then back to the center by going 90 degrees forward again
            stepMotor.turnDegrees(90);
        }
    }
}
