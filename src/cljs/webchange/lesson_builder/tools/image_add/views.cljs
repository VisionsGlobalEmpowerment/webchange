(ns webchange.lesson-builder.tools.image-add.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.image-add.state :as state]
    [webchange.lesson-builder.widgets.select-image.views :refer [select-image]]
    [webchange.ui.index :as ui]))

(defn image-add
  []
  (re-frame/dispatch [::state/init])
  (fn []
    (let [name-value @(re-frame/subscribe [::state/name])
          image-value @(re-frame/subscribe [::state/image])]
      [:div.widget--image-add
       [:h1 "Add Image"]
       [ui/input {:label "Name"
                  :placeholder "Image Name"
                  :required? true
                  :value name-value
                  :on-change #(re-frame/dispatch [::state/set-name %])}]
       [ui/note {:text "Choose an image from our library or upload a new one."}]
       [select-image {:label "Image"
                      :value (:src image-value)
                      :on-change #(re-frame/dispatch [::state/set-image {:src (:url %)}])}]
       [ui/button {:class-name "apply-button"
                   :on-click #(re-frame/dispatch [::state/apply])}
        "Apply"]])))
