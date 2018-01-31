package org.usfirst.frc.team548.robot;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Module {
	private WPI_TalonSRX drive, turn;
	private final double FULL_ROTATION = 4096d, TURN_P, TURN_I, TURN_D, DRIVE_P, DRIVE_I, DRIVE_D;
	private final int TURN_IZONE, DRIVE_IZONE;
	
	/**
	 * Lets make a new module :)
	 * @param driveTalonID First I gotta know what talon we are using for driving
	 * @param turnTalonID Next I gotta know what talon we are using to turn
	 * @param tP I probably need to know the P constant for the turning PID
	 * @param tI I probably need to know the I constant for the turning PID
	 * @param tD I probably need to know the D constant for the turning PID
	 * @param tIZone I might not need to know the I Zone value for the turning PID
	 */
	public Module(int driveTalonID, int turnTalonID, double dP, double dI, double dD, int dIZone, double tP, double tI, double tD, int tIZone) {
		drive = new WPI_TalonSRX(driveTalonID);
		drive.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0); // ?? don't know if zeros are right
		DRIVE_P = dP;
		DRIVE_I = dI;
		DRIVE_D = dD;
		DRIVE_IZONE = dIZone;

		drive.config_kP(0,  DRIVE_P, 500);
		drive.config_kI(0,  DRIVE_I, 500);
		drive.config_kD(0,  DRIVE_D, 500);
		drive.config_IntegralZone(0, DRIVE_IZONE, 500);
		drive.selectProfileSlot(0, 0);
		
		
		turn = new WPI_TalonSRX(turnTalonID);
	
		turn.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0); // ?? don't know if zeros are right
		TURN_P = tP;
		TURN_I = tI;
		TURN_D = tD;
		TURN_IZONE = tIZone;

		turn.config_kP(0,  TURN_P, 500);
		turn.config_kI(0,  TURN_I, 500);
		turn.config_kD(0,  TURN_D, 500);
		turn.config_IntegralZone(0, TURN_IZONE, 500);
		turn.selectProfileSlot(0, 0);
	}
	
	/**
	 * Setting turn motor power
	 * @param p value from -1 to 1
	 */
	public void setTurnPower(double p) {
		this.turn.set(ControlMode.PercentOutput, p); 
	}

	/**
	 * Setting drive motor power
	 * @param p value from -1 to 1
	 */
	public void setDrivePower(double p) {
		this.drive.set(p);
	}

	/**
	 * Getting the turn encoder position
	 * @return turn encoder position
	 */
	public double getTurnEncPos() {
		return turn.getSelectedSensorPosition(0);
	}

	/**
	 * Thank you CD ozrien for this!!!
	 * @return
	 */
	public double getAbsPos() {
		return (turn.getSensorCollection().getPulseWidthPosition() & 0xFFF)/4095d;
	}

	public int getDriveEnc() {
		return drive.getSelectedSensorPosition(0);
	}

	public void resetTurnEnc() {
		this.turn.getSensorCollection().setQuadraturePosition(0,500); 
	}

	public void resetDriveEnc() {
		this.drive.getSensorCollection().setQuadraturePosition(0, 0);
	}
	
	public void setEncPos(int d) {
		turn.getSensorCollection().setQuadraturePosition(d, 500);
	}
	
	/**
	 * Is electrical good? Probably not.... Is the turn encoder connected?
	 * @return true if the encoder is connected
	 */
	public boolean isTurnEncConnected() {
		//return turn.isSensorPresent(FeedbackDevice.CtreMagEncoder_Relative) == FeedbackDeviceStatus.FeedbackStatusPresent;
		return true;  // didn't immediately see a compatible replacement
	}
	
	public int getTurnRotations() {
		return (int) (turn.getSelectedSensorPosition(0) / FULL_ROTATION);
	}
	
	public double getTurnOrientation() {
		return (turn.getSelectedSensorPosition(0) % FULL_ROTATION) / FULL_ROTATION;
	}
	
	public void setDrivePIDToSetPoint(double setpoint) {
		drive.set(ControlMode.Position, setpoint);
	}
	
	public void setTurnPIDToSetPoint(double setpoint) {
		turn.set(ControlMode.Position, setpoint);
	}
	
	/**
	 * Set turn to pos from 0 to 1 using PID
	 * @param setLoc orientation to set to
	 */	
	public void setTurnOrientation(double position) {
		double base = getTurnRotations() * FULL_ROTATION;
		if (getTurnEncPos() >= 0) {
			if ((base + (position * FULL_ROTATION)) - getTurnEncPos() < -FULL_ROTATION/2) {
				base += FULL_ROTATION;
			} else if ((base + (position * FULL_ROTATION)) - getTurnEncPos() > FULL_ROTATION/2) {
				base -= FULL_ROTATION;
			}
			turn.set(ControlMode.Position, (((position * FULL_ROTATION) + (base))));
		} else {
			if ((base - ((1-position) * FULL_ROTATION)) - getTurnEncPos() < -FULL_ROTATION/2) {
				base += FULL_ROTATION;
			} else if ((base -((1-position) * FULL_ROTATION)) - getTurnEncPos() > FULL_ROTATION/2) {
				base -= FULL_ROTATION;
			}
			turn.set(ControlMode.Position, (base- (((1-position) * FULL_ROTATION))));	
		}
	}
	
	
	public double getError() {
		return turn.getClosedLoopError(0);
	}
	
	public void stopBoth() {
		setDrivePower(0);
		setTurnPower(0);
	}
	
	public void stopDrive() {
		setDrivePower(0);
	}
	
	public void setBrakeMode(boolean b) {
		drive.setNeutralMode(b ? NeutralMode.Brake : NeutralMode.Coast);
	}
	
	
	
}