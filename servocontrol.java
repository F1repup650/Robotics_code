package org.firstinspires.ftc.teamcode;

public class servocontrol {
    private double power;
    public double stickControl(double x)
    {
        power = (x+1)/2;
        return power;
    }
}
