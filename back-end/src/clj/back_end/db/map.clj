(ns back-end.db.map
  (:require [qbits.hayt :refer :all]
            [back-end.db.core :refer [select-statement]]))

(defn get-all-places
  []
  (select-statement (select :properties)))




