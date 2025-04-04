import java.util.Random;

public class Pokemon implements Comparable<Pokemon>
{
    private String name;
    private int combatPoints;
    private double fleeRate;

    // constructor
    public Pokemon(String name, int maxCombatPoints, double fleeRate)
    {
        this.name = name.trim();
        this.fleeRate = fleeRate;
        // randomly allocate cp value up to the maximum
        Random randomGenerator = new Random();
        final int randomCombatPoints = randomGenerator.nextInt(maxCombatPoints);
        // ensure cp is not less than 10
        this.combatPoints = Math.max(10, randomCombatPoints);
    }

    // getters
    public String getName()
    {
        return name;
    }

    public int getCombatPoints()
    {
        return combatPoints;
    }

    @Override
    public int compareTo(Pokemon pokemon)
    {
        return this.combatPoints - pokemon.combatPoints;
    }

    // methods
    enum CaptureResult
    {
        FAIL, FLEE, CAUGHT
    }

    private double getEscapeChance (PokeBall ball)
    {
        double escapeChance = combatPoints / 1000.0;    // max cp will be 1000

        switch (ball)
        {
            case GREAT:
                escapeChance *= 0.66666;
                break;
            case ULTRA:
                escapeChance *= 0.33333;
        }
        return escapeChance;
    }

    private boolean shouldFlee()
    {
        Random random = new Random();
        boolean flee = random.nextDouble() < fleeRate;
        return flee;
    }

    CaptureResult attemptCapture(PokeBall ball)
    {
        final double escapeChance = getEscapeChance(ball);
        Random random = new Random();
        final double chance = random.nextDouble();

        if(chance > escapeChance)
        {
            return CaptureResult.CAUGHT;
        }

        if(shouldFlee())
        {
            return CaptureResult.FLEE;
        }
        else
        {
            return CaptureResult.FAIL;
        }
    }
}
