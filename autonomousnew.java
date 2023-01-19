package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Autonomous OpMode (Park and Place)", group="Production")
public class autonomousnew extends LinearOpMode {
    ColorSensor color;
    DcMotor tLeft;
    DcMotor tRight;
    DcMotor bLeft;
    DcMotor bRight;
    Servo motorGrab;
    String colorDetected = "";
    double FWDSPD = 0.5;
    double TRNSPD = 0.25;
    double STRSPD = 0.3;
    double ENCSPD = 0.3;

    @Override
    public void runOpMode() {
        // CARSON THERE IS A CLASS FOR AUTONOMOUS MOTOR CONTROL!!! IN FACT, THERE'S TWO OF THEM!
        mecanumcontrol motor = new mecanumcontrol();
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        tLeft = hardwareMap.dcMotor.get("top_left");
        tRight = hardwareMap.dcMotor.get("top_right");
        bLeft = hardwareMap.dcMotor.get("back_left");
        bRight = hardwareMap.dcMotor.get("back_right");
        color = hardwareMap.get(ColorSensor.class, "Color1");
        DcMotor motorExtension = hardwareMap.dcMotor.get("motor_up");
        motorGrab = hardwareMap.servo.get("servo");
        motor.init();
        color = hardwareMap.get(ColorSensor.class, "Color1");
        telemetry.addData("Status", "Ready to run");
        telemetry.update();
        waitForStart();
        // Go forward for 2.5 seconds
        motor.forward(FWDSPD);
        motorExtension.setPower(1.0);
        sleep(500);
        motorExtension.setPower(0.0);
        sleep(1000);
        motor.stop();
        sleep(1000);
        while(opModeIsActive()) {
            // This entire function does nothing, as "colorDetected" is set to "" at the top of the code
            if (color.red() > color.blue() && color.red() > color.green())// If RED is greater than all other colors
            {
                telemetry.addData("Red: ", color.red());
                telemetry.update();
                colorDetected = "red";


            }
            else if (color.green() > color.blue() && color.green() > color.red())// If GREEN is greater than all other colors
            {
                telemetry.addData("green", color.green());
                telemetry.update();
                colorDetected = "green";

            }
            else if (color.blue() > color.red() && color.blue() > color.green())// If BLUE is greater than all other colors
            {
                telemetry.addData("Blue:", color.blue());
                telemetry.update();
                colorDetected = "blue";

            }
            motorExtension.setPower(1.0);
            sleep(2500);
            motorExtension.setPower(0.0);
            motor.turnRight(STRSPD);
            sleep(200);
            motor.stop();
            break;
        }
    }
}
