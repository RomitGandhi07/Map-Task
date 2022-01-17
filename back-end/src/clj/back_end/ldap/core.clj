(ns back-end.ldap.core
  (:require [clj-ldap.client :as ldap]))

(def connection (atom nil))

(defn create-ldap-connection
  []
  (reset! connection (ldap/connect {:host "localhost:10389"}))
  ;(println (ldap/compare? @connection "cn=demo@gmail.com,ou=demo_users,ou=system" :sn "Demo"))
  ;(println (ldap/search @connection "cn=romit@gmail.com,ou=demo_users,ou=system" {:filter "(uid=RomitGandhi07)"}))

  ;(ldap/release-connection @connection (ldap/bind @connection "cn=romit@gmail.com,ou=demo_users,ou=system" "Demo@123"))
  ;(println (ldap/bind @connection "cn=romit@gmail.com,ou=demo_users,ou=system" "Demo@123"))
  )

(defn ldap-insert
  [dn entry]
  (ldap/add @connection dn entry)
  )

(defn ldap-get
  [dn]
  (ldap/get @connection dn))

(defn ldap-compare
  [dn attribute value]
  (ldap/compare? @connection dn attribute value))

(defn ldap-bind
  [dn password]
  (ldap/bind @connection dn password))