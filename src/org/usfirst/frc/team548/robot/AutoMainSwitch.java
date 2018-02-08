package org.usfirst.frc.team548.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoMainSwitch extends AutoBaseClass{
	AutoBaseClass mSubAuto;
	
	public AutoMainSwitch(int robotPosition) {
		super(robotPosition);
	}	
	
	public void tick() {
		if (isRunning()) {
			
			SmartDashboard.putNumber("Auto Base Step", getCurrentStep());
			
			switch(getCurrentStep()) {
			case 0:
				mSubAuto = new AutoStartToSwitch(this.robotPosition());
				mSubAuto.start();
				advanceStep();
				break;
			case 1:
				if (mSubAuto.autoIsCompleted)
					advanceStep();
				break;
			case 2:
				mSubAuto = new AutoSwitchPlaceCube(this.robotPosition());
				mSubAuto.start();
				advanceStep();
				break;
			case 3:
				if (mSubAuto.autoIsCompleted) {
					this.autoIsCompleted = true;
					advanceStep();
				}
				
				break;
			case 4:
				break;			}
		}
	}
}
