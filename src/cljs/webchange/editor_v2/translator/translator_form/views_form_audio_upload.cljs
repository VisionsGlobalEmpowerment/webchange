(ns webchange.editor-v2.translator.translator-form.views-form-audio-upload
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [clojure.string :refer [blank?]]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor.events :as events]))

(defn drop-event->file
  [event]
  (-> event
      (.-dataTransfer)
      (.-files)
      (.item 0)))

(defn change-event->file
  [event]
  (-> event
      (.. -target -files)
      (.item 0)))

(defn select-file-form
  [{:keys [on-change]}]
  (r/with-let [drag-over? (r/atom false)
               file-input (atom nil)
               drag-over-style {:border        "solid 1px #fff"
                                :border-radius "3px"
                                :border-style  "dashed"}]
              [:div {:id            "drop-target"
                     :on-drag-over  #(do (.preventDefault %)
                                         (reset! drag-over? true))
                     :on-drag-leave #(do (.preventDefault %)
                                         (reset! drag-over? false))
                     :on-drop       #(do (.preventDefault %)
                                         (-> % drop-event->file on-change))
                     :style         (if @drag-over? drag-over-style {})}
               [:input {:type      "file"
                        :ref       #(reset! file-input %)
                        :on-change #(-> % change-event->file on-change)
                        :style     {:display "none"}}]
               [ui/button {:on-click #(.click @file-input)}
                [ui/typography {:variant "h3"}
                 [ic/cloud-upload {:style {:font-size 48
                                           :margin    16}}]]
                [ui/typography {:variant "h5"}
                 (if @drag-over?
                   "Drop file here"
                   "Upload new file")]]]))

(defn upload-file-form
  [{:keys [file on-cancel on-upload]}]
  (r/with-let [alias (r/atom "")
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
                  :value     @alias
                  :on-change #(reset! alias (.. % -target -value))}]]
               [ui/button {:on-click #(on-upload file @alias)
                           :disabled (blank? @alias)
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
