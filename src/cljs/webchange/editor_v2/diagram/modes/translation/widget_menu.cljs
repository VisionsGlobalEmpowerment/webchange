(ns webchange.editor-v2.diagram.modes.translation.widget-menu
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]))

(defn- add-action
  [node position]
  (re-frame/dispatch [::translator-form.actions/add-new-phrase-action node position]))

(defn menu
  [node]
  (r/with-let [menu-anchor (r/atom nil)]
              (let [handle-insert-action (partial add-action node)]
                [:div
                 [ui/icon-button
                  {:on-click #(reset! menu-anchor (.-currentTarget %))}
                  [ic/more-vert {:style {:color "#323232"}}]]
                 [ui/menu
                  {:anchor-el @menu-anchor
                   :open      (boolean @menu-anchor)
                   :on-close  #(reset! menu-anchor nil)}
                  [ui/menu-item
                   {:on-click #(handle-insert-action :after)}
                   "Insert After"]]])))
