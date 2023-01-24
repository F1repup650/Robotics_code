package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo; // Carson, why aren't we using the Continuous Rotation Servos?
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="DO NOT USE ME EXCEPT FOR TESTING!!!", group="Tests")
public class clawlimitfinder extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor motor = hardwareMap.dcMotor.get("motor_up");
        telemetry.addData("Initialized","True");
        double roughpos = 0;
        waitForStart();
        if (isStopRequested()) return;
        while (opModeIsActive()) {
            double rt = gamepad1.right_trigger;
            double lt = gamepad1.left_trigger;
            double tt = (lt * -1) + rt;
            roughpos += tt;
            motor.setPower(tt);
            telemetry.addData("Motor Position (getCurPos): ", motor.getCurrentPosition());
            telemetry.addData("Motor Position (rough): ", roughpos);
            telemetry.update();
        }
    }
}
