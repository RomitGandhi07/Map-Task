(ns back-end.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[back-end started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[back-end has shut down successfully]=-"))
   :middleware identity})
