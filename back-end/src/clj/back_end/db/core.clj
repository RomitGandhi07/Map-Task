(ns back-end.db.core
  (:require [qbits.alia :as alia]
            [qbits.hayt :refer :all]))

(def session (atom nil))

(defn create-indexes
  []
  (alia/execute @session "CREATE INDEX IF NOT EXISTS zipcode ON properties(zipcode);"))

(defn create-session
  []
  (reset! session (alia/session{:session-keyspace "mapProject"
                                :contact-points ["localhost:9042"]
                                :load-balancing-local-datacenter "datacenter1"}))
  (create-indexes))

(defn insert-statement
  [statement]
  (alia/execute @session
                statement))

(defn bulk-insert-using-csv
  [table file-name]
  (println (str "COPY " table " FROM '" file-name "' WITH DELIMITER=',' AND HEADER=TRUE;"))
  (println (.exists (clojure.java.io/file file-name)))
  (alia/execute @session (str "COPY " table " FROM '" file-name "' WITH HEADER=TRUE;"))
  )

(defn select-statement
  [statement]
  (alia/execute @session statement))

(defn execute-query
  [query]
  (alia/execute @session query))