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

(defn get-property-by-zipcode
  [zipcode]
  (select-statement (str "SELECT * FROM properties WHERE zipcode='" zipcode "';")))




