(ns webchange.admin.pages.add-class.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.add-class.state :as state]
    [webchange.admin.widgets.class-form.views :refer [class-add-form]]
    [webchange.admin.widgets.page.views :as page]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [school-id]}]
    (let [handle-save #(re-frame/dispatch [::state/create-class-success])]
      [page/page {:class-name "page--class-add"}
       [page/_header {:title "Add New Class"
                     :icon  "classes"}]
       [page/main-content {:class-name "page--class-add--content"}
        [class-add-form {:school-id school-id
                         :on-save   handle-save}]]])))
