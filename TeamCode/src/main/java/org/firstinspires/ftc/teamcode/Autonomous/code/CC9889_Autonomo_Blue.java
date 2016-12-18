package org.firstinspires.ftc.teamcode.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static com.qualcomm.robotcore.util.Range.clip;

/**
 * Created by Jin on 9/30/2016. #WeGonRideWeGonWin #ObieDidHarambe
 */
@Autonomous(name="AutoBlue", group="Red")
public class CC9889_Autonomo_Blue extends LinearOpMode {

    //Motors
    DcMotor rightShoot;
    DcMotor leftShoot;
    DcMotor RDrive;
    DcMotor LDrive;

    //Servos
    Servo RightBumper;
    Servo LeftBumper;

    //Sensors
    OpticalDistanceSensor RWhiteLine;
    OpticalDistanceSensor LWhiteLine;
    ColorSensor Color;
    GyroSensor Gyro;

    //DcMotor Encoders
    static final double EncoderCounts=1120;
    static final double WheelDiameter=4.0;
    static final double CountsPerInch=EncoderCounts/(WheelDiameter*3.1416);
    static final double DriveSpeed=0.7;
    static final double TurnSpeed=0.1;

    //Flywheel
    double Flywheelnum = 0.0;

    private ElapsedTime runtime=new ElapsedTime();
    private ElapsedTime period  = new ElapsedTime();

    @Override
    public void runOpMode () {

        setup();

        idle();

        waitForStart();

        encoderDrive(0.5, true , 18, 18, 100);//Go forward 18 inches

        super.stop();
    }

    //Functions

    //Setup
    public void setup(){
        //Drive Motors
        LDrive =hardwareMap.dcMotor.get("leftdrive");
        RDrive = hardwareMap.dcMotor.get("rightdrive");

        //Shooter Motors
        leftShoot = hardwareMap.dcMotor.get("flywheelleft");
        rightShoot = hardwareMap.dcMotor.get("flywheelright");

        //Servos
        RightBumper = hardwareMap.servo.get("r");
        LeftBumper = hardwareMap.servo.get("l");

        //Sensors
        Gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");
        Color = hardwareMap.colorSensor.get("colorsensor");
        RWhiteLine = hardwareMap.opticalDistanceSensor.get("rods");
        LWhiteLine = hardwareMap.opticalDistanceSensor.get("lods");

        //Tweaks to the hardware #Linsanity
        LDrive.setDirection(DcMotor.Direction.REVERSE);
        rightShoot.setDirection(DcMotorSimple.Direction.REVERSE);

        //Drive Mode
        LDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        RDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);

        //Drive Wheels no power
        LDrive.setPowerFloat();
        RDrive.setPowerFloat();

        //Flywheel no power
        leftShoot.setPowerFloat();
        rightShoot.setPowerFloat();

        leftShoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightShoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Servo Movement
        LeftBumper.setPosition(0.2);
        RightBumper.setPosition(0.2);
        // start calibrating the gyro.
        /**
        telemetry.addData(">", "Gyro Calibrating. Do Not move! (Please or Josh will keel you.)");
        telemetry.update();
       gyro.calibrate();

        // make sure the gyro is calibrated.
        while (!isStopRequested() && gyro.isCalibrating())  {
            sleep(50);
            idle();
        }
        gyro.resetZAxisIntegrator();
        //Message About Gyro
        telemetry.addData(">", "Gyro Calibrated. Wooooooooooooooo");
        telemetry.update();
         **/
    }

    //Encoders
    public void encoderDrive (double speed, boolean quickstop, double leftInches, double rightInches, long timeoutS){
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = LDrive.getCurrentPosition() + (int)(leftInches * CountsPerInch);
            newRightTarget = RDrive.getCurrentPosition() + (int)(rightInches * CountsPerInch);
            LDrive.setTargetPosition(newLeftTarget);
            RDrive.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            LDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();

            LDrive.setPower(Math.abs(speed));
            RDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() && (runtime.seconds() < timeoutS) && (LDrive.isBusy() && RDrive.isBusy())) {

                LDrive.setPower(speed);
                RDrive.setPower(speed);

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d", LDrive.getCurrentPosition(), RDrive.getCurrentPosition());
                telemetry.update();
                idle();

            }
            if(quickstop == true){
                LDrive.setPower(speed);
                RDrive.setPower(speed);
                sleep(500);
            }
            // Stop all motion;
            LDrive.setPower(0);
            RDrive.setPower(0);

            sleep(timeoutS);   // optional pause after each move
            LDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
            RDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        }
    }

    //Gyro
    public void gyroTurn(double speed, double degrees){
        sleep(500);
        LDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Gyro.resetZAxisIntegrator();
        while (opModeIsActive() && Gyro.getHeading() < degrees){
                LDrive.setPower(speed/2);
                RDrive.setPower(-speed/2);
        }
        while (opModeIsActive() && Gyro.getHeading() > degrees){
            LDrive.setPower(-speed/2);
            RDrive.setPower(speed/2);
        }
        while (opModeIsActive() && Gyro.getHeading() < degrees){
            LDrive.setPower(speed/2.5);
            RDrive.setPower(-speed/2.5);
        }
        LDrive.setPower(0.0);
        RDrive.setPower(0.0);
        sleep(500);
    }

    //Command to find the color of the right side of beacon and press the correct button
    public void HitBeacon(int color){

        sleep(500);
        if(1 == color){

            sleep(1000);

            RDrive.setPower(1.0);
            LDrive.setPower(0.0);
        }else {

            sleep(1000);

            RDrive.setPower(0.0);
            LDrive.setPower(1.0);
        }
        sleep(800);
        RDrive.setPower(0.0);
        LDrive.setPower(0.0);
        encoderDrive(1.0,false, -5,-5,500);
    }

    //Go to white line
    public void FindWhiteTape(double speed, boolean color){
        while (opModeIsActive() && RWhiteLine.getRawLightDetected() < 1.5){
            LDrive.setPower(speed);
            RDrive.setPower(speed);
        }
        while (opModeIsActive() && RWhiteLine.getRawLightDetected() > 1.5){
            LDrive.setPower(speed);
            RDrive.setPower(speed);
        }
        LDrive.setPower(-1.0);
        RDrive.setPower(-1.0);
        sleep(100);
        LDrive.setPower(0.0);
        RDrive.setPower(0.0);

        sleep(500);
        encoderDrive(0.5, false, 2, 2, 50);
        if (opModeIsActive() && color == true){
            while (opModeIsActive() && RWhiteLine.getRawLightDetected() < 1.5){
                LDrive.setPower(0.2);
                RDrive.setPower(-0.2);
            }
        }else if(opModeIsActive() && color == false){
            while (opModeIsActive() && RWhiteLine.getRawLightDetected() < 1.5){
                LDrive.setPower(-0.2);
                RDrive.setPower(0.2);
            }
        }
        LDrive.setPower(0.0);
        RDrive.setPower(0.0);
        sleep(1000);
    }

    //Flywheel Control
    public void Flywheel(int Position){
        if(Position == 0){
            Flywheelnum = 0.0;
            leftShoot.setPower(Flywheelnum);
            rightShoot.setPower(Flywheelnum);
        }else {
            Flywheelnum = 1.0;
            leftShoot.setPower(Flywheelnum);
            rightShoot.setPower(Flywheelnum);
            sleep(500);
            if(Position == 1){
                Flywheelnum = 0.34;
            }else if(Position == 2){
                Flywheelnum = 0.60;
            }else if(Position == 4){
                Flywheelnum = 0.8;
            }else {
                Flywheelnum = 0.0;
            }
            leftShoot.setPower(Flywheelnum);
            rightShoot.setPower(Flywheelnum);
        }
    }

    public void waitForTick(long periodMs) {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}
