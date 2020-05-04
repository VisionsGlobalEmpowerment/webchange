(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.common.views-audio-params
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [clojure.string :refer [capitalize]]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.common.views-target-selector :refer [target-selector]]))

(defn- get-styles
  []
  {:target-control {:margin "6px 8px 0 8px"
                    :width  "200px"}
   :alias-control  {:margin "6px 8px 0 8px"}})

(defn audio-params-panel
  [{:keys [on-save on-cancel]}]
  (r/with-let [props (r/atom {})]
              (let [handle-save (fn [] (on-save @props))
                    handle-cancel (fn [] (on-cancel))
                    styles (get-styles)]
                [:div
                 [ui/icon-button {:on-click handle-cancel}
                  [ic/backspace]]
                 [target-selector {:default-value ""
                                   :on-change     #(swap! props assoc :target %)
                                   :extra-options [{:text  "No Target"
                                                    :value ""}]
                                   :styles        {:control (:target-control styles)}}]
                 [ui/form-control {:style (:alias-control styles)}
                  [ui/text-field {:label     "Alias"
                                  :variant   "outlined"
                                  :value     (or (:alias @props) "")
                                  :on-change #(swap! props assoc :alias (.. % -target -value))}]]
                 [ui/icon-button {:on-click handle-save}
                  [ic/backup]]])))
