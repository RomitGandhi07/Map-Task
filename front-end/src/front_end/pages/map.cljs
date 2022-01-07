(ns front-end.pages.map
  (:require [reagent.dom :as rdom]
            [reagent.core :as reagent]
            ["bingmaps-react" :default BingMapsReact]
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
  (println (-> position
               .-coords
               .-latitude) "Latitude")
  (println (-> position
               .-coords
               .-longitude) "Longitude")
  [:div
   "Location access"])

(defn show-map []
  ;; (if (.-geolocation js/navigator)
  ;;   (do
  ;;    (.getCurrentPosition (.-geolocation js/navigator) showPosition)
  ;;    [:div
  ;;     "H1"]
  ;;    )
  ;;   [:div
  ;;    "Location permission didn't exist"])

  [:> BingMapsReact {"bingMapsKey" "Ah6Onfkpjfr6HCEuVcuu2aoR9GJ75uGwKMBv4cbO-LUADDH5OZSQHuH0qyhedcDU"
                     "height" "500px"
                     "viewOptions" {"center" {"latitude" 23.022505
                                              "longitude" 72.5713621}}
                     "pushPins" [{"center" {"latitude" 23.02
                                            "longitude" 72.57}
                                  "options" {"title" "Mt. Everest"}}
                                 {"center" {"latitude" 23.022505
                                            "longitude" 72.5713621}
                                  "options" {"title" "You are Here"
                                             "color" "red"}}]}]
  ;;[loader]

  ;; [:div
  ;;  [:iframe {:width 500
  ;;            :height 400
  ;;            :frameBorder 0
  ;;            :src "https://www.bing.com/maps/embed?h=400&w=500&cp=23.149830673947775~72.51731872558594&lvl=11&typ=d&sty=r&src=SHELL&FORM=MBEDV8"
  ;;            :scrolling "no"}]]
  )