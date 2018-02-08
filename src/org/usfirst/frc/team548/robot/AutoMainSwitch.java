package org.usfirst.frc.team548.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoMainSwitch extends AutoBaseClass{
	AutoBaseClass mAutoSubroutine;
	
	public AutoMainSwitch(int robotPosition) {
		super(robotPosition);
	}	
	
	public void tick() {
		if (isRunning()) {
			
			SmartDashboard.putNumber("Auto Base Step", getCurrentStep());
			
			switch(getCurrentStep()) {
			case 0:
				mAutoSubroutine = new AutoStartToSwitch(this.robotPosition());
				mAutoSubroutine.start();
				advanceStep();
				break;
			case 1:
				if (mAutoSubroutine.hasCompleted())
					advanceStep();
				break;
			case 2:
				mAutoSubroutine = new AutoSwitchPlaceCube(this.robotPosition());
				mAutoSubroutine.start();
				advanceStep();
				break;
			case 3:
				if (mAutoSubroutine.hasCompleted()) 
					stop();
				break;
			case 4:
				break;			}
		}
	}
}
