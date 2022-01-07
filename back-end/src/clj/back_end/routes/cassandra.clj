(ns back-end.routes.cassandra
  (:require [reitit.ring.middleware.multipart :as multipart]
            [back-end.handlers.cassandra :as cassandra-handlers]))


(defn create-cassandra-routes []
  ["/cassandra"
   {:swagger {:tags ["cassandra"]}}

   ["/bulk-upload" {:post {:summary "Upload bulk data in cassandra using CSV file"
                           ;:responses {200 {:body {:message string?
                           ;                       :data [{:city string?
                           ;                               :lat string?
                           ;                               :long string?
                           ;                               :country string?
                           ;                               :iso2 string?
                           ;                               :state string?
                           ;                               :id string?}]}
                           ;                 :description "Found it!"}}
                           :parameters {:multipart {:file multipart/temp-file-part}}
                           :handler cassandra-handlers/upload-csv-file}}]])