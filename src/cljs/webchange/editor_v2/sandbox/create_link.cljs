(ns webchange.editor-v2.sandbox.create-link)

(defn create-link
  [{:keys [course-slug scene-slug lessons]}]
  (if (seq lessons)
    (let [encoded-lessons (-> lessons clj->js js/JSON.stringify js/btoa js/encodeURIComponent)]
      (str js/location.protocol "//" js/location.host "/s/" course-slug "/" scene-slug "/" encoded-lessons))
    (str js/location.protocol "//" js/location.host "/s/" course-slug "/" scene-slug)))
