(ns webchange.ui.pages.forms.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]
    [webchange.ui.pages.layout :refer [layout panel]]))

(def model {:name     {:label     "Name"
                       :type      :text
                       :required? true}
            :password {:label "Password"
                       :type  :password}
            :type     {:label   "Selection"
                       :type    :select
                       :options [{:text  "Choose"
                                  :value ""}
                                 {:text  "Value 1"
                                  :value "value-1"}
                                 {:text  "Value 2"
                                  :value "value-2"}]}
            :about    {:label "About"
                       :type  :text-multiline}
            :link     {:label "Student Login Link"
                       :type  :link
                       :text  "Copy Link"}
            :archive  {:label    "Archive School"
                       :type     :action
                       :icon     "archive"
                       :on-click #(print "Form Action clicked:" %)}})

(def data {:name     "Custom User"
           :password "admin123456"
           :type     "value-2"
           :about    "And who are you? The proud lord said\nThat I must bow so low?\nOnly a cat of a different coat\nThat's all the truth I know\nIn a coat of gold or a coat of red\nA lion still has claws\nMine are long and sharp my lord\nAs long and sharp as yours"
           :link     "localhost:3000/"
           :archive  :data-to-archive})

(def default-errors (->> (keys model)
                         (map #(vector % "Required Field"))
                         (into {})))

(defn- form-wrapper
  [{:keys [title]}]
  [:div
   [:h3 title]
   (->> (r/current-component)
       (r/children)
       (into [:div {:class-name "form-wrapper"}]))])

(defn form-default
  []
  (let [handle-save #(print "Handle Save:" %)
        handle-cancel #(print "Handle Cancel:" %)]
    [form-wrapper {:title "Empty"}
     [ui/form {:form-id   :form-default
               :model     model
               :on-save   handle-save
               :on-cancel handle-cancel}]]))

(defn form-error
  []
  (let [handle-save #(print "Handle Save:" %)
        handle-cancel #(print "Handle Cancel:" %)]
    [form-wrapper {:title "Filled & Failed"}
     [ui/form {:form-id   :form-error
               :model     model
               :data      data
               :errors    default-errors
               :on-save   handle-save
               :on-cancel handle-cancel}]]))

(defn form-filled
  []
  (let [handle-save #(print "Handle Save:" %)
        handle-cancel #(print "Handle Cancel:" %)]
    [form-wrapper {:title "With Data"}
     [ui/form {:form-id   :form-filled
               :model     model
               :data      data
               :on-save   handle-save
               :on-cancel handle-cancel}]]))

(defn form-disabled
  []
  (let [handle-save #(print "Handle Save:" %)
        handle-cancel #(print "Handle Cancel:" %)]
    [form-wrapper {:title "Disabled"}
     [ui/form {:form-id   :form-filled
               :model     model
               :data      data
               :disabled? true
               :on-save   handle-save
               :on-cancel handle-cancel}]]))

(defn page
  []
  [:div#page--forms
   [layout {:title "Forms"}
    [panel {:class-name "forms-panel"}
     [form-default]
     [form-disabled]
     [form-error]]]])


