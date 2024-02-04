package com.pi4j.example;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;

import java.security.Timestamp;
import java.time.LocalDateTime;

public class Khubaib {

    Context pi4j = Pi4J.newAutoContext();

    private final int[] GPIO_PIN = {5, 6, 13, 19};
    private DigitalOutput[] motor_pins = new DigitalOutput[4];
    private final int STEP_SLEEP = 10;
    private final int STEP_COUNT = 2098;
    private final boolean DIRECTION = true;
    private final int[][] STEP_SEQUENCE = {{1, 1, 0, 0},
            {0, 1, 1, 0},
            {0, 0, 1, 1},
            {1, 0, 0, 1},
            {1, 1, 0, 0},
            {0, 1, 1, 0},
            {0, 0, 1, 1},
            {1, 0, 0, 1}};

    {
        for (int i = 0; i < GPIO_PIN.length; i++) {
            motor_pins[i] = pi4j.create(DigitalOutput.newConfigBuilder(pi4j).address(GPIO_PIN[i]).shutdown(DigitalState.LOW)
                    .initial(DigitalState.LOW).provider("pigpio-digital-output"));
        }
    }

    int motor_step_counter = 0;

    public void cleanUp() {
        for (var i : motor_pins) {
            i.low();
        }
        pi4j.shutdown();
    }

    public static void main(String[] args) throws Exception {
        Khubaib musab = new Khubaib();
        musab.start();
    }

    public void start() throws Exception {
        try {
            for (int i = 0; i < STEP_COUNT; i++) {
                if (motor_step_counter > (STEP_SEQUENCE.length-1)) {
                    motor_step_counter = 0;
                }
                for (int pin = 0; pin < motor_pins.length; pin++) {
                    System.out.println(LocalDateTime.now() +",i=" + i + ",pin=" + pin + ", state=" + STEP_SEQUENCE[motor_step_counter][pin] + ",motor_step_counter=" + motor_step_counter);
                    motor_pins[pin].setState(STEP_SEQUENCE[motor_step_counter][pin]);
                }
                if (DIRECTION) {
                    System.out.println(LocalDateTime.now() +",b4 motor_step_counter" + motor_step_counter);
                    motor_step_counter++;
                    System.out.println(LocalDateTime.now() +",after motor_step_counter" + motor_step_counter);
                } else {
                    System.out.println(LocalDateTime.now() +",b4 motor_step_counter" + motor_step_counter);
                    motor_step_counter++;
                    System.out.println(LocalDateTime.now() +",after motor_step_counter" + motor_step_counter);
                }
                Thread.sleep(STEP_SLEEP);
//                if(i == 3)
//                    break;
            }
            System.out.println(LocalDateTime.now() +",Stepper motor done.");

        } catch (Exception e) {
            System.out.println(LocalDateTime.now() +",Uh oh, an error occurred: " + e.getMessage());
        } finally {
            cleanUp();
            System.out.println(LocalDateTime.now() +",Stepper motor stopped.");
        }
    }


}