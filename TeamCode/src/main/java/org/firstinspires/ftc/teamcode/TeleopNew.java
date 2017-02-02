package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Subsystems.*;

import org.firstinspires.ftc.teamcode.Hardware9889;

/**
 * This OpMode uses the common HardwareK9bot class to define the devices on the robot.
 * All device access is managed through the HardwareK9bot class. (See this class for device names)
 * The code is structured as a LinearOpMode
 *
 * This particular OpMode executes a basic Tank Drive Teleop for the K9 bot
 * It raises and lowers the arm using the Gampad Y and A buttons respectively.
 * It also opens and closes the claw slowly using the X and B buttons.
 *
 * Note: the configuration of the servos is such that
 * as the arm servo approaches 0, the arm position moves up (away from the floor).
 * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */


@TeleOp(name="Teleop New", group="Teleop")
public class TeleopNew extends LinearOpMode {

    /* Declare OpMode members. */

    private Flywheel Flywheel_Intake          = new Flywheel();
    private Drivebase Drivetrain              = new Drivebase();
    private Beacon Beacon                     = new Beacon();
    private waitForTick waitForTick           = new waitForTick();

    Hardware9889   robot           = new Hardware9889();              // Use a K9'shardware

    private ElapsedTime runtime               = new ElapsedTime();
    private ElapsedTime shot                   =new ElapsedTime();

    boolean SmartShot = false;

    @Override
    public void runOpMode() throws InterruptedException {

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        telemetry.addData("Beacon", "");
        telemetry.update();
        Beacon.init(hardwareMap);
        telemetry.addData("Flywheel", "");
        telemetry.update();
        Flywheel_Intake.init(hardwareMap);
        telemetry.addData("Drivetrain", "");
        telemetry.update();
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
            if (gamepad2.dpad_left) {
                leftspeed = -0.2;
                rightspeed = 0.2;
            } else if (gamepad2.dpad_right) {
                leftspeed = 0.2;
                rightspeed = -0.2;
            } else {
                xvalue = -gamepad1.right_stick_x / div;
                yvalue = gamepad1.left_stick_y / div;

                // run until the end of the match (driver presses STOP)
                while (opModeIsActive()) {

                    xvalue = -gamepad1.right_stick_x / div;
                    yvalue = gamepad1.left_stick_y / div;

                    leftspeed = yvalue - xvalue;
                    rightspeed = yvalue + xvalue;

                    Drivetrain.setLeftRightPower(leftspeed, rightspeed);

                    //Lower the max speed of the robot
                    if (gamepad1.left_trigger > 0.01) {
                        div = 4;
                    } else {
                        div = 1;
                    }

                    //Beacon pressing
                    Beacon.BumperSynchronised(!(Drivetrain.getUltrasonic() < 35 || gamepad1.right_bumper));

                    //Smart Shot
                    if (gamepad1.a) {
                        if (SmartShot) {
                            shot.reset();
                            SmartShot = false;
                        }

                        if (shot.milliseconds() > 700) {
                            Flywheel_Intake.AutoShoot(true, true);
                            if (shot.milliseconds() > 1400) {
                                SmartShot = true;
                            }
                        }
                        Flywheel_Intake.AutoShoot(true, false);
                        //Intake ctrl
                        if (Math.abs(gamepad2.right_trigger) > 0.3) {
                            robot.Intake.setPower(0.5);
                        } else if (Math.abs(gamepad2.left_trigger) > 0.3) {
                            robot.Intake.setPower(-1.0);
                        } else if (gamepad2.right_bumper) {
                            robot.IntakeServo.setPower(-1.0);
                            robot.Intake.setPower(1.0);
                        } else {
                            SmartShot = true;

                            //Flywheel
                            Flywheel_Intake.setFlywheel(gamepad2.a);

                            //Intake ctrl
                            if (Math.abs(gamepad2.right_trigger) > 0.01) {
                                Flywheel_Intake.setIntakeMode(2);
                            } else if (gamepad2.left_bumper) {
                                Flywheel_Intake.setIntakeMode(3);
                            } else {
                                Flywheel_Intake.setIntakeMode(4);
                            }
                        }

                        //lift Servo
            /*if(Math.abs(gamepad1.right_trigger) > 0.3){
                robot.lift.setPower(1.0);
            }else if (Math.abs(gamepad1.left_trigger) > 0.3){
                robot.lift.setPower(-0.6);
            }else {
                robot.lift.setPower(0.0);
            }*/

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
        }
    }
            public void updateData (){
                telemetry.addData("Time Left", 120 - runtime.seconds());
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