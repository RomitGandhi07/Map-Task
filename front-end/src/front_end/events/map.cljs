(ns front-end.events.map
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]))

(rf/reg-event-db
 :init-map-data
 (fn [db]
   (-> db
       (dissoc :user-location)
       (dissoc :pushpins)
       (assoc :location-range 1))))

;; (rf/reg-event-db
;;  :create-current-location-pushpin
;;  (fn [db [_ data]]
;;    (assoc db :pushpins [{"center" {"latitude" (:lat data)
;;                                    "longitude" (:lng data)}
;;                          "options" {"title" "You are Here"
;;                                     "color" "red"}
;;                         :current true}])))

(rf/reg-event-fx
 :location-permission-granted
 (fn [{:keys [db]} [_ data]]
   {;;:dispatch [:create-current-location-pushpin data]
    ;;:dispatch [:stop-loading :map]
    :db (-> db
            (assoc-in [:loading :map] false)
            (assoc :user-location (assoc data :view-options {:lat (:lat data)
                                                             :lng (:lng data)}))
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
       (assoc-in [:loading :map] false)
       (assoc :user-location {:permission false}))))

(rf/reg-event-fx
 :find-nearby-properties
 (fn [{:keys [db]}]
   {:http-xhrio {:uri "http://localhost:7410/api/properties/find-nearby-properties"
                 :method :post
                 :params {:lat (str (get-in db [:user-location :lat]))
                          :lng (str (get-in db [:user-location :lng]))
                          :range (str (:location-range db))}
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:find-nearby-properties-success]
                 :on-failure [:http-failure :find-nearby-properties]}}))

(rf/reg-event-db
 :find-nearby-properties-success
 (fn [db [_ resp]]
   (let [current-pushpin (some (fn [pushpin]
                                 (when (:current pushpin)
                                   pushpin)) 
                               (:pushpins db))
         resp-pushpins (mapv (fn [pushpin]
                                        {"center" {"latitude" (:lat pushpin)
                                                   "longitude" (:lng pushpin)}
                                         "options" {"title" (:title pushpin)
                                                    "description" (:description pushpin)
                                                    "icon" "./images/current_location.png"}})
                                      (:data resp))]
     (-> db
         (assoc :pushpins (conj resp-pushpins current-pushpin))
         (assoc :nearby-places (:data resp))))))

(rf/reg-event-db
 :update-location-range
 (fn [db [_ value]]
   (assoc db :location-range value)))

(rf/reg-event-db
 :update-view-options
 (fn [db [_ data]]
   (assoc-in db [:user-location :view-options] data)))