(ns webchange.test.course.common-actions
  (:require
    [clojure.test :refer :all]
    [webchange.templates.common-actions :as ca]
    [webchange.templates.core :as core]))

(defn- has-asset
  [data file]
  (not (empty? (filter (fn [asset] (= (:url asset) file)) (:assets data)))))

(defn- has-action
  [data file]
  (= file (get-in data [:actions (keyword (get-in data [:triggers :music :action])) :id])))

(deftest can-create-background-music
  []
  (let
    [scene {}
     file "/test/test.mp3"
     data {:common-action? true :action :background-music :data {:background-music {:src file}}}
     result (core/update-activity-from-template scene data)]
    (is (get-in result [:triggers :music]))
    (is (has-action result file))
    (is (has-asset result file))))

(deftest can-update-background-music
  []
  (let
    [old-file "/test/test-1.mp3"
     scene {:actions {:start-background-music-1 {:type "audio",
                                                 :id   old-file,
                                                 :loop true}},
            :triggers
                     {:music {:on "start", :action "start-background-music-1"}},
            :assets  [{:url old-file, :size 10, :type "audio"}]}
     file "/test/test.mp3"
     data {:common-action? true :action :background-music :data {:background-music {:src file}}}
     result (core/update-activity-from-template scene data)]
    (is (get-in result [:triggers :music]))
    (is (has-action result file))
    (is (has-asset result file))
    (is (not (has-asset result old-file)))
    (is (not (has-action result old-file)))))

(deftest can-not-delete-asset-background-music
  []
  (let
    [old-file "/test/test-1.mp3"
     scene {:actions
                    {:start-background-music-1 {:type "audio",
                                                :id   old-file,
                                                :loop true}
                     :act-1                    {:type "audio",
                                                :id   old-file}},
            :triggers
                    {:music {:on "start", :action "start-background-music-1"}},
            :assets [{:url old-file, :size 10, :type "audio"}]}
     file "/test/test.mp3"
     data {:common-action? true :action :background-music :data {:background-music {:src file}}}
     result (core/update-activity-from-template scene data)]
    (is (get-in result [:triggers :music]))
    (is (has-action result file))
    (is (has-asset result file))
    (is (has-asset result old-file))
    (is (not (has-action result old-file)))))
