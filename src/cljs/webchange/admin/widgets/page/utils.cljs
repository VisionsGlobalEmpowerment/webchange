(ns webchange.admin.widgets.page.utils)

(defn- has-child?
  [el child-class-name]
  (-> el
      (.querySelector (str "." child-class-name))
      (some?)))
