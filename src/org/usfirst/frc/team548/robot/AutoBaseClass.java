package org.usfirst.frc.team548.robot;

import org.usfirst.frc.team548.robot.DriveAuto;
import com.coderedrobotics.libs.Timer;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class AutoBaseClass {
	DriveAuto mDriveAuto;
	int mRobotPosition;
	boolean mIsRunning = false;
	Timer mAutoTimer;

	public AutoBaseClass(DriveAuto driveAuto, int robotPosition) {
		mDriveAuto = driveAuto;
		mRobotPosition = robotPosition;
		mAutoTimer = new Timer();
	}

	public String getGameData() {
		String gameData;
		return DriverStation.getInstance().getGameSpecificMessage();
	}
	
	public char getMySwitchSide() {
		return getGameData().charAt(0);
	}
	
	public char getMyScaleSide() {
		return getGameData().charAt(1);
	}
	
	public abstract void tick();

	public void start() {
		mAutoTimer.setStage(0);
		mIsRunning = true;
	}

	public void stop() {
		mIsRunning = false;
		mDriveAuto.stop();
	}

	public boolean isRunning() {
		mAutoTimer.tick();  // we need to tick the timer and this is a good place to do it.
		return mIsRunning;
	}

	public int getCurrentStep() {
		return mAutoTimer.getStage();
	}

	public void setStep(int step) {
		mAutoTimer.setStage(step);
	}

	public double getStepTimeRemainingInSeconds() {
		return mAutoTimer.getTimeRemainingSeconds();
	}

	public double getStepTimeRemainingInMilliSeconds() {
		return mAutoTimer.getTimeRemainingMilliseconds();
	}

	public DriveAuto driveAuto() {
		return mDriveAuto;
	}

	public void driveInches(double distance, double angle, double maxPower) {
		mDriveAuto.driveInches(distance, angle, maxPower);
	}

	public void turnDegrees(double degrees, double maxPower) {
		mDriveAuto.turnDegrees(degrees, maxPower);
	}

	public int robotPosition() {
		return mRobotPosition;
	}

	public void advanceStep() {
		mAutoTimer.stopTimerAndAdvanceStage();
	}

	// starts a timer for the time indicated and then immediately advances the
	// stage counter
	// this is typically used when starting a driving maneuver because the next
	// stage would
	// be watching to see when the maneuver was completed.
	public void setTimerAndAdvanceStep(long milliseconds) {
		mAutoTimer.setTimerAndAdvanceStage(milliseconds);
	}
	
	public void setTimer(long milliseconds) {
		mAutoTimer.setTimer(milliseconds);
	}
	
	public boolean timeExpired() {
		return mAutoTimer.timeExpired();
	}

}
