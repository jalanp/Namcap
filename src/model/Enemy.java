//This code follows the Google Java Standards referenced at https://google.github.io/styleguide/javaguide.html
/**
* @file Enemy.java
* @title Enemy
* @author VPB Game Studio
* @date 13/11/2016
* @brief This class represents an Enemy in the game.
* @details This class represents all the functionalities and attributes of an Enemy in the game.	
* @extends Character
*/
package model;

import java.util.ArrayList;
import java.util.Random;

public class Enemy extends Character {
	
	/**
	 * Boolean that represents if player collected big dot or not.
	 */
	public boolean bigDotEaten = false;
	/**
	* Square grid width (pixels)
	*/
	static final int SQUAREWIDTH = 20;
	/**
	* Constant for starting grid position
	*/
	static final int STARTX = 10;
	/**
	* Constant for starting grid position
	*/
	static final int STARTY = 9;
	 /** 
	 * @brief Constructor for Enemy
	 * @details Constructor accepts one parameter for the game's Board model.
     * @param board - Board object that is passed in to add character (in this case, enemy) functionality.
     */
	public Enemy(Board board) {
		super(board);
		this.board.setEnemy(this);
		this.prevX = STARTX*this.sq;
		this.prevY = STARTY*this.sq;
		this.currX = STARTX*this.sq;
		this.currY = STARTY*this.sq;
		this.bigDotEaten = false;
	}
	
	/**
	* @brief Method returns whether or not Enemy can keep moving in the same direction
	* @details This method determines whether the Enemy can keep moving in the current direction it is headed.
	* @param direction - Char value representing the Enemy's current direction.
	* @return Boolean value (true - direction remains valid, or false - direction invalid).
	*/
	public boolean keepMoving(char direction) {		
		switch(direction) {
		case 'L':
			return (!this.isBarrier(this.currX - this.pixelInc, this.currY));
		case 'R':
			return (!this.isBarrier(this.currX + this.sq, this.currY));
		case 'U':
			return (!this.isBarrier(this.currX, this.currY - this.pixelInc));
		case 'D':
			return (!this.isBarrier(this.currX, this.currY + this.sq));
		default:
			return true;
		}
	}
	/**
	 * @brief Method returns an array list of directions
	 * @details This method determines if the Enemy is in a position where it can go in multiple directions. It 
	 * returns an array list of the available directions.
	 * @return ArrayList<String> containing the letters corresponding to the available directions
	 */
	public ArrayList<String> newPath() {
		ArrayList<String> directions = new ArrayList<String>();
		
		if ((!this.isBarrier(this.currX - this.pixelInc, this.currY)) && this.prevDirection != 'R') {
			directions.add("L");
		}
		if ((!this.isBarrier(this.currX + this.sq, this.currY)) && this.prevDirection != 'L') {
			directions.add("R");
		}
		if ((!this.isBarrier(this.currX, this.currY - this.pixelInc)) && this.prevDirection != 'D') {
			directions.add("U");
		}
		if ((!this.isBarrier(this.currX, this.currY + this.sq)) && this.prevDirection != 'U') {
			directions.add("D");
		}
		if ((this.isBarrier(this.currX - this.pixelInc, this.currY)) && (this.isBarrier(this.currX, this.currY - this.pixelInc)) && (this.isBarrier(this.currX, this.currY + this.sq))) {
			directions.add("R");
		}
		if ((this.isBarrier(this.currX + this.sq, this.currY)) && (this.isBarrier(this.currX, this.currY - this.pixelInc)) && (this.isBarrier(this.currX, this.currY + this.sq))) {
			directions.add("L");
		}
		return directions;
	}
	/**
	 * @brief Method returns a random direction based on the array list of directions passed in
	 * @details This method randomly picks a direction for the Enemy to move to from the ArrayList of directions passed in.
	 * @param directions - ArrayList<String> of letters representing the directions that the Enemy is able to move in.
	 * @return returns a char value of the new direction of the Enemy
	 */
	public char randDirection(ArrayList<String> directions) {
		int size = directions.size();
		if (directions.size() > 1) {
			Random randomGenerator = new Random();
			int randomDirection = randomGenerator.nextInt(size) + 1;
			switch(randomDirection) {
				case 1:
					return directions.get(0).charAt(0);
				case 2:
					return directions.get(1).charAt(0);
				case 3:
					return directions.get(2).charAt(0);
				case 4:
					return directions.get(3).charAt(0);
			}
		} else {
			return directions.get(0).charAt(0);
		}
		
		return '!'; //code unreachable
	}

	/**
	* @brief Determines a valid path for the Enemy to move
	* @details This method is the Enemy's AI. It is used to change the direction of the Enemy's movement when the Enemy
	* hits a barrier. The new direction is chosen randomly.
	*/
	public void enemyMove() {
		if (keepMoving(this.prevDirection)) {
			this.newDirection = this.prevDirection;
		}
		
		this.prevX = this.currX;
		this.prevY = this.currY;
		
		//if Enemy in a grid square
		if (this.currX % SQUAREWIDTH == 0 && this.currY % SQUAREWIDTH == 0){
			this.newDirection = this.randDirection(this.newPath());
			switch(this.newDirection) {
			case 'L':
				if (!this.isBarrier(this.currX - this.pixelInc, this.currY)) {
					this.currX -= this.pixelInc;
				}
				break;
			case 'R':
				if (!this.isBarrier(this.currX + this.sq, this.currY)) {
					this.currX += this.pixelInc;
				}
				break;
			case 'U':
				if (!this.isBarrier(this.currX, this.currY - this.pixelInc)) {
					this.currY -= this.pixelInc;
				}
				break;
			case 'D':
				if (!this.isBarrier(this.currX, this.currY + this.sq)) {
					this.currY += this.pixelInc;
				}
				break;
			}
			
			this.prevDirection = this.newDirection;
			
		} else { //else if Enemy not in grid square (i.e. Enemy keeps moving)
			switch(this.newDirection) {
				case 'L':
					this.currX -= this.pixelInc;
					break;
				case 'R':
					this.currX += this.pixelInc;
					break;
				case 'U':
					this.currY -= this.pixelInc;
					break;
				case 'D':
					this.currY += this.pixelInc;
					break;
			}
		}
	}

	/**
	* @brief Resets the Enemy's position to its initial location
	*/
	public void resetPosition() {
		this.prevX = STARTX*this.sq;
		this.prevY = STARTY*this.sq;
		this.currX = STARTX*this.sq;
		this.currY = STARTY*this.sq;
	}
	
	/**
	 * @brief Reverses the Enemy's direction.
	 */
	public void reverseDirection() {
		switch (this.prevDirection) {
			case 'L':
				this.prevDirection = 'R';
				break;
			case 'R':
				this.prevDirection = 'L';
				break;
			case 'U':
				this.prevDirection = 'D';
				break;
			case 'D':
				this.prevDirection = 'U';
				break;
		}
		
	}
	
	/**
	 * @brief Changes the speed of the Enemy.
	 * @param speed - the amount of pixels to move the Enemy.
	 */
	public void changeSpeed(int speed) {
		this.pixelInc = speed;	
	}
}
