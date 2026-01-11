(ns cljmark.core
  (:require [criterium.core :as criterium])
  (:import [freemarker.template Configuration TemplateExceptionHandler]
           [java.io StringWriter]
           [java.util HashMap]))

(defn create-config []
  (doto (Configuration. Configuration/VERSION_2_3_32)
    (.setClassLoaderForTemplateLoading (.getContextClassLoader (Thread/currentThread)) "templates")
    (.setDefaultEncoding "UTF-8")
    (.setTemplateExceptionHandler TemplateExceptionHandler/RETHROW_HANDLER)
    (.setTagSyntax Configuration/SQUARE_BRACKET_TAG_SYNTAX)
    (.setAutoImports {"lib" "lib.ftl"})))

(defn render-template [config template-name data-map]
  (let [template (.getTemplate config template-name)
        writer (StringWriter.)]
    (.process template data-map writer)
    (.toString writer)))

(defn -main [& args]
  (println "FreeMarker Template Rendering Benchmark")
  (println "========================================\n")
  
  (let [config (create-config)
        data (doto (HashMap.)
               (.put "title" "Benchmark Test")
               (.put "user" "John Doe")
               (.put "items" ["Apple" "Banana" "Cherry" "Date" "Elderberry"])
               (.put "count" 42))]
    
    (println "1. With pre-created config")
    (println "----------------------------")
    (println "Warming up...")
    (dotimes [_ 1000]
      (render-template config "sample.ftl" data))
    
    (println "Running benchmark...")
    (criterium/bench
      (render-template config "sample.ftl" data))
    
    (println "\n2. Creating new config each time")
    (println "-----------------------------------")
    (println "Warming up...")
    (dotimes [_ 1000]
      (let [new-config (create-config)]
        (render-template new-config "sample.ftl" data)))
    
    (println "Running benchmark...")
    (criterium/bench
      (let [new-config (create-config)]
        (render-template new-config "sample.ftl" data)))
    
    (println "\nSample output:")
    (println (render-template config "sample.ftl" data))))
