package fr.isika.cda25.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <pre>
 * To make the grid of each player.
 * To use the hit(String coordinate) method and inform us of the end of the game.
 * </pre>
 * 
 * @author yann
 *
 */
public class Grid extends Owner {

	private static Scanner input = new Scanner(System.in);
	private List<Boat> fleet;
	private List<Coordinate> occupiedCoordinate;
	private List<Coordinate> finalGrid;
	private static List<String> availableCoordinate = generateAvailableCoordinate();
	private static List<String> nextLogicCoordinate = new ArrayList<>();

	/**
	 * <pre>
	 * To make the final Grid (with location of boats) of each player.
	 * Will call methods to do it.
	 * </pre>
	 * 
	 * @param boatOwner owner of the Grid
	 * @param fleet     List of all the owner's Boat
	 */
	public Grid(String boatOwner, List<Boat> fleet) {
		super(boatOwner);
		this.fleet = fleet;
		this.occupiedCoordinate = calculOccupiedCoordinate();
		this.finalGrid = makingGrid();
	}

	/**
	 * <pre>
	 * Prints this Grid (with location of the owner's boats).
	 * Every modification of the grid will be print when this method is called.
	 * </pre>
	 */
	public void printGrid() {
		String print = "";
		String format = "%-2s%s%n";
		String print2 = "";
		int k = 1;
		int j = 0;
		System.out.println("\nThe grid of " + boatOwner + " :");
		System.out.printf(format, "", "    A    B    C    D    E    F    G    H    I    J");
		for (int i = 0; i < ListOfCoordinate.values().length; i++) {
			if ((i % 10) == 0) {
				print2 = Integer.toString(k);
				k++;
			}

			print += "    " + finalGrid.get(i).getStatus();

			if (j == 9) {
				System.out.printf(format, print2, print);
				print = "";
				j = -1;
			}
			j++;
			if (i == ListOfCoordinate.values().length - 1) {
				System.out.println();
			}
		}

	}

	/**
	 * <pre>
	 * Allows Franky to shoot in your grid.
	 * Will call methods to optimize the choice of the coordinate.
	 * Designed for owner "FRANKY" only.
	 * </pre>
	 * 
	 * @param shooter          "FRANKY"
	 * @param targetCoordinate coordinate in your grid to shoot
	 */
	public void hit(String shooter, String targetCoordinate) {
		if (shooter.equals(boatOwner)) {
			System.out.println("\nGrid.hit error : the shooter and the target are the same player.");
		} else {
			if (shooter.equals("FRANKY")) {
				System.out.println("\n------------ Franky's turn.");
				int index = ListOfCoordinate.valueOf(targetCoordinate).ordinal();
				if (finalGrid.get(index).getStatus().equals("x")) {
					System.out.println("+++++++++++++++++++++++++++ Franky shoot in " + targetCoordinate
							+ " and hit one of your boats ! +++++++++++++++++++++++++");
					finalGrid.get(index).setStatus("-");
					char firstChar = finalGrid.get(index).getCoordinate().charAt(0);
//						System.out.println("firstChar = "+firstChar);
					if ((index - 10 >= 0) && (index - 10 < ListOfCoordinate.values().length)) {
						nextLogicCoordinate.add(ListOfCoordinate.values()[index - 10].toString());
					}
					if ((firstChar != 'A') && (index - 1 >= 0) && (index - 1 < ListOfCoordinate.values().length)) {
						nextLogicCoordinate.add(ListOfCoordinate.values()[index - 1].toString());
					}
					if ((firstChar != 'J') && (index + 1 >= 0) && (index + 1 < ListOfCoordinate.values().length)) {
						nextLogicCoordinate.add(ListOfCoordinate.values()[index + 1].toString());
					}
					if ((index + 10 >= 0) && (index + 10 < ListOfCoordinate.values().length)) {
						nextLogicCoordinate.add(ListOfCoordinate.values()[index + 10].toString());
					}
//						for (int i = 0; i < nextLogicCoordinate.size(); i++) {
//							System.out.println(nextLogicCoordinate.get(i));
//						}
				} else {
					System.out.println("Franky shoot in " + targetCoordinate + " and miss your boats !");
				}
			} else {
				System.out.println("\nGrid.hit error : this method is design for Franky's to shoot.");
			}
		}

	}

	/**
	 * <pre>
	 * Allows you to shoot in Franky's grid.
	 * Asks and records the coordinate you want to shoot.
	 * There is no special method calls, you are free to shoot wherever you want in Franky's Grid.
	 * Designed for owner "YOU" only.
	 * </pre>
	 * 
	 * @param shooter "YOU"
	 */
	public void hit(String shooter) {
		if (shooter.equals(boatOwner)) {
			System.out.println("\nGrid.hit error : the shooter and the target are the same player.");
		} else {

			boolean test = false;
			if (shooter.equals("YOU")) {
				printDashedLine();
				System.out.println("---------------- Your turn.");
				do {
					System.out.println("Where do you want to shoot ?");
					String targetCoordinate = input.nextLine();
					for (ListOfCoordinate coordinate : ListOfCoordinate.values()) {
						if (targetCoordinate.equals(coordinate.toString())) {
							test = true;
							break;
						}
					}
					if (test) {
						int index = ListOfCoordinate.valueOf(targetCoordinate).ordinal();
						if (finalGrid.get(index).getStatus().equals("x")) {
							System.out.println("++++++++++++++++++++++++++ You shoot in " + targetCoordinate
									+ " and hit one of Franky's boats ! +++++++++++++++++++++++++");
							finalGrid.get(index).setStatus("-");
						} else {
							System.out.println("You shoot in " + targetCoordinate + " and miss Franky's boats !");
						}
					} else {
						System.out.println(
								"\nGrid.hit error : the targetCoordinate is incorrect. Please enter a value between "
										+ ListOfCoordinate.values()[0].toString() + " and "
										+ ListOfCoordinate.values()[ListOfCoordinate.values().length - 1].toString());
					}
				} while (!test);
			} else {
				System.out.println("\nGrid.hit error : this method is design for You to shoot.");
			}
		}

	}

	/**
	 * To make the final grid (with location of boats).
	 * 
	 * @return List of Coordinate (the finals coordinate of the Grid)
	 */
	private List<Coordinate> makingGrid() {
		List<Coordinate> grid = new ArrayList<>();
		for (ListOfCoordinate coordinate : ListOfCoordinate.values()) {
			boolean test = false;
			for (Coordinate occCoordinate : occupiedCoordinate) {
				if (coordinate.toString().equals(occCoordinate.getCoordinate())) {
					grid.add(occCoordinate);
					test = true;
					break;
				}
			}
			if (!test) {
				Coordinate emptyCoordinate = new Coordinate(boatOwner);
				emptyCoordinate.setCoordinate(coordinate.toString());
				grid.add(emptyCoordinate);
			}
		}
		return grid;
	}

	/**
	 * <pre>
	 * To get the number of remaining coordinates (ie the number of remaining parts of boats).
	 * If this number drops to 0, the Grid's owner loose the game.
	 * </pre>
	 * 
	 * @return number of remaining coordinate
	 */
	public int getNumberOfRemainingCoordinate() {
		int numberOfDestroyedCoordinate = 0;
		for (Coordinate coordinate : finalGrid) {
			if (coordinate.getStatus().equals("-")) {
				numberOfDestroyedCoordinate++;
			}
		}
		return occupiedCoordinate.size() - numberOfDestroyedCoordinate;
	}

	/**
	 * <pre>
	 * Prints the number of remaining coordinates (ie the number of remaining parts of boats).
	 * (debug method)
	 * </pre>
	 */
	public void printNumberOfRemainingCoordinate() {
		if (boatOwner.equals("YOU")) {
			System.out.println(boatOwner + " have " + getNumberOfRemainingCoordinate() + " parts of boats remainings.");
		} else if (boatOwner.equals("FRANKY")) {
			System.out.println(boatOwner + " has " + getNumberOfRemainingCoordinate() + " parts of boats remainings.");
		}
	}

	/**
	 * <pre>
	 * Calculates the occupied in this Grid (ie the location of the boats).
	 * Will call the Coordinate.setStatus(String status) method, essential step to make the final Grid.
	 * </pre>
	 * 
	 * @return List of Coordinate which are occupied in this Grid
	 */
	private List<Coordinate> calculOccupiedCoordinate() {
		List<Coordinate> temp = new ArrayList<>();
		for (Boat boat : fleet) {
			for (Coordinate coordinate : boat.getBoatCoordinate()) {
				coordinate.setStatus("x");
				temp.add(coordinate);
			}
		}
		return temp;
	}

	/**
	 * <pre>
	 * Generates a Grid based on the ListOfCoordinate.
	 * At this step, the Grid does not contains the location of the boats.
	 * </pre>
	 * 
	 * @return Grid without occupied coordinates (location of boats)
	 */
	private static List<String> generateAvailableCoordinate() {
		List<String> temp = new ArrayList<>();
		for (ListOfCoordinate coordinate : ListOfCoordinate.values()) {
			temp.add(coordinate.toString());
		}
		return temp;
	}

	/**
	 * <pre>
	 * Randomly choose a coordinate to hit.
	 * If this coordinate has already been shot or if there are priority coordinates, there will be choose first. 
	 * Designed for owner "FRANKY" only.
	 * </pre>
	 * 
	 * @return the coordinate to shoot
	 */
	public static String chooseHitCoordinate() {
		String coordinate = "Error";
		boolean test = false;
		if (!nextLogicCoordinate.isEmpty()) {
			for (String logicCoordinate : nextLogicCoordinate) {
				for (String randomCoordinate : availableCoordinate) {
					if (randomCoordinate.toString().equals(logicCoordinate.toString())) {
						coordinate = logicCoordinate.toString();
						nextLogicCoordinate.remove(logicCoordinate);
						availableCoordinate.remove(randomCoordinate);
						test = true;
						break;
					}
				}
				if (test) {
					break;
				}
			}
		}
		if (!test) {
			nextLogicCoordinate.clear();
			int randomNum = 0;
			randomNum = ThreadLocalRandom.current().nextInt(0, availableCoordinate.size());
			coordinate = availableCoordinate.get(randomNum);
			availableCoordinate.remove(randomNum);
		}
		return coordinate;
	}

	/**
	 * <pre>
	 * Prints this Grid's occupied coordinates (ie the location of the boats).
	 * (debug method)
	 * </pre>
	 */
	public void printOccupiedCoordinate() {
		System.out.println();
		for (Coordinate coordinate : occupiedCoordinate) {
			System.out.println(coordinate.getCoordinate() + " : " + coordinate.getStatus());
		}
	}

	/**
	 *
	 * Prints a dashed line to delimit a round or the end of the game.
	 */
	public static void printDashedLine() {
		System.out.println(
				"\n----------------------------------------------------------------------------------------------------\n");
	}
}
