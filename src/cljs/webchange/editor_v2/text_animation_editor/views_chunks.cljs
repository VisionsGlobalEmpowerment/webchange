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
  [{:keys [text filled? selected? on-click]}]
  (let [styles (get-styles)]
    [chip {:label    text
           :variant  "outlined"
           :color    (cond
                       selected? "secondary"
                       filled? "primary"
                       :else "default")
           :on-click on-click
           :style    (:text-chunk styles)}]))

(defn text-chunks
  [{:keys [active-parts parts selected-chunk-idx on-click]
    :or   {active-parts       []
           selected-chunk-idx -1
           on-click           #()}}]
  [:div.text-chunks
   (for [[index part] (map-indexed vector parts)]
     ^{:key index}
     [text-chunk {:text      part
                  :selected? (= index selected-chunk-idx)
                  :filled?   (some #{index} active-parts)
                  :on-click  #(on-click index)}])])
