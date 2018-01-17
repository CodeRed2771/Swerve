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
				driveInches(140,0,1); //(inches, angle, power)
				break;
			case 1:
				if (driveAuto().hasArrived())
					advanceStep();
				break;
			}
		}
	}
}
