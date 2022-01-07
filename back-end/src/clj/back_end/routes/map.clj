(ns back-end.routes.map
  (:require [back-end.handlers.map :as map-handlers]))


(defn create-map-routes []
  ["/map"
   {:swagger {:tags ["map"]}}

   ["/find-nearby-places" {:get {:summary "Find nearby places of current location"
                           ;:responses {200 {:body {:message string?
                           ;                       :data [{:city string?
                           ;                               :lat string?
                           ;                               :long string?
                           ;                               :country string?
                           ;                               :iso2 string?
                           ;                               :state string?
                           ;                               :id string?}]}
                           ;                 :description "Found it!"}}
                           :handler map-handlers/find-nearby-places}}]])