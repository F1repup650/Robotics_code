package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Mecanum: Test Linear OpMode 2 Player", group="Production")
public class mecanumteleop2player extends LinearOpMode {
    private boolean buttonPreviousState;
    Servo motorGrab;  // Eren said to do this to get the servo to work
    public boolean buttonClick (boolean button) {
        boolean returnVal;
        returnVal = button && !buttonPreviousState;
        buttonPreviousState = button;
        return returnVal;
    }
    double bl = 0.35678;
    double temp1;
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("top_left");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("back_left");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("top_right");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("back_right");
        DcMotor motorExtension = hardwareMap.dcMotor.get("motor_up");
        motorGrab = hardwareMap.servo.get("servo");
        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = gamepad1.left_stick_y * -1; // Remember, this is reversed!
            double rx = gamepad1.left_stick_x * -1; // Counteract imperfect strafing
            double x = gamepad1.right_stick_x * -1;
            boolean rb = gamepad2.right_bumper;
            boolean lb = gamepad1.left_bumper;
            double rt = gamepad2.right_trigger;
            double lt = gamepad2.left_trigger;

            int lbp = 2;
            double rbp = 0.30;
            if(buttonClick(lb)) {
                lbp -= 1.5;
            }
            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), lbp);
            double frontLeftPower = ((y + x + rx) / denominator);
            double backLeftPower = ((y - x + rx) / denominator);
            double frontRightPower = ((y - x - rx) / denominator);
            double backRightPower = ((y + x - rx) / denominator);
            double tt = (lt * -1) + rt;

            if(buttonClick(rb)) {
                rbp -= 0.21;
            }
            motorGrab.setPosition(rbp);


            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);
            motorExtension.setPower(tt);
        }
    }
}
