(ns front-end.events.properties
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]))

(defn properties->pushpins
  [data]
  (mapv (fn [pushpin]
          {"center" {"latitude" (:lat pushpin)
                     "longitude" (:lng pushpin)}
           "options" {"title" (:title pushpin)
                      "description" (:description pushpin)
                      "icon" "./images/current_location.png"}})
        data))

(rf/reg-event-db
 :init-map-data
 (fn [db]
   (-> db
       (dissoc :user-location)
       (dissoc :pushpins)
       (assoc  :view-options nil)
       (assoc  :location-range 1))))

(rf/reg-event-fx
 :location-permission-granted
 (fn [{:keys [db]} [_ data]]
   {;;:dispatch [:stop-loading :map]
    :db (-> db
            (assoc :view-options {:lat (:lat data)
                                  :lng (:lng data)})
            (assoc :user-location data)
            (assoc :pushpins [{"center" {"latitude" (:lat data)
                                         "longitude" (:lng data)}
                               "options" {"title" "You are Here"
                                          "description" ""
                                          "color" "blue"}
                               :current true}]))}))

(rf/reg-event-db
 :location-permission-denied
 (fn [db]
   (-> db
       ;;(assoc-in [:loading :map] false)
       (assoc :user-location {:permission false})
       (assoc :toast {:error "Location permission is required"}))))

(rf/reg-event-fx
 :find-nearby-properties
 (fn [{:keys [db]}]
   {:dispatch [:start-loading :properties]
    :http-xhrio {:uri "http://localhost:7410/api/properties/find-nearby-properties"
                 :method :post
                 :params {:lat (str (get-in db [:user-location :lat]))
                          :lng (str (get-in db [:user-location :lng]))
                          :range (str (:location-range db))}
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:find-nearby-properties-success :properties]
                 :on-failure [:http-failure :properties]}}))

(rf/reg-event-fx
 :find-nearby-properties-success
 (fn [{:keys [db]} [_ path resp]]
   (let [current-pushpin (some (fn [pushpin]
                                 (when (:current pushpin)
                                   pushpin))
                               (:pushpins db))
         resp-pushpins (properties->pushpins (:data resp))]
     {:dispatch [:stop-loading path]
      :db (-> db
              (assoc :pushpins (conj resp-pushpins current-pushpin))
              (assoc :properties (:data resp)))})))

(rf/reg-event-fx
 :search-properties
 (fn [_ [_ data]]
   {:dispatch [:start-loading :properties]
    :http-xhrio {:uri "http://localhost:7410/api/properties/search"
                 :method :post
                 :params data
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:search-properties-success :properties]
                 :on-failure [:http-failure :properties]}}))

(rf/reg-event-fx
 :search-properties-success
 (fn [{:keys [db]} [_ path resp]]
   (let [pushpins (properties->pushpins (:data resp))]
     {:dispatch [:stop-loading path]
      :db (-> db
              (assoc :pushpins pushpins)
              (assoc :properties (:data resp))
              (assoc :view-options (if-not (empty? (:data resp))
                                     {:lat (-> resp
                                               :data
                                               first
                                               :lat)
                                      :lng (-> resp
                                               :data
                                               first
                                               :lng)})))})))

(rf/reg-event-db
 :update-view-options
 (fn [db [_ data]]
   (assoc db :view-options data)))