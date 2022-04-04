(ns webchange.editor-v2.text-animation-editor.chunks-editor.form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.text-animation-editor.chunks-editor.state :as parent-state]
    [webchange.utils.text :refer [text->chunks chunks->parts text-equals-parts?]]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:chunks-editor])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys []} [_ {:keys [chunks text on-change]}]]
    {:dispatch-n [[::set-text text]
                  [::set-chunks chunks]
                  [::set-callback on-change]]}))

;; Chunks

(def chunks-path (path-to-db [:chunks]))

(defn- get-chunks
  [db]
  (get-in db chunks-path []))

(re-frame/reg-sub
  ::chunks
  get-chunks)

(re-frame/reg-event-fx
  ::set-chunks
  (fn [{:keys [db]} [_ value]]
    {:db       (assoc-in db chunks-path value)
     :dispatch [::handle-change]}))

;; Text

(def text-path (path-to-db [:text]))

(defn- get-text
  [db]
  (get-in db text-path))

(re-frame/reg-sub ::text get-text)

(re-frame/reg-sub
  ::origin-text
  (fn []
    (re-frame/subscribe [::text]))
  (fn [text-data]
    (get text-data :origin "")))

(re-frame/reg-sub
  ::trimmed-text
  (fn []
    (re-frame/subscribe [::text]))
  (fn [text-data]
    (get text-data :trimmed "")))

(re-frame/reg-event-fx
  ::set-text
  (fn [{:keys [db]} [_ text]]
    (let [trimmed-text (clojure.string/triml text)
          left-shift (- (count text)
                        (count trimmed-text))]
      {:db       (assoc-in db text-path {:origin     text
                                         :trimmed    trimmed-text
                                         :left-shift left-shift})
       :dispatch [::handle-change]})))

;; Parts

(re-frame/reg-sub
  ::parts
  (fn []
    [(re-frame/subscribe [::text])
     (re-frame/subscribe [::chunks])])
  (fn [[{:keys [origin trimmed]} chunks]]
    (let [chunks-defined? (and (sequential? chunks)
                               (-> chunks count (> 0)))]
      (if chunks-defined?
        (chunks->parts origin chunks)
        [trimmed]))))

(re-frame/reg-event-fx
  ::handle-parts-changed
  (fn [{:keys [db]} [_ parts]]
    (let [{:keys [trimmed left-shift]} (get-text db)
          chunks (->> (text->chunks trimmed parts)
                      (map #(-> %
                                (update :start + left-shift)
                                (update :end + left-shift))))]
      {:dispatch [::set-chunks chunks]})))

;; Callback

(def callback-path (path-to-db [:callback]))

(defn- get-callback
  [db]
  (get-in db callback-path))

(re-frame/reg-event-fx
  ::set-callback
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db callback-path value)}))

(re-frame/reg-event-fx
  ::handle-change
  (fn [{:keys [db]} [_]]
    (let [{:keys [origin]} (get-text db)
          chunks (get-chunks db)
          callback (get-callback db)]
      {:callback {:fn   callback
                  :args {:text   origin
                         :chunks chunks}}})))

(re-frame/reg-fx
  :callback
  (fn [{:keys [fn args]}]
    (when (fn? fn)
      (fn args))))
