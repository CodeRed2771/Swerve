package org.usfirst.frc.team548.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoBaseLine extends AutoBaseClass{
	public AutoBaseLine(DriveAuto driveAuto, int robotPosition){
		super(driveAuto, robotPosition);
	}
	
	public void tick(){
		if(isRunning()){
			
			driveAuto().showEncoderValues();
			SmartDashboard.putNumber("Auto Step", getCurrentStep());
			
			switch(getCurrentStep()){
			case 0:
				setTimerAndAdvanceStep(2000);
				driveInches(-30, 0, .3);
				break;
			case 1:
				if(driveAuto().hasArrived())
					advanceStep();
				break;
			case 2:
				setTimerAndAdvanceStep(3000);
				break;	
			case 3:
				break;
			case 4:
				setTimerAndAdvanceStep(2000);
				driveInches(30, 0, .3);
				break;	
			case 5:
				if(driveAuto().hasArrived())
					advanceStep();
				break;
			case 6:
				setTimerAndAdvanceStep(3000);
				break;	
			case 7:
				break;
			case 8:
				setStep(100000);
			//DriveTrain.setTurnOrientation(0, 0, 0, 0);
			}
		}
	}
}
