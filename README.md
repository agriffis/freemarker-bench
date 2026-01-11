## Usage

Run the benchmark:

```bash
clojure -M:run
```

## Results

```
▶ mise bench
[bench] $ clojure -M:run
FreeMarker Template Rendering Benchmark
========================================

1. New config each time + ?interpret
-------------------------------------
Warming up...
Running benchmark...

2. New config each time + direct import
----------------------------------------
Warming up...
Running benchmark...

3. Pre-created config + ?interpret
-----------------------------------
Warming up...
Running benchmark...

4. Pre-created config + direct import
--------------------------------------
Warming up...
Running benchmark...


========================================
BENCHMARK RESULTS SUMMARY
========================================

Config Strategy      Import Strategy       Mean Time (ns)
------------------------------------------------------------
New each time        With ?interpret            691440.90
New each time        Direct import              510624.23
Pre-created          With ?interpret            133823.07
Pre-created          Direct import                8609.25
```
