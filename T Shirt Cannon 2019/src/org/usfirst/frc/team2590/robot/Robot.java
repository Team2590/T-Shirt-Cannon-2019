/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2590.robot;

import org.usfirst.frc.team2590.utils.NemesisJoystick;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import subsystems.Arm;
import subsystems.Barrel;
import subsystems.Cannon;
import subsystems.Drivetrain;

/**
 * @author Harsh Padhye
 */
public class Robot extends IterativeRobot {

	private static Arm arm;
	private static Barrel barrel;
	private static Cannon cannon;
	private static Drivetrain drivetrain;
	//private static Horn horn;

	private NemesisJoystick leftStick, rightStick;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		arm = Arm.getArmInstance();
		barrel = Barrel.getBarrelInstance();
		cannon = Cannon.getCannonInstance();
		drivetrain = Drivetrain.getDrivetrainInstance();
		//horn = Horn.getHornInstance();

		leftStick = new NemesisJoystick(0);
		rightStick = new NemesisJoystick(1);

	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
	}

	/**
	 * This function is called periodically during operator control.
	 */
	
	@Override
	public void teleopInit() {
	}
	
	
	@Override
	public void teleopPeriodic() {
		drivetrain.setTeleop();
		// driving the robot
		//drivetrain.updateDriveValues(leftStick.getY(), rightStick.getX());
		
		drivetrain.getRobotDrive().arcadeDrive(-leftStick.getY(),rightStick.getX());
		
		//raising, lowering, and stopping arm manually
		if (rightStick.getRawButton(5)) {
			arm.liftManually(0.25);
		}
		if (rightStick.getRawButton(3)) {
			arm.lowerManually(-0.25);
		}
		if (rightStick.getRawButton(6) || rightStick.getFallingEdge(5) || rightStick.getFallingEdge(3)) {
			arm.setStationary();
		}
		
		//rotates the revolver to the next barrel
		if(rightStick.getFallingEdge(2)){
			barrel.resetRevolverEncoder();
			barrel.setRevolverSetpoint(36.0);
			barrel.unlockBarrel();
		}
		
		//fires the cannon
		if(rightStick.getFallingEdge(1)){
			cannon.fireCannon();
		}
		
		//indicates whether the cannon should be ready to fire again
		//SmartDashboard.putBoolean("DB/LED 0", cannon.readyToFire());
		
		
		// updates every subsystem
		arm.update();
		barrel.update();
		cannon.update();
		drivetrain.update();
	//	horn.update();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}

	// Instance methods

	public Arm getArmInstance() {
		return arm;
	}

	public static Barrel getBarrelInstance() {
		return barrel;
	}

	public static Cannon getCannonInstance() {
		return cannon;
	}

	public static Drivetrain getDrivetrainInstance() {
		return drivetrain;
	}

//	public static Horn getHornInstance() {
//		return horn;
//	}
}
