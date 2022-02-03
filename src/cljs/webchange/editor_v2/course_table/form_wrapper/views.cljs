(ns webchange.editor-v2.course-table.form-wrapper.views
  (:require
    [reagent.core :as r]))

(defn- get-container-height
  [container-el]
  (let [table (.closest container-el ".course-table")
        table-header (.querySelector table ".course-table-header")
        table-parent (.-parentElement table)
        table-parent-height (.-clientHeight table-parent)
        table-header-height (.-clientHeight table-header)]
    (- table-parent-height table-header-height)))

(defn- set-max-height
  [container-el max-height]
  (aset (.-style container-el) "max-height" (str max-height "px")))

(defn edit-form-wrapper
  []
  (let [ref (atom nil)
        handle-scroll (fn [event]
                        (.stopPropagation event))]
    (r/create-class
      {:display-name        "edit-form-wrapper"
       :component-did-mount (fn []
                              (->> (get-container-height @ref)
                                   (set-max-height @ref)))
       :reagent-render      (fn []
                              (into [:div {:class-name "edit-form-wrapper"
                                           :on-wheel   handle-scroll
                                           :ref        #(when (some? %) (reset! ref %))}]
                                    (-> (r/current-component)
                                        (r/children))))})))
