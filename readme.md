# This is the Repo for Prowlers' Robotics code #
### Contents (Format: filename.java | Group name    | Name) ###
- autotest1.java       | Tests         | Autonomous OpMode (Direct Motor Control (DMC))
- autotest2.java       | Tests         | Autonomous OpMode (Motor Encoders (ME))
- mecanumteleop.java   | Production    | Mecanum: Test Linear OpMode
- testclawservo.java   | Tests         | Claw Servos
- testcolorsensor.java | Tests         | Color Sensor
- autonomous1.java     | Production    | Autonomous OpMode (Direct Motor Control (DMC)) - Park ONLY
### Control Classes (Format: filename.java | What does it do?)
- normalcontrol.java   | Implements motor controls
- mecanumcontrol.java  | Extends normalcontrol with strafing commands
- servocontrol.java    | Implements servo controls
