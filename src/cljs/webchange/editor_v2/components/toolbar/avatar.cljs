(ns webchange.editor-v2.components.toolbar.avatar
  (:require
    [re-frame.core :as re-frame]
    [webchange.auth.subs :as as]
    [cljs-react-material-ui.reagent :as ui]
    [clojure.string :as string]
    [webchange.routes :refer [location]]))

(defn avatar-panel
  []
  (let [user @(re-frame/subscribe [::as/user])
        initials (str (-> user :first-name first) (-> user :last-name first))]
    [ui/avatar {:style {:cursor "pointer"}
                :on-click #(location :profile)} (string/upper-case initials)]))
