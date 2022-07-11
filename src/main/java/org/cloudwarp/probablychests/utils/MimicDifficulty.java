package org.cloudwarp.probablychests.utils;

public enum MimicDifficulty {
	EASY, MEDIUM, HARD, INSANE;
	public static MimicDifficulty fromInt(int i){
		return switch (i) {
			case 0 -> EASY;
			case 1 -> MEDIUM;
			case 2 -> HARD;
			case 3 -> INSANE;
			default -> MEDIUM;
		};
	}
	public int toInt (){
		return switch (this) {
			case EASY -> 0;
			case MEDIUM -> 1;
			case HARD -> 2;
			case INSANE -> 3;
			default -> 1;
		};
	}
	public int getHealth(){
		return switch (this) {
			case EASY -> 15;
			case MEDIUM -> 30;
			case HARD -> 50;
			case INSANE -> 100;
			default -> 30;
		};
	}
	public int getDamage(){
		return switch (this) {
			case EASY -> 3;
			case MEDIUM -> 5;
			case HARD -> 8;
			case INSANE -> 12;
			default -> 5;
		};
	}
	public double getSpeed(){
		return switch (this) {
			case EASY -> 0.5D;
			case MEDIUM -> 1D;
			case HARD -> 1.5D;
			case INSANE -> 2D;
			default -> 1D;
		};
	}
}
