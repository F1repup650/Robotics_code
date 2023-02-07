package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Encoder Test", group="Tests")
public class encodertest extends LinearOpMode {
    DcMotor tLeft;
    DcMotor tRight;
    DcMotor bLeft;
    DcMotor bRight;
    DcMotor mArm;
    @Override
    public void runOpMode() {
        mecanumcontrol motor = new mecanumcontrol();
        tLeft = hardwareMap.dcMotor.get("top_left");
        motor.tLeft = tLeft;
        tRight = hardwareMap.dcMotor.get("top_right");
        motor.tRight = tRight;
        bLeft = hardwareMap.dcMotor.get("back_left");
        motor.bLeft = bLeft;
        bRight = hardwareMap.dcMotor.get("back_right");
        motor.bRight = bRight;
        mArm = hardwareMap.dcMotor.get("motor_up");
        motor.mArm = mArm;
        motor.init();
        motor.enableEncoders();
        waitForStart();
        while (opModeIsActive()) {
            tLeft.setTargetPosition(0);
            bLeft.setTargetPosition(0);
            tRight.setTargetPosition(0);
            bRight.setTargetPosition(0);
            motor.forward(0.5);
        }
    }
}
