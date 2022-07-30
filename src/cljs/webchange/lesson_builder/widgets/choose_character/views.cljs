(ns webchange.lesson-builder.widgets.choose-character.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.widgets.choose-character.state :as state]
    [webchange.ui.index :as ui]))

(defn- form-list
  [{:keys [items selected on-select]}]
  [:div.widget--choose-character--list
   (for [item items]
     ^{:key (:value item)}
     (let [background-color (or (:background item) (:color item))]
       [:div.widget--choose-character--list-item
        [:div {:class-name (ui/get-class-name {"img-wrapper" true
                                               "img-wrapper-active" (= (:value item) selected)})
               :style (when background-color
                        {:background-color background-color})
               :on-click #(on-select (:value item))}
         (when (:image item)
           [ui/image {:src (:image item)
                      :state :loaded
                      :class-name "character-image"}])]
        [:div.character-label
         (:name item)]]))])

(defn- sitting-form
  []
  (let [characters @(re-frame/subscribe [::state/available-sitting-characters])
        selected-character @(re-frame/subscribe [::state/selected-sitting-character])]
    [form-list {:items characters
                :selected selected-character
                :on-select #(re-frame/dispatch [::state/select-sitting-character %])}]))

(defn- skin-form
  []
  (let [skins @(re-frame/subscribe [::state/available-skins])
        selected-skin @(re-frame/subscribe [::state/selected-skin])]
    [form-list {:items skins
                :selected selected-skin
                :on-select #(re-frame/dispatch [::state/select-skin %])}]))

(defn- head-form
  []
  (let [heads @(re-frame/subscribe [::state/available-heads])
        selected-head @(re-frame/subscribe [::state/selected-head])]
    [form-list {:items heads
                :selected selected-head
                :on-select #(re-frame/dispatch [::state/select-head %])}]))

(defn- clothes-form
  []
  (let [clothes @(re-frame/subscribe [::state/available-clothes])
        selected-clothing @(re-frame/subscribe [::state/selected-clothing])]
    [form-list {:items clothes
                :selected selected-clothing
                :on-select #(re-frame/dispatch [::state/select-clothing %])}]))

(defn choose-character
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [props]
    (let [characters @(re-frame/subscribe [::state/available-characters])
          selected-character @(re-frame/subscribe [::state/selected-character])
          show-extra-params? @(re-frame/subscribe [::state/show-extra-params])]
      [:div.widget--choose-character
       [:div.widget--choose-character--list
        (for [character characters]
          ^{:key (:value character)}
          [:div.widget--choose-character--list-item
           [:div {:class-name (ui/get-class-name {"img-wrapper" true
                                                  "img-wrapper-active" (= (:value character) selected-character)})
                  :style {:background-color (:background character)}}
            [ui/image {:src (:image character)
                       :class-name "character-image"
                       :on-click #(re-frame/dispatch [::state/select-character (:value character)])}]]
           [:div.character-label
            (:name character)]])]
       (when (:character-options show-extra-params?)
         [:div.widget--choose-character--skins
          [:h2 "Character Options"]
          [sitting-form]])
       (when (:skin show-extra-params?)
         [:div.widget--choose-character--skins
          [:h2 "Skin"]
          [skin-form]])
       (when (:head show-extra-params?)
         [:div.widget--choose-character--skins
          [:h2 "Head"]
          [head-form]])
       (when (:clothes show-extra-params?)
         [:div.widget--choose-character--skins
          [:h2 "Clothes"]
          [clothes-form]])])))
