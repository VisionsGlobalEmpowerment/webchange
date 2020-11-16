(ns webchange.editor-v2.dialog.dialog-form.views-form-target
  (:require
    [re-frame.core :as re-frame]
    [cljs-react-material-ui.reagent :as ui]
    [clojure.string :refer [capitalize]]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.dialog.dialog-form.state.actions-defaults :refer [get-inner-action]]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as dialog-form.actions]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]))

(defn- get-styles
  []
  {:wrapper {:style {:margin-bottom "15px"}}
   :title   {:display "inline-block"
             :margin  "5px 0"}
   :control {:margin "0 10px"
             :width  "150px"}})

(defn- value->option
  [value]
  {:text  (capitalize value)
   :value value})

(defn target-block
  []
  (let [targets (->> @(re-frame/subscribe [::translator-form.scene/available-animation-targets])
                     (map value->option))
        current-target (->> @(re-frame/subscribe [::translator-form.actions/current-phrase-action])
                            get-inner-action
                            :target)
        styles (get-styles)]
    [:div (:wrapper styles)
     [ui/typography {:variant "h6"
                     :style   (:title styles)}
      "Phrase teller:"]
     [ui/select {:value         (or current-target "")
                 :display-empty true
                 :on-change     #(re-frame/dispatch [::dialog-form.actions/set-phrase-action-target (->> % .-target .-value)])
                 :style         (:control styles)}
      (for [{:keys [text value]} targets]
        ^{:key value}
        [ui/menu-item {:value value} text])]]))
