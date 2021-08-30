(ns webchange.editor-v2.components.activity-tracks.track.views
  (:require
    [webchange.ui-framework.components.index :refer [icon-button menu]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- track-node
  [{:keys [node on-click]}]
  (let [{:keys [addition-action selected? text title type]} node
        handle-click (fn [] (on-click node))]
    [:div (cond-> {:class-name (get-class-name (-> {"track-node" true
                                                    "selected"   selected?}
                                                   (assoc (str "type-" type) true)))}
                  (fn? on-click) (assoc :on-click handle-click))
     [:div.title (or title text)]
     [:div.footer
      [:div.addition-action
       (when (some? addition-action)
         (let [{:keys [icon handler]} addition-action]
           [icon-button {:icon     icon
                         :on-click #(handler node)}]))]
      [:div.type type]]]))

(defn- actions-component
  [{:keys [actions]}]
  [menu {:items         actions
         :icon          "add"
         :title         "Actions"
         :class-name    "track-menu"
         :list-position "right"}])

(defn track
  [{:keys [actions nodes on-node-click]}]
  [:div.track-wrapper
   [:div.track
    (for [[idx node] (map-indexed vector nodes)]
      ^{:key idx}
      [track-node (merge {:node     node
                          :on-click on-node-click})])]
   (when (some? actions)
     [:div.track-actions
      [actions-component {:actions actions}]])])
