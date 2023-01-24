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
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addData("Initialized","True");
        waitForStart();
        motor.setTargetPosition(9130);
        if (isStopRequested()) return;
        while (opModeIsActive()) {
            double rt = gamepad1.right_trigger;
            double lt = gamepad1.left_trigger;
            double tt = (lt * -1) + rt;
            motor.setPower(tt);
            telemetry.addData("Motor Position (getCurPos)", motor.getCurrentPosition());
            telemetry.update();
        } // 9138 - 0
    }
}
