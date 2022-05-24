(ns webchange.admin.components.pagination.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.pagination.state :as state]
    [webchange.ui-framework.components.index :as ui]))

(defn pagination
  []
  (r/create-class
    {:display-name "Pagination"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :reagent-render
     (fn [{:keys [on-change]}]
       (let [buttons @(re-frame/subscribe [::state/buttons])]
         [:div.component--pagination
          (for [{:keys [id name value]} buttons]
            ^{:key id}
            [ui/button {:class-name "pagination--button"
                        :on-click   #(on-change value)}
             name])]))}))

#_{:total     100
   :per-page  30
   :on-change #(print "Page" %)}