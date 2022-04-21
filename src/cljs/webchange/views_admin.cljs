(ns webchange.views-admin
  (:require
    ["react" :as react]
    [reagent.core :as r]
    [shadow.lazy :as lazy]
    [webchange.util :refer (lazy-component)]))

(def product-listing (lazy-component webchange.admin.views/index))

(defn admin
  [props]
  ;(js/setTimeout (fn []
  ;                 (with-module "admin" (fn [module]
  ;                                        (print "module" module))))
  ;               3000)
  [:div
   "Admin stub"
   [:> react/Suspense {:fallback (r/as-element [:div "Loading ..."])}
    [:> product-listing props]]])
