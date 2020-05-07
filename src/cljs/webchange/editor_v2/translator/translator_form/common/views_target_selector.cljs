(ns webchange.editor-v2.translator.translator-form.common.views-target-selector
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

(defn- target-selector-controlled
  [{:keys [value extra-options styles on-change]}]
  (let [targets (->> @(re-frame/subscribe [::translator-form.audios/available-targets])
                     (map (fn [target] {:text  (capitalize target)
                                        :value target}))
                     (into extra-options))
        styles (-> (get-styles)
                   (deep-merge (or styles {})))]
    [ui/select {:value         value
                :display-empty true
                :on-change     #(on-change (->> % .-target .-value))
                :style         (:control styles)}
     (for [{:keys [text value]} targets]
       ^{:key value}
       [ui/menu-item {:value value} text])]))

(defn target-selector
  [props]
  (if (contains? props :value)
    [target-selector-controlled props]
    (r/with-let [current-target (r/atom (or (:default-value props) ""))
                 handle-change #(do (reset! current-target %)
                                    ((:on-change props) %))]
                [target-selector-controlled (-> props
                                                (assoc :on-change handle-change)
                                                (assoc :value @current-target)
                                                (dissoc :default-value))])))
