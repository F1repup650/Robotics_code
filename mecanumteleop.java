package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
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
        DcMotor motorExtension = hardwareMap.dcMotor.get("motor_up");
        CRServo claw = hardwareMap.crservo.get("Claw");
        CRServo wrist = hardwareMap.crservo.get("Wrist");
        double clawPower;
        double motorEx;
        double wristPower;

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
            double rxs = gamepad2.right_trigger;// motor extension
            double rxa = gamepad2.left_trigger * -1;// motor extension
            double wcx = gamepad2.right_stick_y;// wrist rotation
            boolean ba = gamepad2.a;
            boolean bb = gamepad2.b;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 2);
            double frontLeftPower = ((y + x + rx) / denominator);
            double backLeftPower = ((y - x + rx) / denominator);
            double frontRightPower = ((y - x - rx) / denominator);
            double backRightPower = ((y + x - rx) / denominator);
            double b = .5;
            if(ba) {
                b = 1;
            }
            if(bb) {
                b = 0;
            }

            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);
            clawPower = b;
            wristPower = wcx;
            motorEx = (rxa + rxs) * .75;
            claw.setPower( clawPower );
            wrist.setPower( wristPower );
            motorExtension.setPower(motorEx);
            telemetry.addData("Servos","Claw servo position: %4.2f", clawPower);
            telemetry.addData("Servos","Wrist servo position: %4.2f", wristPower);
            telemetry.update();
        }
    }
}
