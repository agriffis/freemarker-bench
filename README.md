# cljmark - FreeMarker Template Benchmark

A Clojure program that benchmarks FreeMarker template rendering performance using Criterium.

## Prerequisites

- Clojure CLI tools installed
- Java 8 or higher

## Dependencies

- Clojure 1.11.1
- FreeMarker 2.3.32
- Criterium 0.4.6 (benchmarking library)

## Project Structure

```
cljmark/
├── deps.edn
├── src/
│   └── cljmark/
│       └── core.clj
└── resources/
    └── templates/
        └── sample.ftl
```

## Usage

Run the benchmark:

```bash
clojure -M:run
```

## How it Works

1. Creates a FreeMarker configuration with a sample template
2. Prepares test data (title, user, items list, count)
3. Warms up the JVM with 1000 iterations
4. Runs Criterium benchmark to measure execution time
5. Displays detailed statistics and sample output

## Benchmark Results

The program measures:
- Execution time mean
- Standard deviation
- Lower/upper quantiles
- Outlier detection
- Variance analysis

## Customization

You can modify:
- Template: Edit `resources/templates/sample.ftl`
- Test data: Modify the data HashMap in `-main` function
- Benchmark settings: Adjust Criterium options in `core.clj`
