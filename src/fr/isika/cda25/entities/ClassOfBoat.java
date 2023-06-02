package fr.isika.cda25.entities;

public enum ClassOfBoat {
	CARRIER, BATTLESHIP, DESTROYER, SUBMARINE, PATROLBOAT;
	
	/**
	 * Calculates the size of the boat thanks to its class
	 * @param boatClass the class of this boat
	 * @return the size of this boat
	 */
	public static int getBoatSize(String boatClass) {
		switch (boatClass) {
		case "CARRIER":
			return 5;
		case "BATTLESHIP":
			return 4;
		case "DESTROYER":
			return 3;
		case "SUBMARINE":
			return 3;
		case "PATROLBOAT":
			return 2;
		default:
			System.out.println("ClassOfBoat.getBoatSize() error : boatClass value is incorrect.");
			return 0;
		}
	}

}
