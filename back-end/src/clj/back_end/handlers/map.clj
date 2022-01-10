(ns back-end.handlers.map
  (:require [back-end.db.map :refer [get-all-places]]))

; Haversine formula
; a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
; c = 2 ⋅ atan2( √a, √(1−a) )
; d = R ⋅ c
; where φ is latitude, λ is longitude, R is earth’s radius (mean radius = 6,371km);
(defn haversine
  "Implementation of Haversine formula. Takes two sets of latitude/longitude pairs and returns the shortest great circle distance between them (in km)"
  [{lon1 :lng lat1 :lat} {lon2 :lng lat2 :lat}]
  (let [R 6378.137 ; Radius of Earth in km
        dlat (Math/toRadians (- lat2 lat1))
        dlon (Math/toRadians (- lon2 lon1))
        lat1 (Math/toRadians lat1)
        lat2 (Math/toRadians lat2)
        a (+ (* (Math/sin (/ dlat 2)) (Math/sin (/ dlat 2))) (* (Math/sin (/ dlon 2)) (Math/sin (/ dlon 2)) (Math/cos lat1) (Math/cos lat2)))]
    (* R 2 (Math/asin (Math/sqrt a)))))

(defn find-nearby-places
  [{{{:keys [lat lng range]} :body} :parameters}]
  (try
    (let [places (get-all-places)
          places-with-km (mapv (fn [p]
                                 (assoc p :distance (Double/parseDouble
                                                      (format "%.2f"
                                                              (haversine
                                                                {:lat (Double/parseDouble (:lat p))
                                                                 :lng (Double/parseDouble (:lng p))}
                                                                {:lat (Double/parseDouble lat)
                                                                 :lng (Double/parseDouble lng)})))))
                               places)
          filter-places-by-km (sort-by :distance
                               (filter (fn [p]
                                        (< (:distance p) (Integer/parseInt range))) places-with-km))]
      {:status 200
       :body {:message "Ok"
              :data filter-places-by-km}})
    (catch Exception e
      {:status 500
       :body {:error "Something went wrong... Please try again"}})))