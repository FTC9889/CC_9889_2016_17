package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;


/**
 * Created by Joshua H on 2/19/2017.
 */

public class LED_Control {

    //Name
    private DcMotor lights;
    private VoltageSensor voltage;

    private int counter = 0;
    private double brightness = 0.0;


    public LED_Control(){

    }

    public void init(HardwareMap hardwareMap){
        lights = hardwareMap.dcMotor.get("leds");
        voltage = hardwareMap.voltageSensor.get("Motor Controller 1");
        lights.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void setLedMode(int mode){
        if(voltage.getVoltage()<12){
            lights.setPower(0.0);
        }else {
            if(mode == 0){
                lights.setPower(0.0);
            }else if(mode == 1){
                lights.setPower(0.5);
            }else if(mode == 2){//Fade mode
                counter = counter + 1;

                if(counter<2000){
                    lights.setPower(0.2);
                }else if(counter<4000){
                    lights.setPower(0.4);
                }else if(counter<6000){
                    lights.setPower(0.6);
                }else if(counter<8000){
                    lights.setPower(1.0);
                }else if(counter<12000){
                    lights.setPower(0.6);
                }else if(counter<14000){
                    lights.setPower(0.4);
                }else if(counter<16000) {
                    lights.setPower(0.2);
                    counter = 0;
                }
            }
        }
    }
}
