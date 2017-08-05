package org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;


/**
 * Created by Joshua H on 2/19/2017.
 */

public class LED_Control {

    //Name
    private DcMotor lights;

    private int counter = 0;


    public LED_Control(){

    }

    public void init(HardwareMap hardwareMap){
        lights = hardwareMap.dcMotor.get("leds");

        lights.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        setLedMode(false);
    }

    public void setLedMode(boolean on){
        if(on){
            lights.setPower(0.3);
        }else{
            lights.setPower(0.0);
        }
    }
}
