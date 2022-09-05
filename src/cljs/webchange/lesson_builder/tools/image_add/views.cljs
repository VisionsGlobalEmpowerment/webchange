(ns webchange.lesson-builder.tools.image-add.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.tools.image-add.state :as state]
    [webchange.lesson-builder.widgets.select-image.views :refer [select-image]]
    [webchange.ui.index :as ui]))

(defn image-add
  []
  (r/create-class
    {:display-name "Add Image Form"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset (r/props this)]))

     :reagent-render
     (fn []
       (let [name-error @(re-frame/subscribe [::state/name-error])
             name-value @(re-frame/subscribe [::state/name])
             image-value @(re-frame/subscribe [::state/image])
             image-selected? @(re-frame/subscribe [::state/image-selected?])
             apply-disabled? @(re-frame/subscribe [::state/apply-disabled?])]
         [:div.widget--image-add
          [:div.form-wrapper
           [:h1 "Add Image"]
           (if image-selected?
             [ui/input {:label       "Name"
                        :placeholder "Image Name"
                        :required?   true
                        :value       name-value
                        :error       name-error
                        :on-change   #(re-frame/dispatch [::state/set-name %])}]
             [ui/note {:text "Choose an image from our library or upload a new one."}])
           [select-image {:label            "Image"
                          :value            (:src image-value)
                          :class-name-image "image-add--preview"
                          :on-change        #(re-frame/dispatch [::state/set-image {:src  (:url %)
                                                                                    :name (-> % :tags last :name)}])}]]

          [ui/button {:class-name "apply-button"
                      :disabled?  apply-disabled?
                      :on-click   #(re-frame/dispatch [::state/apply])}
           "Apply"]]))}))
