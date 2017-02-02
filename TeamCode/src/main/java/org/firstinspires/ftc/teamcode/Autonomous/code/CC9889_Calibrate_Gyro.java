package org.firstinspires.ftc.teamcode.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.Beacon;
import org.firstinspires.ftc.teamcode.Subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.Subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.Subsystems.waitForTick;

/**
 * Created by Joshua H on 1/21/2017.
 */

@Autonomous(name="Calibrate Gyro", group="Calibrate")
@Disabled
public class CC9889_Calibrate_Gyro extends LinearOpMode {


    /* Declare OpMode members. */
    private Drivebase Drivetrain = new Drivebase();


    private ElapsedTime runtime = new ElapsedTime();

    boolean SmartShot = false;

    @Override
    public void runOpMode() {

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */

        telemetry.addData("Drivetrain", "");
        telemetry.update();
        Drivetrain.init(hardwareMap);


        telemetry.addData("Press Play to Calibrate", "");
        telemetry.update();

        waitForStart();

        telemetry.clearAll();
        telemetry.addData(">", "Gyro Calibrating. Do Not move!");
        telemetry.update();
        Drivetrain.gyro.calibrate();

        // make sure the gyro is calibrated.
        while (!isStopRequested() && Drivetrain.gyro.isCalibrating())  {
            sleep(50);
            idle();
        }

        Drivetrain.resetGyro();

        telemetry.addData(">", "Gyro Calibrated.  Press Start.");
        telemetry.update();

        super.stop();

    }
}
