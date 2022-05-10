(ns webchange.admin.components.form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.components.form.state :as state]
    [webchange.ui-framework.components.index :as c]))

;(defn- text-control
;  [{:keys [disabled?]}]
;  (let [id "about"
;        ;value @(re-frame/subscribe [::state/about])
;        ;error @(re-frame/subscribe [::state/about-error])
;        ;handle-change #(re-frame/dispatch [::state/set-about %])
;        ]
;    [:<>
;     [c/label {:for id} "About"]
;     [c/text-area {:id        id
;                   ;:value     value
;                   ;:error     error
;                   ;:disabled? disabled?
;                   ;:on-change handle-change
;                   }]]))

(defn- text-control
  [{:keys [id label]}]
  (let [
        ;value @(re-frame/subscribe [::state/school-name])
        ;error @(re-frame/subscribe [::state/school-name-error])
        ;handle-change #(re-frame/dispatch [::state/set-school-name %])
        ]
    [:<>
     [c/label {:for id} label]
     [c/input {:id id
               ;:value     value
               ;:error     error
               ;:disabled? disabled?
               ;:on-change handle-change
               }]]))

(defn- form-control
  [{:keys [id options]}]
  (let [{:keys [type]} options
        control-props (merge {:id id} options)]
    (case type
      :text [text-control control-props])))

(defn form
  [{:keys [form-id]}]
  (re-frame/dispatch [::state/init {:form-id form-id}])
  (fn [
       ;{:keys [editable?]
       ; :or   {editable? true}}
       {:keys [model]}
       ]
    (let [loading? false                                    ;@(re-frame/subscribe [::state/data-loading?])
          ]
      [:div.widget--school-form
       (if-not loading?
         [:div.controls
          (for [[field-name field-options] model]
            [form-control {:id      field-name
                           :options field-options}])
          ;[text-control {
          ;               ;:disabled? (not editable?)
          ;               }]
          ;[location-control {:disabled? (not editable?)}]
          ;[about-control {:disabled? (not editable?)}]
          ]
         ;[data-loading-indicator]
         )
       ;(when editable?
       ;  [submit {:disabled? loading?}])
       ])))