(ns webchange.ui-framework.test-page.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.index :refer [button icon icon-button]]
    [webchange.ui-framework.test-page.buttons :refer [buttons]]
    [webchange.ui-framework.test-page.views-icons :refer [icons-group]]
    [webchange.ui-framework.test-page.views-icon-buttons :refer [icon-buttons-group]]))

(defn- row
  [{:keys [title]}]
  (let [this (r/current-component)]
    [:div.test-page-row
     [:span.test-page-row-title
      title ":"]
     (into [:div.test-page-row-wrapper]
           (r/children this))]))

(defn- block
  [{:keys [title]}]
  (let [this (r/current-component)]
    [:div.test-page-element
     (when (some? title)
       [:span.test-page-element-title title])
     (into [:div.test-page-element-wrapper]
           (r/children this))]))

(defn- generate-controls-table
  [{:keys [control children common-props props]
    :or   {children     []
           common-props {}}}]
  (let [[first-prop second-prop] props]
    (map (fn [first-prop-value]
           (map (fn [second-prop-value]
                  [block
                   (into [control (merge common-props
                                         (-> {}
                                             (assoc (:name first-prop) first-prop-value)
                                             (assoc (:name second-prop) second-prop-value)))]
                         children)])
                (:data second-prop)))
         (:data first-prop))))

(defn test-ui
  []
  [:div.test-ui-page
   [:div.grid-sibling]
   [:div.grid-container
    [:div]
    [:div]
    [:div]
    [:div.list
     [:div.header]
     [:div.content
      [:div.list-item]
      [:div.list-item]
      [:div.list-item]
      [:div.list-item]
      [:div.list-item]
      [:div.list-item]
      [:div.list-item]
      [:div.list-item]
      [:div.list-item]
      [:div.list-item]]
     [:div.footer]]]])
