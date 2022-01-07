(ns back-end.db.core
  (:require [qbits.alia :as alia]
            [qbits.hayt :refer :all]))

(def session (atom nil))

(defn create-session
  []
  (reset! session (alia/session{:session-keyspace "mapProject"
                                :contact-points ["localhost:9042"]
                                :load-balancing-local-datacenter "datacenter1"})))

(defn insert-statement
  [statement]
  (alia/execute @session
                statement))

(defn bulk-insert-using-csv
  [table file-name]
  (println (str "COPY " table " FROM '" file-name "' WITH DELIMITER=',' AND HEADER=TRUE;"))
  (println (.exists (clojure.java.io/file file-name)))
  (alia/execute @session (str "COPY " table " FROM '" file-name "' WITH DELIMITER=',' AND HEADER=TRUE;"))
  ;(alia/execute @session
  ;              (alia/prepare @session (str "COPY " table " FROM ? WITH DELIMITER=',' AND HEADER=TRUE;"))
  ;              {:values [file-name]})
  )