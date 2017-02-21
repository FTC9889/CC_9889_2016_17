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


    public LED_Control(){

    }

    public void init(HardwareMap hardwareMap){
        lights = hardwareMap.dcMotor.get("leds");
        voltage = hardwareMap.voltageSensor.get("Motor Controller 1");
        lights.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void setLedMode(boolean on){
        if(voltage.getVoltage()<12.5){
            lights.setPower(0.0);
        }else {
            if(on){
                lights.setPower(0.3);
            }else{
                lights.setPower(0.0);
            }
        }
    }
}
