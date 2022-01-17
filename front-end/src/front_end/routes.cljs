(ns front-end.routes
    (:require [re-frame.core :as rf]
              [reitit.coercion.malli]
              [reitit.frontend]
              [reitit.frontend.easy :as rfe]
              [reitit.frontend.controllers :as rfc]
              [front-end.views :as views]
              [front-end.pages.map :as maps]
              [front-end.pages.property :as property]
              [front-end.pages.properties :as properties]
              [front-end.pages.registration :as registration]))

;; Subs

(rf/reg-sub
 :current-route
 (fn [db]
   (:current-route db)))

;;; Events

(rf/reg-event-fx
 :navigate
 (fn [_cofx [_ & route]]
   {:navigate! route}))

;; Triggering navigation from events.
(rf/reg-fx
 :navigate!
 (fn [route]
   (apply rfe/push-state route)))


(rf/reg-event-db
 :navigated
 (fn [db [_ new-match]]
   (let [old-match   (:current-route db)
         controllers (rfc/apply-controllers (:controllers old-match) new-match)]
     (assoc db :current-route (assoc new-match :controllers controllers)))))

;;; Routes

(def routes
  ["/"
   [""
    {:name      :routes/home
     :view      views/main-panel
     :link-text "Home"}]
   ["about"
    {:name      :routes/about
     :view      views/about-page
     :link-text "About"}]
   ["signup"
    {:name      :routes/signup
     :view      registration/registration
     :link-text "Home"}]
   ["map"
    {:name      :routes/map
     :view      maps/show-map
     :link-text "Map"
     :controllers [{:start (fn [_] 
                             (rf/dispatch [:init-map-data])
                             (rf/dispatch [:start-loading :map]))}]}]
   
   ["search"
    {:name      :routes/search
     :view      properties/search-properties
     :link-text "Search Properties"
     :controllers [{:start (fn [_]
                             (rf/dispatch [:init-map-data]))}]}]

   ["property/:id"
    {:name :routes/property
     :view property/property-info
     :link-text "Property Info"
     :controllers [{:parameters {:path [:id]}
                    :start (fn [{:keys [path]}]
                             (let [{id :id} path]
                               (rf/dispatch [:start-loading :property-info])
                               (rf/dispatch [:fetch-property-by-id id])))}]}]
   ])

(def router
  (reitit.frontend/router
   routes
   {:data {:coercion reitit.coercion.malli/coercion}}))

(defn on-navigate [new-match]
  (when new-match
    (rf/dispatch [:navigated new-match])
    (rf/dispatch [:clear-loading-error])))

(defn init-routes! []
  (rfe/start!
   router
   on-navigate
   {:use-fragment true}))