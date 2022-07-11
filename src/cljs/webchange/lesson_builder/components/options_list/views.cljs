(ns webchange.lesson-builder.components.options-list.views
  (:require
    [webchange.ui.index :as ui]))

;{:items    [{:id   :image-add
;                                :text "Add Image"
;                                :icon "plus"}
;                               {:id   :character-add
;                                :text "Add Character"
;                                :icon "plus"}
;                               {:id   :background
;                                :text "Background"
;                                :icon "plus"}
;                               {:id   :background-music
;                                :text "Background Music"
;                                :icon "plus"}]
;                    :on-click handle-click}

(defn- options-list-item
  [{:keys [id icon text on-click]}]
  (let [handle-click #(when (fn? on-click)
                        (on-click id))]
    [:div {:class-name "options-list-item"
           :on-click   handle-click}
     [:span.options-list-item--name
      text]
     [ui/icon {:icon       icon
               :class-name "options-list-item--icon"}]]))

(defn options-list
  [{:keys [items on-click]}]
  [:div.component--options-list
   (for [{:keys [id] :as item-data} items]
     ^{:key id}
     [options-list-item (assoc item-data :on-click on-click)])])
