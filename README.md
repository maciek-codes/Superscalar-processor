# Superscalar-processor

The Superscalar Processor simuator was created for my Advanced Computer Architecture assignment.

The simulator runs programs written in an imaginary assembly language. See [programs](https://github.com/macqm/Superscalar-processor/tree/master/programs) folder for more.

## Features simulated

I simulate an in-order superscalar processor. I included the following features:

* Number of ALUs
 - option to choose one, two or four
* Global memory
* Register file
* Memory instruction execution unit
* Vector instruction execution unit
* Branch prediction
 - option to choose a static or dynamic branch predictor
 
## Building and runnning
The simulator was tested using Java 1.7 and uses Maven to build. 
The `maven build` command runs the build process while `maven package` will also create a JAR file

To run, provide program name as an argument.

You can also open the simulator in an interactive mode (using `-i`) and examine e.g. the register file.

Other options include:
 * `-predictor=static`, `-predictor=dyamic` or `-predictor=naive` to switch branch predictors
 * `-eNUM` where NUM is desired number of execution units
 * `-v` - verbose mode


