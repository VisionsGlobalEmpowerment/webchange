(ns webchange.editor-v2.components.file-input.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]
    [webchange.ui.theme :refer [get-in-theme]]
    [webchange.ui.utils :refer [deep-merge]]))

(defn get-styles
  ([]
   (get-styles nil))
  ([size]
   {:wrapper          {}
    :button           {:border-color (get-in-theme [:palette :border :default])}
    :button-drag-over {:border-color (get-in-theme [:palette :border :default])
                       :border-style "dashed"
                       :border-width "3px"}
    :icon             (case size
                        "normal" {:font-size "2rem"}
                        {:font-size "3rem"})
    :icon-wrapper     (case size
                        "normal" {:margin "-2px 10px 0px -2px"}
                        {:margin "20px 16px 16px 0"})
    :label            (case size
                        "normal" {:font-size "1rem"}
                        {:font-size "1.5rem"})}))

(defn- drop-event->file
  [event]
  (-> event
      (.-dataTransfer)
      (.-files)
      (.item 0)))

(defn- change-event->file
  [event]
  (-> event
      (.. -target -files)
      (.item 0)))


(defn select-file-form
  [{:keys [text drop-text on-change size styles]}]
  (let [text (or text "Upload new file")
        drop-text (or drop-text "Drop file here")
        styles (-> (get-styles size)
                   (deep-merge (or styles {})))]
    (r/with-let [drag-over? (r/atom false)
                 file-input (atom nil)]
                [:div {:style (:wrapper styles)}
                 [:input {:type      "file"
                          :ref       #(reset! file-input %)
                          :on-change #(-> % change-event->file on-change)
                          :style     {:display "none"}}]
                 [ui/button {:on-click      #(.click @file-input)
                             :on-drag-over  #(do (.preventDefault %)
                                                 (reset! drag-over? true))
                             :on-drag-leave #(do (.preventDefault %)
                                                 (reset! drag-over? false))
                             :on-drop       #(do (.preventDefault %)
                                                 (-> % drop-event->file on-change))
                             :style         (if @drag-over?
                                              (:button-drag-over styles)
                                              (:button styles))}
                  [ui/typography {:variant "h3"
                                  :style   (:icon-wrapper styles)}
                   [ic/cloud-upload {:style (:icon styles)}]]
                  [ui/typography {:variant "h5"
                                  :style   (:label styles)}
                   (if @drag-over?
                     drop-text
                     text)]]])))
