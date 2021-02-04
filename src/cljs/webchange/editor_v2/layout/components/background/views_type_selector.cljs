(ns webchange.editor-v2.layout.components.background.views-type-selector
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.background.state :as background]))

(def available-types [{:value :single
                       :title "Single Image"}
                      {:value :layered
                       :title "Layered Background"}])

(defn- get-styles
  []
  {:selector-wrapper {:margin-bottom "15px"}})

(defn- get-current-index
  [current-type]
  (some (fn [[index {:keys [value]}]]
          (and (= value current-type) index))
        (map-indexed vector available-types)))

(defn- index->type
  [index]
  (-> available-types (nth index) :value))

(defn type-selector
  []
  (let [current-type @(re-frame/subscribe [::background/background-type])
        current-index (get-current-index current-type)
        handle-change (fn [_ index] (re-frame/dispatch [::background/set-type (index->type index)]))
        styles (get-styles)]
    [ui/tabs {:value           current-index
              :on-change       handle-change
              :indicator-color "primary"
              :text-color      "primary"
              :style           (:selector-wrapper styles)}
     (for [{:keys [value title]} available-types]
       ^{:key value}
       [ui/tab {:label title}])]))
