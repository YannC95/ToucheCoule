package fr.isika.cda25.program;

import java.util.ArrayList;
import java.util.List;

import fr.isika.cda25.entities.*;

public class Run {

	public static void main(String[] args) {

//		 Each player begins with 5 boats :
//			1 Carrier (5)
//			1 Battleship (4)
//			1 Destroyer (3)
//			1 Submarine (3)
//			1 Patrol Boat (2)
//		You have to deploy your boat inside a 10x10 grid (A1 to J10)
//		You can choose the first coordinate and the direction.
//		The other part of the boat will be deployed towards the J (direction X)
//		or towards the 10 (Y direction)

//		Debug mode
		boolean frankysGridVisible = false;

//		Creating + deploying Your fleet
		List<Boat> myFleet = new ArrayList<>();
		System.out.println("Waiting for parameters to create your fleet...\n");
		for (ClassOfBoat boatClass : ClassOfBoat.values()) {
			Boat myBoat = new Boat(boatClass.toString(), "YOU");
			myBoat.deployBoat();
			myFleet.add(myBoat);
		}
		System.out.println("\n...your fleet has been created !");

//		Making Your Grid
		Grid myGrid = new Grid("YOU", myFleet);

//		Reset Blacklist before creating Franky's fleet
		Boat.resetBlacklist();

//		Creating + deploying Franky's fleet
		List<Boat> frankysFleet = new ArrayList<>();
		System.out.println("\nProcessing the creation of Franky's fleet...\n");
		for (ClassOfBoat boatClass : ClassOfBoat.values()) {
			Boat frankysBoat = new Boat(boatClass.toString(), "FRANKY", Boat.chooseRandomDirection());
			frankysBoat.deployBoat(Boat.chooseRandomCoordinate());
			frankysFleet.add(frankysBoat);
		}
		System.out.println("\n...Franky's fleet has been created !");

//		Making Franky's Grid
		Grid frankysGrid = new Grid("FRANKY", frankysFleet);

//		Printing the grid before launching the game
		myGrid.printGrid();
		if (frankysGridVisible) {
			frankysGrid.printGrid();
		}
		System.out.println("\nLet's begin the game !");

//		Launch of the game
		do {
			frankysGrid.hit("YOU");
			if (frankysGridVisible) {
				frankysGrid.printGrid();
			}
			myGrid.hit("FRANKY", Grid.chooseHitCoordinate());
			myGrid.printGrid();
		} while (frankysGrid.getNumberOfRemainingCoordinate() > 0 && myGrid.getNumberOfRemainingCoordinate() > 0);

//		Ending the game : printing the winner
		printWinner(frankysGrid, myGrid);

	}

	/**
	 * Prints the winner of the game.
	 * 
	 * @param frankysGrid Franky's Grid
	 * @param myGrid      Your grid
	 */
	public static void printWinner(Grid frankysGrid, Grid myGrid) {
		if (frankysGrid.getNumberOfRemainingCoordinate() == 0) {
			Grid.printDashedLine();
			System.out.println(
					"                                             YOU WIN !                                              ");
			Grid.printDashedLine();
		} else if (myGrid.getNumberOfRemainingCoordinate() == 0) {
			Grid.printDashedLine();
			System.out.println(
					"                                            GAME OVER !                                             ");
			Grid.printDashedLine();
		}
	}
}
