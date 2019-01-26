package controllers;

public class PID {

	private double setpoint;
	private double errorSum;
	private double lastError;

	private double kP;
	private double kI;
	private double kD;
	private double kR;

	public PID(double kP, double kI, double kD, double kR) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.kR = kR;
	}

	public void setSetpoint(double setpoint) {
		this.setpoint = setpoint;
		errorSum = 0;
	}

	public double getOutput(double posInput, double rateInput, double deltaT) {
		
		
		//sum of errors over time
		double error = setpoint - posInput;
		errorSum += error;
		
		//change in error over time
		double deltaError = (error - lastError) / deltaT;

		//KP + KI + KD + KR = output value
		//rate setpoint is negative (0 - input = -input)
		double output = (kP * error) + (kI * errorSum) + (kD * deltaError) + (-rateInput * kR);
		
		//resets last error
		lastError = error;

		return output;
	}

}