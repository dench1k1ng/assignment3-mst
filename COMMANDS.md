# MST Transportation Network - Quick Command Reference

## 🚀 Essential Commands

### Build & Run
```bash
# Clean and compile
mvn clean compile

# Run main application (with default datasets)
mvn exec:java -Dexec.mainClass="mst.Main"

# Run with custom input/output files
mvn exec:java -Dexec.mainClass="mst.Main" -Dexec.args="input.json output.json"
```

### Testing
```bash
# Run all tests (31 total tests)
mvn test

# Run specific test classes
mvn test -Dtest=MSTCorrectnessTest        # 8 correctness tests
mvn test -Dtest=GraphTest                 # 16 graph structure tests  
mvn test -Dtest=MSTPerformanceTest        # 7 performance tests

# Run specific test methods
mvn test -Dtest=GraphTest#testGraphCreation
mvn test -Dtest=MSTPerformanceTest#testSmallGraphPerformance
```

### Analysis & Reports
```bash
# Generate CSV performance summary
mvn exec:java -Dexec.mainClass="mst.util.ResultAnalyzer"

# View results
cat analysis_results.csv
cat src/main/resources/output.json
```

## 📊 Quick Results Overview

### Performance Summary
```bash
# View CSV summary (after running ResultAnalyzer)
cat analysis_results.csv | head -5

# Expected output format:
# Graph_ID,Graph_Name,Vertices,Edges,Density,Prim_Cost,Prim_Ops,Prim_Time_ms,Kruskal_Cost,Kruskal_Ops,Kruskal_Time_ms,Cost_Match
# 1,Small_Simple_Path,4,5,0.833,6.0,20,0,6.0,23,0,YES
# 4,Medium_City_Districts,10,15,0.333,41.0,69,0,41.0,97,0,YES
```

### Test Results Summary
```bash
# Quick test verification
mvn test | grep "Tests run"

# Expected output:
# Tests run: 8, Failures: 0, Errors: 0, Skipped: 0 -- in MSTCorrectnessTest
# Tests run: 16, Failures: 0, Errors: 0, Skipped: 0 -- in mst.GraphTest  
# Tests run: 7, Failures: 0, Errors: 0, Skipped: 0 -- in mst.MSTPerformanceTest
```

## 🔍 Debugging & Development

### Compilation Issues
```bash
# Check Java version
java -version

# Verify Maven setup
mvn -version

# Clean rebuild
mvn clean compile -X
```

### Test Issues
```bash
# Run tests with detailed output
mvn test -Dtest=GraphTest -X

# Run single test method
mvn test -Dtest=GraphTest#testGraphCreation -Dsurefire.printSummary=true
```

## 📁 Important Files

### Input/Output
- `src/main/resources/input.json` - Test datasets (10 graphs)
- `src/main/resources/output.json` - Algorithm results
- `analysis_results.csv` - Performance summary

### Documentation
- `README.md` - Project overview and usage
- `REPORT.md` - Detailed analytical report
- `COMMANDS.md` - This command reference

### Source Code
- `src/main/java/mst/Main.java` - Main application entry point
- `src/main/java/mst/algorithm/` - Prim's and Kruskal's implementations
- `src/main/java/mst/model/` - Graph, Edge, and MSTResult classes
- `src/test/java/` - Comprehensive test suite

## 🎯 Assignment Verification Checklist

```bash
# 1. Algorithm Implementations (50%)
mvn test -Dtest=MSTCorrectnessTest  # ✅ Should pass 8/8 tests

# 2. Testing (10%)  
mvn test                            # ✅ Should pass 31/31 tests

# 3. Performance Analysis (25%)
ls -la REPORT.md                    # ✅ Should exist
ls -la analysis_results.csv         # ✅ Should exist

# 4. Code Quality (15%)
find src -name "*.java" | wc -l     # ✅ Should show ~12 Java files

# 5. Bonus: Graph Design (10%)
ls -la src/main/java/mst/model/     # ✅ Should contain Graph.java and Edge.java
```

## 🚨 Troubleshooting

### Common Issues

1. **Tests not found**
   ```bash
   # Ensure tests are in correct directory
   ls -la src/test/java/
   mvn clean test
   ```

2. **JSON file not found**
   ```bash
   # Check file locations
   ls -la src/main/resources/
   mvn exec:java -Dexec.mainClass="mst.Main"
   ```

3. **Compilation errors**
   ```bash
   # Clean and rebuild
   mvn clean compile
   mvn dependency:resolve
   ```

## 💡 Tips

- Use `-q` flag for quieter output: `mvn test -q`
- Use `-X` flag for debug output: `mvn test -X`
- Check `target/` directory for compiled classes
- All tests should pass with 0 failures/errors
- Performance tests show algorithm operation counts and timing

---

**Quick Status Check:**
```bash
mvn clean test && echo "✅ Project is working correctly!"
```
