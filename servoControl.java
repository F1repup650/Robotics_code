package org.firstinspires.ftc.teamcode;

public class servoControl {
    private double power;
    public double stickControl(double x)
    {
        if (x>0)
        {
            power = 1;
        }
        else if (x<0)
        {
            power = 0;
        }
        else
        {
            power = 0.5;
        }
        return power;
    }
}
