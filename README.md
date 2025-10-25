# transportation bla bla bla assignment

## description
prim & kruskal algorithms for finding mst in city transportation networks

## structure
```
src/
├── main/java/
│   ├── graph/          # graph & edge classes (BONUS)
│   ├── algorithms/     # prim & kruskal implementations
│   ├── models/         # mstresult model
│   ├── utils/          # json handler, generator, visualizer(WOW)
│   └── Main.java       # main app
├── test/java/
│   └── MSTTest.java    # JUnit tests
└── resources/
    ├── input_graphs.json   # 28 test graphs
    └── output_results.json # ma results
```

## build & run
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="Main"
```

## tests
```bash
mvn test
```

## test graphs id
- **ID 1-5**: small (4-6 vertices, 5-25 edges)
- **ID 6-15**: medium (10-19 vertices, 15-51 edges)
- **ID 16-25**: large (20-29 vertices, 30-75 edges)
- **ID 26-28**: extra large (30-50 vertices, 60-120 edges)

## output files
- `input_graphs.json` - all 28 input graphs
- `output_results.json` - mst results for each graph
- `graph_images/` - 28 png visualizations (WOW)

## algorithms

### prim's Algorithm
- **complexity**: O(E log V)
- **approach**: grows MST from starting vertex
- **data structure**: priority Queue

### kruskal's Algorithm
- **complexity**: O(E log E)
- **approach**: sorts all edges, adds without cycles
- **data structure**: union-find (DSU)y