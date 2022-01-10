(ns front-end.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 :name
 (fn [db]
   (:name db)))

(rf/reg-sub
 :loading
 (fn [db]
   (:loading db)))

;; MAP Subs

(rf/reg-sub
 :user-location
 (fn [db]
   (:user-location db)))

(rf/reg-sub
 :pushpins
 (fn [db]
   (:pushpins db)))

(rf/reg-sub
 :location-range
 (fn [db]
   (:location-range db)))