

# Genetic Algorithm Visualizer Report


## Introduction
Evolutionary computation is a way to solve problems by using ideas from natural evolution. Many beginners find it hard to understand how evolutionary algorithms work. Selection, crossover, mutation, and replacement can be confusing when they are only read about in books. This project is a graphical application that shows how a genetic algorithm works. The application lets users see how a population changes over time. Users can watch how the operators affect individuals in each generation. This makes learning easier because users can see the process instead of only reading about it.

The application uses a genetic algorithm. Each individual is represented as a string of genes. The genes can be active or inactive. Selection, crossover, mutation, and replacement operators are applied to the population in each generation. The application shows each operator visually so users can see what happens during the algorithm.
This project is an educational tool. It is not an experiment. Its purpose is to help users understand evolutionary algorithms by watching the process in real time. The application allows beginners to learn faster and more clearly by seeing the algorithm work step by step.

The program also gives users control over some important settings. Users can change the population size, the crossover rate, and the mutation rate. They can start and stop the simulation, step through each generation, and reset the population. These controls let users explore how changing the parameters affects the evolution.

## Functionality
The application starts by creating a population with the number of individuals chosen by the user. Each individual has a chromosome with a series of genes. At the beginning, all genes are inactive. In each generation, the application selects pairs of chromosomes to perform crossover. The selection process uses randomization combined with the crossover rate set by the user. Crossover is a single-point crossover. This means that a point is chosen in the parent chromosomes and the genes after this point are exchanged between the parents to produce offspring. The offspring inherit the good genes from both parents. Each offspring also receives a mutation rate that is calculated based on the mutation rates of the parents. The genes involved in crossover are shown as green squares.

After crossover, the algorithm applies mutation. Each gene in each individual has a chance to be flipped from inactive to active or from active to inactive. The probability of mutation is determined by the mutation rate. Mutated genes are shown as red squares. Genes that are inactive are gray squares. Genes that are active are dark green or blue squares. The darker or more blue the squares are, the higher the fitness of the individual. Individuals with the best fitness in the population are outlined in yellow. The visualization shows the left axis as the list of individuals in the population. The top axis shows the genes in each individual. The population changes in each generation, and users can watch how the selection, crossover, and mutation operators affect the genes.

At the top right, there is a stats panel that shows the current generation, the average fitness of all individuals, and the best fitness of any individual. Below the stats panel, there is a graph that shows the fitness values over all generations. Users can hover over the graph to see the exact fitness values for each generation. 

Below the fitness graph is a control panel. The control panel has buttons to start and stop the simulation, step one generation at a time, restart the population, and randomize the population. There are also sliders to adjust the population size, the crossover rate, and the mutation rate. The population size and crossover rate remain constant while the simulation runs. The mutation rate changes automatically for each offspring. This gives users the ability to see how changing parameters can change the outcome of the evolution.
The application runs the genetic algorithm continuously when the simulation is started. The population changes are updated in real time. Users can pause the simulation to observe details. The visualization shows clearly which genes are selected, which genes are mutated, and which individuals have the best fitness. This makes it easy to understand how evolutionary computation works in practice.

## Software Architecture
The application is written in Java using Java Swing for the graphical interface. The design separates the genetic algorithm from the graphical interface. The genetic algorithm has an individual class. Each individual stores its genes and fitness value. The algorithm implements selection, single-point crossover, mutation, and replacement. The population is stored as arrays. The fitness evaluation calculates which individuals are better than others.

The graphical interface has a panel that shows the population and highlights selected parents, crossover points, mutated genes, and the best individuals. There is a stats panel that shows the current generation, the average fitness, and the best fitness. There is a fitness graph that tracks changes over time. There is a control panel that lets the user start or stop the simulation, step through generations, restart, and randomize the population. The sliders let the user adjust the mutation rate, crossover rate, and population size.

The graphical interface gets the updated population from the algorithm every generation. It updates the visualization to reflect changes. The colors and highlights show the effects of selection, crossover, and mutation. The design keeps the algorithm separate from the interface. This makes the program easier to understand and maintain.
