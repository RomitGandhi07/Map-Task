(ns front-end.events.property
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]))

(rf/reg-event-fx
 :fetch-property-by-id
 (fn [_ [_ id]]
   {:http-xhrio {:uri (str "http://localhost:7410/api/properties/get/" id)
                 :method :get
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:fetch-property-by-id-success :property-info]
                 :on-failure [:http-failure :property-info]}}))

(rf/reg-event-fx
 :fetch-property-by-id-success
 (fn [{:keys [db]} [_ path resp]]
   {:dispatch [:stop-loading path]
    :db (assoc db :property-info (:data resp))}))