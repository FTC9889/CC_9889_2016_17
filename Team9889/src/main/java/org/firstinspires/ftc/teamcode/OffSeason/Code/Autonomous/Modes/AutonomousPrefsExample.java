package org.firstinspires.ftc.teamcode.OffSeason.Code.Autonomous.Modes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.OffSeason.Code.OpMode.Team9889LinearOpMode;

@Autonomous(name = "Offseason Autonomous Prefs", group = "TeleOp")
//@Disabled
public class AutonomousPrefsExample extends Team9889LinearOpMode
{
    @Override
    public void runOpMode()
    {
        getAutonomousPrefs(this);
        //Wait for the start button to be pressed.
        waitForStart();

        while (opModeIsActive())
        {
            //Do stuff
        }
    }


}