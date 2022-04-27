(ns webchange.admin.widgets.page.views
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(def class-names
  {:header   "widget-profile--header"
   :main     "widget-profile--main-content"
   :side-bar "widget-profile--side-bar"})

(defn- has-child?
  [el child-class-name]
  (-> el
      (.querySelector (str "." child-class-name))
      (some?)))

(defn- with-children-classes
  [children]
  (->> children
       (map (fn [[name present]]
              [(->> name (clojure.core/name) (str "with-"))
               present]))
       (into {})))

(defn page
  []
  (let [children (r/atom nil)
        handle-ref (fn [ref]
                     (when (some? ref)
                       (reset! children (->> class-names
                                             (map (fn [[name class-name]]
                                                    [name (has-child? ref class-name)]))
                                             (into {})))))]
    (r/create-class
      {:display-name "Page Layout"
       :reagent-render
       (fn []
         (->> (r/current-component)
              (r/children)
              (into [:div {:class-name (get-class-name (merge {"widget-profile" true}
                                                              (with-children-classes @children)))
                           :ref        handle-ref}])))})))

(defn header
  []
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (:header class-names)}])))

(defn main-content
  [{:keys [footer title]}]
  [:div {:class-name (:main class-names)}
   (when (some? title)
     [:h1 title])
   (->> (r/current-component)
        (r/children)
        (into [:div.widget-profile--main-content--content]))
   [:div.widget-profile--main-content--footer
    footer]])

(defn side-bar
  []
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (:side-bar class-names)}])))

(defn block
  [{:keys [title]}]
  (->> (r/current-component)
       (r/children)
       (into [:div.widget-profile--content-block
              [:h2 title]])))
