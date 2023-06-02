package fr.isika.cda25.entities;

/**
 * <pre>
 * Used as an attribute in Boat an Grid classes.
 * Used in the Grid.hit method.
 * </pre>
 * 
 * @author yann
 *
 */
public class Coordinate extends Owner {

	private String value;
	private String status;

	/**
	 * By default, value = "" and status = "o"
	 * @param boatOwner the name of the player (see ListOfOwner)
	 */
	public Coordinate(String boatOwner) {
		super(boatOwner);
		this.value = "";
		this.status = "o";
	}

	/**
	 * To change the value of this Coordinate
	 * @param coordinate coordinate to set (see ListOfCoordinate)
	 */
	public void setCoordinate(String coordinate) {
		value = coordinate;
	}

	/**
	 * 
	 * To change the status of this Coordinate
	 * @param status status to set (o, x, -)
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * To get the status of Coordinate (o, x, -)
	 * @return Coordinate's status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * To get the coordinate value of Coordinate
	 * @return Coordinate's value
	 */
	public String getCoordinate() {
		return value;
	}
}
