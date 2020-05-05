(ns webchange.editor-v2.diagram.widget.views-toolbar
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]
    [webchange.editor-v2.diagram.diagram-utils.utils :refer [reorder zoom-to-fit]]
    [webchange.editor-v2.utils :refer [keyword->caption]]))

(defn toolbar
  [{:keys [engine force-update]} {:keys [values current-value on-changed]}]
  (r/with-let [menu-anchor (r/atom nil)]
              [:div {:style {:padding          4
                             :position         "absolute"
                             :top              0
                             :left             0
                             :z-index          10
                             :background-color "rgba(0, 0, 0, 0.2)"
                             :width            "100%"
                             :height           50
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
                 "Fit to zoom"]
                [ui/menu-item
                 {:on-click #(reset! force-update (not @force-update))}
                 "Force update"
                 [ui/switch {:checked @force-update}]]]]))
