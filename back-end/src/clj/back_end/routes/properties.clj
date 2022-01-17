(ns back-end.routes.properties
  (:require [back-end.handlers.properties :as properties-handlers]))


(defn create-property-routes []
  ["/properties"
   {:swagger {:tags ["properties"]}}
   ["/find-nearby-properties" {:post {:summary "Find nearby places of current location"
                                      :parameters {:body {:lat string?, :lng string? :range string?}}
                                      :handler properties-handlers/find-nearby-properties}}]
   ["/search" {:post {:summary "Find properties based on search values"
                      :coercion reitit.coercion.spec/coercion
                      :responses {200 {:body {:message string?
                                              :data [{:description string?
                                                      :locality string?
                                                      :bathroom_num string?
                                                      :furnishing string?
                                                      :city string?
                                                      :type string?
                                                      :zipcode string?
                                                      :poster_name string?
                                                      :title string?
                                                      :floor_num string?
                                                      :bedroom_num string?
                                                      :project string?
                                                      :id string?
                                                      :trans string?
                                                      :url string?
                                                      :lat string?
                                                      :area string?
                                                      :floor_count string?
                                                      :dev_name string?
                                                      :user_type string?
                                                      :price string?
                                                      :lng string?
                                                      :post_date string?
                                                      :id_string string?}]}}
                                  500 {:body {:error string?}}}
                      :parameters {:body {:zipcode string? :bedroom_num string?}}
                      :handler properties-handlers/search-properties}}]
   ["/get/:id" {:get {:summary "Get property details by id"
                  :parameters {:path {:id string?}}
                  :handler properties-handlers/fetch-property-by-id}}]])