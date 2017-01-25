package org.firstinspires.ftc.teamcode.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.Subsystems.Drivebase;

/**
 * Created by Joshua H on 1/21/2017.
 */

@Autonomous(name="Calibrate Gyro", group="Calibrate")
public class CC9889_Calibrate_Gyro extends LinearOpMode{

    //OpMode Members
    Drivebase Drivebase = null;
    boolean breakout = false;

    @Override
    public void runOpMode(){
        Drivebase.init(hardwareMap);

        Drivebase.STOP();
        Drivebase.resetEncoders();

        telemetry.addData("Press Play to Calibrate", "");
        telemetry.update();

        waitForStart();
        
        telemetry.clearAll();
        telemetry.addData(">", "Gyro Calibrating. Do Not move!");
        telemetry.update();
        Drivebase.resetGyro();

        // make sure the gyro is calibrated.
        while (!isStopRequested() && Drivebase.GyroisCalibrating())  {
            sleep(50);
            idle();
        }

        Drivebase.resetGyro();

        telemetry.addData(">", "Gyro Calibrated.  Press Start.");
        telemetry.update();

        super.stop();
    }
}
