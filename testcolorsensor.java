package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

@Autonomous(name="Color Sensor", group="Tests")
public class TestColorSensor extends LinearOpMode {
    // Define a variable for our color sensor
    ColorSensor color;
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        telemetry.addData("Status", "Ready to run");
        telemetry.update();
        // Get the color sensor from hardwareMap
        color = hardwareMap.get(ColorSensor.class, "Color1");
        // Wait for the Play button to be pressed
        waitForStart();
        //read sensor below (done)
        //go to parking spot depending on whats read after going forward for 1 second
        // While the Op Mode is running, update the telemetry values.
        while (opModeIsActive()) {
            telemetry.addData("Red", color.red());
            telemetry.addData("Green", color.green());
            telemetry.addData("Blue", color.blue());
            telemetry.addData("Alpha", color.alpha());
            telemetry.addData("ARGB", color.argb());
            telemetry.update();
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
