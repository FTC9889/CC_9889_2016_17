package org.firstinspires.ftc.teamcode.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Joshua H on 1/21/2017.
 */

@Autonomous(name="Calibrate Gyro", group="Calibrate")
public class CC9889_Calibrate_Gyro extends LinearOpMode{

    //OpMode Members
    AutoHardware9889 robot          = new AutoHardware9889();
    boolean breakout = false;

    @Override
    public void runOpMode(){
        robot.init(hardwareMap);

        robot.STOP();
        robot.resetEncoders();

        telemetry.addData("Press Play to Calibrate", "");
        telemetry.update();

        waitForStart();
        
        telemetry.clearAll();
        telemetry.addData(">", "Gyro Calibrating. Do Not move!");
        telemetry.update();
        robot.gyro.calibrate();

        // make sure the gyro is calibrated.
        while (!isStopRequested() && robot.gyro.isCalibrating())  {
            sleep(50);
            idle();
        }

        telemetry.addData(">", "Gyro Calibrated.  Press Start.");
        telemetry.update();
    }
}
