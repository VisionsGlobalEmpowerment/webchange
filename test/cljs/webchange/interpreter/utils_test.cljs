(ns webchange.interpreter.utils-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.interpreter.utils :as utils]))

(deftest merge-scene-actions
  (let [main-scene {:actions {:action-1 {:type        "action"
                                         :description "scene action"
                                         :target      "target 1"}}}
        scene-1 {:actions {:action-2 {:type        "action"
                                      :description "common action"
                                      :target      "target 2"}}}]
    (is (= (utils/merge-scene-data main-scene [scene-1]) {:actions {:action-1 {:type        "action"
                                                                               :description "scene action"
                                                                               :target      "target 1"}
                                                                    :action-2 {:type        "action"
                                                                               :description "common action"
                                                                               :target      "target 2"}}})))

  (let [main-scene {:actions {:action-0 {:type        "action"
                                         :description "scene action"
                                         :target      "target 1"}}}
        scene-1 {:actions {:action-1 {:type        "action"
                                      :description "common action 1"
                                      :target      "target 1"}}}
        scene-2 {:actions {:action-2 {:type        "action"
                                      :description "common action 2"
                                      :target      "target 2"}}}]
    (is (= (utils/merge-scene-data main-scene [scene-1 scene-2]) {:actions {:action-0 {:type        "action"
                                                                                       :description "scene action"
                                                                                       :target      "target 1"}
                                                                            :action-1 {:type        "action"
                                                                                       :description "common action 1"
                                                                                       :target      "target 1"}
                                                                            :action-2 {:type        "action"
                                                                                       :description "common action 2"
                                                                                       :target      "target 2"}}})))

  (let [main-scene {:actions {:action-1 {:type        "action"
                                         :description "scene action"
                                         :target      "target 1"}}}
        scene-1 {:actions {:action-1 {:type        "action"
                                      :description "common action"
                                      :target      "target 2"}}}]
    (is (= (utils/merge-scene-data main-scene [scene-1]) {:actions {:action-1 {:type        "action"
                                                                               :description "common action"
                                                                               :target      "target 2"}}}))))

(deftest merge-scene-objects
  (let [main-scene {:objects {:object-1 {:type "image"
                                         :src  "/raw/img/1.png"}}}
        scene-1 {:objects {:object-2 {:type "image"
                                      :src  "/raw/img/2.png"}}}]
    (is (= (utils/merge-scene-data main-scene [scene-1]) {:objects {:object-1 {:type "image"
                                                                               :src  "/raw/img/1.png"}
                                                                    :object-2 {:type "image"
                                                                               :src  "/raw/img/2.png"}}})))
  (let [main-scene {:objects {:object-0 {:type "image"
                                         :src  "/raw/img/0.png"}}}
        scene-1 {:objects {:object-1 {:type "image"
                                      :src  "/raw/img/1.png"}}}
        scene-2 {:objects {:object-2 {:type "image"
                                      :src  "/raw/img/2.png"}}}]
    (is (= (utils/merge-scene-data main-scene [scene-1 scene-2]) {:objects {:object-0 {:type "image"
                                                                                       :src  "/raw/img/0.png"}
                                                                            :object-1 {:type "image"
                                                                                       :src  "/raw/img/1.png"}
                                                                            :object-2 {:type "image"
                                                                                       :src  "/raw/img/2.png"}}})))

  (let [main-scene {:objects {:object-1 {:type "image"
                                         :src  "/raw/img/1.png"}}}
        scene-1 {:objects {:object-1 {:type "image"
                                      :src  "/raw/img/2.png"}}}]
    (is (= (utils/merge-scene-data main-scene [scene-1]) {:objects {:object-1 {:type "image"
                                                                               :src  "/raw/img/2.png"}}}))))

(deftest merge-scene-assets
  (let [main-scene {:assets [{:url "/raw/audio/1.m4a", :size 1, :type "audio" :alias "voice 1"}]}
        scene-1 {:assets [{:url "/raw/audio/2.m4a", :size 2, :type "audio" :alias "voice 2"}]}]
    (is (= (utils/merge-scene-data main-scene [scene-1]) {:assets [{:url "/raw/audio/1.m4a", :size 1, :type "audio" :alias "voice 1"}
                                                                   {:url "/raw/audio/2.m4a", :size 2, :type "audio" :alias "voice 2"}]})))
  (let [main-scene {:assets [{:url "/raw/audio/0.m4a", :size 1, :type "audio" :alias "voice 0"}]}
        scene-1 {:assets [{:url "/raw/audio/1.m4a", :size 2, :type "audio" :alias "voice 1"}]}
        scene-2 {:assets [{:url "/raw/audio/2.m4a", :size 2, :type "audio" :alias "voice 2"}]}]
    (is (= (utils/merge-scene-data main-scene [scene-1 scene-2]) {:assets [{:url "/raw/audio/0.m4a", :size 1, :type "audio" :alias "voice 0"}
                                                                           {:url "/raw/audio/1.m4a", :size 2, :type "audio" :alias "voice 1"}
                                                                           {:url "/raw/audio/2.m4a", :size 2, :type "audio" :alias "voice 2"}]})))

  (let [main-scene {:assets [{:url "/raw/audio/1.m4a", :size 1, :type "audio" :alias "voice 1"}]}
        scene-1 {:assets [{:url "/raw/audio/1.m4a", :size 1, :type "audio" :alias "voice 1"}]}]
    (is (= (utils/merge-scene-data main-scene [scene-1]) {:assets [{:url "/raw/audio/1.m4a", :size 1, :type "audio" :alias "voice 1"}]}))))

(deftest merge-scene-scene-objects
  (let [main-scene {:scene-objects [["background"] ["object-1" "object-2"]]}
        scene-1 {:scene-objects [["object-3"]]}
        scene-2 {:scene-objects [["object-4"]]}]
    (is (= (utils/merge-scene-data main-scene [scene-1 scene-2]) {:scene-objects [["background"] ["object-1" "object-2"] ["object-3"] ["object-4"]]}))))

(deftest add-scene-tag
  (let [scene {:assets        [{:url "/raw/img/img1.png" :size 1 :type "image"}
                               {:url "/raw/audio/audio1.m4a" :size 2 :type "audio" :alias "vaca voice"}]
               :objects       {:object-1 {:type "image"}
                               :object-2 {:type "text"}}
               :scene-objects [["object-1" "object-2"]]
               :actions       {:action-1 {:type "action"}
                               :action-2 {:type "action"}}}

        tag "tagged"]
    (is (= (utils/add-scene-tag scene tag) {:assets        [{:url "/raw/img/img1.png" :size 1 :type "image" :tagged? true}
                                                            {:url "/raw/audio/audio1.m4a" :size 2 :type "audio" :alias "vaca voice" :tagged? true}]
                                            :objects       {:object-1 {:type "image" :tagged? true}
                                                            :object-2 {:type "text" :tagged? true}}
                                            :scene-objects [["object-1" "object-2"]]
                                            :actions       {:action-1 {:type "action" :tagged? true}
                                                            :action-2 {:type "action" :tagged? true}}}))))
