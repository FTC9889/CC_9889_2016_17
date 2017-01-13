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

        robot.Drivetrain(-0.3, -0.3);

        while (opModeIsActive() && robot.gyro.getIntegratedZValue() < 15){
            robot.waitForTick(50);
            updateData();
        }

        robot.STOP();

        robot.Flywheel(true);
        sleep(1200);
        robot.IntakeServo.setPower(-1.0);
        robot.Intake.setPower(1.0);
        sleep(2500);
        robot.IntakeServo.setPower(0.0);
        robot.Intake.setPower(0.0);
        robot.Flywheel(false);

        //The robot drives up to the white line.
        robot.Drivetrain(0.3, 0.3);

        while (opModeIsActive() && robot.gyro.getIntegratedZValue() > -30){
            robot.waitForTick(50);
            updateData();
        }

        robot.STOP();

        FindWhiteTape(0.7, false);

        HitButton(true);

        robot.Drivetrain(-0.3,0.3);

        sleep(1000);
        robot.STOP();
        robot.Drivetrain(-0.3, -0.3);

        while (opModeIsActive() && robot.gyro.getIntegratedZValue() < 0){
            robot.waitForTick(50);
            updateData();
        }

        robot.STOP();

        robot.Drivetrain(0.1, 0.1);

        while (opModeIsActive() && robot.gyro.getIntegratedZValue() > 0){
            robot.waitForTick(50);
            updateData();
        }

        robot.STOP();

        robot.Drivetrain(-0.1, -0.1);

        while (opModeIsActive() && robot.gyro.getIntegratedZValue() < 0){
            robot.waitForTick(50);
            updateData();
        }

        robot.STOP();

        robot.Drivetrain(0.7,0.7);
        sleep(1000);

        FindWhiteTape(0.7,false);
        HitButton(true);

        super.stop();
    }

    //Go to white line

    public void FindWhiteTape(double speed, boolean color){
        robot.Drivetrain(Math.abs(speed), -Math.abs(speed));

        while (opModeIsActive() && robot.light.getRawLightDetected() < 1.8){
            robot.waitForTick(10);
            updateData();
        }

        robot.STOP();

        //The robot lines itself up with the white line.
        robot.Drivetrain(0.0, 0.4);

        if(color == false){
            while (opModeIsActive() && robot.RWhiteLine.getRawLightDetected() < 1.5){
                robot.waitForTick(50);
                updateData();
            }
        }else {
            while (opModeIsActive() && robot.LWhiteLine.getRawLightDetected() < 1.5){
                robot.waitForTick(50);
                updateData();
            }
        }

        robot.STOP();

    }

    //Follow Line and Press Button

    public void HitButton(boolean color){
        //Here the robot decides which beacon button to press.
        robot.BumperControl(false);
        robot.Drivetrain(0.3,-0.3);
        sleep(1000);
        if(color == true){
            if (robot.Color.red() > robot.Color.blue()){
                robot.RightBumper.setPosition(0.1);
            }else {
                robot.LeftBumper.setPosition(0.9);
            }
        }else {
            if (robot.Color.red() < robot.Color.blue()){
                robot.RightBumper.setPosition(0.1);
            }else {
                robot.LeftBumper.setPosition(0.9);
            }
        }
       sleep(500);

        robot.STOP();

        robot.BumperControl(true);

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
