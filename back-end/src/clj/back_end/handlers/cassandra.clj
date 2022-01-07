(ns back-end.handlers.cassandra
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [back-end.db.cassandra :refer [insert-bulk-data-using-csv]]))

;(defn csv-data->maps [csv-data]
;  (map zipmap
;       (->> (first csv-data)
;            (map keyword)
;            repeat)
;       (rest csv-data)))
;
;(defn upload-csv-file [req]
;  (let [data (->> (-> req
;                      :parameters
;                      :multipart
;                      :file
;                      :tempfile)
;                  io/reader
;                  csv/read-csv
;                  csv-data->maps)
;        updated-data (map #(assoc % :id (java.util.UUID/randomUUID)) data)]
;    {:status 200
;     :body {:message "Ok"
;            :data updated-data}}))

(defn upload-csv-file
  [req]
  (try
    (let [file (-> req
                   :parameters
                   :multipart
                   :file)
          file-path (str "resources/temp/" (java.util.UUID/randomUUID) ".csv")
          _ (io/copy (:tempfile file) (io/file file-path))
          result (insert-bulk-data-using-csv file-path)]
      (println result)
      (io/delete-file file-path)
      {:status 200
       :body {:message "Ok"}})
    (catch Exception e
      (println e)
      {:status 500
       :body {:error "Something went wrong... Please try again"}})))