package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name="Autonomous OpMode (Direct Drive (DD)) - Park ONLY with encoders", group="Production")
public class autotest1 extends LinearOpMode {
    // Define a variable for our color sensor
    ColorSensor color;
    DcMotor tLeft;
    DcMotor tRight;
    DcMotor bLeft;
    DcMotor bRight;
    double FWDSPD = 1;
    double TRNSPD = 0.25;
    double STRSPD = 0.3;
    double ENCSPD = 0.3;

    @Override
    public void runOpMode() {
        mecanumcontrol motor = new mecanumcontrol();
        tLeft  = hardwareMap.dcMotor.get("top_left");
        tRight = hardwareMap.dcMotor.get("top_right");
        bLeft = hardwareMap.dcMotor.get("back_left");
        bRight = hardwareMap.dcMotor.get("back_right");
        init();
        // Get the color sensor from hardwareMap
        color = hardwareMap.get(ColorSensor.class, "Color1");
        // Wait for the Play button to be pressed
        waitForStart();
        motor.enableEncoders();
        // Go forward for 2.5 seconds
        motor.target(1000, 1000, 1000, 1000);
        motor.forward(ENCSPD);
        // Read sensor below (done)
        // Go to parking spot depending on whats read after going forward for 2.5 seconds
        while (opModeIsActive()) {
            if(color.red() > color.blue() && color.red() > color.green())// If RED is greater than all other colors
            {
                telemetry.addData("Park: ", "red 1");
                telemetry.update();
                sleep(1000);
                motor.strafeLeft(STRSPD);
                sleep(2600);
                motor.stop();
                break;

            }
            if(color.green() > color.blue() && color.green() > color.red())// If GREEN is greater than all other colors
            {
                telemetry.addData("Park: ", "green 2");
                telemetry.update();
                break;
            }
            if(color.blue() > color.red() && color.blue() > color.green())// If BLUE is greater than all other colors
            {
                telemetry.addData("Park: ", "blue 3");
                telemetry.update();
                sleep(1000);
                motor.strafeRight(STRSPD);
                sleep(2600);
                motor.stop();
                break;
            }


            /*
            Cone colors:
            1. Red
            2. Blue
            3. Green
            ----->The colors have changed per the conditions of the sensor.<-----
            (The sensor is more consistent if we use red, blue, and green)
            */
        }

    }
}