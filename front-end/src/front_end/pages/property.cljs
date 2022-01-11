(ns front-end.pages.property
  (:require [re-frame.core :as rf]
            [front-end.components.loader :refer [loader]]))

(defn show-property-info
  []
  (let [info @(rf/subscribe [:property-info])]
    [:div.container
     [:div.row
      [:div.col-12
       [:h3.text-center (:title info)]]
      [:div.col-12.mt-4
       [:p 
        [:b "Description:"]
        (:description info)]]
      [:div.col-12.mt-4
       [:p
        [:b "Pincode:"]
        (:zipcode info)]]
      (when-not (empty? (:locality info))
        [:div.col-12
         [:p
          [:b "Locality:"]
          (:locality info)]])]]))

(defn property-info
  []
  (let [loading @(rf/subscribe [:property-info-loading])
        error @(rf/subscribe [:property-info-error])]
    (if loading
      [loader]
      (if error
        [:h3.text-center (:error error)]
        [show-property-info]))))