(ns webchange.editor-v2.scene-diagram.views-diagram
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.diagram-utils.diagram-model.init :refer [init-diagram-model]]
    [webchange.editor-v2.diagram-utils.diagram-widget :refer [diagram-widget]]
    [webchange.editor-v2.scene-diagram.nodes-factory.nodes-factory :refer [get-diagram-items]]
    [webchange.editor-v2.scene-diagram.scene-parser.scene-parser :refer [scene-data->actions-tracks]]
    [webchange.editor-v2.state :as state]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- expand-diagram-button
  []
  (r/with-let [tooltip-open? (r/atom false)
               handle-open-tooltip #(reset! tooltip-open? true)
               handle-close-tooltip #(reset! tooltip-open? false)
               handle-fullscreen #(do (handle-close-tooltip)
                                      (re-frame/dispatch [::state/diagram-fullscreen]))
               handle-exit-fullscreen #(do (handle-close-tooltip)
                                           (re-frame/dispatch [::state/diagram-exit-fullscreen]))
               button (fn [{:keys [title icon on-click]}]
                        [ui/tooltip {:title    title
                                     :open     @tooltip-open?
                                     :on-open  handle-open-tooltip
                                     :on-close handle-close-tooltip}
                         [ui/icon-button {:on-click on-click}
                          icon]])]
    (let [fullscreen? @(re-frame/subscribe [::state/diagram-fullscreen?])]
      (if fullscreen?
        [button {:title    "Exit Full Screen"
                 :icon     (r/as-element [ic/fullscreen-exit])
                 :on-click handle-exit-fullscreen}]
        [button {:title    "Full Screen"
                 :icon     (r/as-element [ic/fullscreen])
                 :on-click handle-fullscreen}]))))

(defn- diagram-toolbar
  []
  [:div.toolbar
   [expand-diagram-button]])

(defn dialogs-diagram
  [{:keys [scene-data class-name]}]
  (let [actions-tracks (scene-data->actions-tracks scene-data)
        {:keys [nodes links]} (get-diagram-items scene-data actions-tracks)
        {:keys [engine]} (init-diagram-model :phrases nodes links {:locked? true})]
    [:div {:class-name (get-class-name (cond-> {"dialogs-diagram" true}
                                               (some? class-name) (assoc class-name true)))}
     [diagram-toolbar]
     [diagram-widget {:engine engine
                      :zoom?  true}]]))
