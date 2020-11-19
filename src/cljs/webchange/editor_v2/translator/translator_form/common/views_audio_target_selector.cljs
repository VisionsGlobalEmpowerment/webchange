(ns webchange.editor-v2.translator.translator-form.common.views-audio-target-selector
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.state.audios :as translator-form.audios]
    [webchange.ui.utils :refer [deep-merge]]))

(defn- get-styles
  []
  {:control {:margin "0 10px"
             :width  "150px"}})

(defn- custom-target-option
  [{:keys [on-change]}]
  (r/with-let [custom-target (r/atom "")]
    (let [handle-save-click (fn [event]
                              (.stopPropagation event)
                              (on-change @custom-target)
                              (reset! custom-target ""))
          handle-input-click (fn [event] (.stopPropagation event))
          handle-input-change (fn [event] (reset! custom-target (.. event -target -value)))
          handle-input-key-down (fn [event] (when (= (.-key event) "Enter") (handle-save-click event)))]
      [ui/menu-item
       [ui/text-field {:placeholder "New Character"
                       :value       @custom-target
                       :on-click    handle-input-click
                       :on-change   handle-input-change
                       :on-key-down handle-input-key-down}]
       [ui/icon-button {:on-click handle-save-click}
        [ic/add]]])))

(defn- value->option
  [value]
  {:text  value
   :value value})

(defn- audio-target-selector-controlled
  [{:keys [value extra-options styles on-change custom-option-available?]}]
  (r/with-let [open? (r/atom false)]
    (let [targets (->> @(re-frame/subscribe [::translator-form.audios/available-audio-targets])
                       (map value->option)
                       ((fn [current-value targets]
                          (if-not (empty? current-value)
                            (concat [(value->option value)] targets)
                            targets)) value)
                       (into extra-options)
                       (distinct))
          handle-custom-target-change (fn [value]
                                        (reset! open? false)
                                        (on-change value))
          styles (-> (get-styles)
                     (deep-merge (or styles {})))]
      [ui/select {:value         value
                  :display-empty true
                  :on-click      #(reset! open? (not @open?))
                  :on-change     #(on-change (->> % .-target .-value))
                  :style         (:control styles)
                  :MenuProps     {:open @open?}}
       (for [{:keys [text value]} targets]
         ^{:key value}
         [ui/menu-item {:value value} text])
       (when custom-option-available?
         [custom-target-option {:on-change handle-custom-target-change}])])))

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
