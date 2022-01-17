(ns back-end.handlers.auth
  (:require [back-end.ldap.auth :as auth-ldap]
            [failjure.core :as f]
            [clojure.string :as string]))

(defn check-user-already-registered
  [email]
  (let [already-exist? (auth-ldap/get-user-by-email email)]
    (when already-exist?
      (f/fail {:status 404
               :error "User already exist with same email address"}))))

(defn check-user-has-not-registered
  [email]
  (let [already-exist? (auth-ldap/get-user-by-email email)]
    (when-not already-exist?
      (f/fail {:status 404
               :error "User has not registered yet..."}))))

(defn login
  "This handler is for login API"
  [{{{:keys [email password] :as data} :body} :parameters}]
  (try
    (f/attempt-all [;; Checks user registered or not
                    _ (check-user-has-not-registered email)

                    user-logged-in? (auth-ldap/bind-user-credentials email password)

                    ;; If user is not successfully logged in then throw error
                    _ (if-not (= "success" (:name user-logged-in?))
                        (f/fail {:status 400 :error "User with given email and password not found..."}))]

                   ;;Send Response
                   {:status 200
                    :body {:message "User successfully logged in..."}}
                   (f/when-failed [{:keys [message]}]
                                  {:status (or (:status message) 500)
                                   :body  {:error (or (:error message) "Something went wrong...Please try again")}}))
    (catch Exception e
      ;(println (.intValue (.getResultCode e)))
      (let [message (.getMessage e)])
      (cond
        (string/starts-with? (.getMessage e) "INVALID_CREDENTIALS")
        {:status 404
         :body {:error "User with given email and password not found..."}}
        :default
        {:status 500
         :body {:error "Something went wrong...Please try again"}}))))

(defn register
  "This handler is for the registration API"
  [{{{:keys [username email password] :as data} :body} :parameters}]
  (try
    (f/attempt-all [;; Checks user is already registered or not
                    _ (check-user-already-registered email)

                    ;; Adds user to LDAP server
                    user-added? (auth-ldap/insert-user data)

                    ;; If user is not successfully added then throw error
                    _ (if-not (= "success" (:name user-added?))
                        (f/fail {:status 400 :error "User registration failed... Please try again..."}))]

                   ;;Send Response
                   {:status 200
                    :body {:message "User successfully added..."}}
                   (f/when-failed [{:keys [message]}]
                                  {:status (or (:status message) 500)
                                   :body  {:error (or (:error message) "Something went wrong...Please try again")}}))
    (catch Exception e
      ;(println (.getMessage e))
      {:status 500
       :body {:error "Something went wrong...Please try again"}})))