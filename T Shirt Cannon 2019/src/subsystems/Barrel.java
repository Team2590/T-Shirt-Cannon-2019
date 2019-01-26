package subsystems;

import org.usfirst.frc.team2590.robot.RobotMap;

import controllers.PID;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import settings.BarrelSettings;

/**
 * Revolving Barrel with 10 Possible Slots for T-Shirts
 * 
 * @author Harsh Padhye, Chinmay Savanur
 *
 */
public class Barrel implements RobotMap, BarrelSettings {

	// Singleton
	private static Barrel barrelInstance = null;

	public static Barrel getBarrelInstance() {
		if (barrelInstance == null) {
			barrelInstance = new Barrel();
		}
		return barrelInstance;
	}

	// Numbers
	private int barrelIndex; // current barrel index
	private double spinSpeed; // speed to go at when free spinning
	private int numOpenBarrels; // the amount of open barrels
	private double setpoint; // setpoint to go to
	private boolean[] barrelHasShirt; // if the barrel has a tshirt in it or not

	// Manipulators
	private Solenoid lock;
	private Victor revolver;
	private Encoder revolverEncoder;

	// States that the arm could be in, either fully stopped or moving between
	private barrelStates barrelState = barrelStates.LOCKED;
	private enum barrelStates {
		LOCKED, UNLOCKED, FREE_SPIN
	}

	// revolver controller
	private PID revolverCont;

	public Barrel() {
		// variables
		spinSpeed = 0;
		barrelIndex = 0;
		setpoint = 0;
		numOpenBarrels = AmountOBarrels;
		barrelHasShirt = new boolean[AmountOBarrels];

		//solenoids , manipulators and controllers
		lock = new Solenoid(lockerSolenoid);
		revolver = new Victor(revolverPWM);
		revolverEncoder = new Encoder(revolverEncoderA, revolverEncoderB);
		revolverCont = new PID(revolverKP, revolverKI, revolverKD, revolverIntegralCap);

		revolverEncoder.setReverseDirection(true);
	}

	public void update() {
		// the current barrel
		//barrelIndex = (int) revolverEncoder.getDistance() / (360 / AmountOBarrels);

		if (Math.abs(barrelIndex) > AmountOBarrels - 1) {
			//revolverEncoder.reset();
		}

		switch (barrelState) {
		case LOCKED:
			revolver.set(0);
			lock.set(false);
			break;
		case UNLOCKED:
			lock.set(true);
			revolver.set(revolverCont.getOutput(revolverEncoder.get(),0.00, 0.01));
			System.out.println(Math.abs(setpoint - revolverEncoder.get()));//prints out error
			
			//if the revolver within x degrees of next barrel, begin dragging the lock
			if(Math.abs(setpoint - revolverEncoder.get()) < 18){
				lock.set(false);
			}
			if (Math.abs(setpoint - revolverEncoder.get()) <= 2 && Math.abs(revolverEncoder.getRate()) < 1.0) { //this is in ticks/degrees
				barrelState = barrelStates.LOCKED;
				System.out.println("LOCKED AND LOADED");
			}
			break;
		case FREE_SPIN:
			lock.set(true);
			revolver.set(spinSpeed);
			break;
		}
	}
	
	/**
	 * setsthe setpoint for the revolver controller
	 * @param setpoint: setpoint in degrees for the barrel to turn to
	 * NOTE: THis setpoint will probably be 36 degrees, assuming the encoder is reset each spin
	 */
	public void setRevolverSetpoint(double stp) {
		setpoint = stp;
		revolverCont.setSetpoint(setpoint);
	}
	
	/**
	 * simple accessor method to unlock the barrel
	 */
	public void unlockBarrel() {
		barrelState = barrelStates.UNLOCKED;
	}
	
	/**
	 * resets the revovler encoder
	 */
	public void resetRevolverEncoder() {
		revolverEncoder.reset();
	}

	/**
	 * Spins to that barrel
	 * 
	 * @param setpoint:
	 *            the barrel to spin to 0 - Amount of barrels-1
	 */
	public void setBarrelIndex(double setpoint) {
		this.setpoint = Math.abs(setpoint) * (360 / AmountOBarrels);
		revolverCont.setSetpoint(setpoint);
		barrelState = barrelStates.UNLOCKED;
	}

	/**
	 * Sends the controller to the closest full (has a tshirt) barrel
	 */
	public void goToClosest() {

		// check that the current barrel isnt full
		if (!barrelHasShirt[barrelIndex] && numOpenBarrels > 0) {
			// if were on the last barrel, go to the first and recursively call the function
			// to find the closest full one
			if (barrelIndex == AmountOBarrels - 1) {
				setBarrelIndex(0);
				goToClosest();
			}

			// set the closest barrel to 0
			double closest = 0;

			// run throught the shot array
			for (int i = 0; i < AmountOBarrels - 1; i++) {
				// if the barrel in position i is full
				if (barrelHasShirt[i]) {
					// if i is closer than the last closest then set it
					if (Math.abs(closest - barrelIndex) > Math.abs(i - barrelIndex)) {
						closest = i;
					}
				}
			}
			setBarrelIndex(closest);
		}

	}

	/**
	 * Gets the current barrel index
	 * 
	 * @return: current barrel index 0 - AmountOBarrels-1
	 */
	public int getCurrentBarrel() {
		return barrelIndex;
	}

	/**
	 * Resets the barrel shot counter and sends the barrel to home
	 */
	public void reset() {
		for (int i = 0; i < AmountOBarrels; i++) {
			barrelHasShirt[i] = false;
		}
		setBarrelIndex(1);
	}

	/**
	 * Sets the barrel to be fired or not (later be able to check if we have a
	 * tshirt in the barrel)
	 * 
	 * @param index
	 *            : barrel index 0 - AmountOBarrels-1
	 * @param fired
	 *            : has it been fired or not
	 */
	public void setShotBarrel(int index, boolean fired) {
		barrelHasShirt[index] = fired;
		numOpenBarrels += fired ? -1 : 1;
	}

	/**
	 * Unlocks the revolver and spins at a constant percent
	 * 
	 * @param spinSpd
	 *            : percent to spin at
	 */
	public void startFreeSpin(double spinSpd) {
		spinSpeed = spinSpd;
		barrelState = barrelStates.FREE_SPIN;
	}

	/**
	 * Stops spinning, usually used after startFreeSpin
	 */
	public void stopFreeSpin() {
		spinSpeed = 0;
		barrelState = barrelStates.LOCKED;
	}

	/*
	 * When implementing PID, manipulate P and D: Manipulate P to increase speed
	 * between endpoints Manipulate D to prevent overshooting
	 */

}
