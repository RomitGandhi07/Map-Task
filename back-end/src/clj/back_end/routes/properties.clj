(ns back-end.routes.properties
  (:require [back-end.handlers.properties :as properties-handlers]))


(defn create-property-routes []
  ["/properties"
   {:swagger {:tags ["properties"]}}
   ["/find-nearby-properties" {:post {:summary "Find nearby places of current location"
                                      :parameters {:body {:lat string?, :lng string? :range string?}}
                                      :handler properties-handlers/find-nearby-properties}}]
   ["/search" {:post {:summary "Find properties based on search values"
                      :parameters {:body {:zipcode string? :bedroom_num string?}}
                      :handler properties-handlers/search-properties}}]
   ["/get/:id" {:get {:summary "Get property details by id"
                  :parameters {:path {:id string?}}
                  :handler properties-handlers/fetch-property-by-id}}]])