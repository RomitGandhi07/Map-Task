(ns back-end.routes.map
  (:require [back-end.handlers.map :as map-handlers]))


(defn create-map-routes []
  ["/map"
   {:swagger {:tags ["map"]}}

   ["/find-nearby-places" {:post {:summary "Find nearby places of current location"
                                  :parameters {:body {:lat string?, :lng string? :range string?}}
                                  :handler map-handlers/find-nearby-places}}]])