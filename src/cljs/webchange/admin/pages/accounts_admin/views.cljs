(ns webchange.admin.pages.accounts-admin.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.accounts-admin.state :as state]
    [webchange.admin.widgets.accounts-list.views :refer [accounts-list]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]))

(defn- header
  []
  (let [handle-add-click #(re-frame/dispatch [::state/add-admin])]
    [page/header {:title   "Admins"
                  :icon    "user"
                  :actions [ui/icon-button {:icon     "add"
                                            :title    "Add Admin Account"
                                            :on-click handle-add-click}
                            "Add Admin Account"]}]))

(defn- content
  []
  (let []
    [page/main-content
     [accounts-list {:account-type "admin"}]]))

(defn page
  []
  (fn []
    [page/page {:class-name "page--accounts-admins"}
     [header]
     [content]]))
