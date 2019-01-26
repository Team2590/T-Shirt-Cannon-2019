package subsystems;

import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import settings.DrivetrainSettings;

/**
 * Drivetrain class
 * 
 * @author Harsh Padhye, Chinmay Savanur
 *
 */
public class Drivetrain implements RobotMap, DrivetrainSettings {

	// singleton
	private static Drivetrain drivetrainInstance = null;

	public static Drivetrain getDrivetrainInstance() {
		if (drivetrainInstance == null) {
			drivetrainInstance = new Drivetrain();
		}
		return drivetrainInstance;
	}

	private drivetrainStates driveState = drivetrainStates.STOPPED;

	private enum drivetrainStates {
		STOPPED, TELEOP
	}

	// RobotDrive
	private DifferentialDrive driveSystem;

	// drive victors
	private Victor leftVictor;
	private Victor rightVictor;

	// teleoperated driving stuff
	private double turnValue;
	private double driveValue;

	public Drivetrain() {

		// instantiate the drive values
		turnValue = 0;
		driveValue = 0;

		// set the enums to the default cases
		driveState = driveState.STOPPED;

		// instantiate the drive victors
		leftVictor = new Victor(leftDrivePWM);
		rightVictor = new Victor(rightDrivePWM);

		// set motors to brake mode

		driveSystem = new DifferentialDrive(leftVictor, rightVictor);
	}

	/**
	 * The loop controller constantly refreshes this loop at a certain
	 * predetermined refresh rate
	 * 
	 * @param deltaT
	 *            : the delta time value between cycles
	 */
	public void update() {
		// updates the driving loop
		switch (driveState) {
		case STOPPED:
			leftVictor.set(0);
			rightVictor.set(0);
			break;
		// general driving
		case TELEOP:
			// these may have to be flipped
			// leftVictor.set(driveValue + turnValue);
			// rightVictor.set(driveValue - turnValue);
			break;
		default:
			break;
		}

	}

	/**
	 * changes the turning and driving values, most commonly used in teleop
	 * 
	 * @param driveV
	 *            : driving value
	 * @param turnV
	 *            : turning value
	 */

	/**
	 * @SuppressWarnings("static-access") public void updateDriveValues(double
	 * driveV, double turnV) { // set the desired drive state driveState =
	 * driveState.TELEOP;
	 * 
	 * // update the turning and driving values turnValue = turnV; driveValue =
	 * driveV; }
	 */

	public DifferentialDrive getRobotDrive() {
		return driveSystem;
	}

	/**
	 * Stops the drivebase
	 */
	@SuppressWarnings("static-access")
	public void stopDriveTrain() {
		driveState = driveState.STOPPED;
	}

	@SuppressWarnings("static-access")
	public void setTeleop() {
		driveState = driveState.TELEOP;
	}

}
