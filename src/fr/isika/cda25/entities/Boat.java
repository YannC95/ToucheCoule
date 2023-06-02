package fr.isika.cda25.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <pre>
 * To make and deploy the boats.
 * Boats will be used to make the grid of each player
 * </pre>
 * 
 * @author yann
 *
 */
public class Boat extends Owner {

	private static Scanner input = new Scanner(System.in);
	private int boatSize;
	private String boatDirection;
	private String boatClass;
	private List<Coordinate> boatCoordinate;
	private static List<Coordinate> blackList = new ArrayList<>();
	private final int LINE_SIZE = 10;

	/**
	 * Designed for owner "YOU" only.
	 * 
	 * @param boatClass the class of the boat (see ClassOfBoat)
	 * @param boatOwner "YOU"
	 */
	public Boat(String boatClass, String boatOwner) {
		super(boatOwner);
		this.boatCoordinate = new ArrayList<>();
		this.boatClass = checkBoatClass(boatClass);
		this.boatSize = calculBoatSize(boatClass);
		this.boatDirection = askDirection();
	}

	/**
	 * <pre>
	 * To make a boat.
	 * Method chooseRandomDirection() will provide a random correct direction to this constructor.	 
	 * Designed for owner "FRANKY" only.
	 * </pre>
	 * 
	 * @param boatClass the class of the boat (see ClassOfBoat)
	 * @param boatOwner "FRANKY"
	 * @param direction the direction of deployment of the boat (see Directions)
	 */
	public Boat(String boatClass, String boatOwner, String direction) {
		super(boatOwner);
		this.boatCoordinate = new ArrayList<>();
		this.boatClass = boatClass;
		this.boatSize = calculBoatSize(boatClass);
		this.boatDirection = direction;
	}

	/**
	 * <pre>
	 * To make a boat.
	 * Asks and records the direction of deployment for this boat
	 * Designed for owner "YOU" only.
	 * </pre>
	 * 
	 * @return direction of deployment of your boat
	 */
	public String askDirection() {
		String direction = "";
		do {
			System.out.println("Which direction do you want to deploy your " + boatClass + " (X/Y) ?");
			direction = input.nextLine();
		} while (!checkBoatDirection(direction));
		return direction;
	}

	/**
	 * <pre>
	 * Deploys a boat in your grid. 
	 * Designed for owner "YOU" only.
	 * 1) Asks you to give the first coordinate of the boat via Scanner input.
	 * 2) Checks if the coordinate is allowed (inside the grid and calls the checkBlacklist method).
	 * 3) Calls the calculOtherCoordinate method to generate the other coordinate of the boat.
	 * If at least one of the otherCoordinate is prohibited (outside grid or in the BL), the deployBoat method will call itself until all the coordinate are allowed.
	 * </pre>
	 * 
	 */
	public void deployBoat() {
		boolean test = false;
		do {
			System.out.println("Where do you want to deploy your " + boatClass + " (first coordinate) ?");
			String deployCoordinate = input.nextLine();
			if (!checkBlacklist(deployCoordinate)) {
				System.out.println(
						"Boat.checkBlacklist() in deployBoat error : deployCoordinate value is already used. Please enter another value.");
				continue;

			}
			for (ListOfCoordinate coordinate : ListOfCoordinate.values()) {
				if (deployCoordinate.equals(coordinate.toString())) {
					Coordinate firstCoordinate = new Coordinate(boatOwner);
					firstCoordinate.setCoordinate(coordinate.toString());
					boatCoordinate.add(firstCoordinate);
					test = true;
					break;
				}
			}
			if (!test) {
				System.out.println(
						"Boat.deployBoat() error : deployCoordinate value is incorrect. Please enter a value between "
								+ ListOfCoordinate.values()[0].toString() + " and "
								+ ListOfCoordinate.values()[ListOfCoordinate.values().length - 1].toString());

			}
		} while (!test);
		calculOtherCoordinate();
	}

	/**
	 * <pre>
	 * Allows Franky to deploy randomly a boat in his grid. 
	 * Designed for owner "FRANKY" only.
	 * 1) Checks if the coordinate is allowed (inside the grid and calls the checkBlacklist method).
	 * 2) Calls the calculOtherCoordinate method to generate the other coordinate of the boat.
	 * If at least one of the otherCoordinate is prohibited (outside grid or in the BL), the chooseRandomCoordinate method and the deployBoat method will call itself until all the coordinate are allowed.
	 * </pre>
	 * 
	 * @param deployCoordinate coordinate where to deploy Franky's boat
	 * 
	 */
	public void deployBoat(String deployCoordinate) {
		do {
			if (!checkBlacklist(deployCoordinate)) {
				System.out.println(
						"Boat.checkBlacklist() in deployBoat error : deployCoordinate value is already used. Please enter another value.");
				deployCoordinate = chooseRandomCoordinate();
				continue;
			}
			Coordinate firstCoordinate = new Coordinate(boatOwner);
			firstCoordinate.setCoordinate(deployCoordinate);
			boatCoordinate.add(firstCoordinate);
		} while (boatCoordinate.isEmpty());
		calculOtherCoordinate();
	}

	/**
	 * <pre>
	 * Generates the otherCoordinate of the boat.
	 * Will be called by the deployBoat method.
	 * 1) Calculates the otherCoordinate thanks to the firstCoordinate (deployBoat) and checks if all the coordinate are allowed (inside the grid and calls the checkBlacklist method).
	 * 2) Adds all the coordinate into the field boatCoordinate of this Boat.
	 * If at least one of the otherCoordinate is prohibited (outside grid or in the BL), the deployBoat method will be call again until all the coordinates are allowed.
	 * </pre>
	 * 
	 */
	private void calculOtherCoordinate() {
		int index = ListOfCoordinate.valueOf(boatCoordinate.get(0).getCoordinate()).ordinal();
		boolean testBL = true;
		try {
			if (boatDirection.equals("X")) {
				for (int i = 1; i < boatSize; i++) {
					if (!checkBlacklist(ListOfCoordinate.values()[index + i].toString())) {
						System.out.println(
								"Boat.checkBlacklist() in calculOtherCoordinate error : otherCoordinate value is already used. Please enter another deploy Coordinate value.");
						testBL = false;
						break;

					}
					Coordinate otherCoordinate = new Coordinate(boatOwner);
					otherCoordinate.setCoordinate(ListOfCoordinate.values()[index + i].toString());
					boatCoordinate.add(otherCoordinate);
				}
				if (!testBL || (boatCoordinate.get(0).getCoordinate().toString().charAt(1) != boatCoordinate
						.get(boatCoordinate.size() - 1).getCoordinate().toString().charAt(1))) {
					System.out.println(
							"Boat.calculOtherCoordinate() error : deployCoordinate value is incorrect, at least one part of the boat is outside the grid or the blacklist has been triggered.");
					boatCoordinate.clear();
					if (boatOwner.equals("YOU")) {
						deployBoat();
					} else {
						deployBoat(chooseRandomCoordinate());
					}
				} else {
					refreshBlacklist();
//					Debug mode
//					for (Coordinate coordinate : blackList) {
//						System.out.println("BL = " + coordinate.getCoordinate().toString());
//					}
					if (boatOwner.equals("FRANKY")) {
						System.out.println("The boat " + boatClass + " of " + boatOwner + " has been deployed.");
					} else {
						System.out.println("The boat " + boatClass + " of " + boatOwner + " has been deployed between "
								+ boatCoordinate.get(0).getCoordinate().toString() + " and "
								+ boatCoordinate.get(boatCoordinate.size() - 1).getCoordinate().toString());
					}

				}
			} else {

				for (int i = 1; i < boatSize; i++) {
					if (!checkBlacklist(ListOfCoordinate.values()[index + i * LINE_SIZE].toString())) {
						System.out.println(
								"Boat.checkBlacklist() in calculOtherCoordinate error : otherCoordinate value is already used. Please enter another deploy Coordinate value.");
						testBL = false;
						break;
					}
					Coordinate otherCoordinate = new Coordinate(boatOwner);
					otherCoordinate.setCoordinate(ListOfCoordinate.values()[index + i * LINE_SIZE].toString());
					boatCoordinate.add(otherCoordinate);
				}
				if (!testBL || (boatCoordinate.get(0).getCoordinate().toString().charAt(
						0) != (boatCoordinate.get(boatCoordinate.size() - 1).getCoordinate().toString().charAt(0)))) {
					System.out.println(
							"Boat.calculOtherCoordinate() error : deployCoordinate value is incorrect, at least one part of the boat is outside the grid or the blacklist has been triggered.");
					boatCoordinate.clear();
					if (boatOwner.equals("YOU")) {
						deployBoat();
					} else {
						deployBoat(chooseRandomCoordinate());
					}
				} else {
					refreshBlacklist();
//					Debug mode
//					for (Coordinate coordinate : blackList) {
//						System.out.println("BL = " + coordinate.getCoordinate().toString());
//					}
					if (boatOwner.equals("FRANKY")) {
						System.out.println("The boat " + boatClass + " of " + boatOwner + " has been deployed.");
					} else {
						System.out.println("The boat " + boatClass + " of " + boatOwner + " has been deployed between "
								+ boatCoordinate.get(0).getCoordinate().toString() + " and "
								+ boatCoordinate.get(boatCoordinate.size() - 1).getCoordinate().toString());
					}
				}

			}
		} catch (ArrayIndexOutOfBoundsException e) {
			boatCoordinate.clear();
			System.out.println(
					"Boat.calculOtherCoordinate() error : deployCoordinate value is incorrect, at least one part of the boat is outside the grid.");
			if (boatOwner.equals("YOU")) {
				deployBoat();
			} else {
				deployBoat(chooseRandomCoordinate());
			}
		}

	}

	/**
	 * Checks if this coordinate is already used by another boat (so in the
	 * blacklist).
	 * 
	 * @param coordinate coordinate to check
	 * @return false if the coordinate is in the blacklist
	 */
	private boolean checkBlacklist(String coordinate) {
		// return false if the coordinate is in the blacklist
		for (Coordinate coordinateBL : blackList) {
			if (coordinate.equals(coordinateBL.getCoordinate().toString())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Adds coordinate value to the blacklist.
	 */
	private void refreshBlacklist() {
		for (Coordinate coordinate : boatCoordinate) {
			blackList.add(coordinate);
		}
	}

	/**
	 * Reset the blacklist after a fleet is fully deployed to allow the other player
	 * to use all his grid.
	 */
	public static void resetBlacklist() {
		blackList.clear();
	}

	/**
	 * Prints the size, direction, class, owner and location (all the coordinates)
	 * of a boat.
	 */
	public void printBoatSpecifications() {
		System.out.println("size : " + boatSize);
		System.out.println("direction : " + boatDirection);
		System.out.println("class : " + boatClass);
		System.out.println("owner : " + boatOwner);
		for (Coordinate coordinate : boatCoordinate) {
			System.out.println("coordinate : " + coordinate.getCoordinate());
		}

	}

	/**
	 * Checks if the variable boatClass is a correct value (see ClassOfBoat).
	 * 
	 * @param boatClass the class of this boat
	 * @return the boatClass if the value is correct
	 */
	private String checkBoatClass(String boatClass) {
		for (ClassOfBoat type : ClassOfBoat.values()) {
			if (boatClass.equals(type.toString())) {
				return boatClass;
			}
		}
		System.out.println("Boat.checkBoatClass() error : boatClass value is incorrect.");
		return "Error";
	}

	/**
	 * Calculates the size of the boat thanks to its class.
	 * 
	 * @param boatClass the class of this boat
	 * @return the size of the boat
	 */
	private int calculBoatSize(String boatClass) {
		if (boatClass.equals("")) {
			System.out.println("Boat.calculBoatSize() error : boatClass value is incorrect.");
			return 0;
		} else {
			return ClassOfBoat.getBoatSize(boatClass);
		}
	}

	/**
	 * Checks if the variable boatDirection is correct
	 * 
	 * @param boatDirection direction of deployment of your boat
	 * @return true if the direction is correct (see Directions)
	 */
	private boolean checkBoatDirection(String boatDirection) {
		for (Directions direction : Directions.values()) {
			if (boatDirection.equals(direction.toString())) {
				return true;
			}
		}
		System.out.println("Boat.checkBoatDirection() error : boatDirection value is incorrect.");
		return false;
	}

	/**
	 * 
	 * To get the List of Coordinate of a boat.
	 * 
	 * @return List of Coordinate of this boat
	 */
	public List<Coordinate> getBoatCoordinate() {
		return boatCoordinate;
	}

	/**
	 * <pre>
	 * Caculates randomly a direction to deploy a boat.
	 * Designed for owner "FRANKY" only.
	 * </pre>
	 * 
	 * @return a random correct direction (See Directions)
	 */
	public static String chooseRandomDirection() {
		int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
		if (randomNum == 0) {
			return "X";
		} else {
			return "Y";
		}
	}

	/**
	 * <pre>
	 * To get a random correct coordinate value.
	 * Designed for owner "FRANKY" only.
	 * </pre>
	 * 
	 * @return a random correct coordinate (see ListOfCoordinate)
	 */
	public static String chooseRandomCoordinate() {
		return ListOfCoordinate.values()[ThreadLocalRandom.current().nextInt(0, ListOfCoordinate.values().length - 1)]
				.toString();
	}

}
