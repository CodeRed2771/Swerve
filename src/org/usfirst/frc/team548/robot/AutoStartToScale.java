package org.usfirst.frc.team548.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoStartToScale extends AutoBaseClass{

	public AutoStartToScale(int robotPosition) {
		super(robotPosition);
	}
	
	public void tick() {
		if(isRunning()) {
			SmartDashboard.putNumber("Auto Step", getCurrentStep());
			
			switch(getCurrentStep()) {
			case 0:
				setTimerAndAdvanceStep(3000);
				if(robotPosition() == 1) {
					this.driveInches(36, 30, .3);
				} else {
					this.driveInches(36, -30, .3);
				}
				break;
			case 1:
				if(driveCompleted())
					advanceStep();
				break;
			case 2:
				setTimerAndAdvanceStep(3000);
				this.driveInches(48, 0, .3);
				break;
			case 3:
				if(driveCompleted())
					advanceStep();
				break;
			case 4:
				stop();
				break;
			}
		}
	}
}