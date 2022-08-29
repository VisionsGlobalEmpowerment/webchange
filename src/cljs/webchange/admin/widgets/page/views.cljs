(ns webchange.admin.widgets.page.views
  (:require
    [reagent.core :as r]
    [webchange.admin.widgets.page.block.views :as block-views]
    [webchange.admin.widgets.page.content.views :as content-views]
    [webchange.admin.widgets.page.footer.views :as footer-views]
    [webchange.admin.widgets.page.header.views :as header-views]
    [webchange.admin.widgets.page.side-bar.views :as side-bar-views]
    [webchange.admin.widgets.page.utils :refer [has-child?]]
    [webchange.ui.index :refer [get-class-name]]))

(def block block-views/block)
(def content content-views/content)
(def side-bar side-bar-views/side-bar)
(def header header-views/header)
(def footer footer-views/footer)

(defn form-wrapper
  [{:keys [actions]}]
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name "widget--form-wrapper"}
              (when (some? actions)
                [:div {:class-name "widget--form-wrapper--actions"}
                 actions])])))

(defn single-page
  [{:keys [align-center? background-image? class-name class-name-content footer form-container? header search]}]
  [:div {:class-name (get-class-name {"widget--single-page"                        true
                                      "widget--single-page--with-background-image" background-image?
                                      "widget--single-page--with-footer"           (some? footer)
                                      "widget--single-page--form-container"        form-container?
                                      "widget--single-page--align-center"          align-center?
                                      class-name                                   (some? class-name)})}
   (when (some? search)
     [:div {:class-name "widget--single-page--search-container"}
      search])
   (when (some? header)
     [header-views/header header])
   (->> (r/current-component)
        (r/children)
        (into [:div {:class-name (get-class-name {"widget--single-page--content" true
                                                  class-name-content             (some? class-name-content)})}
               (when (some? footer)
                 [footer-views/footer footer])]))])

(def child-components {header-views/component-class-name   "bbs--page--with-header"
                       content-views/component-class-name  "bbs--page--with-content"
                       side-bar-views/component-class-name "bbs--page--with-side-bar"
                       footer-views/component-class-name   "bbs--page--with-footer"})

(defn page
  [{:keys [align-content class-name]}]
  (r/with-let [children-class-names (r/atom (->> (vals child-components)
                                                 (map #(vector % false))
                                                 (into {})))
               handle-ref (fn [el]
                            (when (some? el)
                              (->> child-components
                                   (reduce (fn [result [child-class-name parent-class-name]]
                                             (assoc result parent-class-name (has-child? el child-class-name)))
                                           {})
                                   (reset! children-class-names))))]
    (->> (r/current-component)
         (r/children)
         (into [:div {:class-name (get-class-name (merge {"bbs--page"                                      true
                                                          (str "bbs--page--align-content--" align-content) (some? align-content)
                                                          class-name                                       (some? class-name)}
                                                         @children-class-names))
                      :ref        handle-ref}]))))
