(ns webchange.editor-v2.layout.flipbook.page-text.views-text-control
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.layout.flipbook.page-text.state :as state]))

(defn- update-rows-number
  [el value]
  (->> value
       (filter #(re-find #"\n" %))
       (count)
       (inc)
       (set! (.-rows el))))

(defn- text-input
  []
  (let [el (atom nil)
        dirty? (atom false)]
    (r/create-class
      {:display-name "stage-text-input"

       :component-did-update
                     (fn [this]
                       (let [{:keys [value]} (r/props this)]
                         (when (and (not (= value (.-value @el)))
                                    (not @dirty?))
                           (set! (.-value @el) value)
                           (update-rows-number @el value))))

       :reagent-render
                     (fn [{:keys [value on-change disabled?]}]
                       [ui/text-field {:default-value value
                                       :on-change     (fn [event]
                                                        (let [value (.. event -target -value)]
                                                          (reset! dirty? true)
                                                          (update-rows-number @el value)
                                                          (on-change value)))
                                       :placeholder   "Enter page text here"
                                       :multiline     true
                                       :full-width    true
                                       :variant       "outlined"
                                       :disabled      disabled?
                                       :inputProps    {:ref   #(when (some? %) (reset! el %))
                                                       :style {:height   "auto"
                                                               :overflow "hidden"}}}])})))

(defn text-control
  [{:keys [id]}]
  (let [value @(re-frame/subscribe [::state/current-text id])
        handle-change (fn [value] (re-frame/dispatch [::state/set-current-text id value]))
        loading? @(re-frame/subscribe [::state/loading? id])]
    [:div
     [text-input {:value     value
                  :on-change handle-change
                  :disabled? loading?}]]))
