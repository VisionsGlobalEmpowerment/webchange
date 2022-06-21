(ns webchange.ui.components.breadcrumbs.views
  (:require
    [clojure.spec.alpha :as s]
    [webchange.ui.components.icon.views :refer [system-icon]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(s/def :breadcrumbs/route some?)
(s/def :breadcrumbs/text string?)
(s/def :breadcrumbs/item (s/keys :req-un [:breadcrumbs/text :breadcrumbs/route]))
(s/def :breadcrumbs/items (s/coll-of :breadcrumbs/item))

(defn- breadcrumbs-item
  [{:keys [last-item? on-click route text]}]
  (let [handle-click #(when-not last-item? (on-click route))]
    [:div {:class-name (get-class-name {"bbs--breadcrumbs-item"            true
                                        "bbs--breadcrumbs-item--last-item" last-item?})
           :on-click   handle-click}
     text]))

(defn- breadcrumbs-delimiter
  []
  [:div {:class-name "bbs--breadcrumbs-delimiter"} "|"])

(defn breadcrumbs
  [{:keys [items on-click]}]
  {:pre [(s/valid? :breadcrumbs/items items)
         (fn? on-click)]}
  (let [has-arrow? (-> items count (> 1))
        handle-arrow-click #(-> items butlast last :route on-click)]
    [:div {:class-name "bbs--breadcrumbs"}
     (when has-arrow?
       [system-icon {:icon       "arrow-left"
                     :class-name "back-button"
                     :on-click   handle-arrow-click}])
     (for [[idx item] (map-indexed vector items)]
       (let [last-item? (->> items count dec (>= idx))]
         ^{:key idx}
         [:<>
          [breadcrumbs-item (merge item
                                   {:on-click   on-click
                                    :last-item? last-item?})]
          (when-not last-item?
            [breadcrumbs-delimiter])]))]))
