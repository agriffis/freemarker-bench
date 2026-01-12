(ns freemarker-bench.core
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

(def nano-seconds 1000000000)

(defn -main [& args]
  (println "FreeMarker Template Rendering Benchmark")
  (println "========================================\n")

  (binding [criterium/*default-benchmark-opts*
            (assoc criterium/*default-benchmark-opts* :target-execution-time 10000)]
    (let [my-content (slurp (io/resource "templates/my_content.ftl"))
          data-without-interpret (doto (HashMap.)
                                   (.put "title" "Benchmark Test")
                                   (.put "user" "John Doe")
                                   (.put "items" ["Apple" "Banana" "Cherry" "Date" "Elderberry"])
                                   (.put "count" 42))
          data-with-interpret (doto (.clone data-without-interpret)
                                (.put "myContent" my-content))
          results (atom [])]

      (println "1. New config each time + ?interpret")
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
      (println)

      (println "2. New config each time + direct import")
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
      (println)

      (println "3. Pre-created config + ?interpret")
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
      (println)

      (println "4. Pre-created config + direct import")
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
      (println)

      ;; Print results table
      (println)
      (println "========================================")
      (println "BENCHMARK RESULTS SUMMARY")
      (println "========================================")
      (println)
      (printf "%-20s %-20s %15s%n" "Config Strategy" "Import Strategy" "Mean Time (ns)")
      (println (apply str (repeat 60 "-")))
      (doseq [r @results]
        (printf "%-20s %-20s %15.2f%n" (:config r) (:interpret r) (* (:mean r) nano-seconds)))
      (println))))
