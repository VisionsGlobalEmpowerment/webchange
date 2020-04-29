(ns webchange.editor-v2.translator.translator-form.audio-assets.common.views-target-selector
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [clojure.string :refer [capitalize]]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.state.audios :as translator-form.audios]
    [webchange.ui.utils :refer [deep-merge]]))

(defn- get-styles
  []
  {:control {:margin "0 10px"
             :width  "150px"}})

(defn target-selector
  [{:keys [default-value extra-options styles on-change]}]
  (let [targets (->> @(re-frame/subscribe [::translator-form.audios/available-targets])
                     (map (fn [target] {:text  (capitalize target)
                                        :value target}))
                     (into extra-options))
        styles (-> (get-styles)
                   (deep-merge (or styles {})))]
    (r/with-let [current-target (r/atom default-value)]
                [ui/select {:value         @current-target
                            :display-empty true
                            :on-change     #(do (reset! current-target (->> % .-target .-value))
                                                (on-change @current-target))
                            :style         (:control styles)}
                 (for [{:keys [text value]} targets]
                   ^{:key value}
                   [ui/menu-item {:value value} text])])))
