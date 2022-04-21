(ns webchange.views-admin
  (:require
    ["react" :as react]
    [reagent.core :as r]
    [shadow.lazy :as lazy]
    [webchange.util :refer (lazy-component)]
    ))

;    [reagent.core :as r]
;    ["react" :as react]
;    [shadow.loader :as loader]


#_(def admin-wrapper (.lazy react (fn [& args]
                                    (js/console.log "args" args)
                                    (loader/load "admin" (fn [component]
                                                           (print "component" component))))))

;(loader.init!)

;(defn admin
;  []
;  (r/with-let [ ]
;    (js/setTimeout (fn []
;                     (-> (loader/load "admin")
;                         (.then (fn [component]
;                                  (print "component" component)) #(print "err" %))))
;                   1000)
;    [:div
;     "Admin loader"]))

;"/js/compiled/admin.js"

#_(def x (.lazy react (fn [& args]
                      (js/console.log "args" args)
                      ;(loader/load "admin" (fn [component]
                      ;                       (print "component" component)))
                      (.resolve js/Promise #js {:default (r/reactify-component (fn [] [:div "xxx"]))}))))


(def product-listing (lazy-component webchange.admin.views/index))

(defn admin
  []
  ;(js/setTimeout (fn []
  ;                 (with-module "admin" (fn [module]
  ;                                        (print "module" module))))
  ;               3000)
  [:div
   "Admin stub"
   [:> react/Suspense {:fallback (r/as-element [:div "Loading ..."])}
    [:> product-listing]]])
