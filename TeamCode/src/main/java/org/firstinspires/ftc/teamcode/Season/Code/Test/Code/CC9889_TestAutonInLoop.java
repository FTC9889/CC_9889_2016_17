package org.firstinspires.ftc.teamcode.Season.Code.Test.Code;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Season.Code.Subsystems.Beacon;
import org.firstinspires.ftc.teamcode.Season.Code.Subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.Season.Code.Subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.Season.Code.Subsystems.waitForTick;
import org.firstinspires.ftc.teamcode.Season.Code.Subsystems.*;


/**
 * Created by joshua on 3/3/17.
 */
@Disabled
public class CC9889_TestAutonInLoop extends OpMode {

    private int AutonStep = 0;
    private int randomnumberthatweneedforsomething = 1;
    private ElapsedTime runtime               = new ElapsedTime();
    private ElapsedTime shot                  =new ElapsedTime();


    boolean SmartShot = false;

    double leftspeed, rightspeed, xvalue, yvalue;
    int div = 1;

    Flywheel Flywheel_Intake          = new Flywheel();
    Drivebase Drivetrain              = new Drivebase();
    Beacon Beacon                     = new Beacon();
    waitForTick waitForTick           = new waitForTick();
    LED_Control led_control           = new LED_Control();

    @Override
    public void init(){
        // Init Hardwawre
        // note: waitForTick does not have a hardware map
        Beacon.init(hardwareMap);
        Flywheel_Intake.init(hardwareMap);
        Drivetrain.init(hardwareMap, false);
        led_control.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Robot", " Running");    //
        telemetry.update();

        Drivetrain.STOP();
    }

    @Override
    public void init_loop(){
        if (gamepad1.dpad_up) {
            telemetry.clearAll();
            randomnumberthatweneedforsomething = 1;
            telemetry.addData("Autonomous 1", "= Shoot and Park on Center");
        } else if(gamepad1.dpad_right){
            telemetry.clearAll();
            randomnumberthatweneedforsomething = 2;
            telemetry.addData("Autonomous 2", "= 2 Beacon");
        }else if (gamepad1.dpad_down) {
            telemetry.clearAll();
            randomnumberthatweneedforsomething = 3;
            telemetry.addData("Autonomous 3","= 1  Beacon and Ramp");
        }else if(gamepad1.dpad_left) {
            telemetry.clearAll();
            randomnumberthatweneedforsomething = 4;
            telemetry.addData("Autonomous 4", "= 1 Beacon and Hit Cap Ball");
        }
        telemetry.update();
    }

    @Override
    public void start(){
        runtime.reset();
    }

    @Override
    public void loop(){
        switch (AutonStep){
            case 0:
                if(runtime.seconds() == 20){
                    AutonStep = 1;
                    runtime.reset();
                }
                break;
            case 1:
                Flywheel_Intake.setFlywheel(true);
                AutonStep = 2;
                break;
            case 2:
                Drivetrain.setLeftRightPower(-0.6, -0.6);
                if(Drivetrain.InchesAreWeThereYet(35)){
                    AutonStep = 3;
                    Drivetrain.STOP();
                }
                break;
            case 3:
                if(runtime.milliseconds() == 500){
                    AutonStep = 4;
                    runtime.reset();
                    Drivetrain.resetEncoders();
                }
                break;
            case 4:
                Flywheel_Intake.setIntakeMode(1);
                AutonStep = 5;
                break;
            case 5:
                if(runtime.seconds() == 2){
                    AutonStep = 6;
                    runtime.reset();
                }
                break;
            case 6:
                Flywheel_Intake.setIntakeMode(0);
                Flywheel_Intake.setFlywheel(false);
                AutonStep = 7;
                break;
            case 7:
                Drivetrain.setLeftRightPower(-0.6, -0.6);
                if(Drivetrain.InchesAreWeThereYet(35)){
                    Drivetrain.STOP();
                    Drivetrain.resetEncoders();

                }
        }
    }
}
