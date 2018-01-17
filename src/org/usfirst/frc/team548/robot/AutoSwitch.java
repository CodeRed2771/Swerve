package org.usfirst.frc.team548.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoSwitch extends AutoBaseClass{
	public AutoSwitch(DriveAuto driveAuto, int robotPosition) {
		super(driveAuto, robotPosition);
	}	
	
	public void tick() {
		if (isRunning()) {
			
			this.driveAuto().showEncoderValues();
			SmartDashboard.putNumber("Auto Step", getCurrentStep());
			
			switch(getCurrentStep()) {
			case 0:
				setTimerAndAdvanceStep(2000); //this number needs to be tested
				driveInches(145,0,.5); //(inches, angle, power), inches needs to be adjusted
				break;
			case 1:
				if (driveAuto().hasArrived())
					advanceStep();
				break;
			case 2:
				setTimerAndAdvanceStep(1000);
				//Elevator.raiseLift();
				break;
			case 3:
				//if (Elevator.hasArrived())
					advanceStep();
				break;
			case 4:
				setTimerAndAdvanceStep(3000);
				turnDegrees(90, 1);
				break;
			case 5:
				//if (driveAuto().hasArrived())
					//advanceStep();
				break;
			case 6:
				setTimerAndAdvanceStep(2000);
				driveInches(10,90,1);
				break;
			case 7:
				if (driveAuto().hasArrived())
					advanceStep();
				break;
			case 8:
				//DropCube
				break;
			}
		}
	}
}
