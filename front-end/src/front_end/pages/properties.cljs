(ns front-end.pages.properties
  (:require [reagent.dom :as rdom]
            [reagent.core :as reagent]
            [re-frame.core :as rf]
            [front-end.components.toastNotification :refer [toast-notification]]
            [front-end.components.bingmap :refer [render-bing-map]]
            [front-end.components.loader :refer [loader]]))
;; MAP

(defn show-bing-map
  "This is to display the bing map"
  []
  (let [view-options @(rf/subscribe [:view-options])
        pushpins @(rf/subscribe [:pushpins])]
    [:div.col-8.mt-1 {:style {:margin-right "2%"
                        :margin-left "2%"
                        :height "100%"}}
     [:h4.text-center "Map"]
     [:hr]
     [render-bing-map view-options pushpins]]))

;; PROPERTIES LISTING

(defn show-property 
  "This is to display single property in properties list"
  [property]
  [:div.d-flex
   [:div
    [:img {:src "./images/location.png"
           :height "30"
           :width "30"}]]
   [:div.mx-3
    [:a {:href (str "/#/property/" (:id property))
         :target "_blank"
         :style {:text-decoration "none"
                 :color "black"}}
     [:h6
      {:style {:cursor "pointer"}
       :onMouseOver (fn [_]
                      (rf/dispatch [:update-view-options {:lat (:lat property)
                                                          :lng (:lng property)}]))}
      (:title property)]]
    [:p (:city property)]]
   [:hr]])

(defn show-properties-listing
  "This is to display the properties listing"
  []
  (let [loading @(rf/subscribe [:properties-loading])
        nearby-places @(rf/subscribe [:properties])]
    [:div.col-4.mt-1 {:style {:margin-right "2%"
                        :margin-left "2%"
                        :max-width "20%"
                        :height "100%"}}
     [:h4.text-center "Properties"]
     [:hr]
     (if loading
       [loader]
       [:div {:style {:max-height "70vh"
                      :overflow "auto"}}
        (if-not (empty? nearby-places)
          (for [place nearby-places]
            ^{:key (:id place)}
            [show-property place])
          [:p "No properties"])])]))

;; PERMISSION GRANTED & DENINED

(defn permission-granted
  "Permission Granted"
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
    (rf/dispatch [:find-nearby-properties])))

(defn permission-denied
  "Permission Denied"
  []
  (rf/dispatch [:location-permission-denied]))

(defn display-properties-nearby-user-location
  []
  (.getCurrentPosition (.-geolocation js/navigator)
                       permission-granted
                       permission-denied))

;; SEARCH BAR

(defn search-bar
  "This function is responsibe for displaying the search bar"
  []
  (let [search-value (reagent/atom {:zipcode ""
                                    :bedroom_num ""})]
    (fn []
      [:div.row
      ;;  (pr-str @search-value)
       [:div.mt-2.col-12
        [:div.d-flex
         [:input.form-control {:type :num
                               :placeholder "Enter zipcode"
                               :minLength 6
                               :maxLength 6
                               :on-change (fn [e]
                                            (swap! search-value assoc-in [:zipcode] (-> e .-target .-value)))}]
         [:select.mx-2.form-select
          {:on-change (fn [e]
                        (swap! search-value assoc-in [:bedroom_num] (-> e .-target .-value)))}
          [:option {:selected true
                    :disabled true
                    :value ""} "Bedrooms"]
          [:option {:value ""} "Any"]
          [:option {:value "1"} "1"]
          [:option {:value "2"} "2"]
          [:option {:value "3"} "3"]
          [:option {:value "4"} "4"]
          [:option {:value "5"} "5"]]
         [:button.mx-2.btn-btn-primary {:on-click (fn [e]
                                               (.preventDefault e)
                                               (rf/dispatch [:search-properties @search-value]))}
          [:i.fas.fa-search]]
         [:button.mx-2.btn-btn-primary {:type :button
                                        :on-click (fn [e]
                                                    (.preventDefault e)
                                                    (display-properties-nearby-user-location))}
          [:i.fas.fa-search-location]]]]])))

;; MAIN VIEW

(defn search-properties
  "This is the main view"
  []
  [:div.container
   [toast-notification]
   [:div.row
    [:div.col-6
     [search-bar]]]
   [:div.row.mt-4
    [:div.col-12
     [:div.d-flex
      [show-properties-listing]
      [show-bing-map]]]]])