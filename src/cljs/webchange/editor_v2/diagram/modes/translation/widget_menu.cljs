(ns webchange.editor-v2.diagram.modes.translation.widget-menu
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]))

(defn- add-action
  [position node]
  (re-frame/dispatch [::translator-form.actions/add-new-phrase-action node position]))

(defn- delete-action
  [node]
  (re-frame/dispatch [::translator-form.actions/delete-phrase-action node]))

(def actions
  {:insert-before {:text "Insert Before"
                   :handler (partial add-action :before)}
   :insert-after  {:text "Insert After"
                   :handler (partial add-action :after)}
   :delete        {:text "Delete"
                   :handler delete-action}})

(defn menu
  [node]
  (r/with-let [menu-anchor (r/atom nil)]
              (let [close-menu #(reset! menu-anchor nil)
                    available-actions (->> @(re-frame/subscribe [::translator-form.actions/phrase-node-available-actions node])
                                           (select-keys actions))]
                (when-not (empty? available-actions)
                  [:div
                   [ui/icon-button
                    {:on-click #(reset! menu-anchor (.-currentTarget %))}
                    [ic/more-vert {:style {:color "#323232"}}]]
                   [ui/menu
                    {:anchor-el @menu-anchor
                     :open      (boolean @menu-anchor)
                     :on-close  close-menu}
                    (for [[key {:keys [text handler]}] available-actions]
                      ^{:key key}
                      [ui/menu-item
                       {:on-click #(do (handler node)
                                       (close-menu))}
                       text])]]))))
