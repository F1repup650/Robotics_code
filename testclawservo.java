package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name="Claw Servos", group="Tests")
public class testclawservo extends LinearOpMode {
    private boolean buttonPreviousState;
    public boolean buttonClick (boolean button) {
        boolean returnVal;
        returnVal = button && !buttonPreviousState;
        buttonPreviousState = button;
        return returnVal;
    }
    @Override
    public void runOpMode() throws InterruptedException {
        servocontrol servo = new servocontrol();
        CRServo claw = hardwareMap.crservo.get("Claw");
        CRServo wrist = hardwareMap.crservo.get("Wrist");
        double clawPower;
        double wristPower;

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double lx = gamepad1.left_stick_x; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;
            clawPower = servo.stickControl(lx);
            wristPower = servo.stickControl(rx);
            claw.setPower( clawPower );
            wrist.setPower( wristPower );
            telemetry.addData("Servos","Claw servo power: %4.2f", clawPower);
            telemetry.addData("Servos","Wrist servo power: %4.2f", wristPower);
            telemetry.update();
        }
    }
}
