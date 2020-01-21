(ns webchange.editor-v2.diagram.widget
  (:require
    ["@projectstorm/react-diagrams" :refer [DiagramWidget]]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]
    [webchange.editor-v2.diagram.diagram-model.init :refer [init-diagram-model]]
    [webchange.editor-v2.diagram.diagram-utils.utils :refer [reorder zoom-to-fit]]
    [webchange.editor-v2.utils :refer [keyword->caption]]))

(defn reorder-nodes
  [this]
  (let [engine (aget this "engine")]
    (.setTimeout js/window #(do (reorder engine)
                                (zoom-to-fit engine)) 100)))

(defn toolbar
  [engine {:keys [values current-value on-changed]}]
  (r/with-let [menu-anchor (r/atom nil)]
              [:div {:style {:padding          4
                             :position         "absolute"
                             :top              0
                             :left             0
                             :z-index          10
                             :background-color "rgba(0, 0, 0, 0.2)"
                             :width            "100%"
                             :height           36
                             :display          "flex"
                             :justify-content  "space-between"}}
               (when-not (= 0 (count values))
                 [ui/select {:value     (or current-value "")
                             :on-change #(let [value (keyword (.. % -target -value))]
                                           (on-changed value))}
                  (for [value values]
                    ^{:key (str value)}
                    [ui/menu-item {:value value
                                   :style {:text-transform "capitalize"}}
                     (keyword->caption value)])])
               (when-not (nil? engine)
                 [ui/icon-button
                  {:style    {:padding 4}
                   :on-click #(reset! menu-anchor (.-currentTarget %))}
                  [ic/more-vert {:style {:font-size 12}}]])
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

(defn empty-graph-placeholder
  []
  [:div {:style {:display         "flex"
                 :height          "100%"
                 :align-items     "center"
                 :justify-content "center"}}
   [:div {:style {:color     "white"
                  :font-size "20px"}}
    "Empty"]])

(defn diagram-render
  [{:keys [graph mode root-selector]}]
  (if (empty? graph)
    [:div.diagram-container
     [toolbar nil root-selector]
     [empty-graph-placeholder]]
    (let [engine (init-diagram-model graph mode)
          this (r/current-component)]
      (aset this "engine" engine)
      [:div.diagram-container
       [toolbar engine root-selector]
       [:> DiagramWidget {:diagramEngine engine}]])))

(def diagram-widget
  (with-meta diagram-render
             {:component-did-mount  diagram-did-mount
              :component-did-update diagram-did-update}))
