package info.gridworld.maze;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;
import info.gridworld.actor.Flower;
import info.gridworld.grid.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.HashMap;

import javax.swing.JOptionPane;

/**
 * A <code>MazeBug</code> can find its way in a maze. <br />
 * The implementation of this class is testable on the AP CS A and AB exams.
 */
public class MazeBug extends Bug {
	public Location next;
	public Location last;
	public boolean isEnd = false;

    // each ArrayList<Location> contains <src, visiterA, visitedB...(4 dirs)>
    // hence the name cross(+) location.
	public Stack<ArrayList<Location>> crossLocation = new Stack<ArrayList<Location>>();
	public Integer stepCount = 0;
	boolean hasShown = false;//final message has been shown

    // number of times the direction is chosen
    // 0 - NORTH, 1 - EAST, 2 - SOUTH, 3 - WEST
    int[] dirCount = { 0, 0, 0, 0 };
    int[] validDirections = { Location.NORTH, Location.EAST, Location.SOUTH, Location.WEST }

	/**
	 * Constructs a box bug that traces a square of a given side length
	 * 
	 * @param length
	 *            the side length
	 */
	public MazeBug() {
		setColor(Color.GREEN);
		last = new Location(0, 0);

        // initialize the count for each valid direction
        dirCount = new HashMap<int, int>();
        for (int d : validDirections)
        {
            dirCount.put(d, 0);
        }

        // TODO push <(0,0)> into the stack
        ArrayList<Location> firstCross = new ArrayList<Location>();
        firstCross.add(last);
        crossLocation.push(firstCross);
	}

	/**
	 * Moves to the next location of the square.
	 */
	public void act() {
		boolean willMove = canMove();
		if (isEnd == true) {
            //to show step count when reach the goal		
            if (hasShown == false) {
                String msg = stepCount.toString() + " steps";
                JOptionPane.showMessageDialog(null, msg);
                hasShown = true;
            }
        } else if (willMove) {
            move();
            //increase step count when move 
            stepCount++;
        } else {  // not the end but can't move
            back(); 
            stepCount++;
        }
	}

	/**
	 * Find all positions that can be move to.
     * Visted locations are excluded.
	 * 
	 * @param loc
	 *            the location to detect.
	 * @return List of positions.
	 */
	public ArrayList<Location> getValid(Location loc) {
		Grid<Actor> gr = getGrid();
		if (gr == null)
			return null;
		ArrayList<Location> valid = new ArrayList<Location>();
		
        // return an empty list if the stack is empty
        if (crossLocation.size() <= 0)
            return valid;

        // TODO check each of the 4 directions
        for (int d : validDirections)
        {
            Location check = loc.getAdjacentLocation(d);

            // if it is out of the grid, skip
            if (!gr.isValid(check))
                continue;

            Actor neighbor = (Actor) gr.get(check);
            // TODO if it is null, add it
            if (neighbor == null)
            {
                valid.add(check);
            }
            // TODO if it is a red rock, add it
            else if (neighbor instanceof Rock && neighbor.getColor().equals(Color.RED))
            {
                valid.add(check); 
            }
        }

		return valid;
	}

	/**
	 * Tests whether this bug can move forward into a location that is empty or
	 * contains a flower.
	 * 
	 * @return true if this bug can move.
	 */
	public boolean canMove() {
        // TODO get valid adjacent locations
		Location loc = getLocation();
        ArrayList<Location> valid = getValid(loc);

        // if there is no valid location around
        if (valid.size() <= 0)
        {
            return false;
        } else {
            return true;
        }
    }

    private void pickNext()
    {
        // TODO get valid adjacent locations
		Location loc = getLocation();
        ArrayList<Location> valid = getValid(loc);

        int maxCount = 0;
        for (Location check : valid)
        {
            Actor neighbor = (Actor) gr.get(check);
            int dirIndex = loc.getDirectionToward(check) / 90;
            // TODO check if the end is in them, move to the end if it is
            if (neighbor instanceof Rock && neighbor.getColor().equals(Color.RED))
            {
                last = next;
                next = check;
                break;
            }
            // TODO choose the one with max probability
            if (dirCount[dirIndex]) >= maxCount)  
            {
                maxCount = dirCount[dirIndex];
                last = next;
                next = check;
            }
        }
   
    }

	/**
	 * Moves the bug forward, putting a flower into the location it previously
	 * occupied.
	 */
	public void move() {
		Grid<Actor> gr = getGrid();
		if (gr == null)
			return;

        // pick next location (via field next and last)
        // assert: canMove -- getValid.size != 0 -- next != here
        pickNext();

		if (!gr.isValid(next)) {
			removeSelfFromGrid();
            return;
        }

        // move to the next location
        Location loc = getLocation();
        setDirection(loc.getDirectionToward(next));
        moveTo(next);

        // if the end is reached, mark it
        Actor nextActor = (Actor) gr.get(next);
        if (nextActor instanceof Rock)
        {
            isEnd = true;
        }

        // TODO increase the probability for this direction
        int dirIndex = loc.getDirectionToward(next) / 90;
        dirCount[dirIndex]++;

        // drop flower at the previous location
        dropFlower(loc);

        // TODO add next to the end of the top linked list (visited from here)
        // <loc, ..., next>
        crossLocation.peek().add(next);

        // TODO push a new <(next)> into the stack
        // now the top looks like
        // <next>
        // <loc, ..., next>
        ArrayList<Location> newTop = new ArrayList<Location>();
        newTop.add(next);
        crossLocation.push(newTop);
	}

    // TODO drop flower at loc
    private void dropFlower(Location loc)
    {
		Grid<Actor> gr = getGrid();
		if (gr == null)
			return;
		Flower flower = new Flower(getColor());
		flower.putSelfInGrid(gr, loc);
    }

    // TODO move back to src
    private void back()
    {
		Grid<Actor> gr = getGrid();
		if (gr == null)
			return;

        // the stack is empty, there is no way to go back
        if (crossLocation.size() <= 0)
            return;

        Location loc = getLocation();

        // pop out the top(contains current location)   
        crossLocation.pop();

        // if there is no source, remove self from grid and return
        if (crossLocation.size() <= 0)
        {
            removeSelfFromGrid();
            return;
        }

        // get the src of top. which is [0] at
        // the linked list one way below the top
        next = crossLocation.peek().get(0);
        last = loc;

        // drop flower
        dropFlower(loc);

        // move back to src
        setDirection(loc.getDirectionToward(next));
        moveTo(next);
    }
}