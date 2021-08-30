(ns webchange.editor-v2.dialog.dialog-form.views-form-diagram
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.components.activity-tracks.track.views :refer [track]]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as dialog-form.actions]
    [webchange.editor-v2.dialog.utils.dialog-action :as actions-defaults]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.editor-v2.dialog.dialog-form.diagram.items-factory.nodes-factory :refer [get-diagram-items]]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form]
    [webchange.utils.scene-action-data :refer [dialog-action? get-inner-action]]))

(defn- add-action
  [action path]
  (let [scene-data @(re-frame/subscribe [::translator-form.scene/scene-data])
        node-list-count (count (get-in scene-data (concat path [:data])))
        node {:path (concat path [(if (= node-list-count 0) 0 (dec node-list-count))])}
        relative-position (if (= node-list-count 0) :before :after)]
    (re-frame/dispatch [::dialog-form.actions/add-new-scene-action action relative-position node])))

(defn- add-concept-action
  [path]
  (let [scene-data @(re-frame/subscribe [::translator-form.scene/scene-data])
        node-list-count (count (get-in scene-data (concat path [:data])))
        node {:path (concat path [(if (= node-list-count 0) 0 (dec node-list-count))])}
        relative-position (if (= node-list-count 0) :before :after)]
    (re-frame/dispatch [::dialog-form.actions/add-new-phrase-concept-action relative-position node])))

(def actions {:insert-after         {:text    "Insert activity"
                                     :handler (partial add-action actions-defaults/default-action)}
              :insert-concept-after {:text    "Insert concept"
                                     :handler (partial add-concept-action)}})

(defn menu
  [path]
  (r/with-let [menu-anchor (r/atom nil)]
    (let [close-menu #(reset! menu-anchor nil)]
      (when-not (empty? actions)
        [:div.diagram-menu-container
         [ui/icon-button
          {:on-click #(reset! menu-anchor (.-currentTarget %))}
          [ic/more-vert {:style {:color "#323232"}}]]
         [ui/menu
          {:anchor-el @menu-anchor
           :open      (boolean @menu-anchor)
           :on-close  close-menu}
          (for [[key {:keys [text handler]}] actions]
            ^{:key key}
            [ui/menu-item
             {:on-click #(do (handler path)
                             (close-menu))}
             text])]]))))

(defn simple-dialog
  [{:keys [path]}]                                          ;; data coming in is a string
  (let [scene-data @(re-frame/subscribe [::translator-form.scene/scene-data])
        {:keys [nodes]} (get-diagram-items scene-data path)
        nodes-data (->> nodes
                        (map (fn [{:keys [data]}]
                               (let [inner-action (get-inner-action (:data data))]
                                 (-> data
                                     (assoc :title (or (:phrase-text inner-action)
                                                       (:phrase-placeholder inner-action)))
                                     (assoc :type (if (dialog-action? inner-action) "dialog" ""))))))
                        (filter (fn [{:keys [type]}]
                                  (= type "dialog"))))
        handle-node-click (fn [node-data]
                            (re-frame/dispatch [::translator-form.actions/set-current-phrase-action node-data]))]
    [track {:nodes         nodes-data
            :on-node-click handle-node-click}]))

(defn diagram-block
  []
  (let [settings @(re-frame/subscribe [::translator-form/components-settings :diagram])
        dialog-action @(re-frame/subscribe [::translator-form.actions/current-dialog-action-info])
        path (:path dialog-action)]
    (when-not (:hide? settings)
      [:div.diagram-block
       [menu path]
       [simple-dialog {:path path}]])))
