package org.firstinspires.ftc.teamcode;

public class mecanumcontrol extends normalcontrol {
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
