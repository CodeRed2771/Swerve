package org.usfirst.frc.team548.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoBaseLine extends AutoBaseClass{
	public AutoBaseLine(DriveAuto driveAuto, int robotPosition){
		super(driveAuto, robotPosition);
	}
	
	public void tick(){
		if(isRunning()){
			this.driveAuto().showEncoderValues();
			SmartDashboard.putNumber("Auto Step", getCurrentStep());
			
			switch(getCurrentStep()){
			case 0:
				setTimerAndAdvanceStep(2000);
				driveInches(135, 0, .5);
				break;
			case 1:
				if(driveAuto().hasArrived())
					advanceStep();
				break;
			}
		}
	}
}
