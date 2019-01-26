package org.usfirst.frc.team2590.utils;

import edu.wpi.first.wpilibj.Timer;

public class DelayedBoolean {

	private double startTime;
	private double fuse;

	public DelayedBoolean(double fuseLength) {
		fuse = fuseLength * 1000;
		startTime = Timer.getFPGATimestamp() * 1000;
	}

	public boolean getDone() {
		return Timer.getFPGATimestamp() * 1000 - startTime > fuse;
	}

	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setDelayTime(double dTime) {
		// TODO Auto-generated method stub

	}

	public void resetBoolean() {
		// TODO Auto-generated method stub
		
	}

}
