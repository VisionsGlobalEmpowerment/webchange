(ns webchange.editor-v2.diagram.widget
  (:require
    ["@projectstorm/react-diagrams" :refer [DiagramWidget]]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]
    [webchange.editor-v2.diagram.diagram-model.init :refer [init-diagram-model]]
    [webchange.editor-v2.diagram.diagram-utils.utils :refer [reorder zoom-to-fit]]))

(defn reorder-nodes
  [this]
  (let [engine (aget this "engine")]
    (.setTimeout js/window #(do (reorder engine)
                                (zoom-to-fit engine)) 100)))

(defn toolbar
  [engine]
  (r/with-let [menu-anchor (r/atom nil)]
              [:div {:style {:padding          4
                             :position         "absolute"
                             :text-align       "right"
                             :top              0
                             :left             0
                             :z-index          10
                             :background-color "rgba(0, 0, 0, 0.2)"
                             :width            "100%"
                             :height           28}}
               [ui/icon-button
                {:style    {:padding 4}
                 :on-click #(reset! menu-anchor (.-currentTarget %))}
                [ic/more-vert {:style {:font-size 12}}]]
               [ui/menu
                {:anchor-el @menu-anchor
                 :open      (boolean @menu-anchor)
                 :on-close  #(reset! menu-anchor nil)}
                [ui/menu-item
                 {:on-click #(reorder engine)}
                 "Reorder"]
                [ui/menu-item
                 {:on-click #(zoom-to-fit engine)}
                 "Fit to zoom"]]]))

(defn diagram-did-mount
  [this]
  (reorder-nodes this))

(defn diagram-did-update
  [this]
  (reorder-nodes this))

(defn diagram-render
  [{:keys [graph mode]}]
  (let [engine (when-not (nil? graph)
                 (init-diagram-model graph mode))
        this (r/current-component)]
    (aset this "engine" engine)
    [:div.diagram-container
     [toolbar engine]
     (when-not (nil? engine)
       [:> DiagramWidget {:diagramEngine engine}])]))

(def diagram-widget
  (with-meta diagram-render
             {:component-did-mount  diagram-did-mount
              :component-did-update diagram-did-update}))
