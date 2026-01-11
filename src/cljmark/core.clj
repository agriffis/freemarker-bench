(ns cljmark.core
  (:require [criterium.core :as criterium]
            [clojure.java.io :as io])
  (:import [freemarker.template Configuration TemplateExceptionHandler]
           [java.io StringWriter]
           [java.util HashMap]))

(defn create-config [use-interpret?]
  (doto (Configuration. Configuration/VERSION_2_3_32)
    (.setClassLoaderForTemplateLoading (.getContextClassLoader (Thread/currentThread)) "templates")
    (.setDefaultEncoding "UTF-8")
    (.setTemplateExceptionHandler TemplateExceptionHandler/RETHROW_HANDLER)
    (.setTagSyntax Configuration/SQUARE_BRACKET_TAG_SYNTAX)
    (.setAutoImports (if use-interpret?
                       {"lib" "lib.ftl" "my" "my.ftl"}
                       {"lib" "lib.ftl" "my" "my_content.ftl"}))))

(defn render-template [config template-name data-map]
  (let [template (.getTemplate config template-name)
        writer (StringWriter.)]
    (.process template data-map writer)
    (.toString writer)))

(defn -main [& args]
  (println "FreeMarker Template Rendering Benchmark")
  (println "========================================\n")

  (let [my-content (slurp (io/resource "templates/my_content.ftl"))
        data-with-interpret (doto (HashMap.)
                              (.put "title" "Benchmark Test")
                              (.put "user" "John Doe")
                              (.put "items" ["Apple" "Banana" "Cherry" "Date" "Elderberry"])
                              (.put "count" 42)
                              (.put "myContent" my-content))
        data-without-interpret (doto (HashMap.)
                                 (.put "title" "Benchmark Test")
                                 (.put "user" "John Doe")
                                 (.put "items" ["Apple" "Banana" "Cherry" "Date" "Elderberry"])
                                 (.put "count" 42))
        results (atom [])]

    ;; Test 1: Pre-created config with ?interpret
    (println "1. Pre-created config + ?interpret")
    (println "-----------------------------------")
    (let [config (create-config true)]
      (println "Warming up...")
      (dotimes [_ 100]
        (render-template config "sample.ftl" data-with-interpret))
      (println "Running benchmark...")
      (let [result (criterium/benchmark
                    (render-template config "sample.ftl" data-with-interpret)
                    {})]
        (swap! results conj {:config "Pre-created"
                             :interpret "With ?interpret"
                             :mean (first (:mean result))})))

    ;; Test 2: Pre-created config without ?interpret (direct import)
    (println "\n2. Pre-created config + direct import")
    (println "--------------------------------------")
    (let [config (create-config false)]
      (println "Warming up...")
      (dotimes [_ 100]
        (render-template config "sample.ftl" data-without-interpret))
      (println "Running benchmark...")
      (let [result (criterium/benchmark
                    (render-template config "sample.ftl" data-without-interpret)
                    {})]
        (swap! results conj {:config "Pre-created"
                             :interpret "Direct import"
                             :mean (first (:mean result))})))

    ;; Test 3: New config each time with ?interpret
    (println "\n3. New config each time + ?interpret")
    (println "-------------------------------------")
    (println "Warming up...")
    (dotimes [_ 100]
      (let [config (create-config true)]
        (render-template config "sample.ftl" data-with-interpret)))
    (println "Running benchmark...")
    (let [result (criterium/benchmark
                  (let [config (create-config true)]
                    (render-template config "sample.ftl" data-with-interpret))
                  {})]
      (swap! results conj {:config "New each time"
                           :interpret "With ?interpret"
                           :mean (first (:mean result))}))

    ;; Test 4: New config each time without ?interpret (direct import)
    (println "\n4. New config each time + direct import")
    (println "----------------------------------------")
    (println "Warming up...")
    (dotimes [_ 100]
      (let [config (create-config false)]
        (render-template config "sample.ftl" data-without-interpret)))
    (println "Running benchmark...")
    (let [result (criterium/benchmark
                  (let [config (create-config false)]
                    (render-template config "sample.ftl" data-without-interpret))
                  {})]
      (swap! results conj {:config "New each time"
                           :interpret "Direct import"
                           :mean (first (:mean result))}))

    ;; Print results table
    (println "\n\n")
    (println "========================================")
    (println "BENCHMARK RESULTS SUMMARY")
    (println "========================================")
    (println)
    (printf "%-20s %-20s %15s%n" "Config Strategy" "Import Strategy" "Mean Time (ns)")
    (println (apply str (repeat 60 "-")))
    (doseq [r @results]
      (printf "%-20s %-20s %15.2f%n" (:config r) (:interpret r) (:mean r)))
    (println)))
