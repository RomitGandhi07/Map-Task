(ns front-end.core
  (:require
   [day8.re-frame.http-fx]
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [front-end.events]
   [front-end.events.map]
   [front-end.subs]
   [front-end.config :as config]
   [front-end.routes :as routes]))


(defn main-page
  []
  (let [current-route @(re-frame/subscribe [:current-route])]
    [:div
     (when current-route
       [(-> current-route :data :view)])]))

(defn root []
  [:div#root
   {:style {:width "100vw"}}
   [main-page]])

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (routes/init-routes!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [root] root-el)))

(defn init []
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
