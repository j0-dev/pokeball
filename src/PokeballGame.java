import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class PokeballGame
{
    private int ballCount;
    private int greatBallCount;
    private int ultraBallCount;
    private ArrayList<Pokemon> pokemonsInWild;
    private ArrayList<Pokemon> caughtPokemon;
    private Pokemon foundPokemon;

    private Random randomNumberGenerator;

    private ArrayList<Pokemon> getAllPokemonFromFile()
    {
        ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>();
        File dataFile = new File("Data.dat");

        try
        {
            Scanner fileScanner = new Scanner(dataFile);
            fileScanner.useDelimiter(",");
            fileScanner.useLocale(Locale.UK);    // uses UK style decimal point

            while (fileScanner.hasNext())
            {
                final String name = fileScanner.next();
                final int combatPoints = fileScanner.nextInt();
                final double fleeRate = fileScanner.nextDouble();

                Pokemon pokemon = new Pokemon(name, combatPoints, fleeRate);
                pokemons.add(pokemon);
            }
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("Error loading Pokemon file: " + ex.getMessage());
        }

        return pokemons;
    }

    // constructor
    PokeballGame ()
    {
        randomNumberGenerator = new Random();
        caughtPokemon = new ArrayList<Pokemon>();

        ballCount = 5;
        greatBallCount = 2;
        ultraBallCount = 1;

        // load pokemon from file
        pokemonsInWild = getAllPokemonFromFile();
    }

    // getters
    public Pokemon getFoundPokemon()
    {
        return foundPokemon;
    }

    public ArrayList<Pokemon> getCaughtPokemon()
    {
        return caughtPokemon;
    }

    // methods
    String visitPokestop()
    {
        // todo: limit result of this method by time
        // todo: use singular of balls when finding only one ball
        int numOfStandardBalls = randomNumberGenerator.nextInt(4);    // between 0-3
        int numOfGreatBalls = randomNumberGenerator.nextInt(3);
        int numOfUltraBalls = randomNumberGenerator.nextInt(2);

        ballCount += numOfStandardBalls;
        greatBallCount += numOfGreatBalls;
        ultraBallCount += numOfUltraBalls;

        return "Gained " + numOfStandardBalls + " balls, " +
                numOfGreatBalls + " great balls and " +
                numOfUltraBalls + " ultra balls.";
    }

    int getBallCount(PokeBall ballType)
    {
        return switch (ballType)
        {
            case GREAT -> greatBallCount;
            case ULTRA -> ultraBallCount;
            default -> ballCount;
        };
    }

    private boolean useBall(PokeBall ball)
    {
        switch(ball)
        {
            case ULTRA:
                if(ultraBallCount > 0)
                {
                    ultraBallCount--;
                    return true;
                }
            case GREAT:
                if (greatBallCount > 0)
                {
                    greatBallCount--;
                    return true;
                }
            case STANDARD:
            default:
                if(ballCount > 0)
                {
                    ballCount--;
                    return true;
                }
        }

        return false;
    }

    boolean lookForPokemon()
    {
        double chance = randomNumberGenerator.nextDouble();

        if(chance > 0.5)
        {
            // random pokemon
            int pokemonPosition = randomNumberGenerator.nextInt(pokemonsInWild.size());
            foundPokemon = pokemonsInWild.get(pokemonPosition);
            return true;
        }
        foundPokemon = null;
        return false;
    }

    private void replacePokemon()
    {
        ArrayList<Pokemon> allAvailablePokemon = getAllPokemonFromFile();
        int randomIndex = randomNumberGenerator.nextInt(allAvailablePokemon.size());
        Pokemon randomPokemon = allAvailablePokemon.get(randomIndex);
        pokemonsInWild.add(randomPokemon);
    }

    Pokemon.CaptureResult attemptToCatchPokemon(PokeBall ball)
    {
        if(useBall(ball))
        {
            final Pokemon.CaptureResult captureResult = foundPokemon.attemptCapture(ball);

            switch (captureResult)
            {
                case CAUGHT:
                    caughtPokemon.add(foundPokemon);
                    pokemonsInWild.remove(foundPokemon);
                    replacePokemon();
                    foundPokemon = null;
                    break;
                case FAIL:
                    break;
                case FLEE:
                    foundPokemon = null;
            }

            return captureResult;
        }
        else
        {
            // no pokeballs
            return null;
        }
    }
}
