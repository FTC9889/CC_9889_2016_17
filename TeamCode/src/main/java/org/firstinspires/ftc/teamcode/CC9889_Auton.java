package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware9889;

/**
 * Created by Jin on 1/7/2017.
 */
@Autonomous(name="LSC Auto", group="Teleop")
public class CC9889_Auton extends LinearOpMode {

    /* Declare OpMode members. */
    Hardware9889 robot           = new Hardware9889();

    @Override
    public void runOpMode() {


            /* Initialize the hardware variables.
             * The init() method of the hardware class does all the work here
             */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();
        //Servo Movement
        robot.BumperControl(true);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        robot.Drivetrain(-1.0, -1.0);
        sleep(1500);;
        robot.Drivetrain(0.0,0.0);
        robot.STOP();
    }
}