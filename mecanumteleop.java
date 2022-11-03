package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="Mecanum: Test Linear OpMode", group="Linear Opmode")
public class MecanumTeleOp extends LinearOpMode {
    private boolean buttonPreviousState;
    public boolean buttonClick (boolean button) {
        boolean returnVal;
        returnVal = button && !buttonPreviousState;
        buttonPreviousState = button;
        return returnVal;
    }
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("top_left");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("back_left");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("top_right");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("back_right");
        DcMotor motorArm = hardwareMap.dcMotor.get("arm_motor");
        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = gamepad1.left_stick_y; // Remember, this is reversed!
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;
            double ax = gamepad2.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = ((y + x + rx) / denominator);
            double backLeftPower = ((y - x + rx) / denominator);
            double frontRightPower = ((y - x - rx) / denominator);
            double backRightPower = ((y + x - rx) / denominator);
            double armPower = ax;
            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);
            motorArm.setPower(ax);
        }
    }
}
