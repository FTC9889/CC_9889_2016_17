package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Subsystems.*;

@TeleOp(name="Teleop", group="Teleop")
public class TeleopNew extends LinearOpMode {

    /* Declare OpMode members. */
    Flywheel Flywheel_Intake          = new Flywheel();
    Drivebase Drivetrain              = new Drivebase();
    Beacon Beacon                     = new Beacon();
    waitForTick waitForTick           = new waitForTick();
    ElapsedTime runtime = new ElapsedTime();

    boolean SmartShot = false;

    @Override
    public void runOpMode() {

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        Flywheel_Intake.init(hardwareMap);
        sleep(1000);
        Drivetrain.init(hardwareMap);
        sleep(1000);
        Beacon.init(hardwareMap);
        sleep(1000);
        waitForTick.init(hardwareMap);

        double leftspeed, rightspeed, xvalue, yvalue;
        int div = 1;

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Robot", " Running");    //
        telemetry.update();

        //Servo Movement
        Beacon.BumperSynchronised(true);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

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
            if (gamepad1.left_trigger > 0.3){
                div = 4;
            }else {
                div = 1;
            }



            //Beacon pressing
            if(Drivetrain.getUltrasonic() < 35 && !gamepad1.right_bumper){
                Beacon.BumperSynchronised(false);
            }else {
                Beacon.BumperSynchronised(true);
            }


            if(gamepad2.y){//Auto Shoot
                Flywheel_Intake.AutoShoot(true);
                SmartShot = false;
            }else {//Manual Control
                if (!SmartShot){
                    Flywheel_Intake.AutoShoot(false);
                    SmartShot = true;
                }

                //Flywheel
                Flywheel_Intake.setFlywheel(gamepad2.a);

                //Intake ctrl
                if(Math.abs(gamepad2.right_trigger) > 0.3){
                    Flywheel_Intake.setIntakeMode(2);
                }else if(Math.abs(gamepad2.left_trigger) > 0.3){
                    Flywheel_Intake.setIntakeMode(3);
                }else if(gamepad2.right_bumper){
                    Flywheel_Intake.setIntakeMode(1);
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