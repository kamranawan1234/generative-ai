package ga;

public class Individual
{
    private boolean[] chromosome;
    private int fitness;

    public Individual(int length)
    {
        chromosome = new boolean[length];
    }

    public boolean[] getChromosome()
    {
        return chromosome;
    }

    public void setGene(int index, boolean value)
    {
        chromosome[index] = value;
    }

    public int getFitness()
    {
        return fitness;
    }

    public void evaluateFitness()
    {
        fitness = 0;
        for (boolean gene : chromosome)
        {
            if (gene) fitness++;
        }
    }
}
