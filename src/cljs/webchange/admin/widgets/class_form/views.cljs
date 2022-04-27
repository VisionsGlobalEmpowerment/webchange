(ns webchange.admin.widgets.class-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.widgets.class-form.state :as state]
    [webchange.admin.widgets.utils :refer [get-uid]]
    [webchange.ui-framework.components.index :refer [button input]]))

(defn- class-name
  [{:keys [id]}]
  (let [name @(re-frame/subscribe [::state/class-name id])
        validation-error @(re-frame/subscribe [::state/class-name-validation-error id])
        handle-change #(re-frame/dispatch [::state/set-class-name id %])]
    [input {:label      "Class Name"
            :class-name "class-name"
            :value      name
            :error      validation-error
            :on-change  handle-change}]))

(defn class-form
  [props]
  (r/with-let [id (get-uid)]
    (re-frame/dispatch [::state/init id props])
    [:div.widget--class-form
     [class-name {:id id}]]))
