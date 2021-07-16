(ns webchange.editor-v2.activity-form.generic.components.select-stage.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.state.state-book :as state]
    [webchange.utils.deep-merge :refer [deep-merge]]))

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
  [{:keys [styles on-change]
    :or   {styles {}}}]
  (let [stages @(re-frame/subscribe [::state/stage-options])
        enabled? (seq stages)
        current-stage @(re-frame/subscribe [::state/current-stage-idx])
        styles (-> (get-styles)
                   (deep-merge styles))
        on-change (if on-change on-change
                                #(re-frame/dispatch [::state/select-stage (.. % -target -value)]))]
    (when enabled?
      [ui/form-control {:margin     "normal"
                        :full-width true
                        :style      (:container styles)}
       [ui/select {:value         (or current-stage "")
                   :display-empty true
                   :variant       "outlined"
                   :on-change     on-change
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
