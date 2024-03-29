(ns webchange.admin.widgets.course-info-form.view
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.course-info-form.state :as state]
    [webchange.utils.languages :refer [language-options]]
    [webchange.validation.specs.course-spec :as course-spec]
    [webchange.ui.index :as ui]))

(defn publish-control
  []
  (let [published? @(re-frame/subscribe [::state/published])
        toggle #(re-frame/dispatch [::state/set-published %])
        loading? @(re-frame/subscribe [::state/publish-loading?])]
    [ui/switch {:checked?       published?
                :indeterminate? loading?
                :label          "Publish"
                :on-change      toggle
                :color          "yellow-1"
                :class-name     "switch-control"}]))

(defn course-info-form
  [{:keys [class-name course-slug] :as props}]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [editable? on-save]
        :or   {editable? true}}]
    (let [loading? @(re-frame/subscribe [::state/data-loading?])
          saving? @(re-frame/subscribe [::state/data-saving?])
          data @(re-frame/subscribe [::state/form-data])
          can-publish? @(re-frame/subscribe [::state/can-publish?])
          model {:name {:label "Course Name"
                        :type  :text}
                 :lang {:label   "Language"
                        :type    :select
                        :options language-options}
                 :locked {:label "Lock Course"
                          :type (if editable? :switch :empty)}
                 :publish {:label "Publish"
                           :type (if (and can-publish? editable?) :custom :empty)
                           :control publish-control}}
          handle-save #(re-frame/dispatch [::state/save % {:on-success on-save}])]
      [ui/form {:form-id    (-> (str "course-info-" course-slug)
                                (keyword))
                :data       data
                :model      model
                :spec       ::course-spec/course
                :on-save    handle-save
                :disabled?  (not editable?)
                :loading?   loading?
                :saving?    saving?
                :class-name class-name}])))
