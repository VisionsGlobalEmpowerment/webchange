(ns webchange.editor-v2.scene.data.stage.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.scene.state.stage :as stage-state]))

(defn- get-styles
  []
  {:img-option {:height          "auto"
                :display         "flex"
                :justify-content "space-between"
                :align-items     "center"
                :padding-right   "16px"}
   :image      {:max-width     "120px"
                :max-height    "60px"
                :border-radius "5px"
                :border        "solid 1px #464646"}})

(defn select-stage
  []
  (let [stages @(re-frame/subscribe [::stage-state/stage-options])
        enabled? (seq stages)
        current-stage @(re-frame/subscribe [::stage-state/current-stage])
        styles (get-styles)]
    (when enabled?
      [ui/form-control {:full-width true
                        :margin     "normal"}
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
           name
           (when (some? img)
             [:img {:src   img
                    :style (:image styles)}])])]])))
