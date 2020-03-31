(ns webchange.editor-v2.translator.translator-form.views-form-audio-upload
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [clojure.string :refer [blank?]]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor.events :as events]
    [webchange.editor-v2.components.file-input.views :refer [select-file-form]]))

(defn- some-blank?
  [object fields]
  (->> fields
       (some (fn [field]
               (->> field
                    (get object)
                    blank?)))
       (boolean)))

(defn upload-file-form
  [{:keys [file on-cancel on-upload]}]
  (r/with-let [props (r/atom {})
               file-name (.-name file)]
              [:div
               [ui/button {:on-click #(on-cancel)
                           :color    "default"
                           :style    {:margin "0 16px"}}
                [ic/backspace]]
               [ui/typography {:variant "body1"
                               :style   {:display     "inline-flex"
                                         :line-height "60px"}}
                file-name]
               [ui/form-control
                {:style {:margin "0 16px"}}
                [ui/text-field
                 {:label     "Alias"
                  :value     (or (:alias @props) "")
                  :on-change #(swap! props assoc :alias (.. % -target -value))}]]
               [ui/form-control
                {:style {:margin "0 16px"}}
                [ui/text-field
                 {:label     "Target"
                  :value     (or (:target @props) "")
                  :on-change #(swap! props assoc :target (.. % -target -value))}]]
               [ui/button {:on-click #(on-upload file @props)
                           :disabled (some-blank? @props [:alias :target])
                           :variant  "contained"
                           :color    "secondary"}
                "Upload"]]))

(defn upload-audio-form
  [{:keys [scene-id]}]
  (r/with-let [selected-file (r/atom nil)]
              [ui/card {:style {:margin "16px 0"}}
               [ui/card-content {:style {:text-align "center"}}
                (if (nil? @selected-file)
                  [select-file-form {:on-change #(reset! selected-file %)}]
                  [upload-file-form {:file      @selected-file
                                     :on-upload #(do (re-frame/dispatch [::events/upload-asset scene-id %1 %2])
                                                     (reset! selected-file nil))
                                     :on-cancel #(reset! selected-file nil)}])]]))
