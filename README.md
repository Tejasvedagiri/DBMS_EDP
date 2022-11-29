# Edge-Disjoint Partitioning

Create sub-graphs and generate the shortest path for a graph

## Repository

https://github.com/Tejasvedagiri/DBMS_EDP

## Setting up environment

### Java (Java 11) and maven 3.3.

Download and install Java11 or higher.

```
git clone https://github.com/Tejasvedagiri/DBMS_EDP.git
cd /DBMS_EDP
```

### Compiling

```
maven clean install
```

## Arguments/Parameters for code

* Add Arguments
    * go to Run -> Run…
    * specify command line arguments for, and then select the arguments tab.
    * enter the arguments

```
  File Path   
  --File Path           		Local File path (dataset)
  --No. of partitions   		Number of label partitions 
  --Unique vertex count 		##
  --No. of queries to be executed       ##
  --Source node id (optional)		##
  --Target node id (optional)		##
  --Index partitions size      
```

## End to end Run
Sample command after maven clean install
```
mvn clean install
cd target
java -jar DBMS_EDP-jar-with-dependencies.jar data/unit_test.txt 3 9 100 1 9 2
java -jar DBMS_EDP-jar-with-dependencies.jar data/main_small.txt 46 3734 20000 1 9 20
```
### Running on high-end servers
```
```