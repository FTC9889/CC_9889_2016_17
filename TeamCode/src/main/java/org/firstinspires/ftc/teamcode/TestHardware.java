package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.Beacon;
import org.firstinspires.ftc.teamcode.Subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.Subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.Subsystems.LED_Control;
import org.firstinspires.ftc.teamcode.Subsystems.waitForTick;

/**
 * Created by Joshua H on 2/21/2017.
 */
@TeleOp(name="Teleop%", group="Teleop")
@Disabled
public class TestHardware extends LinearOpMode {
    /* Declare OpMode members. */
    private Flywheel Flywheel_Intake          = new Flywheel();
    private Drivebase Drivetrain              = new Drivebase();
    private org.firstinspires.ftc.teamcode.Subsystems.Beacon Beacon                     = new Beacon();
    private org.firstinspires.ftc.teamcode.Subsystems.waitForTick waitForTick           = new waitForTick();
    private LED_Control led_control           = new LED_Control();
    private ElapsedTime runtime               = new ElapsedTime();

    @Override
    public void runOpMode(){
        Beacon.init(hardwareMap);
        Flywheel_Intake.init(hardwareMap);
        Drivetrain.init(hardwareMap);
        led_control.init(hardwareMap);

        waitForStart();
        runtime.reset();

        Flywheel_Intake.AutoShoot(true, false);

        sleep(1000);

        while (opModeIsActive() && runtime.seconds()<120){
            Flywheel_Intake.AutoShoot(true, true);
        }
        super.stop();
    }
}
