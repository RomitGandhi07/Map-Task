(ns back-end.routes.auth
  (:require [back-end.handlers.auth :as auth-handlers]))


(defn create-auth-routes []
  ["/auth"
   {:swagger {:tags ["Authentication"]}}

   ["/login" {:post {:summary "Login API"
                     :parameters {:body {:email string? :password string?}}
                     :handler auth-handlers/login}}]

   ["/register" {:post {:summary "Registration API"
                        :parameters {:body {:username string?, :email string? :password string?}}
                        :handler auth-handlers/register}}]])