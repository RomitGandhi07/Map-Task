(ns back-end.db.cassandra
  (:require [qbits.hayt :refer :all]
            [back-end.db.core :refer [insert-statement bulk-insert-using-csv]]))

(defn insert-bulk-data-using-csv
  [file]
  (bulk-insert-using-csv
    "cities"
    file))

(defn insert-bulk-data
  [data]
  (doseq [d data]
    (insert-statement (insert :properties
                              (values d)))))
