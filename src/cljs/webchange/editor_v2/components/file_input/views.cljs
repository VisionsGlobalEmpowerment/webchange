(ns webchange.editor-v2.components.file-input.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]))

(defn- get-styles
  [size]
  {:icon         (case size
                   "normal" {:font-size "2rem"}
                   {:font-size "3rem"})
   :icon-wrapper (case size
                   "normal" {:margin "-2px 10px 0px -2px"}
                   {:margin "20px 16px 16px 0"})
   :label        (case size
                   "normal" {:font-size "1rem"}
                   {:font-size "1.5rem"})
   :drag-over    {:border        "solid 1px #fff"
                  :border-radius "3px"
                  :border-style  "dashed"}})

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
  [{:keys [on-change size]}]
  (let [styles (get-styles size)]
    (r/with-let [drag-over? (r/atom false)
                 file-input (atom nil)]
                [:div {:id            "drop-target"
                       :on-drag-over  #(do (.preventDefault %)
                                           (reset! drag-over? true))
                       :on-drag-leave #(do (.preventDefault %)
                                           (reset! drag-over? false))
                       :on-drop       #(do (.preventDefault %)
                                           (-> % drop-event->file on-change))
                       :style         (if @drag-over? (:drag-over styles) {})}
                 [:input {:type      "file"
                          :ref       #(reset! file-input %)
                          :on-change #(-> % change-event->file on-change)
                          :style     {:display "none"}}]
                 [ui/button {:on-click #(.click @file-input)}
                  [ui/typography {:variant "h3"
                                  :style   (:icon-wrapper styles)}
                   [ic/cloud-upload {:style (:icon styles)}]]
                  [ui/typography {:variant "h5"
                                  :style   (:label styles)}
                   (if @drag-over?
                     "Drop file here"
                     "Upload new file")]]])))
