(ns front-end.pages.map
  (:require [reagent.dom :as rdom]
            [reagent.core :as reagent]
            ["bingmaps-react" :default BingMapsReact]
            [re-frame.core :as rf]
            [front-end.components.loader :refer [loader]]))

;; (defn home-render []
;;   [:div {:style {:height "300px"}}])

;; (defn home-did-mount [this]
;;   (let [map-canvas (rdom/dom-node this)
;;         map-options (clj->js {"center" (js/google.maps.LatLng. -34.397, 150.644)
;;                               "zoom" 8})]
;;     (js/google.maps.Map. map-canvas map-options)))

;; (defn google-map2 []
;;   ;; [:div {:style {:height "100vh"
;;   ;;                :width "100%"}}
;;   ;;  ;[:> GoogleMapReact {"bootstrapURLKeys" "AIzaSyD5-ksUdNXNKAxSTatFkZsnNRgJhcn8zqA"}]
;;   ;;  ]
;;   (reagent/create-class {:reagent-render home-render
;;                          :component-did-mount home-did-mount}))

(defn permission-granted
  [position]
  (let [lat (-> position
                .-coords
                .-latitude)
        lng (-> position
                .-coords
                .-longitude)]
    (rf/dispatch-sync [:location-permission-granted {:lat lat
                                                     :lng lng
                                                     :permission true}])
    (rf/dispatch [:find-nearby-places])))

(defn permission-denied []

  (rf/dispatch [:location-permission-denied]))

(defn range-slider
  "This function is responsible for creating the range slider"
  []
  (let [location-range @(rf/subscribe [:location-range])]
    [:div {:style {:display "flex"
                   :justify-content "center"
                   :flex-direction "column"
                   :align-items "center"}}
     [:h3 "Range"]
     [:h5 (str location-range "Km")]
     [:input.form-range {:style {:width "40%"}
                         :type "range"
                         :value location-range
                         :min "1"
                         :max "5"
                         :on-change (fn [event]
                                      (rf/dispatch-sync [:update-location-range (-> event .-target .-value)])
                                      (rf/dispatch [:find-nearby-places]))}]]))

(defn show-place [place]
  [:div.d-flex
   [:div
    [:img {:src "./images/location.png"
           :height "30"
           :width "30"}]]
   [:div.mx-3
    [:h6
     {:style {:cursor "pointer"}
      :on-click (fn [_]
                  (rf/dispatch [:update-view-options {:lat (:lat place)
                                                      :lng (:lng place)}]))}
     (:title place)]
    [:p (str (:city place) "," (:distance place) "Km")]]
   [:hr]])

(defn show-places-listing
  []
  (let [nearby-places @(rf/subscribe [:nearby-places])]
    [:div.mt-1 {:style {:margin-right "2%"
                   :margin-left "2%"
                   :max-width "15%"
                   :height "100%"}}
     [:h4.text-center (str "Properties (" (count nearby-places) ")")]
     [:hr]
     [:div {:style {:max-height "70vh"
                    :overflow "auto"}}
      (if-not (empty? nearby-places)
        (for [place nearby-places]
          ^{:key (:id place)}
          [show-place place])
        [:p "No places"])]]))

(defn show-bing-map
  [user-location pushpins]
  [:div.d-flex.justify-content-center.align-items-center
   ;;(pr-str user-location)
  ;;  (pr-str pushpins)
   [:> BingMapsReact {"bingMapsKey" "Ah6Onfkpjfr6HCEuVcuu2aoR9GJ75uGwKMBv4cbO-LUADDH5OZSQHuH0qyhedcDU"
                      "viewOptions" {"center" {"latitude" (get-in user-location [:view-options :lat])
                                               "longitude" (get-in user-location [:view-options :lng])}
                                     "zoom" 16}
                      "mapOptions" {"navigationBarMode" "square"}
                      "height" "70vh"
                      "width" "60vw"
                      "pushPinsWithInfoboxes" pushpins}]])

(defn show-map []
  (let [loading @(rf/subscribe [:loading])
        user-location @(rf/subscribe [:user-location])
        pushpins @(rf/subscribe [:pushpins])]
    [:<>
     (when loading
       [loader])
     (if user-location
       (if (and (:permission user-location) pushpins)
         [:div
          [range-slider]
          [:div.d-flex.flex-row
           [show-places-listing]
           [:div.flex-grow-1 {:style {:max-width "80vw"}}
            [:h3.text-center "Map View"]
            [:hr]
            [show-bing-map user-location pushpins]]]]
         [:div
          "Location permission is required and you must give the location permission."])
       (if (and (.-geolocation js/navigator) loading)
         (.getCurrentPosition (.-geolocation js/navigator)
                              permission-granted
                              permission-denied)

         [:div
          "Location permission didn't exist"]))]))