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
 :map-loading
 (fn [db]
   (get-in db [:loading :map])))

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

(rf/reg-sub
 :nearby-places
 (fn [db]
   (:nearby-places db)))

;; Property Subs
(rf/reg-sub
 :property-info-loading
 (fn [db]
   (get-in db [:loading :property-info])))

(rf/reg-sub
 :property-info-error
 (fn [db]
   (get-in db [:error :property-info])))

(rf/reg-sub
 :property-info
 (fn [db]
   (:property-info db)))