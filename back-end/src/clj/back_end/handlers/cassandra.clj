(ns back-end.handlers.cassandra
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [back-end.db.cassandra :refer [insert-bulk-data insert-bulk-data-using-csv]]))

(defn csv-data->maps [csv-data]
  (map zipmap
       (->> (first csv-data)
            (map keyword)
            repeat)
       (rest csv-data)))

(defn upload-csv-file [req]
  (try
    (let [data (->> (-> req
                        :parameters
                        :multipart
                        :file
                        :tempfile)
                    io/reader
                    csv/read-csv
                    csv-data->maps)
          ;data (mapv (fn [v]
          ;             (assoc v :zipcode (str (rand-nth (range 400001 400051)))))
          ;           data)
          _ (insert-bulk-data data)]
      {:status 200
       :body {:message "Ok"
              :data (take 5 data)}})
    (catch Exception e
      (println e)
      {:status 500
       :body {:error "Something went wrong... Please try again"}})))

;(defn upload-csv-file
;  [req]
;  (try
;    (let [file (-> req
;                   :parameters
;                   :multipart
;                   :file)
;          file-path (str "resources/temp/" (java.util.UUID/randomUUID) ".csv")
;          _ (io/copy (:tempfile file) (io/file file-path))
;          result (insert-bulk-data-using-csv file-path)]
;      (println result)
;      (io/delete-file file-path)
;      {:status 200
;       :body {:message "Ok"}})
;    (catch Exception e
;      (println e)
;      {:status 500
;       :body {:error "Something went wrong... Please try again"}})))
(repeat [1 3 4])