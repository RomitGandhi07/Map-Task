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
 :start-loading
 (fn [db]
   (assoc db :loading true)))

(re-frame/reg-event-db
 :stop-loading
 (fn [db]
   (assoc db :loading false)))

(re-frame/reg-event-db
 :http-failure
 (fn [db [_ resp]]
   (assoc db :error resp)))


