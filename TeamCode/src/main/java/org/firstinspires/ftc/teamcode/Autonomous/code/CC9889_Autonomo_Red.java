package org.firstinspires.ftc.teamcode.Autonomous.Code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
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
@Autonomous(name="AutoRed", group="Blue")
public class CC9889_Autonomo_Blue extends LinearOpMode {

    //DcMotors
    DcMotor leftDrive;
    DcMotor rightDrive;
    DcMotor leftShoot;
    DcMotor rightShoot;

    //Servos
    CRServo Color;
    Servo Shoot;
    Servo touchservo;

    //Sensors
    ModernRoboticsI2cGyro gyro;
    ColorSensor colorSensor;
    ModernRoboticsI2cRangeSensor rangeSensor;
    OpticalDistanceSensor ods;
    TouchSensor touchtouch;

    //Core Device Interface
    DeviceInterfaceModule CDI;

    //DcMotor Encoders
    static final double EncoderCounts=1120;
    static final double WheelDiameter=4.0;
    static final double CountsPerInch=EncoderCounts/(WheelDiameter*3.1416);
    static final double DriveSpeed=0.7;
    static final double TurnSpeed=0.1;

    //Flywheel
    double Flywheelnum = 0.0;

    private ElapsedTime runtime=new ElapsedTime();

    @Override
    public void runOpMode () {

        setup();

        LED();//LED() is used to show where in the program we are.

        idle();

        waitForStart();

        Flywheel(1);

        sleep(1000);

        shoot();

        Flywheel(0);
        Flywheel(0);
        Flywheel(0);
        Flywheel(0);

        encoderDrive(0.5, true ,18,18, 100);//Go forward 16 inches

        LED();

        encoderDrive(0.1, true, -7, 7, 100);

        FindWhiteTape(0.4, false);

        TouchSignal(0.2, false);

        HitBeacon(1);

        leftDrive.setPower(-1.0);
        rightDrive.setPower(-1.0);
        sleep(1500);
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);

        super.stop();
    }

    //Functions

    //Setup
    public void setup(){
        //Drive Motors
        leftDrive =hardwareMap.dcMotor.get("leftDrive");
        rightDrive = hardwareMap.dcMotor.get("rightDrive");

        //Shooter Motors
        leftShoot = hardwareMap.dcMotor.get("leftShoot");
        rightShoot = hardwareMap.dcMotor.get("rightShoot");

        //Servos
        Color = hardwareMap.crservo.get("colorServo"); //This is crservo
        Shoot = hardwareMap.servo.get("ServoShoot");
        touchservo = hardwareMap.servo.get("touchservo");

        //Sensors
        gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");
        colorSensor = hardwareMap.colorSensor.get("color");
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "sensor_range");
        touchtouch = hardwareMap.touchSensor.get("touch");
        ods = hardwareMap.opticalDistanceSensor.get("ods");

        //Core Device Interface
        CDI = hardwareMap.deviceInterfaceModule.get("Device Interface Module 1");

        //Tweaks to the hardware #Linsanity
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightShoot.setDirection(DcMotorSimple.Direction.REVERSE);

        //Drive Mode
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);

        //Drive Wheels no power
        leftDrive.setPowerFloat();
        rightDrive.setPowerFloat();

        //Flywheel no power
        leftShoot.setPowerFloat();
        rightDrive.setPowerFloat();

        //Servo Movement
        Shoot.setPosition(0.2);
        Color.setPower(0.0);
        // start calibrating the gyro.
        /**telemetry.addData(">", "Gyro Calibrating. Do Not move! (Please or Josh will keel you.)");
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
            newLeftTarget = leftDrive.getCurrentPosition() + (int)(leftInches * CountsPerInch);
            newRightTarget = rightDrive.getCurrentPosition() + (int)(rightInches * CountsPerInch);
            leftDrive.setTargetPosition(newLeftTarget);
            rightDrive.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();

            leftDrive.setPower(Math.abs(speed));
            rightDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() && (runtime.seconds() < timeoutS) && (leftDrive.isBusy() && rightDrive.isBusy())) {

                leftDrive.setPower(speed);
                rightDrive.setPower(speed);

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d", leftDrive.getCurrentPosition(), rightDrive.getCurrentPosition());
                telemetry.update();
                idle();

            }
            if(quickstop == true){
                leftDrive.setPower(speed);
                rightDrive.setPower(speed);
                sleep(500);
            }
            // Stop all motion;
            leftDrive.setPower(0);
            rightDrive.setPower(0);

            sleep(timeoutS);   // optional pause after each move
            leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
            rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        }
    }

    //Gyro
    public void gyroTurn(double speed, double degrees){
        sleep(500);
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        gyro.resetZAxisIntegrator();
        while (opModeIsActive() && gyro.getHeading() > degrees){
            leftDrive.setPower(speed/2);
            rightDrive.setPower(-speed/2);
        }
        while (opModeIsActive() && gyro.getHeading() < degrees){
            leftDrive.setPower(-speed/2);
            rightDrive.setPower(speed/2);
        }
        while (opModeIsActive() && gyro.getHeading() > degrees){
            leftDrive.setPower(speed/2.5);
            rightDrive.setPower(-speed/2.5);
        }
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);
        sleep(500);
    }

    //Touch
    public void TouchSignal(double speed, boolean color){
        Color.setPower(0.0);
        Shoot.setPosition(0.2);
        touchservo.setPosition(0.45);
        runtime.reset();
        while(opModeIsActive()&& touchtouch.isPressed() == false && runtime.seconds() < 3){
            leftDrive.setPower(speed);
            rightDrive.setPower(speed);
            idle();
        }
        while(opModeIsActive() && touchtouch.isPressed() == true){
            leftDrive.setPower(-speed);
            rightDrive.setPower(-speed);
            idle();
        }
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);

        touchservo.setPosition(0.0);

        encoderDrive(0.7,true, -0.5,-0.5,50);
        sleep(500);
        encoderDrive(0.5, true, 2,2, 50);

    }

    //Command to find the color of the right side of beacon and press the correct button
    public void HitBeacon(int color){

        Color.setPower(-1.0);
        sleep(900);
        Color.setPower(0.0);
        sleep(500);
        if(FindColor() == color){
            Color.setPower(1.0);
            sleep(1000);
            Color.setPower(0.0);
            rightDrive.setPower(1.0);
            leftDrive.setPower(0.0);
        }else {
            Color.setPower(1.0);
            sleep(1000);
            Color.setPower(0.0);
            rightDrive.setPower(0.0);
            leftDrive.setPower(1.0);
        }
        sleep(800);
        rightDrive.setPower(0.0);
        leftDrive.setPower(0.0);
        encoderDrive(1.0,false, -5,-5,500);
    }

    //Go to white line
    public void FindWhiteTape(double speed, boolean color){
        while (opModeIsActive() && ods.getRawLightDetected() < 1.5){
            leftDrive.setPower(speed);
            rightDrive.setPower(speed);
        }
        while (opModeIsActive() && ods.getRawLightDetected() > 1.5){
            leftDrive.setPower(speed);
            rightDrive.setPower(speed);
        }
        leftDrive.setPower(-1.0);
        rightDrive.setPower(-1.0);
        sleep(100);
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);

        sleep(500);
        encoderDrive(0.5, false, 2, 2, 50);
        if (opModeIsActive() && color == true){
            while (opModeIsActive() && ods.getRawLightDetected() < 1.5){
                leftDrive.setPower(0.2);
                rightDrive.setPower(-0.2);
            }
        }else if(opModeIsActive() && color == false){
            while (opModeIsActive() && ods.getRawLightDetected() < 1.5){
                leftDrive.setPower(-0.2);
                rightDrive.setPower(0.2);
            }
        }
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);
        sleep(500);
    }

    //Drive to the white line in front of the beacon
    public void WhiteTape (boolean color){
        if (opModeIsActive() && color == false){
            while (opModeIsActive() && ods.getRawLightDetected() < 2.0){
                leftDrive.setPower(0.1);
                rightDrive.setPower(-0.1);
            }
        }else if(opModeIsActive() && color == true){
            while (opModeIsActive() && ods.getRawLightDetected() < 2.0){
                leftDrive.setPower(-0.1);
                rightDrive.setPower(0.1);
            }
        }
        if (opModeIsActive() && color == true){
            while (opModeIsActive() && ods.getRawLightDetected() < 2.0){
                leftDrive.setPower(0.1);
                rightDrive.setPower(-0.1);
            }
        }else if(opModeIsActive() && color == false){
            while (opModeIsActive() && ods.getRawLightDetected() < 2.0){
                leftDrive.setPower(-0.1);
                rightDrive.setPower(0.1);
            }
        }

        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);
        leftDrive.setPowerFloat();
        rightDrive.setPowerFloat();


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

    //Popper
    public void shoot(){
        Shoot.setPosition(0.6);
        sleep(1000);
        Shoot.setPosition(0.2);
    }

    //Return Color
    //If output is 0, Red is on left. If output is 1, Red is on right
    public int FindColor(){
        colorSensor.enableLed(false);
        if(colorSensor.blue() > colorSensor.red()){
            return 0;
        }else {
            return 1;
        }
    }

    //Led Indicators
    public void LED(){
        int ledstate = 0;

        if (ledstate == 0){
            CDI.setLED(1, true);
            CDI.setLED(0, false);
            ledstate = 1;
        }else {
            CDI.setLED(1, false);
            CDI.setLED(0, true);
            ledstate = 0;
        }
    }
}
