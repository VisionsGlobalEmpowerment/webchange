(ns webchange.utils.lazy-component
  (:require
    ["react" :as react]
    [reagent.core :as r]
    [shadow.lazy :as lazy]))

(defn lazy-component [loadable]
  (react/lazy
    (fn []
      (-> (lazy/load loadable)
          (.then (fn [root-el]
                   ;; we need wrap the loaded component one extra level so live-reload actually works
                   ;; since React will keep a reference to the initially loaded fn and won't update it
                   #js {:default (r/reactify-component (fn [props] [@loadable (js->clj props :keywordize-keys true)]))}
                   ))))))
