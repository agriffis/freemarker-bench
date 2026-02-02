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

5. Pre-created config + indirect import
----------------------------------------
Warming up...
Running benchmark...


========================================
BENCHMARK RESULTS SUMMARY
========================================

Config Strategy      Import Strategy       Mean Time (ns)
------------------------------------------------------------
New each time        With ?interpret            608431.52
New each time        Direct import              485778.13
Pre-created          With ?interpret            129890.90
Pre-created          Direct import                8516.88
Pre-created          Indirect import              9534.57
```
