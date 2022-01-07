(ns front-end.components.loader
  (:require ["react-loader-spinner" :default Loader]))

(defn loader 
  ([] (loader {}))
  ([data] [:div {:style {:text-align "center"
                         :margin-top "5%"}}
           [:> Loader {:type (get data :type "Oval")
                       :color (get data :color "#000000")
                       :height (get data :height "35")
                       :width (get data :width "35")}]]))