(ns webchange.editor-v2.layout.components.activity-stage.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.layout.components.activity-stage.state :as stage-state]
    [webchange.state.state-flipbook :as state-flipbook]
    [webchange.ui.utils :refer [deep-merge]]))

(defn- get-styles
  []
  {:container  {}
   :img-option {:height          "auto"
                :display         "flex"
                :justify-content "space-between"
                :align-items     "center"
                :padding-right   "16px"}
   :text       {:overflow      "hidden"
                :text-overflow "ellipsis"
                :max-width     "200px"
                :margin-right  "16px"}
   :image      {:max-width     "120px"
                :max-height    "60px"
                :border-radius "5px"
                :border        "solid 1px #464646"}})

(defn select-stage
  [{:keys [styles]
    :or   {styles {}}}]
  (let [stages @(re-frame/subscribe [::state-flipbook/stage-options])
        enabled? (seq stages)
        current-stage @(re-frame/subscribe [::state-flipbook/current-stage])
        styles (-> (get-styles)
                   (deep-merge styles))]
    (when enabled?
      [ui/form-control {:margin "normal"
                        :style  (:container styles)}
       [ui/select {:value         (or current-stage "")
                   :display-empty true
                   :variant       "outlined"
                   :on-change     #(re-frame/dispatch [::stage-state/select-stage (.. % -target -value)])
                   :render-value  (fn [value]
                                    (->> (fn []
                                           (let [{:keys [idx img name]} (if-not (= value "")
                                                                          (some (fn [stage] (and (= (:idx stage) value) stage)) stages)
                                                                          {:idx  ""
                                                                           :name "Select stage"})]
                                             [:div {:value idx
                                                    :style (if (some? img) (:img-option styles) {})}
                                              name]))
                                         (r/reactify-component)
                                         (r/create-element)))}
        (for [{:keys [idx name img]} stages]
          ^{:key idx}
          [ui/menu-item {:value idx
                         :style (if (some? img) (:img-option styles) {})}
           [:div {:style (:text styles)} name]
           (when (some? img)
             [:img {:src   img
                    :style (:image styles)}])])]])))
