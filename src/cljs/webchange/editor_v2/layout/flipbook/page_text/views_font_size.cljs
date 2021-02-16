(ns webchange.editor-v2.layout.flipbook.page-text.views-font-size
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.layout.flipbook.page-text.state :as state]))

(def font-sizes [8 9 10 11 12 14 18 24 30 36 48 60 72 96])

(defn- get-styles
  []
  (let [input-width 62
        button-radius 5
        button-common {:min-width 0
                       :padding   "0 13px"
                       :width     24}]
    {:container    {:display     "flex"
                    :align-items "center"}
     :label        {:margin-right "16px"}
     :control      {:display "flex"}
     :button-left  (merge button-common
                          {:border-radius (str button-radius "px 0 0 " button-radius "px")
                           :border-right  "0"
                           :margin-right  -2})
     :button-right (merge button-common
                          {:border-radius (str "0 " button-radius "px " button-radius "px 0")
                           :border-left   "0"
                           :margin-left   -2})
     :button-icon  {:font-size "14px"}
     :input        {:width         input-width
                    :border-radius "0"}
     :input-input  {:border-radius "0"}
     :menu         {:margin-left input-width}}))

(defn font-size-control
  [{:keys [id]}]
  (r/with-let [menu-anchor (r/atom nil)
               menu-open? (r/atom false)
               handle-menu-close #(reset! menu-open? false)
               handle-menu-open #(reset! menu-open? true)]
    (let [value @(re-frame/subscribe [::state/current-font-size id])
          disabled? @(re-frame/subscribe [::state/loading? id])
          handle-change (fn [font-size]
                          (re-frame/dispatch [::state/set-current-font-size id font-size]))
          handle-dec-click (fn [] (handle-change (dec value)))
          handle-inc-click (fn [] (handle-change (inc value)))
          handle-input-click (fn [] (when-not disabled? (handle-menu-open)))
          handle-input-change (fn [event] (handle-change (.parseInt js/Number (.. event -target -value))))
          handle-menu-item-click (fn [value]
                                   (handle-change value)
                                   (handle-menu-close))
          styles (get-styles)]
      [:div {:style (:container styles)}
       [ui/typography {:variant "body1"
                       :style   (:label styles)}
        "Font Size:"]
       [:div {:style (:control styles)}
        [ui/button {:variant  "outlined"
                    :style    (:button-left styles)
                    :on-click handle-dec-click
                    :disabled disabled?}
         [ic/remove {:style (:button-icon styles)}]]
        [ui/text-field {:value      value
                        :on-click   handle-input-click
                        :on-change  handle-input-change
                        :variant    "outlined"
                        :style      (:input styles)
                        :disabled   disabled?
                        :inputProps {:style (:input-input styles)
                                     :ref   #(when (some? %) (reset! menu-anchor %))}}]
        [ui/menu {:anchor-el            @menu-anchor
                  :open                 @menu-open?
                  :style                (:menu styles)
                  :disableAutoFocusItem true
                  :on-close             handle-menu-close}
         (for [size font-sizes]
           ^{:key size}
           [ui/menu-item {:on-click #(handle-menu-item-click size)} size])]
        [ui/button {:variant  "outlined"
                    :style    (:button-right styles)
                    :on-click handle-inc-click
                    :disabled disabled?}
         [ic/add {:style (:button-icon styles)}]]]])))
