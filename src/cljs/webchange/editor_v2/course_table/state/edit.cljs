(ns webchange.editor-v2.course-table.state.edit
  (:require
    [webchange.editor-v2.course-table.state.db :as db]
))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:edit])
       (db/path-to-db)))



;(or (= field :skills)
;    (= field :abbr-global)) (assoc :dispatch [::skills/init-skills data])
;(= field :tags) (assoc :dispatch [::tags/init-tags data])
;(= field :activity) (assoc :dispatch [::activity/init-activities data])
;(= field :concepts) (assoc :dispatch [::concepts/init-concepts data])