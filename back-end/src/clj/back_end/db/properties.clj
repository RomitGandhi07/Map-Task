(ns back-end.db.properties
  (:require [qbits.hayt :refer :all]
            [back-end.db.core :refer [select-statement]]))

(defn get-all-properties
  []
  (select-statement (select :properties)))

(defn get-property-by-id
  [id]
  (select-statement (select :properties
                            (where {:id id}))))

(defn get-properties-by-search-values
  [data]
  (println data)
  (select-statement (select :properties
                            (where (into {} (filter (fn [v]
                                                      (println (empty? (second v)))
                                                      (not (empty? (second v))))
                                                      data)))
                            (allow-filtering)))
  )




