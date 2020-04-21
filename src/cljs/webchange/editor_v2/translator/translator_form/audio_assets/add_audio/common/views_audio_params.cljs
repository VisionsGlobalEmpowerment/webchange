(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.common.views-audio-params
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [clojure.string :refer [capitalize]]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.audio-assets.subs :as subs]))

(defn- get-styles
  []
  {:target-control {:margin "6px 8px 0 8px"
                    :width  "200px"}
   :alias-control  {:margin "6px 8px 0 8px"}})

(defn audio-params-panel
  [{:keys [on-save on-cancel]}]
  (r/with-let [props (r/atom {})]
              (let [targets (->> @(re-frame/subscribe [::subs/available-targets])
                                 (map (fn [target] {:text  (capitalize target)
                                                    :value target}))
                                 (into [{:text  "No Target"
                                         :value ""}]))
                    handle-save (fn [] (on-save @props))
                    handle-cancel (fn [] (on-cancel))
                    styles (get-styles)]
                [:div
                 [ui/icon-button {:on-click handle-cancel}
                  [ic/backspace]]
                 [ui/select {:value         (or (:target @props) "")
                             :display-empty true
                             :on-change     #(swap! props assoc :target (.. % -target -value))
                             :style         (:target-control styles)}
                  (for [{:keys [text value]} targets]
                    ^{:key value}
                    [ui/menu-item {:value value} text])]
                 [ui/form-control {:style (:alias-control styles)}
                  [ui/text-field {:label     "Alias"
                                  :variant   "outlined"
                                  :value     (or (:alias @props) "")
                                  :on-change #(swap! props assoc :alias (.. % -target -value))}]]
                 [ui/icon-button {:on-click handle-save}
                  [ic/backup]]])))
