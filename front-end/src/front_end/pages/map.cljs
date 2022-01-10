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
    [:div {:style {:width "50%"}}
     [:h3 "Range"]
     [:h5 (str location-range "Km")]
     [:input.form-range {:type "range"
                         :min "10"
                         :max "150"
                         :step "10"
                         :on-change (fn [event]
                                      (rf/dispatch-sync [:update-location-range (-> event .-target .-value)])
                                      (rf/dispatch [:find-nearby-places]))}]]))

(defn show-place [place]
  [:div.d-flex.mt-4
   [:div
    [:img {:src "./images/location.png"
           :height "30"
           :width "30"}]]
   [:div.m-2
    [:h6
     {:style {:cursor "pointer"}
      :on-click (fn [_]
                  (rf/dispatch [:update-view-options {:lat (:lat place)
                                                      :lng (:lng place)}]))}
     (:city place)]
    [:p (str (:city place) "," (:distance place) "Km")]]
   [:hr]])

(defn show-places-listing
  []
  (let [nearby-places @(rf/subscribe [:nearby-places])]
    [:div {:style {:margin-right "2%"
                   :margin-left "2%"
                   :overflow "auto"
                   :height "100%"}}
     [:h3.text-center "List"]
     (if-not (empty? nearby-places)
       (for [place nearby-places]
         ^{:key (:id place)}
         [show-place place])
       [:p "No places"])]))

(defn show-bing-map
  [user-location pushpins]
  [:div.mt-4
   ;;(pr-str user-location)
  ;;  (pr-str pushpins)
   [:> BingMapsReact {"bingMapsKey" "Ah6Onfkpjfr6HCEuVcuu2aoR9GJ75uGwKMBv4cbO-LUADDH5OZSQHuH0qyhedcDU"
                      "viewOptions" {"center" {"latitude" (get-in user-location [:view-options :lat])
                                               "longitude" (get-in user-location [:view-options :lng])}}
                      "mapOptions" {"navigationBarMode" "square"}
                      "height" "500px"
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
         [:div {:height "400px"}
          [:div.d-flex.flex-row
           [show-places-listing]
           [:div.flex-grow-1
            [:h3.text-center "Map View"]
            [show-bing-map user-location pushpins]]]
          [range-slider]]
         [:div
          "Location permission is required and you must give the location permission."])
       (if (and (.-geolocation js/navigator) loading)
         (.getCurrentPosition (.-geolocation js/navigator)
                              permission-granted
                              permission-denied)

         [:div
          "Location permission didn't exist"]))]))