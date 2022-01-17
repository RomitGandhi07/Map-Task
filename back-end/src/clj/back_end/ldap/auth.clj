(ns back-end.ldap.auth
  (:require [back-end.ldap.core :refer [ldap-insert ldap-get ldap-compare ldap-bind]]))

(def remaining-dn-path "ou=demo_users,ou=system")

(defn insert-user
  [data]
  (let [dn (str "cn=" (:email data) "," remaining-dn-path)]
    (ldap-insert dn {:objectClass #{"inetOrgPerson"}
                     :sn (:email data)
                     :uid (:username data)
                     :userPassword (:password data)})))

(defn get-user-by-email
  [email]
  (ldap-get (str "cn=" email "," remaining-dn-path)))

(defn bind-user-credentials
  [email password]
  (ldap-bind (str "cn=" email "," remaining-dn-path) password))
