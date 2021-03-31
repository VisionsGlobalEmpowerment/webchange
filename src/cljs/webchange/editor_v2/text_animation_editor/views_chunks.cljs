(ns webchange.editor-v2.text-animation-editor.views-chunks
  (:require
    [webchange.ui.theme :refer [get-in-theme]]
    [webchange.ui-framework.components.index :refer [chip]]))

(defn- get-styles
  []
  {:audio-container {:padding "16px"}
   :text-chunk      {:color       (get-in-theme [:palette :text :primary])
                     :margin-left "8px"}})

(defn- text-chunk
  [{:keys [text selected? on-click]}]
  (let [styles (get-styles)]
    [chip {:label    text
           :variant  "outlined"
           :color    (if selected? "secondary" "primary" )
           :on-click on-click
           :style    (:text-chunk styles)}]))

(defn text-chunks
  [{:keys [parts selected-chunk-idx on-click]
    :or   {selected-chunk-idx -1
           on-click           #()}}]
  [:div.text-chunks
   (for [[index part] (map-indexed vector parts)]
     ^{:key index}
     [text-chunk {:text      part
                  :selected? (= index selected-chunk-idx)
                  :on-click  #(on-click index)}])])
