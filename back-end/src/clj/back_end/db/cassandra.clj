(ns back-end.db.cassandra
  (:require [back-end.db.core :refer [bulk-insert-using-csv]]))

(defn insert-bulk-data-using-csv
  [file]
  (bulk-insert-using-csv
    "cities"
    file))
