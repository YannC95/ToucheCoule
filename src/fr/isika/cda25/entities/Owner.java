package fr.isika.cda25.entities;

/**
 * Owner of the Coordinate, Boat and Grid.
 * 
 * @author yann
 *
 */
public abstract class Owner {

	protected String boatOwner;

	public Owner(String boatOwner) {
		this.boatOwner = checkOwner(boatOwner);
	}

	/**
	 *  Checks if the variable boatOwner is a correct value (see ListOfOwner).
	 * @param boatOwner boatOwner of this boat
	 * @return the owner of the boat if the value is correct
	 */
	private String checkOwner(String boatOwner) {
		for (ListOfOwner owner : ListOfOwner.values()) {
			if (boatOwner.equals(owner.toString())) {
				return boatOwner;
			}
		}
		System.out.println("Owner.checkOwner() error : boatOwner value is incorrect.");
		return "";
	}

}
