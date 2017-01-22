package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
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
    Hardware9889   robot           = new Hardware9889();              // Use a K9'shardware

    @Override
    public void runOpMode() {


        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        double leftspeed, rightspeed, xvalue, yvalue;
        int div = 1;

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();

        //Servo Movement
        robot.BumperControl(true);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            xvalue = -gamepad1.right_stick_x/div;
            yvalue = gamepad1.left_stick_y/div;

            leftspeed =  yvalue - xvalue;
            rightspeed = yvalue + xvalue;

            robot.Drivetrain(leftspeed, rightspeed);

            if (gamepad1.left_trigger > 0.3){
                div = 4;
            }else {
                div = 1;
            }

            //Flywheel
            robot.Flywheel(gamepad2.a);

            //Beacon pressing
            if(gamepad1.right_bumper){
                robot.BumperControl(false);
            }else {
                robot.BumperControl(true);
            }

            //Intake ctrl
            if(Math.abs(gamepad2.right_trigger) > 0.3){
                robot.Intake.setPower(0.5);
            }else if(Math.abs(gamepad2.left_trigger) > 0.3){
                robot.Intake.setPower(-1.0);
            }else if(gamepad2.right_bumper){
                robot.IntakeServo.setPower(-1.0);
                robot.Intake.setPower(1.0);
            }else {
                robot.IntakeServo.setPower(1.0);
                robot.Intake.setPower(0.0);
            }

            //lift Servo
            /*if(Math.abs(gamepad1.right_trigger) > 0.3){
                robot.lift.setPower(1.0);
            }else if (Math.abs(gamepad1.left_trigger) > 0.3){
                robot.lift.setPower(-0.6);
            }else {
                robot.lift.setPower(0.0);
            }*/

            updateData();

            // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
            robot.waitForTick(40);
        }

        robot.flyWheel.setPower(0.0);
        robot.IntakeServo.setPower(0.0);
        robot.Intake.setPower(0.0);
        robot.Drivetrain(0.0,0.0);
    }

    public void updateData(){
        telemetry.addData("Right Speed", robot.RDrive1.getPower());
        telemetry.addData("Left Speed", robot.LDrive1.getPower());
        telemetry.addData("Right Encoder", robot.getRightEncoder());
        telemetry.addData("Left Encoder", robot.getLeftEncoder());
        telemetry.addData("Right Encoder in Inches", robot.getRightEncoderinInches());
        telemetry.addData("Left Encoder in Inches", robot.getLeftEncoderinInches());
        telemetry.addData("Right Power", robot.RDrive1.getPower());
        telemetry.addData("Left Power", robot.LDrive1.getPower());
        telemetry.addData("Gyro Z-axis", robot.gyro.getIntegratedZValue());
        telemetry.addData("Left ODS", robot.BackODS.getRawLightDetected());
        telemetry.addData("Right ODS", robot.FrontODS.getRawLightDetected());

        telemetry.update();
    }
}