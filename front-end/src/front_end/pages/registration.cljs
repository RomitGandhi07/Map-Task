(ns front-end.pages.registration
  (:require [re-frame.core :as rf]
            [fork.re-frame :as fork]
            [front-end.components.toastNotification :refer [toast-notification]]))

(defn registration
  []
  (let [errors nil]
    [:div.container
     [toast-notification]
     [:div.row
      [:div.col-6
       [:h3 "Sign Up"]]]
     (when errors
       [:div
        ;;[error errors]
        "Error"
        ])
     [:div.columns
      [:div.column.is-4.is-offset-4
       [fork/form
        {:initial-values {:username ""
                          :email ""
                          :password ""}
         :keywordize-keys true
         ;;:validation validate-user-registration-data,
         :on-submit (fn [state]
                      ;;(rf/dispatch [:user-registration (:values state)])
                      (js/console.log state)
                      ),
         :prevent-default? true}
        (fn [{:keys
              [values
               touched
               errors
               handle-change
               handle-blur
               handle-submit
               attempted-submissions]}]
          (let [render-error (fn [x]
                               (when (and (touched x) (get errors x))
                                 [:div.has-text-danger (get errors x)]))]
            [:form
             {:on-submit handle-submit}
             [:div.form-group
              [:label.col-form-label "Username"]
              [:div
               [:input.form-control {:type :text
                                     :name :username
                                     :value (:username values)
                                     :on-change handle-change
                                     :on-blur handle-blur
                                     :placeholder "Enter your user name"}]]
              [render-error :username]]
             [:div.form-group
              [:label.col-form-label "Email"]
              [:div
               [:input.form-control {:type :email
                                    :name :email
                                    :value (:email values)
                                    :on-change handle-change
                                    :on-blur handle-blur
                                    :placeholder "Enter your email Eg: abc@xyz.com"}]]
              [render-error :email]]

             [:div.form-group
              [:label.col-form-label "Password"]
              [:div
               [:input.form-control {:type :password
                                     :name :password
                                     :value (:password values)
                                     :on-change handle-change
                                     :on-blur handle-blur
                                     :placeholder "Enter your password"}]]
              [render-error :password]]
             [:div.text-center.mt-4
              [:button.btn.btn-primary
               {:disabled (and (seq errors) (> attempted-submissions 0))}
               "Submit"]]
             ]))]]]]))