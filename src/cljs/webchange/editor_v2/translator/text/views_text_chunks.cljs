(ns webchange.editor-v2.translator.text.views-text-chunks
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- get-styles
  []
  {:audio-container {:padding "16px"}
   :text-chunk      {:color       (get-in-theme [:palette :text :primary])
                     :margin-left "8px"}})

(defn- text-chunk
  [{:keys [text selected? on-click]}]
  (let [styles (get-styles)]
    [ui/chip {:label    text
              :variant  "outlined"
              :color    (if selected? "primary" "secondary")
              :on-click on-click
              :style    (:text-chunk styles)}]))

(defn text-chunks
  [{:keys [parts selected-chunk-idx on-click]
    :or   {selected-chunk-idx -1
           on-click           #()}}]
  [:div
   (for [[index part] (map-indexed vector parts)]
     ^{:key index}
     [text-chunk {:text      part
                  :selected? (= index selected-chunk-idx)
                  :on-click  #(on-click index)}])])
