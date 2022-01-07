(ns back-end.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [back-end.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[back-end started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[back-end has shut down successfully]=-"))
   :middleware wrap-dev})
