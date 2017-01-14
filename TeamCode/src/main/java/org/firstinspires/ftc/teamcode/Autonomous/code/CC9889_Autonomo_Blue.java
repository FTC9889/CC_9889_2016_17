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

    int randomnumberthatweneedforsomething = 0;
    boolean breakout = false;
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
        }

        while (breakout == false) {
            if (gamepad1.dpad_up) {
                randomnumberthatweneedforsomething = 1;
            } else if (gamepad1.dpad_right) {
                randomnumberthatweneedforsomething = 2;
            } else if (gamepad1.dpad_down) {
                randomnumberthatweneedforsomething = 3;
            }else if (gamepad1.dpad_left) {
                randomnumberthatweneedforsomething = 0;
            }else if(gamepad1.a) {
                breakout = true;
            }

            telemetry.addData(">", "Gyro Calibrated. ¯\\_(ツ)_/¯");
            telemetry.addData("Autonomous Number", randomnumberthatweneedforsomething);
            telemetry.addData("Autonomous 0", "= 1 Beacon and Park on Ramp");
            telemetry.addData("Autonomous 1", "= 1 Beacon and Hit Cap Ball");
            telemetry.addData("Autonomous 2", "= 2 Beacon and Stop");
            telemetry.update();
        }

        telemetry.addData("Now running Autonomous #", randomnumberthatweneedforsomething);
        waitForStart();

        robot.STOP();

        if (randomnumberthatweneedforsomething == 3) {
            sleep(20000);
            EncoderDrive(0.7,-35,-35);
            robot.Flywheel(true);
            robot.resetEncoders();
            sleep(1200);
            robot.IntakeServo.setPower(-1.0);
            robot.Intake.setPower(1.0);
            sleep(2100);
            robot.IntakeServo.setPower(0.0);
            robot.Intake.setPower(0.0);
            robot.Flywheel(false);
            EncoderDrive(0.7,-20,-20);

        }else {

            //Robot drives forward 20 inches
            EncoderDrive(1.0, -20, -20);

            updateData();

            while (opModeIsActive() && robot.gyro.getIntegratedZValue() < 15) {
                robot.Drivetrain(-0.3, -0.3);
                robot.waitForTick(50);
            }

            robot.STOP();

            updateData();

            robot.Flywheel(true);
            sleep(1200);
            robot.IntakeServo.setPower(-1.0);
            robot.Intake.setPower(1.0);
            sleep(2100);
            robot.IntakeServo.setPower(0.0);
            robot.Intake.setPower(0.0);
            robot.Flywheel(false);

            //The robot drives up to the white line.
            robot.Drivetrain(0.3, 0.3);

            while (opModeIsActive() && robot.gyro.getIntegratedZValue() > -30) {
                sleep(4);
                robot.waitForTick(50);
            }

            robot.STOP();

            FindWhiteTape(0.7, false);
            HitButton(true);

            robot.STOP();

            if (randomnumberthatweneedforsomething == 1) {
                //1 Beacon and Hit Cap Ball
                robot.Drivetrain(-0.8, 0.8);

                sleep(1500);

                robot.STOP();

                robot.Drivetrain(0.3, -0.3);

                sleep(1500);

                robot.STOP();

                robot.Drivetrain(-0.3, 0.3);
                sleep(3000);
                robot.STOP();

            } else if (randomnumberthatweneedforsomething == 2) {
                //2 Beacon and Stop
                robot.Drivetrain(-0.1, -0.1);

                while (opModeIsActive() && robot.gyro.getIntegratedZValue() < 0) {
                    sleep(4);
                    robot.waitForTick(50);
                }
                robot.STOP();

                robot.Drivetrain(0.7, -0.7);
                sleep(1600);
                robot.STOP();

                while (opModeIsActive() && robot.light.getRawLightDetected() < 1.) {
                    sleep(4);
                    robot.waitForTick(50);
                }

                robot.STOP();

                robot.Drivetrain(0.4, 0.4);

                while (opModeIsActive() && robot.RWhiteLine.getRawLightDetected() < 1.5) {
                    sleep(4);
                    robot.waitForTick(50);
                }
                robot.STOP();

                HitButton(true);
            } else {
                //1 Beacon and Park on Ramp
                robot.Drivetrain(-0.1, -0.1);

                while (opModeIsActive() && robot.gyro.getIntegratedZValue() < 0) {
                    sleep(4);
                    robot.waitForTick(50);
                }
                robot.STOP();

                robot.Drivetrain(-0.8, 0.8);
                sleep(1000);
                robot.STOP();
            }
        }
        super.stop();
    }

    //Go to white line

    public void FindWhiteTape(double speed, boolean color){
        robot.Drivetrain(Math.abs(speed), -Math.abs(speed));
        sleep(1500);

        while (opModeIsActive() && robot.light.getRawLightDetected() < 1.8){
            sleep(4);
            robot.waitForTick(50);
        }

        robot.STOP();

        //The robot lines itself up with the white line.
        robot.Drivetrain(0.0, 0.4);

        if(color == false){
            while (opModeIsActive() && robot.RWhiteLine.getRawLightDetected() < 1.5){
                sleep(4);
                robot.waitForTick(50);
            }
        }else {
            while (opModeIsActive() && robot.LWhiteLine.getRawLightDetected() < 1.5){
                sleep(4);
                robot.waitForTick(50);
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
       sleep(700);

        robot.STOP();

        robot.Drivetrain(-0.3, 0.3);
        sleep(1000);
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
