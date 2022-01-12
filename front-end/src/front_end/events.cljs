(ns front-end.events
  (:require
   [re-frame.core :as re-frame]
   [front-end.db :as db]
   ))

(re-frame/reg-event-db
 :initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 :clear-loading-error
 (fn [db _]
   (-> db
       (assoc :loading {})
       (assoc :error {}))))

(re-frame/reg-event-db
 :start-loading
 (fn [db [_ path]]
   (assoc-in db [:loading path] true)))

(re-frame/reg-event-db
 :stop-loading
 (fn [db [_ path]]
   (assoc-in db [:loading path] false)))

(re-frame/reg-event-db
 :http-failure
 (fn [db [_ path resp]]
   (-> db
       (assoc-in [:error path] {:status (:status resp)
                                :error (:error (:response resp))})
       (assoc-in [:loading path] false)
       (assoc :toast {:error (get-in resp [:response :error])}))))

(re-frame/reg-event-db
 :clear-toast-notification
 (fn [db]
   (dissoc db :toast)))


