(ns front-end.pages.map
  (:require [reagent.dom :as rdom]
            [reagent.core :as reagent]
            ["bingmaps-react" :default BingMapsReact]
            ["react-rangeslider" :default Slider]
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

(defn showPosition [position]
  (rf/dispatch [:save-user-location {:lat (-> position
                                              .-coords
                                              .-latitude)
                                     :lng (-> position
                                              .-coords
                                              .-longitude)}])
  (println (-> position
               .-coords
               .-latitude) "Latitude")
  (println (-> position
               .-coords
               .-longitude) "Longitude"))

(defn show-map2 []
  ;; (if (.-geolocation js/navigator)
  ;;   (do
  ;;    (.getCurrentPosition (.-geolocation js/navigator) showPosition)
  ;;    [:div
  ;;     "H1"]
  ;;    )
  ;;   [:div
  ;;    "Location permission didn't exist"])
(let [location-permission-access (reagent/atom false)
          loading (rf/subscribe [:loading])]
  (fn []
    [:<>
     (if @loading
       [loader]
       [:h1
        "Loading complete"])
;;      (if (.-geolocation js/navigator)
;;        (do
;;          @location-permission-access
;;          [:div "Hi"]
;; )
;;        [:div
;;         "Oops Your browser didn't support location permission please check your browser version"])
     ]))
  ;; (let [loading (rf/subscribe [:loading])]
  ;;   [:<>
  ;;    (when @loading
  ;;      [loader])
  ;;    (if (.-geolocation js/navigator)
  ;;      (.getCurrentPosition (.-geolocation js/navigator)
  ;;                           (fn [pos]
  ;;                             (rf/dispatch [:stop-loading])
  ;;                             [:div.demo
  ;;                              [:> BingMapsReact {"bingMapsKey" "Ah6Onfkpjfr6HCEuVcuu2aoR9GJ75uGwKMBv4cbO-LUADDH5OZSQHuH0qyhedcDU"
  ;;                                                 "height" "500px"
  ;;                                                 "viewOptions" {"center" {"latitude" 23.022505
  ;;                                                                          "longitude" 72.5713621}}
  ;;                                                 "pushPins" [{"center" {"latitude" 23.02
  ;;                                                                        "longitude" 72.57}
  ;;                                                              "options" {"title" "Mt. Everest"}}
  ;;                                                             {"center" {"latitude" 23.022505
  ;;                                                                        "longitude" 72.5713621}
  ;;                                                              "options" {"title" "You are Here"
  ;;                                                                         "color" "red"}}]}]])
  ;;                           (fn [_]
  ;;                             (rf/dispatch [:stop-loading])
  ;;                             (js/console.log "Location permission denied")
  ;;                             [:div
  ;;                              "Location permission is compulsory, you denied the permission"]))
  ;;      [:div
  ;;       "Oops Your browser didn't support location permission please check your browser version"])])
  ;;[loader]
  )




              ;; (.getCurrentPosition (.-geolocation js/navigator)
              ;;                      (fn [pos]
              ;;                        (reset! location-permission-access true)
              ;;                        (js/console.log "Here")
              ;;                        (rf/dispatch [:stop-loading])
              ;;                        [:div.demo
              ;;                         [:> BingMapsReact {"bingMapsKey" "Ah6Onfkpjfr6HCEuVcuu2aoR9GJ75uGwKMBv4cbO-LUADDH5OZSQHuH0qyhedcDU"
              ;;                                            "height" "500px"
              ;;                                            "viewOptions" {"center" {"latitude" 23.022505
              ;;                                                                     "longitude" 72.5713621}}
              ;;                                            "pushPins" [{"center" {"latitude" 23.02
              ;;                                                                   "longitude" 72.57}
              ;;                                                         "options" {"title" "Mt. Everest"}}
              ;;                                                        {"center" {"latitude" 23.022505
              ;;                                                                   "longitude" 72.5713621}
              ;;                                                         "options" {"title" "You are Here"
              ;;                                                                    "color" "red"}}]}]])
              ;;                      (fn [_]
              ;;                        (reset! location-permission-access true)
              ;;                        (rf/dispatch [:stop-loading])
              ;;                        (js/console.log "Location permission denied")
              ;;                        [:div
              ;;                         "Location permission is compulsory, you denied the permission"]))

(defn permission-accepted
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
    (rf/dispatch [:find-nearby-places {"lat" (str lat)
                                       "lng" (str lng)}])
    ))

(defn permission-denied []
  (rf/dispatch [:stop-loading])
  (rf/dispatch [:location-permission-denied]))

(defn range-slider 
  "This function is responsible for creating the range slider"
  []
  (let [location-range @(rf/subscribe [:location-range])]
    [:div
     [:h5 (str location-range "Km")]
     [:input {:type "range"
              :min "10"
              :max "100"
              :step "10"
              :on-change (fn [event]
                           (rf/dispatch-sync [:update-location-range (-> event .-target .-value)])
                           (rf/dispatch [:find-nearby-places]))}]]
    ))

(defn show-map []
  (let [loading @(rf/subscribe [:loading])
        user-location @(rf/subscribe [:user-location])
        pushpins @(rf/subscribe [:pushpins])]
    ;;(js/console.log loading (nil? user-location))
    [:<>
     (when loading
       [loader])
     (if user-location
       (if (and (:permission user-location) pushpins)
         [:div
          (pr-str user-location)
          (pr-str pushpins)
          [:> BingMapsReact {"bingMapsKey" "Ah6Onfkpjfr6HCEuVcuu2aoR9GJ75uGwKMBv4cbO-LUADDH5OZSQHuH0qyhedcDU"
                             "viewOptions" {"center" {"latitude" (:lat user-location)
                                                      "longitude" (:lng user-location)}}
                             "mapOptions" {"navigationBarMode" "square"}
                             "height" "500px"
                             "pushPins" pushpins}]
          [range-slider]]
         [:div
          "Location permission is required and you must give the location permission."])
       (if (and (.-geolocation js/navigator) loading)
         (.getCurrentPosition (.-geolocation js/navigator)
                              permission-accepted
                              permission-denied)

         [:div
          "Location permission didn't exist"]))]))