(ns webchange.admin.widgets.navigation.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.routes :as routes]
    [webchange.admin.widgets.navigation.state :as state]
    [webchange.ui.index :as ui]
    [webchange.ui-framework.components.index :as c]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.utils.map :refer [map->list]]))

(declare children-list)

(defn- route->redirect-params
  [{:keys [page page-params]}]
  (cond-> []
          (some? page) (conj page)
          (some? page-params) (concat (map->list page-params))))

(defn- children-item
  [{:keys [active? children route text]}]
  (let [route-params (route->redirect-params route)]
    [ui/navigation-link {:route  route-params
                         :router @routes/router
                         :class-name (get-class-name {"children-item" true
                                                      "active-item"   active?})}
     [:div.text text]
     (when-not (empty? children)
       [:<>
        [c/icon {:icon       "chevron-right"
                 :class-name "icon"}]
        [children-list {:items children}]])]))

(defn- children-list
  [{:keys [items position]
    :or   {position "right"}}]
  [:div {:class-name (get-class-name {"children-list"            true
                                      (str "position-" position) true})}
   (for [{:keys [id] :as item} items]
     ^{:key id}
     [children-item item])])

(defn- root-item
  [{:keys [active? children icon route text]}]
  (let [route-params (route->redirect-params route)]
    [:div {:class-name (get-class-name {"root-item"       true
                                        "navigation-item" true
                                        "active-item"     active?})}
     [ui/navigation-link {:route  route-params
                          :router @routes/router}
      [ui/navigation-icon {:icon       icon
                           :class-name "root-icon"}]
      [:div.text text]]
     (when-not (empty? children)
       [:<>
        [ui/icon {:icon       "caret-down"
                  :class-name "expand-icon"}]
        [children-list {:items    children
                        :position "bottom"}]])]))

(defn navigation
  []
  (let [items @(re-frame/subscribe [::state/navigation-items])]
    [:div.top-bar--navigation
     [ui/logo-with-name {:class-name "logo"}]
     (for [{:keys [id] :as item} items]
       ^{:key id}
       [root-item item])]))
