(ns webchange.editor-v2.translator.translator-form.common.views-audio-target-selector
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.state.audios :as translator-form.audios]
    [webchange.utils.deep-merge :refer [deep-merge]]
    [webchange.ui-framework.components.index :refer [select]]))

(defn- get-styles
  []
  {:control {:margin "0 10px"
             :width  "150px"}})

(defn- value->option
  [value]
  {:text  value
   :value value})

(defn- audio-target-selector-controlled
  [{:keys [value extra-options on-change]}]
  (let [targets (->> @(re-frame/subscribe [::translator-form.audios/available-audio-targets])
                     (map value->option)
                     (into extra-options)
                     (distinct))]
    [select {:value     value
             :options   targets
             :variant   "outlined"
             :on-change #(on-change %)}]))

(defn audio-target-selector
  [props]
  (if (contains? props :value)
    [audio-target-selector-controlled props]
    (r/with-let [current-target (r/atom (or (:default-value props) ""))
                 handle-change #(do (reset! current-target %)
                                    ((:on-change props) %))]
      [audio-target-selector-controlled (-> props
                                            (assoc :on-change handle-change)
                                            (assoc :value @current-target)
                                            (dissoc :default-value))])))
