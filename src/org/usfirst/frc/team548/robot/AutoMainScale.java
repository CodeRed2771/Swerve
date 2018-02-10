package org.usfirst.frc.team548.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoMainScale extends AutoBaseClass{

	public AutoMainScale(int robotPosition) {
		super(robotPosition);
	}
	
	public void tick() {
		if(isRunning()) {
			SmartDashboard.putNumber("Auto Step", getCurrentStep());
			
			switch(getCurrentStep()) {
			case 0:
				setTimerAndAdvanceStep(2000);
				this.driveInches(48, 0, .2);
			case 1:
				if(driveCompleted())
					advanceStep();
			case 2:
				if(robotPosition() == 1) {
					setTimerAndAdvanceStep(2000);
					this.driveInches(12, 12, .1);
				} else {
					setTimerAndAdvanceStep(2000);
					this.driveInches(12, -12, .1);
				}
			case 3:
				if(driveCompleted())
					advanceStep();
				break;
			case 4:
				setTimerAndAdvanceStep(1000);
				this.driveInches(12, 0, .1);
			case 5:
				if(driveCompleted())
					advanceStep();
			case 6:
				stop();
				break;
			}
		}
	}
	
}
