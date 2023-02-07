package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Autonomous OpMode Park ONLY - OLD", group="Production")
public class autonomousnewold extends LinearOpMode {
    ColorSensor color;
    DcMotor tLeft;
    DcMotor tRight;
    DcMotor bLeft;
    DcMotor bRight;
    DcMotor mArm;
    Servo motorGrab;
    String colorDet = "";
    double FWDSPD = 0.35;
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
        motor.tLeft = tLeft;
        tRight = hardwareMap.dcMotor.get("top_right");
        motor.tRight = tRight;
        bLeft = hardwareMap.dcMotor.get("back_left");
        motor.bLeft = bLeft;
        bRight = hardwareMap.dcMotor.get("back_right");
        motor.bRight = bRight;
        color = hardwareMap.get(ColorSensor.class, "Color1");
        mArm = hardwareMap.dcMotor.get("motor_up");
        motor.mArm = mArm;
        motorGrab = hardwareMap.servo.get("servo");
        motor.init();
        telemetry.addData("Status", "Ready to run");
        telemetry.update();
        motor.disableEncoders();
        waitForStart();
        //---------------Motor_Stuff---------------
        motor.forward(FWDSPD);
        mArm.setPower(1.0);
        sleep(500);
        mArm.setPower(0.0);
        sleep(1000);
        motor.stop();
        sleep(1000);
            colorDet = colorCheck();
            if(colorDet.equals("red")) {
                telemetry.addData("Red: ", color.red());
                telemetry.update();
                sleep(1000);
                tLeft.setPower(STRSPD);
                tRight.setPower(-STRSPD);
                bLeft.setPower(-STRSPD);
                bRight.setPower(STRSPD);
                sleep(1000);
                motor.stop();
                motor.forward(STRSPD);
                sleep(1200);
                motor.stop();
            }
            else if(colorDet.equals("blue")) {
                telemetry.addData("Blue:", color.blue());
                telemetry.update();
                sleep(1000);
                tLeft.setPower(-STRSPD);
                tRight.setPower(STRSPD);
                bLeft.setPower(STRSPD);
                bRight.setPower(-STRSPD);
                sleep(1100);
                motor.stop();
                motor.forward(STRSPD);
                sleep(1200);
                motor.stop();
            }


    }
    public String colorCheck() {
        if (color.red() > color.blue() && color.red() > color.green())// If RED is greater than all other colors
        {
            return "red";
        }
        else if (color.green() > color.blue() && color.green() > color.red())// If GREEN is greater than all other colors
        {
            return "green";
        }
        else if (color.blue() > color.red() && color.blue() > color.green())// If BLUE is greater than all other colors
        {
            return "blue";
        } else {
            return "NaN";
        }
    }
}