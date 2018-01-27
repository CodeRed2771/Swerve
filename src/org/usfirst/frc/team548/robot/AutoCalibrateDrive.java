package org.usfirst.frc.team548.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoCalibrateDrive extends AutoBaseClass {

	public AutoCalibrateDrive(DriveAuto driveAuto, int robotPosition) {
		super(driveAuto, robotPosition);
		
	}

	public void tick() {
		
		if (isRunning()) {		
			this.driveAuto().showEncoderValues();
			SmartDashboard.putNumber("Auto Step", getCurrentStep());

			switch (getCurrentStep()) {
			case 0:
				setTimerAndAdvanceStep(2000);
				driveInches(50, 45, 1);
				break;
			case 1:
				if (driveAuto().hasArrived())
					advanceStep();
				break;
			case 2:
				setTimerAndAdvanceStep(2000);
				driveInches(50, -45, 1);
				break;
			case 3:
				if (driveAuto().hasArrived())
					advanceStep();
				break;
			case 4:
				setTimerAndAdvanceStep(2000);
				driveInches(-50, 45, 1);
				break;
			case 5:
				if (driveAuto().hasArrived())
					advanceStep();
				break;
			case 6:
				setTimerAndAdvanceStep(2000);
				driveInches(-100, -45, 1);
				break;
			case 7:
				if (driveAuto().hasArrived())
					advanceStep();
				break;
				
			case 8:
				setTimerAndAdvanceStep(2000);
				driveInches(-50, 45, 1);
				break;
			case 9:
				if (driveAuto().hasArrived())
					advanceStep();
				break;
			case 10:
				setTimerAndAdvanceStep(2000);
				driveInches(50, -45, 1);
				break;
			case 11:
				if (driveAuto().hasArrived())
					advanceStep();
				break;
				
			case 12:
				setTimerAndAdvanceStep(2000);
				driveInches(50, 45, 1);
				break;		
			case 13:
				if (driveAuto().hasArrived())
					advanceStep();
				break;
			}
		}

	}

}
