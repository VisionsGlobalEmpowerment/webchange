(ns webchange.templates.library.flipbook
  (:require
    [webchange.templates.core :as core]))

(def metadata {:id          24
               :name        "flipbook"
               :tags        ["Book Creator"]
               :description "Some description of flipbook mechanics and covered skills"
               :lesson-sets []
               :options     {:cover-layout {:label   "Cover layout"
                                            :type    "lookup-image"
                                            :options [{:name  "Title at top"
                                                       :value :title-top
                                                       :src   "/images/templates/cover_layout/title_at_top.png"}
                                                      {:name  "Title at bottom"
                                                       :value :title-bottom
                                                       :src   "/images/templates/cover_layout/title_at_bottom.png"}]}
                             :cover-image  {:label "Cover image"
                                            :type  "image"}
                             :cover-title  {:label "Title"
                                            :type  "string"}}
               :actions     {:add-page {:title   "Add page",
                                        :options {:image {:label "Image"
                                                          :type  "image"}
                                                  :text  {:label "Text"
                                                          :type  "string"}}}}})

(def template {:assets [{:url "/raw/flip_page/front.png" :size 1 :type "image"}
                        {:url "/raw/flip_page/back.png" :size 1 :type "image"}
                        {:url "/raw/flip_page/spread-1-left.png" :size 1 :type "image"}
                        {:url "/raw/flip_page/spread-1-right.png" :size 1 :type "image"}
                        {:url "/raw/flip_page/spread-2-left.png" :size 1 :type "image"}
                        {:url "/raw/flip_page/spread-2-right.png" :size 1 :type "image"}]
        :objects       {:page-1       {:type       "group"
                                       :x          960
                                       :y          250
                                       :transition "page-1"
                                       :children   ["page-1-image" "page-1-text"]}
                        :page-1-image {:type    "image"
                                       :x       0
                                       :y       0
                                       :width   960
                                       :height  1080
                                       :scale-x 0.5
                                       :scale-y 0.5
                                       :src     "/raw/flip_page/front.png"}
                        :page-1-text  {:type           "text"
                                       :x              300
                                       :y              50
                                       :font-size      90
                                       :scale-x        0.5
                                       :scale-y        0.5
                                       :vertical-align "top"
                                       :fill           "white"
                                       :text           "Page 1"}
                        :page-2       {:type       "group"
                                       :x          480
                                       :y          250
                                       :visible    false
                                       :transition "page-2"
                                       :children   ["page-2-image" "page-2-text"]}
                        :page-2-image {:type    "image"
                                       :x       0
                                       :y       0
                                       :scale-x 0.5
                                       :scale-y 0.5
                                       :width   1000
                                       :height  650
                                       :src     "/raw/flip_page/spread-1-left.png"}
                        :page-2-text  {:type           "text"
                                       :x              50
                                       :y              50
                                       :font-size      90
                                       :scale-x        0.5
                                       :scale-y        0.5
                                       :vertical-align "top"
                                       :fill           "white"
                                       :text           "Page 2"}
                        :page-3       {:type       "group"
                                       :x          960
                                       :y          250
                                       :visible    false
                                       :transition "page-3"
                                       :children   ["page-3-image" "page-3-text"]}
                        :page-3-image {:type    "image"
                                       :x       0
                                       :y       0
                                       :width   960
                                       :height  1080
                                       :scale-x 0.5
                                       :scale-y 0.5
                                       :src     "/raw/flip_page/spread-1-right.png"}
                        :page-3-text  {:type           "text"
                                       :x              300
                                       :y              50
                                       :font-size      90
                                       :scale-x        0.5
                                       :scale-y        0.5
                                       :vertical-align "top"
                                       :fill           "white"
                                       :text           "Page 3"}
                        :page-4       {:type       "group"
                                       :x          480
                                       :y          250
                                       :visible    false
                                       :transition "page-4"
                                       :children   ["page-4-image" "page-4-text"]}
                        :page-4-image {:type    "image"
                                       :x       0
                                       :y       0
                                       :scale-x 0.5
                                       :scale-y 0.5
                                       :width   1000
                                       :height  650
                                       :src     "/raw/flip_page/spread-2-left.png"}
                        :page-4-text  {:type           "text"
                                       :x              50
                                       :y              50
                                       :font-size      90
                                       :scale-x        0.5
                                       :scale-y        0.5
                                       :vertical-align "top"
                                       :fill           "white"
                                       :text           "Page 4"}
                        :page-5       {:type       "group"
                                       :x          960
                                       :y          250
                                       :visible    false
                                       :transition "page-5"
                                       :children   ["page-5-image" "page-5-text"]}
                        :page-5-image {:type    "image"
                                       :x       0
                                       :y       0
                                       :width   960
                                       :height  1080
                                       :scale-x 0.5
                                       :scale-y 0.5
                                       :src     "/raw/flip_page/spread-2-right.png"}
                        :page-5-text  {:type           "text"
                                       :x              300
                                       :y              50
                                       :font-size      90
                                       :scale-x        0.5
                                       :scale-y        0.5
                                       :vertical-align "top"
                                       :fill           "white"
                                       :text           "Page 5"}
                        :page-6       {:type       "group"
                                       :x          480
                                       :y          250
                                       :visible    false
                                       :transition "page-6"
                                       :children   ["page-6-image" "page-6-text"]}
                        :page-6-image {:type    "image"
                                       :x       0
                                       :y       0
                                       :scale-x 0.5
                                       :scale-y 0.5
                                       :width   1000
                                       :height  650
                                       :src     "/raw/flip_page/back.png"}
                        :page-6-text  {:type           "text"
                                       :x              50
                                       :y              50
                                       :font-size      90
                                       :scale-x        0.5
                                       :scale-y        0.5
                                       :vertical-align "top"
                                       :fill           "white"
                                       :text           "Page 6"}
                        }
        :scene-objects [["page-5" "page-3" "page-1" "page-2" "page-4" "page-6"]]
        :actions       {:start-scene {:type "sequence-data"
                                      :data [{:type "empty" :duration 1000}
                                             {:type     "flip"
                                              :target-1 "page-1"
                                              :target-2 "page-2"
                                              :target-3 "page-3"}
                                             {:type "empty" :duration 1000}
                                             {:type     "flip"
                                              :target-1 "page-3"
                                              :target-2 "page-4"
                                              :target-3 "page-5"}
                                             {:type "empty" :duration 1000}
                                             {:type     "flip"
                                              :target-1 "page-5"
                                              :target-2 "page-6"}]}}
        :triggers      {:start {:on     "start"
                                :action "start-scene"}}
        :metadata      {:autostart true}
        :audio         {}})

(defn create-activity
  [args]
  ;; args: {:name w, :template-id 24, :skills [4], :cover-title yyy, :cover-layout title-top, :cover-image {:src /upload/OGBDVDTILOHWTAOU.png}}
  (-> template
      (assoc-in [:metadata :actions] (:actions metadata))))

(defn update-activity
  [old-data args]
  ;; args: {:image {:src /upload/RQOZLXZQNIVMWKAQ.png}, :text hgj}
  old-data)

(core/register-template
  (:id metadata) metadata create-activity update-activity)