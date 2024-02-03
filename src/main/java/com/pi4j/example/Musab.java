package com.pi4j.example;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;

public class Musab {

    static Context pi4j = Pi4J.newAutoContext();

    private static final int[] GPIO_PIN = {5,6,13,19};
    private static DigitalOutput[] motor_pins = new DigitalOutput[4];
    private static final int STEP_SLEEP = 2;
    private static final int STEP_COUNT = 4096;
    private static final boolean DIRECTION = true;
    private static final int[][] STEP_SEQUENCE = {{1,0,0,1},
            {1,0,0,0},
            {1,1,0,0},
            {0,1,0,0},
            {0,1,1,0},
            {0,0,1,0},
            {0,0,1,1},
            {0,0,0,1}};

    static{
        for(int i =0; i < GPIO_PIN.length;i++){
            motor_pins[i] = pi4j.create( DigitalOutput.newConfigBuilder(pi4j).address(GPIO_PIN[i]).shutdown(DigitalState.LOW)
                    .initial(DigitalState.LOW).provider("pigpio-digital-output"));
        }
    }
    static int motor_step_counter = 0 ;

    public static void cleanUp() {
        for(var i: motor_pins){
            i.low();
        }
        pi4j.shutdown();
    }

    public static void main(String[] args) throws Exception{

        try {
            for (int i = 0; i < STEP_COUNT; i++) {
                if(motor_step_counter > 7){
                    motor_step_counter =0;
                }
                for(int pin = 0; pin < motor_pins.length; pin++){
                    System.out.println("i="+i+",pin="+pin+", state="+STEP_SEQUENCE[motor_step_counter][pin]+",motor_step_counter="+motor_step_counter);
                    motor_pins[pin].setState(STEP_SEQUENCE[motor_step_counter][pin]);
                }
                if(DIRECTION){
                    System.out.println("b4 motor_step_counter"+motor_step_counter);
                    motor_step_counter++;
                    System.out.println("after motor_step_counter"+motor_step_counter);
                }
                else{
                    System.out.println("b4 motor_step_counter"+motor_step_counter);
                    motor_step_counter++;
                    System.out.println("after motor_step_counter"+motor_step_counter);
                }
                Thread.sleep(STEP_SLEEP);
//                if(i == 3)
//                    break;
            }
            System.out.println("Stepper motor done.");

        } catch (Exception e) {
            System.out.println("Uh oh, an error occurred: " + e.getMessage());
        } finally {
            cleanUp();
            System.out.println("Stepper motor stopped.");
        }
    }


}