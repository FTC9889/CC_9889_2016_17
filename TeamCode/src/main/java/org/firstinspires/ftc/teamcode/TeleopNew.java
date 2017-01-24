package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Teleop", group="Teleop")
public class TeleopNew extends LinearOpMode {

    /* Declare OpMode members. */
    Hardware9889   robot           = new Hardware9889();              // Use a K9'shardware
    private ElapsedTime runtime = new ElapsedTime();

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

            robot.Drivetrain(leftspeed, rightspeed);

            //Lower the max speed of the robot
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
                robot.Intake.setPower(0.3);
            }else if(Math.abs(gamepad2.left_trigger) > 0.3){
                robot.Intake.setPower(-1.0);
            }else if(gamepad2.right_bumper){
                robot.IntakeServo.setPower(-1.0);
                robot.Intake.setPower(0.25);
            }else {
                robot.IntakeServo.setPower(1.0);
                robot.Intake.setPower(0.0);
            }

            updateData();

            // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
            robot.waitForTick(40);
        }

        robot.flyWheel.setPower(0.0);
        robot.IntakeServo.setPower(0.0);
        robot.Intake.setPower(0.0);
        robot.Drivetrain(0.0,0.0);
        super.stop();
    }

    public void updateData(){
        telemetry.addData("Time Left", 120-runtime.seconds());
        telemetry.addData("Right Speed", robot.RDrive1.getPower());
        telemetry.addData("Left Speed", robot.LDrive1.getPower());
        telemetry.addData("Right Encoder", robot.getRightEncoder());
        telemetry.addData("Left Encoder", robot.getLeftEncoder());
        telemetry.addData("Right Encoder in Inches", robot.getRightEncoderinInches());
        telemetry.addData("Left Encoder in Inches", robot.getLeftEncoderinInches());
        telemetry.addData("Gyro Z-axis", robot.gyro.getIntegratedZValue());
        telemetry.addData("Ultrasonic Sensor Raw Value", robot.getUltrasonic());
        telemetry.addData("Left ODS", robot.BackODS.getRawLightDetected());
        telemetry.addData("Right ODS", robot.FrontODS.getRawLightDetected());
        telemetry.update();
    }
}