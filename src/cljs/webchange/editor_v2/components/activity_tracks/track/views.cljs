(ns webchange.editor-v2.components.activity-tracks.track.views
  (:require
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- track-node
  [{:keys [node on-click]}]
  (let [{:keys [selected? title type]} node
        handle-click (fn [] (on-click node))]
    [:div (cond-> {:class-name (get-class-name {"track-node" true
                                                "selected"   selected?})}
                  (fn? on-click) (assoc :on-click handle-click))
     [:div.title title]
     [:div.type type]]))

(defn track
  [{:keys [nodes on-node-click]}]
  [:div.track
   (for [[idx node] (map-indexed vector nodes)]
     ^{:key idx}
     [track-node (merge {:node     node
                         :on-click on-node-click})])])
