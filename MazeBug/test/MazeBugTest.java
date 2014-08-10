import static org.junit.Assert.*;
import info.gridworld.actor.Actor;
import info.gridworld.actor.ActorWorld;
import info.gridworld.actor.Flower;
import info.gridworld.actor.Rock;
import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import info.gridworld.grid.UnboundedGrid;
import info.gridworld.maze.MazeBug;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MazeBugTest
{
    private String mapPath;

    @Parameters
    public static Collection<Object[]> data()
    {
        return Arrays
                .asList(new Object[][]
                {
                        { "/home/joyeecheung/2014training/stage3/MazeBug/map/FinalMaze01.txt" },
                        { "/home/joyeecheung/2014training/stage3/MazeBug/map/FinalMaze02.txt" },
                        { "/home/joyeecheung/2014training/stage3/MazeBug/map/FinalMaze03.txt" },
                        { "/home/joyeecheung/2014training/stage3/MazeBug/map/FinalMaze04.txt" },
                        { "/home/joyeecheung/2014training/stage3/MazeBug/map/FinalMaze05.txt" },
                        { "/home/joyeecheung/2014training/stage3/MazeBug/map/EasyMaze.txt" },
                        { "/home/joyeecheung/2014training/stage3/MazeBug/map/OneRoadMaze.txt" }
                });
    }

    public MazeBugTest(String mapPath)
    {
        this.mapPath = mapPath;
    }

    @Test
    public void testSteps()
    {
        try
        {
            FileReader reader = new FileReader(mapPath);
            BufferedReader br = new BufferedReader(reader);
            ActorWorld world = loadMap(br);
            Grid<Actor> grid = world.getGrid();
            // find the maze bug
            MazeBug bug = null;

            for (Location loc : grid.getOccupiedLocations())
            {
                Actor a = grid.get(loc);
                if (a instanceof MazeBug)
                    bug = (MazeBug) a;
            }

            assertNotNull("Can't find the MazeBug.", bug);
            assertSame("Can't find the MazeBug.", bug.getGrid(), grid);

            int count = 0;
            // while !isEnd, act
            while (!bug.isEnd && count < 1000)
            {
                bug.act();

                count++;
            }
            assertFalse("It takes too long to find the path", count >= 1000);

            PrintWriter pw = null;
            try
            {
                File file = new File(
                        "/home/joyeecheung/2014training/stage3/MazeBug/result.txt");
                if (!file.exists())
                {
                    file.createNewFile();
                }

                BufferedWriter bw = new BufferedWriter(
                        new FileWriter(
                                "/home/joyeecheung/2014training/stage3/MazeBug/result.txt",
                                true));
                pw = new PrintWriter(bw);
                pw.println(mapPath + '\t' + count);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (pw != null)
                {
                    pw.close();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private ActorWorld loadMap(BufferedReader br)
    {
        String str = null;
        ActorWorld world = new ActorWorld();

        try
        {
            str = br.readLine();
            if (str.equals("unbounded"))
            {
                world.setGrid(new UnboundedGrid<Actor>());
            }
            else
            {
                world.setGrid(new BoundedGrid<Actor>(10, 10));
            }

            while (((str = br.readLine()) != null))
            {
                String[] para = new String[4];
                para = str.split("\t");
                Color aColor = actorColor(para[3]);
                int direction = actorDirection(para[2]);
                Location loc = actorLocation(para[1]);
                Actor act = newActor(para[0]);
                act.setColor(aColor);
                act.setDirection(direction);
                world.add(loc, (Actor) act);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return world;
    }

    private Color actorColor(String s)
    {
        String[] rgb = s.split("=");
        String r = new String(), g = new String(), b = new String();
        r += rgb[2].split(",")[0];
        g += rgb[3].split(",")[0];
        b += rgb[4].split("]")[0];
        Color c = new Color(Integer.parseInt(r), Integer.parseInt(g),
                Integer.parseInt(b));
        return c;
    }

    private int actorDirection(String s)
    {
        String[] dir = s.split("=");
        int d = Integer.parseInt(dir[1]);
        return d;
    }

    private Location actorLocation(String s)
    {
        String[] loc = s.split("=");
        String x = new String(), y = new String();
        int i = 1;
        while (loc[1].charAt(i) != ',')
        {
            x += loc[1].charAt(i);
            i++;
        }
        i++;
        while (loc[1].charAt(i) != ')')
        {
            if ((loc[1].charAt(i) >= '0') && (loc[1].charAt(i) <= '9'))
                y += loc[1].charAt(i);
            i++;
        }
        Location l = new Location(Integer.parseInt(x), Integer.parseInt(y));
        return l;
    }

    private Actor newActor(String s)
    {
        String act = new String();
        String temp = new String();

        for (int i = s.length() - 1; i >= 0; i--)
        {
            if (s.charAt(i) == '.')
            {
                break;
            }
            temp += s.charAt(i);
        }
        for (int j = temp.length() - 1; j >= 0; j--)
        {
            act += temp.charAt(j);
        }

        Actor a = new Actor();
        if (act.equals("Rock"))
        {
            a = new Rock();
        }
        if (act.equals("Flower"))
        {
            a = new Flower();
        }
        if (act.equals("MazeBug"))
        {
            a = new MazeBug();
        }
        return a;
    }
}
