package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class normalcontrol {
    DcMotor tLeft;
    DcMotor tRight;
    DcMotor bLeft;
    DcMotor bRight;
    DcMotor mArm;
    public void init()
    {
        tLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        tRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        tLeft.setDirection(DcMotor.Direction.REVERSE);
        tRight.setDirection(DcMotor.Direction.FORWARD);
        bLeft.setDirection(DcMotor.Direction.FORWARD);
        bRight.setDirection(DcMotor.Direction.REVERSE);
        mArm.setDirection(DcMotor.Direction.FORWARD);
    }
    public void target(int tLTarget, int tRTarget, int bLTarget, int bRTarget)
    {
        tLeft.setTargetPosition(tLTarget);
        tRight.setTargetPosition(tRTarget);
        bLeft.setTargetPosition(bLTarget);
        bRight.setTargetPosition(bRTarget);
    }
    public void enableEncoders()
    {
        tLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        tRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void enableEncPos()
    {
        tLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        tRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void disableEncoders()
    {
        tLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        tRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void forward(double SPD)
    {
        tLeft.setPower(SPD);
        tRight.setPower(SPD);
        bLeft.setPower(SPD);
        bRight.setPower(SPD);

    }
    public void reverse(double SPD)
    {
        tLeft.setPower(-SPD);
        bLeft.setPower(-SPD);
        tRight.setPower(-SPD);
        bRight.setPower(-SPD);
    }
    public void turnLeft(double SPD)
    {
        tLeft.setPower(-SPD);
        tRight.setPower(SPD);
        bLeft.setPower(-SPD);
        bRight.setPower(SPD);
    }
    public void turnRight(double SPD)
    {
        tLeft.setPower(SPD);
        tRight.setPower(-SPD);
        bLeft.setPower(SPD);
        bRight.setPower(-SPD);
    }
    public void stop() {
        tLeft.setPower(0);
        tRight.setPower(0);
        bLeft.setPower(0);
        bRight.setPower(0);
    }
    public void strafeLeft(double SPD)
    {
        tLeft.setPower(SPD);
        bLeft.setPower(-SPD);
        tRight.setPower(-SPD);
        bRight.setPower(SPD);
    }
    public void strafeRight(double SPD)
    {
        tRight.setPower(SPD);
        bRight.setPower(-SPD);
        tLeft.setPower(-SPD);
        bLeft.setPower(SPD);
    }
}