(ns front-end.components.bingmap
  (:require ["bingmaps-react" :default BingMapsReact]))

(defn render-bing-map
  "This function is responsible for rendering the bing map"
  [view-options pushpins]
  (let [map-options {"bingMapsKey" "Ah6Onfkpjfr6HCEuVcuu2aoR9GJ75uGwKMBv4cbO-LUADDH5OZSQHuH0qyhedcDU"
                     "viewOptions" {"zoom" (if (empty? view-options)
                                             10
                                             16)}
                     "mapOptions" {"navigationBarMode" "square"}
                     "height" "70vh"
                     "width" "55vw"
                     "pushPinsWithInfoboxes" (if (empty? pushpins)
                                               []
                                               pushpins)}
        updated-map-options (if-not (empty? view-options)
                              (assoc-in map-options ["viewOptions" "center"]
                                        {"latitude" (:lat view-options)
                                         "longitude" (:lng view-options)})
                              map-options)]
    [:> BingMapsReact updated-map-options]))