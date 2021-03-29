(ns webchange.editor-v2.dialog.dialog-form.views-audios-list-item-data
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.common.views-audio-target-selector :refer [audio-target-selector]]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- get-styles
  []
  {:info-form         {:display         "flex"
                       :justify-content "space-between"
                       :width           "100%"}
   :target-form       {:margin-right "16px"}
   :target-form-label {:top "-16px"}
   :target-form-input {:margin 0}
   :alias-form        {:width "170px"}
   :form-button       {:margin-left "8px"
                       :padding     "8px"}
   :menu-item-icon    {:font-size "18px"}
   :form-button-save  {:margin-left      "8px"
                       :padding          "6px"
                       :background-color (get-in-theme [:palette :secondary :main])}})

(defn audio-data
  [{:keys [target]}]
  [:div
   (when-not (nil? target)
     [ui/chip {:label target
               :style {:margin  "0 10px 0 0"
                       :padding "0"}}])])

(defn audio-data-form
  [{:keys [alias target on-save on-cancel]}]
  (r/with-let [current-data (r/atom {:alias  alias
                                     :target target})]
    (let [styles (get-styles)]
      [:div {:style (:info-form styles)}
       [:div
        [ui/form-control {:style (:target-form styles)}
         [ui/input-label {:html-for "target"
                          :style    (:target-form-label styles)}
          "Character"]
         [audio-target-selector {:default-value            (or (:target @current-data) "")
                                 :styles                   {:control (:target-form-input styles)}
                                 :on-change                #(swap! current-data assoc :target %)
                                 :custom-option-available? true}]]
        [ui/form-control {:style (:alias-form styles)}
         [ui/text-field {:label     "File Name"
                         :value     (or (:alias @current-data) "")
                         :on-change #(swap! current-data assoc :alias (-> % .-target .-value))
                         :variant   "outlined"}]]]
       [:div
        [ui/tooltip {:title "Cancel" :placement "top"}
         [ui/icon-button
          {:style    (:form-button styles)
           :color    "primary"
           :on-click on-cancel}
          [ic/clear {:style (:menu-item-icon styles)}]]]
        [ui/tooltip {:title "Save" :placement "top"}
         [ui/icon-button
          {:style    (:form-button-save styles)
           :on-click #(on-save @current-data)}
          [ic/done {:style (:menu-item-icon styles)}]]]]])))
