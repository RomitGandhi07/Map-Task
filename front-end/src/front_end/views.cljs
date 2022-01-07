(ns front-end.views)

(defn main-panel []
  (let [name "Re-frame"]
    [:div
     [:h1
      "Hello from " name]
     ]))

(defn about-page []
  [:h1 "This is about1 page"])
