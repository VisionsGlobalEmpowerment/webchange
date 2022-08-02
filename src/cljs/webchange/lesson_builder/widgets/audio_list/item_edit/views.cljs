(ns webchange.lesson-builder.widgets.audio-list.item-edit.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.widgets.audio-list.item-edit.state :as state]
    [webchange.ui.index :as ui]))

(defn item-edit
  []
  (let [ref (atom nil)]
    (r/create-class
      {:component-did-mount
       (fn [this]
         (re-frame/dispatch [::state/init (r/props this)])
         (js/setTimeout #(.select @ref) 100))

       :component-will-unmount
       (fn [this]
         (re-frame/dispatch [::state/save (r/props this)]))

       :reagent-render
       (fn [{:keys [url]}]
         (let [value @(re-frame/subscribe [::state/value url])
               handle-change #(re-frame/dispatch [::state/set-value url %])
               handle-enter-press #(re-frame/dispatch [::state/save {:url url}])
               handle-esc-press #(re-frame/dispatch [::state/reset {:url url}])]
           [ui/input {:value          value
                      :on-change      handle-change
                      :on-enter-press handle-enter-press
                      :on-esc-press   handle-esc-press
                      :class-name     "audio-item--item-edit"
                      :ref-atom       ref}]))})))
