(ns front-end.events.map
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]))

(rf/reg-event-db
 :init-map-data
 (fn [db]
   (-> db
       (dissoc :user-location)
       (dissoc :pushpins)
       (assoc :location-range 50))))

(rf/reg-event-db
 :create-current-location-pushpin
 (fn [db [_ data]]
   (assoc db :pushpins [{"center" {"latitude" (:lat data)
                                   "longitude" (:lng data)}
                         "options" {"title" "You are Here"
                                    "color" "red"}
                        :current true}])))

(rf/reg-event-fx
 :location-permission-granted
 (fn [{:keys [db]} [_ data]]
   {
    ;;:dispatch [:create-current-location-pushpin data] 
    :db (-> db 
            (assoc :loading false)
            (assoc :user-location data)
            (assoc :pushpins [{"center" {"latitude" (:lat data)
                                         "longitude" (:lng data)}
                               "options" {"title" "You are Here"
                                          "color" "red"}
                               ;;:current true
                               }]))}))

(rf/reg-event-db
 :location-permission-denied
 (fn [db]
   (assoc db :user-location {:permission false})))

(rf/reg-event-fx
 :find-nearby-places
 (fn [{:keys [db]} [_ data]]
   {:http-xhrio {:uri "http://localhost:7410/api/map/find-nearby-places"
                 :method :post
                 :params {:lat (str (get-in db [:user-location :lat]))
                          :lng (str (get-in db [:user-location :lng]))
                          :range (str (:location-range db))}
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:find-nearby-places-success]
                 :on-failure [:http-failure :delete-book]}}))

(rf/reg-event-db
 :find-nearby-places-success
 (fn [db [_ resp]]
   (let [current-pushpin (some (fn [pushpin]
                                 (when (= "You are Here" (get-in pushpin ["options" "title"]))
                                   pushpin)) 
                               (:pushpins db))
         resp-pushpins (mapv (fn [pushpin]
                                        {"center" {"latitude" (:lat pushpin)
                                                   "longitude" (:lng pushpin)}
                                         "options" {"title" (:city pushpin)}})
                                      (:data resp))]
     (assoc db :pushpins (conj resp-pushpins current-pushpin)))))

(rf/reg-event-db
 :update-location-range
 (fn [db [_ value]]
   (assoc db :location-range value)))