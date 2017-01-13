package org.firstinspires.ftc.teamcode.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static com.qualcomm.robotcore.util.Range.clip;

/**
 * Created by Jin on 9/30/2016. #WeGonRideWeGonWin #ObieDidHarambe
 */
 @Autonomous(name="AutoBlue", group="Blue")
public class CC9889_Autonomo_Blue extends LinearOpMode {


    /* Declare OpMode members. */
    AutoHardware9889 robot           = new AutoHardware9889();
    @Override
    public void runOpMode () {
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData(">", "Gyro Calibrating. Do Not move!");
        telemetry.update();
        robot.resetEncoders();
        robot.STOP();
        updateData();
        while (!isStopRequested() && robot.gyro.isCalibrating())  {
            sleep(50);
            idle();
        }telemetry.addData(">", "Gyro Calibrated. ¯\\_(ツ)_/¯");
        telemetry.update();

        waitForStart();

        robot.STOP();

        //Robot drives forward 20 inches
        EncoderDrive(1.0, -20, -20);

        //The robot drives up to the white line.
        robot.Drivetrain(0.3, 0.3);

        while (opModeIsActive() && robot.gyro.getIntegratedZValue() > -25){
            robot.waitForTick(50);
            updateData();
        }

        robot.STOP();

        robot.Drivetrain(-0.1, -0.1);

        while (opModeIsActive() && robot.gyro.getIntegratedZValue() < -25){
            robot.waitForTick(50);
            updateData();
        }

        robot.STOP();

        //The robot detects the white line with the Lego light sensor.
        robot.Drivetrain(0.7, -0.7);

        while (opModeIsActive() && robot.light.getRawLightDetected() < 1.8){
            robot.waitForTick(10);
            updateData();
        }

        robot.STOP();

        //The robot lines itself up with the white line.
        robot.Drivetrain(0.0, 0.4);

        while (opModeIsActive() && robot.RWhiteLine.getRawLightDetected() < 1.5){
            robot.waitForTick(50);
            updateData();
        }

        robot.STOP();

        //Here the robot decides which beacon button to press.
        robot.BumperControl(false);
        robot.Drivetrain(0.5,-0.5);
        sleep(1000);
        if (robot.Color.red() > robot.Color.blue()){
            robot.RightBumper.setPosition(0.8);
        }else {
            robot.LeftBumper.setPosition(0.2);
        }

        sleep(500);

        robot.STOP();

        super.stop();
    }

    //Go to white line

    public void FindWhiteTape(double speed, boolean color){
        robot.STOP();

        while (opModeIsActive() && robot.RWhiteLine.getRawLightDetected() < 1.5 && robot.LWhiteLine.getRawLightDetected() < 1.5){
            robot.Drivetrain(speed,speed);
            robot.waitForTick(25);
        }
        robot.STOP();
        robot.resetEncoders();

        while (opModeIsActive() && robot.getLeftEncoderinInches() > 4 && robot.getRightEncoder() > 4){
            robot.Drivetrain(0.2, 0.2);
            robot.waitForTick(55);
        }
        robot.resetEncoders();

        while (opModeIsActive() && robot.getLeftEncoderinInches() > 4 && robot.getRightEncoder() > 4){
            robot.Drivetrain(0.2, 0.2);
            robot.waitForTick(55);
        }

        robot.STOP();
        robot.resetEncoders();

        if (opModeIsActive() && color == true){
            while (opModeIsActive() && robot.LWhiteLine.getRawLightDetected() < 1.5){
                robot.Drivetrain(0.5,-0.5);
                robot.waitForTick(25);
            }
        }else {
            while (opModeIsActive() && robot.RWhiteLine.getRawLightDetected() < 1.5){
                robot.Drivetrain(-0.5,0.5);
                robot.waitForTick(10);
            }
        }
        robot.STOP();
    }

    //Follow Line and Press Button

    public void HitButton(boolean color){

        robot.STOP();
        robot.resetEncoders();
     
        while (opModeIsActive() && robot.getLeftEncoderinInches() > 2 && robot.getRightEncoderinInches() > 2){
            robot.Drivetrain(0.1, 0.1);
            robot.waitForTick(25);
        }

        robot.STOP();

        if(opModeIsActive() && color == true){

            robot.Drivetrain(0.1,0.1);

            sleep(1050);

            if(robot.Color.red() < robot.Color.blue()){
                robot.LeftBumper.setPosition(0.8);
            }else {
                robot.RightBumper.setPosition(0.2);
            }
            sleep(1000);

            robot.Drivetrain(0,0);

        }else if(opModeIsActive()){

            robot.Drivetrain(0.1,0.1);

            sleep(1050);
         
            if(robot.Color.red() > robot.Color.blue()){
                robot.LeftBumper.setPosition(0.8);
            }else {
                robot.RightBumper.setPosition(0.2);
            }

            sleep(1000);

            robot.Drivetrain(0,0);
        }

        if(opModeIsActive()){
            robot.STOP();
            robot.resetEncoders();

            while (opModeIsActive() && robot.getLeftEncoderinInches() > 2 && robot.getRightEncoderinInches() > 2){
                robot.Drivetrain(0.1, 0.1);
                robot.waitForTick(25);
            }

            while (opModeIsActive() && robot.getLeftEncoderinInches() > 2 && robot.getRightEncoderinInches() > 2){
                robot.Drivetrain(0.1, 0.1);
                robot.waitForTick(25);

            }
            robot.STOP();
        }
    }

    public void updateData() {
        telemetry.addData("Lego Light Sensor", robot.light.getRawLightDetected());
        telemetry.addData("Right Speed", robot.RDrive1.getPower());
        telemetry.addData("Left Speed", robot.LDrive1.getPower());
        telemetry.addData("Right Encoder", robot.getRightEncoder());
        telemetry.addData("Left Encoder", robot.getLeftEncoder());
        telemetry.addData("Right Encoder pos", robot.RDrive1.getCurrentPosition());
        telemetry.addData("Left Encoder pos", robot.LDrive1.getCurrentPosition());
        telemetry.addData("Right Encoder in Inches", robot.getRightEncoderinInches());
        telemetry.addData("Left Encoder in Inches", robot.getLeftEncoderinInches());
        telemetry.addData("Gyro Z-axis", robot.gyro.getIntegratedZValue());
        telemetry.addData("Left ODS", robot.LWhiteLine.getRawLightDetected());
        telemetry.addData("Right ODS", robot.RWhiteLine.getRawLightDetected());

        telemetry.update();
    }

    public void EncoderDrive(double speed, int left, int right) {

        int newLeftTarget;
        int newRightTarget;

        if (opModeIsActive()) {

            newLeftTarget = robot.LDrive2.getCurrentPosition() + (int)(-left * robot.CountsPerInch);
            newRightTarget = robot.RDrive2.getCurrentPosition() + (int)(right * robot.CountsPerInch);

            if (newLeftTarget < 0 && newRightTarget < 0) {
                robot.Drivetrain(-Math.abs(speed),-Math.abs(speed));
                while (opModeIsActive() && newLeftTarget < robot.LDrive2.getCurrentPosition() && newRightTarget < robot.RDrive2.getCurrentPosition()) {
                    updateData();
                    sleep(4);
                    robot.waitForTick(50);
                }
            }else if (newLeftTarget > 0 && newRightTarget <0) {
                robot.Drivetrain(Math.abs(speed),-Math.abs(speed));
                while (opModeIsActive() && newLeftTarget > robot.LDrive2.getCurrentPosition() && newRightTarget < robot.RDrive2.getCurrentPosition()) {
                    updateData();
                    sleep(4);
                    robot.waitForTick(50);
                }
            }else if (newLeftTarget <0 && newRightTarget > 0) {
                robot.Drivetrain(-Math.abs(speed),Math.abs(speed));
                while (opModeIsActive() && newLeftTarget < robot.LDrive2.getCurrentPosition() && newRightTarget > robot.RDrive2.getCurrentPosition()) {
                    updateData();
                    sleep(4);
                    robot.waitForTick(50);
                }
            }else if (newLeftTarget > 0 && newRightTarget > 0) {
                robot.Drivetrain(Math.abs(speed),Math.abs(speed));
                while (opModeIsActive() && newLeftTarget > robot.LDrive2.getCurrentPosition() && newRightTarget > robot.RDrive2.getCurrentPosition()) {
                    updateData();
                    sleep(4);
                    robot.waitForTick(50);
                }
            }
            robot.STOP();
        }

    }
}
