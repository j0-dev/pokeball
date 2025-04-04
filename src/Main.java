import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        PokeballGame game = new PokeballGame();
        Scanner inputScanner = new Scanner(System.in);

        while (true)
        {
            //
            System.out.println("\nWhat do you want to do? [L]ook for a Pokemon, " +
                    "view your [F]ound Pokemon, [I]tems, " +
                    "[V]isit Pokestop, [E]xit?");
            String input = inputScanner.nextLine();

            //
            switch (input.toUpperCase())
            {
                case "L":
                    findPokemon(game);
                    break;
                case "F":
                    displayInventory(game);
                    break;
                case "V":
                    System.out.println(game.visitPokestop());
                    break;
                case "I":
                    displayBallCounts(game);
                    break;
                case "E":
                    return;    // exits programme
                default:
                    break;    // invalid input does nothing
            }
        }
    }

    private static void findPokemon(PokeballGame game)
    {
        if(game.lookForPokemon())
        {
            Pokemon pokemon = game.getFoundPokemon();
            System.out.println("\nPokemon found: " + pokemon.getName() +
                    ", Combat Points: " + pokemon.getCombatPoints());

            while(true)
            {
                //
                displayBallCounts(game);
                System.out.println("\nChoose: [B]all, [G]reat ball or [U]ltra ball ([E]xit):");
                Scanner inputScanner = new Scanner(System.in);
                String ballType = inputScanner.nextLine();

                if(ballType.equalsIgnoreCase("E"))
                {
                    break;
                }

                //
                PokeBall ball = PokeBall.STANDARD;

                switch(ballType.toUpperCase())
                {
                    case "U":
                        ball = PokeBall.ULTRA;
                        break;
                    case "G":
                        ball = PokeBall.GREAT;
                        break;
                }

                Pokemon.CaptureResult result = game.attemptToCatchPokemon(ball);
                if(result == null)
                {
                    System.out.println("\nAttempt failed. No Pokeballs!");
                    return;
                }

                switch (result)
                {
                    case CAUGHT:
                        System.out.println("\nYou caught " + pokemon.getName() +
                                "! Combat Points: " + pokemon.getCombatPoints());
                        return;    // catch loop exited - pokemon caught
                    case FAIL:
                        System.out.println("\nFailed to catch Pokemon!");
                        break;    // catch loop restarts
                    case FLEE:
                        System.out.println("\nThe Pokemon has fled!");
                        return;    // exited
                }
            }
        }
        else
        {
            System.out.println("\nDid not find Pokemon.");
        }
    }

    private static void displayBallCounts(PokeballGame game)
    {
        int ballCount = game.getBallCount(PokeBall.STANDARD);
        int greatBallCount = game.getBallCount(PokeBall.GREAT);
        int ultraBallCount = game.getBallCount(PokeBall.ULTRA);

        System.out.println("\nYou have " + ballCount + " STANDARD balls, " +
                greatBallCount + " GREAT balls, and " + ultraBallCount +  " ULTRA balls.");
    }

    private static void displayInventory(PokeballGame game)
    {
        ArrayList<Pokemon> pokemons = game.getCaughtPokemon();

        System.out.println("\nPokemon you've caught:");
        if(!pokemons.isEmpty())
        {
            for (Pokemon pokemon : pokemons)
            {
                System.out.println(pokemon.getName() + ": " + pokemon.getCombatPoints() + " combat points");
            }
            System.out.println();
        }
        else
        {
            System.out.println("\nYou have not caught any Pokemon.\n");
        }
    }

    private static void comparePokemon(Pokemon p1, Pokemon p2)
    {
        int result = p1.compareTo(p2);

        System.out.println("Comparing " + p1 + " and " + p2);
        if (result < 0)
        {
            System.out.println(p1.getName() + "is stronger.");
        }
        else if (result > 0)
        {
            System.out.println(p2.getName() + "is stronger.");
        }
        else
        {
            System.out.println("They are equal in strength.");
        }
    }
}