package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Subsystems.*;

/**
 * Created by Jin on 12/17/2017.
 */

@TeleOp(name="Teleop", group="Teleop")
public class TeleopNew extends LinearOpMode {

    /* Declare OpMode members. */
    private Flywheel Flywheel_Intake          = new Flywheel();
    private Drivebase Drivetrain              = new Drivebase();
    private Beacon Beacon                     = new Beacon();
    private waitForTick waitForTick           = new waitForTick();

    private ElapsedTime runtime               = new ElapsedTime();
    private ElapsedTime shot                   =new ElapsedTime();

    boolean SmartShot = false;

    @Override
    public void runOpMode() throws InterruptedException{

        //////////////////////////////////////////////////////////////////
        //   Note:                                                      //
        //      To see the methods called please refer below.           //
        //==============================================================//
        //    ____________________________________________________      //
        //   | Name                ==      Class                  |     //
        //   |--------------------------------------------------- |     //
        //   | Beacon              ==      Subsystem.Beacon       |     //
        //   | Flywheel_Intake     ==      Subsystems.Flywheel    |     //
        //   | Drivetrain          ==      Subsystems.Drivebas    |     //
        //   | waitForTick         ==      Subsystems.waitForTick |     //
        //   ------------------------------------------------------     //
        //////////////////////////////////////////////////////////////////

        // Init Hardwawre
        // note: waitForTick does not have a hardware map
        Beacon.init(hardwareMap);
        Flywheel_Intake.init(hardwareMap);
        Drivetrain.init(hardwareMap);


        // Send telemetry message to signify robot waiting;
        telemetry.addData("Robot", " Running");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        double leftspeed, rightspeed, xvalue, yvalue;
        int div = 1;

        //Reset the time to allow for timer to stop automatically
        runtime.reset();

        //Run until the timer reaches 120 seconds or the STOP button is pressed
        while (opModeIsActive() && runtime.seconds() < 120) {
            //Turning control for Driver 2, so he can adjust the shot on the fly. Disables Driver 1's control
            if(gamepad2.dpad_left){
                leftspeed = -0.2;
                rightspeed = 0.2;
            }else if(gamepad2.dpad_right){
                leftspeed = 0.2;
                rightspeed = -0.2;
            }else {
                xvalue = -gamepad1.right_stick_x/div;
                yvalue = gamepad1.left_stick_y/div;

                leftspeed =  yvalue - xvalue;
                rightspeed = yvalue + xvalue;
            }

            Drivetrain.setLeftRightPower(leftspeed, rightspeed);

            //Lower the max speed of the robot
            if (gamepad1.left_trigger > 0.1){
                div = 4;
            }else {
                div = 1;
            }

            //Beacon pressing
            Beacon.BumperSynchronised(!(Drivetrain.getUltrasonic() < 35 || gamepad1.right_bumper));

            //Smart Shot
            if(gamepad1.right_trigger > 0.1){
                if (SmartShot) {
                    shot.reset();
                    SmartShot = false;
                }

                if(shot.milliseconds() > 700){
                    Flywheel_Intake.AutoShoot(true, true);
                    if(shot.milliseconds() > 1400){
                        SmartShot = true;
                    }
                }
                Flywheel_Intake.AutoShoot(true, false);

            }else {
                SmartShot = true;

                //Flywheel
                Flywheel_Intake.setFlywheel(gamepad2.a);

                //Intake ctrl
                if(Math.abs(gamepad2.right_trigger) > 0.01){
                    Flywheel_Intake.setIntakeMode(2);
                }else if(gamepad2.left_bumper) {
                    Flywheel_Intake.setIntakeMode(3);
                }else {
                    Flywheel_Intake.setIntakeMode(4);
                }
            }

            updateData();

            // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
            waitForTick.function(40);
        }

        Flywheel_Intake.setFlywheel(false);
        Flywheel_Intake.setIntakeMode(0);
        Drivetrain.STOP();
        super.stop();
    }

    public void updateData(){
        telemetry.addData("Time Left", 120-runtime.seconds());
        telemetry.addData("Right Speed", Drivetrain.getRightPower());
        telemetry.addData("Left Speed", Drivetrain.getLeftPower());
        telemetry.addData("Right Encoder", Drivetrain.getRightEncoder());
        telemetry.addData("Left Encoder", Drivetrain.getLeftEncoder());
        telemetry.addData("Right Encoder in Inches", Drivetrain.getRightEncoderinInches());
        telemetry.addData("Left Encoder in Inches", Drivetrain.getLeftEncoderinInches());
        telemetry.addData("Gyro Z-axis", Drivetrain.getGyro());
        telemetry.addData("Ultrasonic Sensor Raw Value", Drivetrain.getUltrasonic());
        telemetry.addData("Back ODS", Drivetrain.getBackODS());
        telemetry.addData("Front ODS", Drivetrain.getFrontODS());
        telemetry.update();
    }
}