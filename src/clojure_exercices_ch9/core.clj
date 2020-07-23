(ns clojure-exercices-ch9.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defmacro wait
  "Sleep `timeout` seconds before evaluating body"
  [timeout & body]
  `(do (Thread/sleep ~timeout) ~@body))

(let [saying3 (promise)]
  (future (deliver saying3 (wait 100 "Cheerio!")))
  @(let [saying2 (promise)]
     (future (deliver saying2 (wait 400 "Pip pip!")))
     @(let [saying1 (promise)]
        (future (deliver saying1 (wait 200 "'Ello, gov'na!")))
        (println @saying1)
        saying1)
     (println @saying2)
     saying2)
  (println @saying3)
  saying3)


(defn p-race
  [values]
  (let [p (promise)]
    (doseq [v values]
      (future (wait (* v 1000) (deliver p v))))
    p))

(def p (p-race [3 2 5 7]))
(println @p)


(defn p-all
  [values]
  (let [promises (map
                  (fn [v] (future (wait (* v 1000) v)))
                  values)]
    (future (map deref promises))))

(def p-data (p-all [3 2 2 1]))
(println @p-data)