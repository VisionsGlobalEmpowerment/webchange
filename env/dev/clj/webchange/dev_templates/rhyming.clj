(ns webchange.dev-templates.rhyming
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.templates.utils.common :as common]
            [webchange.course.core :as core]))

(comment
  (def test-course-slug (-> (t/create-test-course) :slug))
  (def test-course-slug "test-course-english-hxegcfhy")
  (def scene-slug "test-activity")

  (let [data {:template-id 27
              :name        "Rhyming Activity"
              :left        "Left G"
              :right       "Right G"}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug))

  (do (core/update-activity-template! test-course-slug scene-slug t/user-id)
      (t/update-activity test-course-slug scene-slug))

  (-> (core/get-scene-latest-version test-course-slug scene-slug)
      (get-in [:objects :right-gate :scale]))

  (let [course-slug "english"
        scene-slug "rhyming-at-and-ig"
        ball-dialog-name "ball-dialog-0"]
    (-> (core/get-scene-latest-version course-slug scene-slug)
        (common/remove-action-from-tracks ball-dialog-name)))
  
  (let [course-slug "english"
        scene-slug "rhyming-at-and-ig"]
    (-> (core/get-scene-latest-version course-slug scene-slug)
        ))

  (let [user-id 1
        course-slug "english"
        scene-slug "rhyming-at-and-ig"
        template-options {:action "template-options"
                          :data    {:left "mat",
                                    :balls
                                    [{:img {:src "/upload/DLWPFCJQZWAQVBES.png"},
                                      :side "left",
                                      :text "cat",
                                      :object "ball-group-0",
                                      :deleted true,
                                      :action-name "add-ball",
                                      :object-img-name "ball-img-0",
                                      :object-text-name "ball-text-0"}
                                     {:img {:src "/upload/GOSDMBWZTIPHFCWT.png"},
                                      :side "left",
                                      :text "hat",
                                      :object "ball-group-1",
                                      :action-name "add-ball",
                                      :object-img-name "ball-img-1",
                                      :object-text-name "ball-text-1"}
                                     {:img {:src "/upload/VOYSYLLFVMQIPFEF.png"},
                                      :side "left",
                                      :text "bat",
                                      :object "ball-group-2",
                                      :action-name "add-ball",
                                      :object-img-name "ball-img-2",
                                      :object-text-name "ball-text-2"}
                                     {:img {:src "/upload/DBDCQDYEBOLLDRGF.png"},
                                      :side "left",
                                      :text "cat",
                                      :object "ball-group-3",
                                      :action-name "add-ball",
                                      :object-img-name "ball-img-3",
                                      :object-text-name "ball-text-3"}
                                     {:img {:src "/upload/SGUHVFXBSKBAGNUQ.png"},
                                      :side "left",
                                      :text "dig",
                                      :object "ball-group-4",
                                      :deleted true,
                                      :action-name "add-ball",
                                      :object-img-name "ball-img-4",
                                      :object-text-name "ball-text-4"}
                                     {:img {:src "/upload/VXJZBZNWEWQNVYXS.png"},
                                      :side "right",
                                      :text "wig",
                                      :object "ball-group-5",
                                      :action-name "add-ball",
                                      :object-img-name "ball-img-5",
                                      :object-text-name "ball-text-5"}
                                     {:img {:src "/upload/XPDOMZMMUWEBPUOO.png"},
                                      :side "right",
                                      :text "dig",
                                      :object "ball-group-6",
                                      :action-name "add-ball",
                                      :object-img-name "ball-img-6",
                                      :object-text-name "ball-text-6"}
                                     {:img {:src "/upload/UPKJUKWOIEMOHKOH.png"},
                                      :side "left",
                                      :text "pig",
                                      :object "ball-group-7",
                                      :deleted true,
                                      :action-name "add-ball",
                                      :object-img-name "ball-img-7",
                                      :object-text-name "ball-text-7"}
                                     {:img {:src "/upload/JBYYYOLYHGXAGFCL.png"},
                                      :side "right",
                                      :text "pig",
                                      :object "ball-group-8",
                                      :action-name "add-ball",
                                      :object-img-name "ball-img-8",
                                      :object-text-name "ball-text-8"}
                                     {:img {:src "/upload/RPKEMWPSNBXVLXCC.png"},
                                      :side "left",
                                      :text "Rat",
                                      :object "ball-group-9",
                                      :deleted true,
                                      :action-name "add-ball",
                                      :object-img-name "ball-img-9",
                                      :object-text-name "ball-text-9"}],
                                    :right "pig"}}]
    (core/update-activity! course-slug scene-slug template-options user-id))
  )
