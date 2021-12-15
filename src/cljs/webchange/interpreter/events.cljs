(ns webchange.interpreter.events
  (:require
    [ajax.core :refer [json-request-format json-response-format]]
    [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
    [re-frame.core :as re-frame]
    [webchange.common.events :as ce]
    [webchange.common.svg-path.path-to-transitions :as path-utils]
    [webchange.interpreter.core :as i]
    [webchange.interpreter.renderer.scene.components.dragging :as dg]
    [webchange.interpreter.lessons.activity :as lessons-activity]
    [webchange.interpreter.renderer.state.overlays :as overlays]
    [webchange.interpreter.sound :as sound]
    [webchange.interpreter.renderer.question.component :as question-component]
    [webchange.progress.tags :as tags]
    [webchange.interpreter.utils :refer [add-scene-tag merge-scene-data]]
    [webchange.interpreter.utils.find-exit :refer [find-exit-position find-path]]
    [webchange.interpreter.variables.events :as vars.events]
    [webchange.interpreter.variables.core :as vars.core]
    [webchange.sw-utils.state.status :as sw-status]
    [webchange.interpreter.events-objects-emitter]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.interpreter.renderer.scene.components.animation.utils.movements :as movements]
    [webchange.interpreter.renderer.scene.components.counter.state]
    [webchange.interpreter.renderer.scene.components.timer.state]
    [webchange.interpreter.renderer.scene.components.flipbook.state]
    [webchange.interpreter.renderer.scene.components.wrapper-interface :as w]
    [webchange.audio-utils.recorder :as audio-recorder]
    [webchange.resources.manager :as resources-manager]
    [webchange.interpreter.renderer.scene.components.text.chunks :as tc]
    [webchange.interpreter.renderer.scene.app :as app]
    [webchange.editor-v2.assets.events :as assets-events]
    [webchange.state.warehouse :as warehouse]
    [webchange.logger.index :as logger]
    [webchange.interpreter.subs :as interpreter-subs]
    [webchange.utils.numbers :as numbers]))

(ce/reg-simple-executor :audio ::execute-audio)
(ce/reg-simple-executor :start-audio-recording ::execute-start-audio-recording)
(ce/reg-simple-executor :stop-audio-recording ::execute-stop-audio-recording)
(ce/reg-simple-executor :play-video ::execute-play-video)
(ce/reg-simple-executor :path-animation ::execute-path-animation)
(ce/reg-simple-executor :state ::execute-state)
(ce/reg-simple-executor :mass-state ::execute-mass-state)
(ce/reg-simple-executor :set-attribute ::execute-set-attribute)
(ce/reg-simple-executor :empty ::execute-empty)
(ce/reg-simple-executor :animation ::execute-animation)
(ce/reg-simple-executor :add-animation ::execute-add-animation)
(ce/reg-simple-executor :start-animation ::execute-start-animation)
(ce/reg-simple-executor :remove-animation ::execute-remove-animation)
(ce/reg-simple-executor :set-skin ::execute-set-skin)
(ce/reg-simple-executor :set-slot ::execute-set-slot)
(ce/reg-simple-executor :animation-props ::execute-set-animation-props)
(ce/reg-simple-executor :animation-sequence ::execute-animation-sequence)
(ce/reg-simple-executor :scene ::execute-scene)
(ce/reg-simple-executor :location ::execute-location)
(ce/reg-simple-executor :transition ::execute-transition)
(ce/reg-simple-executor :stop-transition ::execute-stop-transition)
(ce/reg-simple-executor :move ::execute-move)
(ce/reg-simple-executor :scene-exit ::execute-scene-exit)
(ce/reg-simple-executor :placeholder-audio ::execute-placeholder-audio)
(ce/reg-simple-executor :test-transitions-collide ::execute-test-transitions-collide)
(ce/reg-simple-executor :test-transition-and-pointer-collide ::execute-test-transition-and-pointer-collide)
(ce/reg-simple-executor :test-transitions-and-pointer-collide ::execute-test-transitions-and-pointer-collide)
(ce/reg-simple-executor :start-activity ::execute-start-activity)
(ce/reg-simple-executor :stop-activity ::execute-stop-activity)
(ce/reg-simple-executor :finish-activity ::execute-finish-activity)
(ce/reg-simple-executor :text-animation ::execute-text-animation)
(ce/reg-simple-executor :set-text ::execute-set-text)
(ce/reg-simple-executor :clear-painting-area ::execute-clear-painting-area)
(ce/reg-simple-executor :animate-next-text ::execute-animate-next-text)
(ce/reg-simple-executor :reset-animate-text ::execute-reset-animate-text)
(ce/reg-simple-executor :pick-correct ::execute-pick-correct)
(ce/reg-simple-executor :pick-wrong ::execute-pick-wrong)
(ce/reg-simple-executor :set-current-concept ::execute-set-current-concept)
(ce/reg-simple-executor :set-interval ::execute-set-interval)
(ce/reg-simple-executor :set-timeout ::execute-set-timeout)
(ce/reg-simple-executor :start-timeout-counter ::execute-start-timeout-counter)
(ce/reg-simple-executor :next-timeout-counter ::execute-next-timeout-counter)
(ce/reg-simple-executor :remove-interval ::execute-remove-interval)
(ce/reg-simple-executor :set-traffic-light ::execute-set-traffic-light)
(ce/reg-simple-executor :show-question ::execute-show-question)
(ce/reg-simple-executor :hide-question ::execute-hide-question)
(ce/reg-simple-executor :upload-screenshot ::execute-upload-screenshot)
(ce/reg-simple-executor :char-movement ::execute-char-movement)


(re-frame/reg-event-fx
  ::update-progress-screenshots
  (fn [{:keys [db]} [_ screenshot]]
    (let [activity-name (:current-scene db)
          {level :level lesson :lesson} (lessons-activity/name->activity-action db activity-name)
          screenshot (-> screenshot
                         (assoc :date (.toUTCString (js/Date.))))]
      {:db         (-> db
                       (update-in [:progress-data :student-assets level lesson activity-name] conj screenshot)
                       (update-in [:progress-data :student-assets level lesson activity-name] vec))
       :dispatch-n (list [:progress-data-changed])
       })))



(re-frame/reg-event-fx
  ::execute-upload-screenshot
  (fn [{:keys [db]} [_ {:keys [var-name] :as action}]]
    "Execute `copy-current-user-to-variable` action - allow to load current user to variable to corresponding variable.

    Action params:
    :var-name - variable name to set.

    Example:
    {:type 'copy-current-user-to-variable'
     :var-name 'answer-clickable'}"
    (app/take-screenshot
      #(do
         (re-frame/dispatch [::assets-events/upload-asset % {:type      "image"
                                                             :on-finish (fn [result]
                                                                          (re-frame/dispatch [::update-progress-screenshots result]))}])))
    {:dispatch (ce/success-event action)}))


(re-frame/reg-event-fx
  ::execute-show-question
  (fn [{:keys [db]} [_ {:keys [data continue-flow] :as action}]]
    "Execute `show-question` action - allows to popup with question functionality

    Action params:
    :data - keep question and answer description.
      :type - type of question page, it incapsulate some logic and basic layout
      :question - text of question
      :success - action will be called in case correct answer
      :fail - action will be called in case wrong answer
      :skip - action will be called in case skip clicked
      :audio-data - data which describe how to play and animate text in case listen icon is clicked
      :image - question image
      :answers - array which describes each available answer
        :text - answer text
        :correct - boolean show if this is correct answer or not
        :audio-data - data which describe how to play and animate text in case listen icon is clicked
    Audio data:
       :audio - audio file to play
       :start - start audio position in seconds
       :duration - duration of fragment to play
       :animation - animation type (e.g. 'color', 'bounce')
       :fill - color which will be used to highlight in case 'color' animation
       :data - data which describes duration, start time, chunk number and other data to animate text
          :start - time position to start animation
          :end - time position to end animation
          :duration - duration of animation,
          :at - the same as start
          :chunk - chunk number

    :Example
    {
        :type 'show-question'
        :data {
            :type 'type-1'
            :question   'What is the same about these two groups?'
            :success    'correct-answer-question',
            :fail       'fail-answer-question'
            :skip       'skip-question'
            :audio-data {
                :audio     '/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a',
                :start     2.826,
                :duration  4.785,
                :animation 'color',
                :fill 0x00B2FF
                :data [
                    {:start 2.826, :end 3.052, :duration 0.226, :at 2.826, :chunk 0}
                    {:start 3.092, :end 3.252, :duration 0.16, :at 3.092, :chunk 1}
                    {:start 3.306, :end 3.492, :duration 0.186, :at 3.306, :chunk 2}
                    {:start 3.692, :end 3.799, :duration 0.107, :at 3.692, :chunk 3}
                    {:start 3.839, :end 4.092, :duration 0.253, :at 3.839, :chunk 4}
                    {:start 4.212, :end 4.399, :duration 0.187, :at 4.212, :chunk 5}
                    {:start 4.465, :end 4.612, :duration 0.147, :at 4.465, :chunk 6}
                    {:start 4.665, :end 4.785, :duration 0.12, :at 4.665, :chunk 7}
                ]
            }
            :image      '/raw/img/categorize/question.png'
            :screenshot? false
            :answers    [
                {:text       'A. They are both red'
                :correct    false
                :audio-data {
                    :audio     '/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a',
                    :start     2.826,
                    :duration  4.785,
                    :animation  'color',
                    :fill 0x00B2FF
                    :data [
                        {:start 2.826, :end 3.052, :duration 0.226, :at 2.826, :chunk 0}
                        {:start 3.092, :end 3.252, :duration 0.16, :at 3.092, :chunk 1}
                        {:start 3.306, :end 3.492, :duration 0.186, :at 3.306, :chunk 2}
                        {:start 3.692, :end 3.799, :duration 0.107, :at 3.692, :chunk 3}
                        {:start 3.839, :end 4.092, :duration 0.253, :at 3.839, :chunk 4}
                    ]
                }
                ....
            ]
        }
    }"
    (let [wrappers (scene/get-object-name db :question-overlay)]
      (question-component/create (assoc data :parent (:object wrappers)) db action)
      {:dispatch-n (list [::webchange.interpreter.renderer.state.overlays/show-question]
                         (when-not continue-flow (ce/success-event action)))})))


(re-frame/reg-event-fx
  ::execute-hide-question
  (fn [{:keys [db]} [_ {:keys [data] :as action}]]
    "Execute `hide-question` action - allows to close overlay with question

    Example:
    {:type 'hide-question'}"
    (let [wrappers (scene/get-object-name db :question-overlay)
          object (:object wrappers)
          children (vec (array-seq (.-children object)))]
      (doseq [child children]
        (.removeChild object child))
      {:dispatch-n (list [::webchange.interpreter.renderer.state.overlays/hide-question]
                         (ce/success-event action))})))



(re-frame/reg-fx
  :execute-audio
  (fn [{:keys [flow-id skippable] :as params}]
    (sound/init)
    (-> (sound/play-audio params)
        (.then (fn [audio] (ce/register-flow-remove-handler! flow-id (fn [] (.stop audio))))))))

(re-frame/reg-fx
  :stop-all-audio
  (fn []
    (sound/stop-all-audio!)))

(re-frame/reg-fx
  :start-audio-recording
  (fn [{:keys [on-success]}]
    (audio-recorder/start on-success)))

(re-frame/reg-fx
  :stop-audio-recording
  (fn [{:keys [var-name on-ended]}]
    (audio-recorder/stop (fn [audio-blob]
                           (re-frame/dispatch [::warehouse/upload-audio-blob
                                               {:blob audio-blob}
                                               {:on-success [::stop-audio-recording-success var-name on-ended]
                                                :on-failure [::stop-audio-recording-failure on-ended]}])))))

(re-frame/reg-event-fx
  ::stop-audio-recording-success
  (fn [{:keys [_]} [_ var-name on-ended {:keys [url]}]]
    (vars.core/set-variable! var-name url)
    {:load-resources {:urls     [url]
                      :on-ended on-ended}}))

(re-frame/reg-event-fx
  ::stop-audio-recording-failure
  (fn [{:keys [_]} [_ on-ended]]
    (on-ended)))

(re-frame/reg-fx
  :load-resources
  (fn [{:keys [urls on-ended]
        :or   {urls     []
               on-ended #{}}}]
    (resources-manager/load-resources urls {:on-complete on-ended})))

(re-frame/reg-fx
  :music-volume
  (fn [value]
    (sound/init)
    (sound/music-volume (/ value 100))))

(re-frame/reg-fx
  :effects-volume
  (fn [value]
    (sound/init)
    (sound/effects-volume (/ value 100))))

(re-frame/reg-fx
  :load-course
  (fn [{:keys [course-id scene-id]}]
    (i/load-course {:course-id course-id}
                   (fn [course] (do (re-frame/dispatch [:complete-request :load-course])
                                    (re-frame/dispatch [::sw-status/set-current-course course-id])
                                    (re-frame/dispatch [::set-course-data course])
                                    (re-frame/dispatch [::load-progress {:course-id course-id
                                                                         :scene-id  scene-id}])
                                    (re-frame/dispatch [::load-lessons course-id]))))))

(re-frame/reg-fx
  :load-course-data
  (fn [{:keys [course-id]}]
    (i/load-course {:course-id course-id} #(re-frame/dispatch [::set-course-data %]))))

(re-frame/reg-fx
  :load-scene
  (fn [{:keys [course-id scene-id]}]
    (i/load-scene {:course-id course-id
                   :scene-id  scene-id}
                  (fn [scene]
                    (re-frame/dispatch [::set-scene scene-id scene])
                    (re-frame/dispatch [::store-scene scene-id scene])))))

(defn- progress-initialized?
  [progress]
  (and progress (:next progress)))

(re-frame/reg-fx
  :load-progress
  (fn [{:keys [course-id scene-id]}]
    (i/load-progress course-id (fn [progress]
                                 (re-frame/dispatch [:complete-request :load-progress])
                                 (if (progress-initialized? progress)
                                   (re-frame/dispatch [::set-progress-data progress])
                                   (re-frame/dispatch [::init-default-progress progress]))
                                 (re-frame/dispatch [::progress-loaded course-id scene-id])))))

(re-frame/reg-fx
  :load-lessons
  (fn [[course-id]]
    (i/load-lessons {:course-id         course-id
                     :cb                (fn [{:keys [items lesson-sets datasets]}]
                                          (re-frame/dispatch [:complete-request :load-lessons])
                                          (re-frame/dispatch [::set-course-lessons-data lesson-sets datasets items]))
                     :on-asset-complete #(do (re-frame/dispatch [::set-dataset-loaded]))})))

(re-frame/reg-fx
  :transition
  (fn [params]
    (i/interpolate params)))

(re-frame/reg-fx
  :switch-animation
  (fn [{:keys [state id track] :or {track 0} :as action}]
    (let [loop (if (contains? action :loop) (:loop action) true)]
      (w/set-animation state track id loop))))

(re-frame/reg-fx
  :add-animation
  (fn [{:keys [state id track] :or {track 0} :as action}]
    (let [loop (if (contains? action :loop) (:loop action) true)]
      (w/add-animation state track id loop 0))))

(re-frame/reg-fx
  :remove-animation
  (fn [{:keys [state track] :or {track 0}}]
    (w/remove-animation state track 0.2)))

(re-frame/reg-fx
  :set-slot
  (fn [{:keys [state slot-name slot-attachment-name image region attachment]}]
    (w/set-slot state slot-name image {:attachment-params    attachment
                                       :slot-attachment-name slot-attachment-name
                                       :region-params        region})))

(re-frame/reg-fx
  :set-skin
  (fn [{:keys [state skin]}]
    (w/set-skin state skin)))

(re-frame/reg-fx
  :animation-props
  (fn [{component :state {:keys [scaleX scaleY x y]} :props}]
    (let [position {:x x
                    :y y}
          current-scale (w/get-scale component {:abs? true})]
      (w/set-scale component {:x (* (:x current-scale) (or scaleX 1))
                              :y (* (:y current-scale) (or scaleY 1))})
      (w/set-position component position))))

(defn get-audio-key
  [db id]
  (get-in db [:scenes (:current-scene db) :audio (keyword id)]))

(re-frame/reg-event-fx
  ::execute-placeholder-audio
  (fn [{:keys [db]} [_ {:keys [var-name] :as action}]]
    "Execute `placeholder-audio` action - play audio with params from variable. Deprecated.

    Action params:
    :var-name - variable name.
    :id - variable field name to get audio id.
    :start - variable field name to get audio start time.
    :duration - variable field name to get audio duration time.
    :offset - variable field name to get audio offset."
    (let [scene-id (:current-scene db)
          scene (get-in db [:scenes scene-id])
          variable (get-in scene [:variables var-name])
          audio-params (-> action
                           (assoc :key (get-audio-key db (get variable (-> action :id keyword))))
                           (assoc :start (get variable (-> action :start keyword)))
                           (assoc :duration (get variable (-> action :duration keyword)))
                           (assoc :offset (get variable (-> action :offset keyword))))]
      {:execute-audio (-> audio-params
                          (assoc :on-ended #(ce/dispatch-success-fn action)))})))

(defn- without-params
  [object params]
  (apply dissoc object params))

(defn execute-transition
  [db {:keys [transition-id transition-tag to from skippable kill-after] :as action}]
  (let [scene-id (:current-scene db)
        transition (or
                     (some-> db (get-in [:transitions scene-id transition-id]) deref)
                     (scene/get-scene-object db (keyword transition-id)))
        id (or transition-tag transition-id)]
    (when transition
      (let [transition-params [:duration :easing :loop :yoyo :repeat :speed]]
        {:transition {:id         id
                      :component  transition
                      :to         (without-params to transition-params)
                      :from       from
                      :params     (select-keys to transition-params)
                      :on-ended   #(ce/dispatch-success-fn action)
                      :skippable  skippable
                      :kill-after kill-after}}))))

(defn point->transition
  ([to transition-id]
   {:type          "transition"
    :transition-id transition-id
    :to            to})
  ([to transition-id move-speed]
   (point->transition (assoc to :speed move-speed) transition-id)))

(defn execute-transitions-sequence
  [transitions {:keys [transition-id] :as action}]
  (let [data (map #(point->transition % transition-id) transitions)]
    {:dispatch [::ce/execute-sequence-data (merge action {:data data})]}))

(re-frame/reg-event-fx
  ::execute-transition
  (fn [{:keys [db]} [_ {:keys [to] :as action}]]
    "Execute `transition` action - object motion animation.

    Action params:
    :transition-id - name of transition. Must match the field `:transition` of component we want to move.
    :to - motion parameters:
        :duration - duration of animation in sec
        :loop - (optional) Repeat motion as 'yo-yo' animation when `true`. Default `false`.

        If you want to change some component params linearly, define their end value, like `:x 100` to move component horizontally.
        If you want to move the component along a cubic bezier curve, define `:bezier` param with vector of 3 points.
        When drawing a curve, the first point will be the current position of the component.

    Example 1: simple transition
    {:type          'transition',
     :transition-id 'mari',
     :to            {:x        1403,
                     :y        657,
                     :duration 1.3}}

    Example 2: repeating animation
    {:type          'transition',
     :transition-id 'box1',
     :to            {:x        668,
                     :y        798,
                     :loop     false,
                     :duration 0.7}}

    Example 3: bezier motion
    {:type          'transition',
     :transition-id 'ball-transition',
     :to            {:bezier   [{:x 825, :y 342},
                                {:x 655, :y 342},
                                {:x 314, :y 942}],
                     :duration 1.4}}"
    (if (or (:path to) (:letter-path to))
      (execute-transitions-sequence (path-utils/path->transitions to) action)
      (execute-transition db action))))

(re-frame/reg-event-fx
  ::execute-stop-transition
  (fn [{:keys [db]} [_ {:keys [id] :as action}]]
    "Execute `stop-transition` action - abort running transition.

    Action params:
    :id - name of transition. Must match the field `:transition` of component is being moved.

    Example:
    {:type 'stop-transition'
     :id   'swings'}"
    (i/kill-transition! id)
    {:dispatch (ce/success-event action)}))

(defn insert-move-rotations
  [[head & tail] {:keys [animation-target transition-id]} move-speed]
  (reduce (fn [tx tr]
            (let [last-x (-> tx last :to :x)]
              (if (< last-x (:x tr))
                (concat tx [{:type "animation-props" :target animation-target :props {:scaleX -1}} (point->transition tr transition-id move-speed)])
                (concat tx [{:type "animation-props" :target animation-target :props {:scaleX 1}} (point->transition tr transition-id move-speed)]))))
          [(point->transition head transition-id move-speed)] tail))

(re-frame/reg-event-fx
  ::execute-move
  (fn [{:keys [db]} [_ {:keys [from to graph animation-target animation-on-start animation-on-stop default-position move-speed] :as action}]]
    "Execute `move` action - animation of moving a component along the graph.

    Action params:
    :transition-id - name of transition. Must match the field `:transition` of component we want to move.
    :to - name of destination graph node.
    :from - name of start graph node.
    :default-position - name of start graph node if `:from` is not defined.
    :animation-on-start - animation name for component when start moving.
    :animation-on-stop - animation name for component when stop moving.
    :animation-target - animation name for component when moving is going.
    :move-speed - moving speed.
    :graph - description of graph nodes. Each node is described by its coordinates and `:links` field - node connections.

    Example:
    {:type               'move',
     :transition-id      'vera-transition',
     :to                 'park',
     :from               'home',
     :default-position   'crossroads',
     :animation-on-start 'go_front',
     :animation-on-stop  'idle',
     :animation-target   'vera-go',
     :move-speed         160,
     :graph              {:home       {:x 1000, :y 620, :links ['crossroads']},
                          :feria      {:x 590, :y 960, :links ['crossroads']},
                          :park       {:x 1325, :y 960, :links ['crossroads']},
                          :crossroads {:x 1070, :y 665, :links ['home' 'feria' 'park']}}}"
    (let [from (if (i/nav-node-exists? graph from) from default-position)
          path-names (i/find-nav-path from to graph)
          path (map #(-> (get graph (keyword %)) (select-keys [:x :y])) path-names)
          data (concat
                 [{:type "animation" :target animation-target :id animation-on-start}]
                 (insert-move-rotations path action move-speed)
                 [{:type "animation" :target animation-target :id animation-on-stop}])]
      {:dispatch [::ce/execute-sequence-data (merge action {:data data})]})))

(defn resolve-scene-id
  [location-data {:keys [level lesson]}]
  (if lesson
    (->> location-data
         (filter #(and (<= (:level %) level) (<= (:lesson %) lesson)))
         (last)
         (:scene))
    (->> location-data
         (filter #(<= (:level %) level))
         (last)
         (:scene))))

(re-frame/reg-event-fx
  ::execute-location
  (fn [{:keys [db]} [_ {:keys [location-id] :as action}]]
    "Execute `location` action - change current scene by location name.
    Used when the same location can be resolved with different scene id depending on the current level.
    Locations are defined in course data.

    Action params:
    :location-id - location name.

    Example:
    {:type        'location',
     :location-id 'painting-tablet'}"
    (let [location-key (keyword location-id)
          locations (get-in db [:course-data :locations])
          next (get-in db [:progress-data :next])
          scene-id (if (contains? locations location-key)
                     (resolve-scene-id (get locations location-key) next)
                     location-id)]
      {:dispatch-n (list [::set-current-scene scene-id] (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-scene
  (fn [{:keys [db]} [_ {:keys [scene-id] :as action}]]
    "Execute `scene` action - change current scene.

    Action params:
    :scene-id - next scene name.

    Example:
    {:type     'scene',
     :scene-id 'map'}"
    (let [next (get-in db [:progress-data :next])
          location-scene-id (some-> (get-in db [:course-data :locations])
                                    (get (keyword scene-id))
                                    (resolve-scene-id next))
          current-scene (get-in db [:current-scene])
          scene (get-in db [:course-data :scene-list (keyword current-scene)])
          exit-scene-id (some->> scene
                                 :outs
                                 (filter #(= (:object %) scene-id))
                                 first
                                 :name)]
      {:dispatch-n (list [::set-current-scene (or location-scene-id exit-scene-id scene-id)] (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-scene-exit
  (fn [{:keys [db]} [_ {:keys [exit-point] :as action}]]
    (let [current-scene (get-in db [:current-scene])
          scene (get-in db [:course-data :scene-list (keyword current-scene)])
          out-scene-id (->> scene
                            :outs
                            (filter #(= (:object %) exit-point))
                            first
                            :name)
          current-course (:current-course db)]
      (if out-scene-id
        {:dispatch-n (list [::set-current-scene out-scene-id] (ce/success-event action))}
        {:dispatch-n (list [::open-student-course-dashboard current-course])}
        ))))

;; audio action
;; First try to get audio url from :audio field
;; Second try to get audio url from :id field and scene :audios map
;; Last return :id as audio url
(re-frame/reg-event-fx
  ::execute-audio
  [ce/event-as-action ce/with-flow]
  (fn [{:keys [db]} {:keys [id audio] :as action}]
    "Execute `audio` action - play audio file.
    First trying to get audio url from `:audio`.
    Then trying to get url by audio id from scene `:audios` section.
    Lastly getting `:id` as audio url.

    Action params:
    :id - (one of id or audio is required).
    :audio - (one of id or audio is required).
    :loop - boolean flag indicating whether to repeat audio or not.
    :start - time in sec when to start
    :duration - audio interval duration in sec
    :volume - audio volume ([0..1])

    Example 1: audio src defined in `:id` key
    {:type 'audio',
     :id   '/raw/audio/background/Parque.mp3',
     :loop true}

    Example 2: play defined interval of audio
    {:type     'audio',
     :id       '/raw/audio/l2/a7/L2_A7_Mari.m4a',
     :start    52.432,
     :duration 3.228,
     :volume   0.2}

    Example 3: play audio by its id
    {:type 'audio',
     :id   'background',
     :loop true}

    In this case, the scene must have an `:audio` section with a `:background` key defined in it:
    {...
     :audio {:background '/raw/audio/background/POL-daily-special-short.mp3'}
     ...}

    Example 4: audio src defined in `:audio` key
    {:type  'audio',
     :audio '/upload/OOUGTOFOCYKRPXPD.m4a'}"
    {:execute-audio (-> action
                        (assoc :key (or audio (get-audio-key db id) id))
                        (assoc :on-ended #(ce/dispatch-success-fn action)))}))

(re-frame/reg-event-fx
  ::execute-start-audio-recording
  [ce/event-as-action ce/with-flow]
  (fn [{:keys [_]} action]
    {:start-audio-recording {:on-success #(ce/dispatch-success-fn action)}}))

(re-frame/reg-event-fx
  ::execute-stop-audio-recording
  [ce/event-as-action ce/with-flow]
  (fn [{:keys [_]} action]
    {:stop-audio-recording (-> action
                               (assoc :on-ended #(ce/dispatch-success-fn action)))}))

(re-frame/reg-event-fx
  ::execute-stop-audio
  [ce/event-as-action]
  (fn [{:keys [db]} _]
    {:stop-all-audio nil}))

(re-frame/reg-event-fx
  ::execute-play-video
  [ce/event-as-action ce/with-flow]
  (fn [{:keys [_]} {:keys [target src flow-id start end] :as action}]
    "Execute `play-video` action - play video file.

    Action params:
    :target - name of video object (component).
    :src - video file url.

    Example:
    {:type   'play-video',
     :target 'letter-video',
     :src    '/raw/video/l2a1/letter-a.mp4'}"
    (let [target (keyword target)]
      (ce/register-flow-remove-handler! flow-id (fn []
                                                  (re-frame/dispatch [::scene/change-scene-object target [[:stop]]])))
      {:dispatch [::scene/change-scene-object target [[:set-src {:src     src
                                                                 :options {:play   true
                                                                           :start  start
                                                                           :end    end
                                                                           :on-end #(ce/dispatch-success-fn action)}}]]]})))

(re-frame/reg-event-fx
  ::execute-path-animation
  [ce/event-as-action ce/with-flow]
  (fn [{:keys [db]} {:keys [target state flow-id] :as action}]
    "Execute `path-animation` action - run svg path animation.

    Action params:
    :target - name of `animated-svg-path` component.
    :state - 'play' for start playing or
             'reset' for reset `animated-svg-path` component state

    Example:
    {:type   'path-animation',
     :target 'letter-tutorial-path',
     :state  'play'}"
    (let [{:keys [animated-svg-path-start animated-svg-path-stop animated-svg-path-reset]} (scene/get-scene-object db (keyword target))
          on-end #(ce/dispatch-success-fn action)]
      (case state
        "play" (animated-svg-path-start on-end)
        "reset" (do (animated-svg-path-reset)
                    (on-end)))

      (ce/register-flow-remove-handler! flow-id animated-svg-path-stop)
      {})))

(re-frame/reg-event-fx
  ::stop-path-animation
  [ce/event-as-action]
  (fn [{:keys [db]} {:keys [target]}]
    (let [{:keys [animated-svg-path-stop]} (scene/get-scene-object db (keyword target))]
      (animated-svg-path-stop)
      {})))

(re-frame/reg-event-fx
  ::execute-mass-state
  (fn [{:keys [db]} [_ {:keys [targets] :as action}]]
    "Execute `state` action - apply component state.

    Action params:
    :targets - component names.
    :id - state name.

    Example:
    {:type   'state',
     :targets ['bubble-1' 'bubble-2' 'bubble-3'],
     :id     'hidden'}"
    (let [actions (map (fn [target] [::execute-state (assoc action :target target)]) targets)]
      {:dispatch-n actions})))

(re-frame/reg-event-fx
  ::execute-state
  (fn [{:keys [db]} [_ {:keys [target id params] :as action}]]
    "Execute `state` action - apply component state.

    Action params:
    :target - component name.
    :id - state name.

    Example:
    {:type   'state',
     :target 'bubble-1',
     :id     'hidden'}"
    (let [scene-id (:current-scene db)
          scene (get-in db [:scenes scene-id])
          target-path (->> (clojure.string/split (clojure.core/name target) ".")
                           (map numbers/try-parse-int)
                           (map (fn [path-step]
                                  (if (string? path-step)
                                    (keyword path-step)
                                    path-step))))
          object (get-in scene (concat [:objects] target-path))
          states (get object :states)
          states-with-aliases (reduce-kv (fn [m k v] (assoc m k (get states (keyword v)))) states (get object :states-aliases))
          state (merge (get states-with-aliases (keyword id)) params)]
      {:dispatch-n (list [::scene/set-scene-object-state (keyword target) state]
                         (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-set-attribute
  (fn [{:keys [_]} [_ {:keys [target attr-name attr-value] :as action}]]
    "Execute `set-attribute` action - set component attribute value.

    Action params:
    :target - component name.
    :attr-name - attribute name.
    :attr-value - attribute value.

    Example:
    {:type       'set-attribute'
     :target     'letter-path'
     :attr-name  'x'
     :attr-value 0}"
    (let [patch {(keyword attr-name) attr-value}]
      {:dispatch-n (list [::scene/set-scene-object-state (keyword target) patch]
                         (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-empty
  (fn [{:keys [_]} [_ action]]
    "Execute `empty` action - delay in ms.

    Action params:
    :duration - delay duration in ms.

    Example:
    {:type     'empty',
     :duration 700}"
    {:dispatch-later [{:ms (int (:duration action)) :dispatch (ce/success-event action)}]}))

(re-frame/reg-event-fx
  ::execute-animation
  (fn [{:keys [db]} [_ action]]
    "Execute `animation` action - immediately run animation of `animation` component.

    Action params:
    :target - `animation` component name.
    :id - animation name.
    :track - (optional) track number. Animations on different tracks are played simultaneously. Default 0.
    :loop - (optional) repeat animation or not.

    Example 1:
    {:type   'animation',
     :target 'vera',
     :id     'volley_call'}

    Example 2:
    {:type   'animation',
     :target 'mari',
     :id     'wand_hit',
     :track  2
     :loop   true}"
    (let [scene-id (:current-scene db)
          animation-wrapper (or (get-in db [:scenes scene-id :animations (:target action)])
                                (->> (:target action) keyword (scene/get-scene-object db)))]
      {:switch-animation (-> action
                             (assoc :state animation-wrapper))
       :dispatch-n       (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-animation-sequence
  (fn [{:keys [db]} [_ {:keys [flow-id] :as action}]]
    "Execute `animation-sequence` action - play audio file and speaking animation simultaneously.

    Action params:
    :target - `animation` component name.
    :audio - audio file url.
    :start - start of audio interval in sec.
    :duration - duration of audio interval in sec.
    :data - speaking animation timing. A vector of all animation fragments. Each fragment consists of:
            :anim - speaking animation name. Currently, only 'talk' is available.
            :start - time of start animation
            :end - time of end animation
    :track - track number. Animations on different tracks are played simultaneously.
             For this reason, the track should be different from the tracks of other animations.

    Example:
    {:type     'animation-sequence',
     :target   'mari',
     :audio    '/upload/PPHETAQQKJEPPXTD.m4a',
     :start    21.156,
     :duration 2.746,
     :data     [{:anim 'talk', :start 21.28, :end 22.02}
                {:anim 'talk', :start 22.32, :end 23.51}],
     :track    1}"
    (if-not (ce/skip-flow? flow-id)
      (let [animation-actions (i/animation-sequence->actions action)
            audio-action (i/animation-sequence->audio-action action)]
        (if audio-action
          {:dispatch [::ce/execute-parallel (assoc action :data (conj animation-actions audio-action))]}
          {:dispatch [::ce/execute-parallel (assoc action :data animation-actions)]}))
      {:dispatch (ce/success-event action)})))

(re-frame/reg-event-fx
  ::execute-add-animation
  (fn [{:keys [db]} [_ action]]
    "Execute `add-animation` action - add animation to animations que.

    Action params:
    :target - `animation` component name.
    :id - animation name.
    :loop - (optional) repeat animation or not.
    :track - (optional) track number. Animations on different tracks are played simultaneously. Default 0.

    Example:
    {:type   'add-animation',
     :target 'box1',
     :id     'idle2',
     :loop   true}"
    (let [scene-id (:current-scene db)
          animation-wrapper (or (get-in db [:scenes scene-id :animations (:target action)])
                                (->> (:target action) keyword (scene/get-scene-object db)))]
      {:add-animation (-> action
                          (assoc :state animation-wrapper))
       :dispatch-n    (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-start-animation
  (fn [{:keys [db]} [_ action]]
    "Execute `start-animation` action - start animation of `animation` component.
    Used when `animation` component was initialized with parameter `:start false`

    Action params:
    :target - `animation` component name.

    Example:
    {:type   'start-animation',
     :target 'book'}"
    (let [scene-id (:current-scene db)
          animation-wrapper (or (get-in db [:scenes scene-id :animations (:target action)])
                                (->> (:target action) keyword (scene/get-scene-object db)))]
      (w/start-animation animation-wrapper)
      {:dispatch-n (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-remove-animation
  (fn [{:keys [db]} [_ action]]
    "Execute `remove-animation` action - remove animations from `animation` component.

    Action params:
    :target - `animation` component name.
    :track - (optional) track number. Default 0.

    Example:
    {:type   'remove-animation'
     :target 'book'}"
    (let [scene-id (:current-scene db)
          animation-wrapper (or (get-in db [:scenes scene-id :animations (:target action)])
                                (->> (:target action) keyword (scene/get-scene-object db)))]
      {:remove-animation (-> action (assoc :state animation-wrapper))
       :dispatch-n       (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-set-skin
  (fn [{:keys [db]} [_ action]]
    "Execute `set-skin` action - set character appearance.

    Action params:
    :target - `animation` component name.
    :skin - skin name. Available skin can be found in `skeleton.json` of animation in `resources/public/raw/anim/` folder.

    Example:
    {:type   'set-skin',
     :target 'senoravaca'
     :skin   'idle'}"
    (let [scene-id (:current-scene db)
          animation-wrapper (or (get-in db [:scenes scene-id :animations (:target action)])
                                (->> (:target action) keyword (scene/get-scene-object db)))]
      {:set-skin   (-> action (assoc :state animation-wrapper))
       :dispatch-n (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-set-slot
  (fn [{:keys [db]} [_ action]]
    "Execute `set-slot` action - set image to the front side of a box.

    Action params:
    :target - `animation component name`. Must be `boxes` animation.
    :slot - slot name. For `boxes` only 'box1' slt is available.
    :image - url of image to set.
    :attachment - image position params.

    Example:
    {:type       'set-slot',
     :target     'box4',
     :slot-name  'box1',
     :image      '/raw/img/elements/axe.png'
     :attachment {:x 40, :scale-x 4, :scale-y 4}}"
    (let [scene-id (:current-scene db)
          animation-wrapper (or (get-in db [:scenes scene-id :animations (:target action)])
                                (->> (:target action) keyword (scene/get-scene-object db)))]
      {:set-slot   (-> action (assoc :state animation-wrapper))
       :dispatch-n (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-set-animation-props
  (fn [{:keys [db]} [_ action]]
    "Execute `animation-props` action - set properties of `animation` component. Deprecated. Use `set-attribute` instead."
    (let [scene-id (:current-scene db)
          animation-wrapper (or (get-in db [:scenes scene-id :animations (:target action)])
                                (->> (:target action) keyword (scene/get-scene-object db)))]
      {:animation-props (-> action (assoc :state animation-wrapper))
       :dispatch-n      (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-start-activity
  (fn [{:keys [db]} [_ {activity-name :id :as action}]]
    "Execute `start-activity` action - set activity as started.

    Action params:
    :id - activity name.

    Example:
    {:type 'start-activity'
     :id   'pinata'}"
    (let [activity-name (or activity-name (:current-scene db))
          activity-action (lessons-activity/name->activity-action db activity-name)]
      {:db         (assoc db
                     :activity-started true
                     :activity-start-time (js/Date.)
                     :activity (select-keys activity-action [:level :lesson :activity :activity-name]))
       :dispatch-n (list
                     [::disable-navigation]
                     [::add-pending-event :activity-started activity-action]
                     (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-stop-activity
  (fn [{:keys [db]} [_ {activity-name :id :as action}]]
    "Execute `stop-activity` action - stop activity when the user exits without completing it;.

    Action params:
    :id - activity name.

    Example:
    {:type 'stop-activity'
     :id   'pinata'}"
    (let [activity-name (or activity-name (:current-scene db))
          activity-action (lessons-activity/name->activity-action db activity-name)
          start-time (get db :activity-start-time)
          time-spent (if start-time
                       (- (js/Date.) (get db :activity-start-time))
                       0)
          activity-started? (:activity-started db)]
      (if activity-started?
        {:db         (assoc db :activity-started false)
         :dispatch-n (list
                       [::add-pending-event :activity-stopped (assoc activity-action :time-spent time-spent)]
                       (ce/success-event action))}
        {:dispatch (ce/success-event action)}))))

(defn activity-score
  [db]
  {:correct   (vars.core/get-variable :score-correct)
   :incorrect (vars.core/get-variable :score-incorrect)
   :mistake   (vars.core/get-variable :score-mistake)})

(defn activity-score-percentage
  [db]
  (let [score (activity-score db)
        has-score? (:correct score)]
    (if has-score?
      (-> (- (:correct score) (:incorrect score))
          (/ (:correct score))
          (* 100)
          js/Math.round)
      100)))

(defn activity-finished-event
  [db {activity-name :id}]
  (let [activity-name (or activity-name (:current-scene db))
        activity-action (lessons-activity/name->activity-action db activity-name)
        score (activity-score db)
        start-time (get db :activity-start-time)
        time-spent (if start-time
                     (- (js/Date.) (get db :activity-start-time))
                     0)]
    [::add-pending-event :activity-finished (merge activity-action {:score      score
                                                                    :time-spent time-spent})]))

(defn lesson-activity-finished?
  [db {activity-name :id}]
  (let [next (get-in db [:progress-data :next])
        activity-name (or activity-name (:current-scene db))
        activity-action (lessons-activity/name->activity-action db activity-name)
        current-activity? (= next activity-action)

        score-percentage (-> (lessons-activity/workflow-action db activity-action) :expected-score-percentage)
        score-passed? (if score-percentage
                        (> (activity-score-percentage db) score-percentage)
                        true)]
    (and current-activity? score-passed?)))


(defn get-lesson-activity-tags
  [db {activity-name :id}]
  (let [activity-name (or activity-name (:current-scene db))
        activity-action (lessons-activity/name->activity-action db activity-name)
        tags-by-score (-> (lessons-activity/workflow-action db activity-action) :tags-by-score)
        current-tags (get-in db [:progress-data :current-tags] [])]
    (if tags-by-score
      (let [
            current-tags (if (nil? current-tags) [] current-tags)
            current-tags (tags/remove-tags current-tags tags/learning-level-tags)
            score (activity-score-percentage db)
            tags-to-add (map (fn [[tag [minm maxm]]]
                               (if (and (<= minm score) (> maxm score)) (name tag))) tags-by-score)
            new-tags (filter #(some? %) (concat tags-to-add current-tags))
            ]
        new-tags)
      current-tags)))

(defn- next-activity-name
  [db]
  (get-in db [:progress-data :next :activity-name]))

(defn- has-next-activity?
  [db]
  (next-activity-name db))


(re-frame/reg-event-fx
  ::goodbye-activity
  (fn [{:keys [db]} _]
    {:dispatch-n (list [::overlays/show-goodbye-screen]
                       [::reset-navigation])}))


(re-frame/reg-event-fx
  ::execute-finish-activity
  (fn [{:keys [db]} [_ action]]
    "Execute `finish-activity` action - set activity as finished.

    Action params:
    :id - activity name.

    Example:
    {:type 'finish-activity',
     :id   'cinema'}"
    (when (:activity-started db)
      (let [show-goodbye (and (not (lesson-activity-finished? db action)) (not (has-next-activity? db)))
            events (cond-> (list)
                           (lesson-activity-finished? db action) (conj [::finish-next-activity])
                           show-goodbye (conj [::goodbye-activity])
                           (not show-goodbye) (conj [::overlays/show-activity-finished])
                           :always (conj (activity-finished-event db action))
                           :always (conj [::reset-navigation]))
            lesson-activity-tags (get-lesson-activity-tags db action)
            finished (get-in db [:progress-data :next])
            db (cond-> db
                       :always lessons-activity/clear-loaded-activity
                       :always (assoc :activity-started false)
                       :always (assoc-in [:progress-data :current-tags] lesson-activity-tags)
                       (lesson-activity-finished? db action) (lessons-activity/finish finished))]
        (ce/skip)
        (ce/remove-timers!)
        {:db         db
         :dispatch-n events}))))

(defn activity-progress-event
  [db]
  (let [activity-progress (lessons-activity/activity-progress db)]
    [::add-pending-event :activity-progress {:activity-progress activity-progress}]))

(re-frame/reg-event-fx
  ::finish-next-activity
  (fn [{:keys [db]} _]
    {:dispatch-n (list (activity-progress-event db))}))

(re-frame/reg-event-fx
  :progress-data-changed
  (fn [{:keys [db]} _]
    (let [course-id (:current-course db)
          progress (:progress-data db)
          events (:pending-events db)
          loading? (get-in db [:loading :save-progress])]
      (if loading?
        {:db (assoc db :schedule-save-progress true)}
        {:db         (-> db
                         (assoc-in [:loading :save-progress] true)
                         (dissoc :pending-events))
         :http-xhrio {:method          :post
                      :uri             (str "/api/courses/" course-id "/current-progress")
                      :params          {:progress progress :events events}
                      :format          (json-request-format)
                      :response-format (json-response-format {:keywords? true})
                      :on-success      [::save-progress-success]
                      :on-failure      [:api-request-error :save-progress]}}))))

(re-frame/reg-event-fx
  ::save-progress-success
  (fn [{:keys [db]} _]
    (let [scheduled? (:schedule-save-progress db)]
      (if scheduled?
        {:db         (assoc db :schedule-save-progress false)
         :dispatch-n (list [:complete-request :save-progress] [:progress-data-changed])}
        {:dispatch-n (list [:complete-request :save-progress])}))))

(re-frame/reg-event-fx
  ::set-music-volume
  (fn [{:keys [db]} [_ value]]
    (when value
      {:db           (assoc-in db [:settings :music-volume] value)
       :music-volume value})))

(re-frame/reg-event-fx
  ::set-effects-volume
  (fn [{:keys [db]} [_ value]]
    (when value
      {:db             (assoc-in db [:settings :effects-volume] value)
       :effects-volume value})))

(re-frame/reg-event-fx
  ::start-course
  (fn-traced [{:keys [db]} [_ course-id scene-id]]
    (if (not= course-id (:loaded-course db))
      {:dispatch-n (list [::load-course course-id scene-id])})))

(re-frame/reg-event-fx
  ::load-course
  (fn-traced [{:keys [db]} [_ course-id scene-id]]
    (if (not= course-id (:loaded-course db))
      {:db          (-> db
                        (assoc :loaded-course course-id)
                        (assoc :current-course course-id)
                        (assoc-in [:loading :load-course] true))
       :load-course {:course-id course-id
                     :scene-id  scene-id}})))

(re-frame/reg-event-fx
  ::load-scenes-with-skills
  (fn [{:keys [db]} [_ course-id]]
    {:db         (assoc-in db [:loading :load-scenes-with-skills] true)
     :http-xhrio {:method          :get
                  :uri             (str "/api/courses/" course-id "/scenes-with-skills")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-scenes-with-skills-success]
                  :on-failure      [:api-request-error :load-scenes-with-skills]}}))

(re-frame/reg-event-fx
  ::load-scenes-with-skills-success
  (fn [{:keys [db]} [_ scenes-with-skills]]
    (let [scene-skills (->> scenes-with-skills
                            (map (juxt :name :skills))
                            (into {}))
          scene-placeholders (->> scenes-with-skills
                                  (map (juxt :name :is-placeholder))
                                  (into {}))]
      {:db (-> db
               (assoc-in [:loading :load-scenes-with-skills] false)
               (assoc :scene-skills scene-skills)
               (assoc :scene-placeholders scene-placeholders))})))

(re-frame/reg-event-fx
  ::load-course-data
  (fn-traced [{:keys [db]} [_ course-id]]
    (if (not= course-id (:loaded-course db))
      {:dispatch         [::load-scenes-with-skills course-id]
       :load-course-data {:course-id course-id}
       :load-lessons     [course-id]})))

(re-frame/reg-event-fx
  ::set-current-course
  (fn [{:keys [db]} [_ course-name]]
    {:db (assoc db :current-course course-name)}))

(re-frame/reg-event-fx
  ::load-scene
  (fn [{:keys [db]} [_ scene-id]]
    (let [loaded (get-in db [:scene-loading-complete scene-id])]
      (when (not loaded) {:load-scene {:course-id (:current-course db)
                                       :scene-id  scene-id}}))))

(re-frame/reg-event-fx
  ::clear-current-scene
  (fn [{:keys [db]} _]
    (let [current-scene (:current-scene db)]
      {:db         (assoc db :activity-started false)
       :dispatch-n (list [::vars.events/clear-vars {:keep-running true}]
                         [::execute-stop-audio]
                         [::ce/execute-remove-flows {:flow-tag (str "scene-" current-scene)}]
                         [::ce/execute-remove-timers])})))

(defn merge-with-templates
  [db scene]
  (let [scene-templates-names (:templates scene)
        scene-has-templates? (> (count scene-templates-names) 0)]
    (if scene-has-templates?
      (let [course-templates (get-in db [:course-data :templates])
            templates (map #(->> % keyword (get course-templates)) scene-templates-names)]
        (merge-scene-data scene (map #(add-scene-tag % "template") templates)))
      scene)))

(defn reset-scene-flows!
  [scene-id]
  (vars.core/clear-vars! false)
  (sound/stop-all-audio!)
  (ce/execute-remove-flows! {:flow-tag (str "scene-" scene-id)})
  (ce/remove-timers!))

(re-frame/reg-event-fx
  ::set-current-scene
  (fn [{:keys [db]} [_ scene-id]]
    (let [current-scene (:current-scene db)
          stored-scene (get-in db [:store-scenes scene-id])
          merged-scene (merge-with-templates db stored-scene)]
      (reset-scene-flows! current-scene)
      {:db         (-> db
                       (assoc :current-scene scene-id)
                       (assoc-in [:scenes scene-id] merged-scene)
                       (assoc :current-scene-data (get-in db [:scenes scene-id]))
                       (assoc :activity-started false)
                       (assoc :scene-started false)
                       (assoc-in [:progress-data :variables :last-location] current-scene))
       :dispatch-n (list [::load-scene scene-id]
                         [::set-stage-size (keyword (get-in merged-scene [:metadata :stage-size] "contain"))])})))

(re-frame/reg-event-fx
  ::set-stage-size
  (fn [{:keys [db]} [_ stage-size]]
    {:db (assoc-in db [:stage-size] stage-size)}))

(re-frame/reg-event-fx
  ::reset-scene-flows
  (fn [_ [_ scene-id]]
    (reset-scene-flows! scene-id)
    {}))

(re-frame/reg-event-fx
  ::set-course-data
  (fn [{:keys [db]} [_ course]]
    {:db (assoc db :course-data course)}))

(re-frame/reg-event-fx
  ::set-scene
  (fn [{:keys [db]} [_ scene-id scene]]
    (let [current-scene (:current-scene db)
          merged-scene (merge-with-templates db scene)]
      {:db         (cond-> (assoc-in db [:scenes scene-id] merged-scene)
                           (= current-scene scene-id) (assoc :current-scene-data merged-scene))
       :dispatch-n [[::set-stage-size (keyword (get-in merged-scene [:metadata :stage-size] "contain"))]]})))

(re-frame/reg-event-fx
  ::set-scenes-data
  (fn [{:keys [db]} [_ scenes-data]]
    (let [processed-scenes (->> scenes-data
                                (map (fn [[scene-name scene-data]]
                                       [scene-name (merge-with-templates db scene-data)]))
                                (into {}))]
      {:db (update-in db [:scenes] merge processed-scenes)})))

(re-frame/reg-event-fx
  ::store-scene
  (fn [{:keys [db]} [_ scene-id scene]]
    {:db (assoc-in db [:store-scenes scene-id] scene)}))

(re-frame/reg-event-fx
  ::set-progress-data
  (fn [{:keys [db]} [_ data]]
    {:db (assoc db :progress-data data)}))

(re-frame/reg-event-fx
  ::init-default-progress
  (fn [{:keys [db]} [_ progress]]
    (let [default-progress (get-in db [:course-data :default-progress])]
      {:db (update-in db [:progress-data] merge progress default-progress)})))

(def default-triggers
  {:start [[::reset-navigation]]})

(re-frame/reg-event-fx
  ::trigger
  (fn [{:keys [db]} [_ trigger]]
    (let [scene-id (:current-scene db)
          scene (get-in db [:scenes scene-id])
          actions (->> (:triggers scene)
                       (filter #(= trigger (-> % second :on keyword)))
                       (map second)
                       (map #(-> % :action keyword))
                       (map (fn [action-name] [action-name (get-in scene [:actions action-name])]))
                       (map (fn [[display-name action]] [::ce/execute-action (assoc action :display-name display-name)])))
          default-actions (get default-triggers trigger)]
      {:dispatch-n (concat actions default-actions)})))

(defn- next-scene-location
  [db]
  (let [next-activity (next-activity-name db)
        current-scene (get-in db [:current-scene])
        scene-list (interpreter-subs/navigation-scene-list db)]
    (->> (find-path current-scene next-activity scene-list)
         (drop 1)
         (take-last 3)
         (first))))

(defn- next-location
  [db]
  (let [next-activity (next-activity-name db)
        current-scene (get-in db [:current-scene])
        scene-list (interpreter-subs/navigation-scene-list db)]
    (find-exit-position current-scene next-activity scene-list)))

(re-frame/reg-event-fx
  ::next-scene
  (fn [{:keys [db]} [_ _]]
    (let [next-scene-id (-> db next-scene-location)]
      {:dispatch-n (list [::set-current-scene next-scene-id])})))

(re-frame/reg-event-fx
  ::run-next-activity
  (fn [{:keys [db]} [_ _]]
    (let [next-activity (next-activity-name db)]
      {:dispatch-n (list [::set-current-scene next-activity])})))

(re-frame/reg-event-fx
  ::restart-scene
  (fn [{:keys [db]} [_ _]]
    (let [scene-id (:current-scene db)]
      (vars.core/set-global-variable! :force-scene-update true)
      {:dispatch-n (list [::overlays/hide-activity-finished]
                         [::set-current-scene scene-id])})))

(re-frame/reg-event-fx
  ::back-scene
  (fn [{:keys [db]} [_ _]]
    {:dispatch-n (list [::trigger :back]
                       [::execute-scene-exit {:exit-point "back"}])}))

(re-frame/reg-event-fx
  ::open-student-dashboard
  (fn [{:keys [db]} [_ _]]
    (let [course-id (:current-course db)]
      {:dispatch-n (list [::clear-current-scene]
                         [:open-student-course-dashboard course-id])})))

(re-frame/reg-event-fx
  ::open-student-course-dashboard
  (fn [{:keys [db]} [_ course-id]]
    {:dispatch-n (list [::clear-current-scene]
                       [:open-student-course-dashboard course-id])}))

(re-frame/reg-event-fx
  ::load-progress
  (fn [{:keys [db]} [_ {:keys [course-id scene-id]}]]
    {:db            (assoc-in db [:loading :load-progress] true)
     :load-progress {:course-id course-id
                     :scene-id  scene-id}}))

(re-frame/reg-event-fx
  ::load-lessons
  (fn [{:keys [db]} [_ course-id]]
    {:db           (-> db
                       (assoc-in [:loading :load-lessons] true)
                       (assoc-in [:loading :load-lessons-assets] true))
     :load-lessons [course-id]}))

(defn- prepare-datasets-items [data]
  (into {} (map #(identity [(:id %) %]) data)))

(re-frame/reg-event-fx
  ::set-course-dataset-items
  (fn [{:keys [db]} [_ data]]
    (let [prepared (prepare-datasets-items data)]
      {:db (assoc db :dataset-items prepared)})))

(re-frame/reg-event-fx
  ::set-course-datasets
  (fn [{:keys [db]} [_ data]]
    {:db (assoc db :datasets data)}))

(defn- prepare-lesson [{data :data :as lesson}]
  (assoc lesson :item-ids (map #(:id %) (:items data))))

(defn- prepare-lessons [lessons]
  (into {} (map #(identity [(:name %) (prepare-lesson %)]) lessons)))

(re-frame/reg-event-fx
  ::set-course-lessons
  (fn [{:keys [db]} [_ data]]
    (let [prepared (prepare-lessons data)]
      {:db (assoc db :lessons prepared)})))

(re-frame/reg-event-fx
  ::update-course-lessons
  (fn [{:keys [db]} [_ data]]
    (let [prepared (prepare-lessons data)]
      {:db (update db :lessons merge prepared)})))

(re-frame/reg-event-db
  ::set-dataset-loading-progress
  (fn [db [_ value]]
    (assoc db :dataset-loading-progress value)))

(re-frame/reg-event-db
  ::set-dataset-loaded
  (fn [db _]
    (assoc-in db [:loading :load-lessons-assets] false)))

(re-frame/reg-event-fx
  ::set-course-lessons-data
  (fn [{:keys [db]} [_ lesson-sets datasets dataset-items]]
    "Execute ::set-course-lessons, ::set-course-datasets and ::set-course-dataset-items in single event."
    {:db (-> db
             (assoc :lessons (prepare-lessons lesson-sets))
             (assoc :datasets datasets)
             (assoc :dataset-items (prepare-datasets-items dataset-items)))}))

(re-frame/reg-event-fx
  ::execute-test-transitions-collide
  (fn [{:keys [db]} [_ {:keys [transition-1 transition-2 success fail] :as action}]]
    "Execute `transitions-collide` action - check if components intersect.

    Action params:
    :success - name of action to call if components intersect.
    :fail - name of action to call if components don't intersect.
    :transition-1 - transition name of the first component.
    :transition-2 - transition name of the second component.

    Example:
    {:type         'test-transitions-collide',
     :success      'check-box3',
     :fail         'box-3-revert',
     :transition-1 'box3',
     :transition-2 'box-ph'}"
    (let [transition-1-wrapper (->> transition-1 keyword (scene/get-scene-object db))
          transition-2-wrapper (->> transition-2 keyword (scene/get-scene-object db))
          success (ce/get-action success db action)
          fail (ce/get-action fail db action)]
      (if (i/collide? (:object transition-1-wrapper) (:object transition-2-wrapper))
        {:dispatch-n (list [::ce/execute-action success] (ce/success-event action))}
        {:dispatch-n (list [::ce/execute-action fail] (ce/success-event action))}))))


(re-frame/reg-event-fx
  ::execute-test-transition-and-pointer-collide
  (fn [{:keys [db]} [_ {:keys [transition success fail] :as action}]]
    "Execute `transition-and-pointer-collide` action - if mouse and component intersect.

    Action params:
    :success - name of action to call if components intersect.
    :fail - name of action to call if components don't intersect.
    :transition-1 - transition name of the first component.
    :transition-2 - transition name of the second component.

    Example:
    {:type         'test-transition-and-pointer-collide',
     :success      'check-box3',
     :fail         'box-3-revert',
     :transition   'box3'}"
    (let [transition-wrapper (->> transition keyword (scene/get-scene-object db))]
      (if (i/collide-with-coords? (:object transition-wrapper) (dg/get-mouse-position))
        (if success
          {:dispatch [::ce/execute-action (ce/cond-action db action :success)]}
          {:dispatch [::ce/execute-action (ce/success-event action)]})
        (if fail
          {:dispatch [::ce/execute-action (ce/cond-action db action :fail)]}
          {:dispatch [::ce/execute-action (ce/success-event action)]})))))


(re-frame/reg-event-fx
  ::execute-test-transitions-and-pointer-collide
  (fn [{:keys [db]} [_ {:keys [transitions success fail action-params] :as action}]]
    "Execute `transitions-and-pointer-collide` action - if mouse and component intersect.

    Action params:
    :success - name of action to call if components intersect.
    :fail - name of action to call if components don't intersect.
    :transitions - transitions name to check collide.

    Example:
    {:type        'test-transitions-and-pointer-collide',
     :success     'highlight',
     :fail        'unhighlight',
     :transition ['transition-1' 'transition-2' 'transition-3']}]}"
    (if (dg/position-empty?)
      {:dispatch (ce/success-event action)}
      (let [get-transition-wrapper #(->> % keyword (scene/get-scene-object db))
            pointer-position (dg/get-mouse-position)
            actions (->> transitions
                         (map-indexed (fn [idx transition]
                                        (let [{:keys [object get-object-data set-object-data]} (get-transition-wrapper transition)
                                              data (get-object-data)
                                              collide? (get data :collide?)
                                              params (merge (get action-params idx) {:transition transition} (:params action))]
                                          (if (i/collide-with-coords? object pointer-position)
                                            (let [data-collide (merge data {:collide? true})]
                                              (when (not collide?)
                                                (set-object-data data-collide)
                                                (when success
                                                  [::ce/execute-action (assoc (ce/cond-action db action :success) :params params)])))
                                            (let [data-collide (merge data {:collide? false})]
                                              (when (or collide? (nil? collide?))
                                                (set-object-data data-collide)
                                                (when fail
                                                  [::ce/execute-action (assoc (ce/cond-action db action :fail) :params params)])))))))
                         (remove nil?))]
        {:dispatch-n (vec (conj actions (ce/success-event action)))}))))

(re-frame/reg-event-fx
  ::execute-set-text
  (fn [{:keys [db]} [_ {:keys [target text] :as action}]]
    {:dispatch-n (list [::scene/change-scene-object (keyword target) [[:set-text {:text text}]]]
                       (ce/success-event action))}))

(re-frame/reg-event-fx
  ::execute-clear-painting-area
  (fn [{:keys [db]} [_ {:keys [target] :as action}]]
    {:dispatch-n (list [::scene/change-scene-object (keyword target) [[:clear-area {}]]]
                       (ce/success-event action))}))

(re-frame/reg-event-fx
  ::execute-text-animation
  (fn [{:keys [db]} [_ {:keys [flow-id] :as action}]]
    "Execute `text-animation` action - play audio file and text chunks animation simultaneously.

    Action params:
    :target - `text` component name. The component must contain `:chunks` property.
    :audio - audio file url.
    :start - start time in sec.
    :duration - audio interval duration in sec.
    :data - data defining the animation time for each chunk.

    Example:
    {:type      'text-animation',
     :target    'title-text',
     :audio     '/raw/audio/l1/a6/lion/2Mis_primeras_palabras.mp3',
     :start     2.693,
     :duration  2.999,
     :data      [{:at 2.826, :chunk 0}
                 {:at 3.092, :chunk 1}]}"
    (if-not (ce/skip-flow? flow-id)
      (let [animation-actions (i/text-animation-sequence->actions db action)
            audio-action (i/animation-sequence->audio-action action)]
        (if audio-action
          {:dispatch [::ce/execute-parallel (assoc action :data (concat [audio-action] animation-actions))]}
          {:dispatch [::ce/execute-parallel (assoc action :data animation-actions)]}))
      {:dispatch (ce/success-event action)})))

(re-frame/reg-event-fx
  ::execute-animate-next-text
  (fn [{:keys [db]} [_ {:keys [target] :as action}]]
    "Execute `text-animation` action - play audio file and text chunks animation simultaneously.

    Action params:
    :target - `text` component name. The component must contain `:chunks` property.
    :audio - audio file url.
    :start - start time in sec.
    :duration - audio interval duration in sec.
    :data - data defining the animation time for each chunk.

    Example:
    {:type      'animate-next-text',
     :target    'title-text',
     :animation 'color',
     :fill '#ff0000'
     }"
    (let [variable-name (tc/chunk-animated-variable target)
          last (vars.core/get-variable variable-name)
          last (if (nil? last) -1 last)
          animation-actions (i/text-animation-sequence->actions db (-> action
                                                                       (assoc :start 0)
                                                                       (assoc :duration 0.5)
                                                                       (assoc :data [{:at 0 :end 0.5 :duration 0.5 :chunk (inc last)}])))]
      (vars.core/set-variable! variable-name (inc last))
      {:dispatch [::ce/execute-parallel (assoc action :data animation-actions)]})))

(re-frame/reg-event-fx
  ::execute-reset-animate-text
  (fn [{:keys [db]} [_ {:keys [target] :as action}]]
    "Execute `execute-reset-animate-text` action - reset text animation status

    Action params:
    :target - `text` component name. The component must contain `:chunks` property.
    :fill  - color to revert

    Example:
    {:type      'animate-next-text',
     :target    'title-text',
     :animation 'color',
     :fill '#ff0000'
     }"
    (let [variable-name (tc/chunk-animated-variable target)]
      (vars.core/set-variable! variable-name nil)
      {:dispatch (ce/success-event action)})))

(re-frame/reg-event-fx
  ::reset-navigation
  (fn [{:keys [db]} _]
    (let [next-activity (next-activity-name db)
          current-scene (get-in db [:current-scene])
          scene-list (get-in db [:course-data :scene-list])
          exit (next-location db)
          activity-started? (:activity-started db)
          show-navigation? (and (not activity-started?) (not= next-activity current-scene))
          navigation-items (->> (get-in scene-list [(keyword current-scene) :outs])
                                (map :object)
                                (remove nil?)
                                (map (fn [target] {:target (keyword target)
                                                   :active (and show-navigation?
                                                                (= target (:object exit)))})))]
      {:dispatch-n (map (fn [{:keys [target active]}]
                          [::scene/change-scene-object target [[:set-filter {:filter "pulsation"
                                                                             :remove (not active)}]]])
                        navigation-items)})))

(re-frame/reg-event-fx
  ::disable-navigation
  (fn [{:keys [db]} _]
    (let [current-scene (get-in db [:current-scene])
          scene-list (get-in db [:course-data :scene-list])
          navigation-items (->> (get-in scene-list [(keyword current-scene) :outs])
                                (map :object)
                                (remove nil?))]
      {:dispatch-n (map (fn [target]
                          [::scene/change-scene-object (keyword target) [[:set-filter {:filter "pulsation"
                                                                                       :remove true}]]])
                        navigation-items)})))

(re-frame/reg-event-fx
  ::progress-loaded
  (fn [{:keys [db]} [_ course-id scene-id]]
    (let [progress (:progress-data db)]
      {:dispatch-n (list [::load-settings]
                         [::set-current-scene (or scene-id (get-in progress [:next :activity-name]))])})))

(re-frame/reg-event-fx
  ::add-pending-event
  (fn [{:keys [db]} [_ type data]]
    (let [event (-> (or data {})
                    (assoc :id (-> (random-uuid) str))
                    (assoc :type type)
                    (assoc :created-at (-> (js/Date.) .toISOString)))]
      {:db       (update-in db [:pending-events] conj event)
       :dispatch [:progress-data-changed]})))

(re-frame/reg-event-fx
  ::start-playing
  (fn [{:keys [db]} _]
    {:db       (-> db
                   (assoc :playing true)
                   (assoc :scene-started true))
     :dispatch [::add-pending-event :course-started]}))

(re-frame/reg-event-fx
  ::stop-playing
  (fn [{:keys [db]} _]
    {:db (assoc db :playing false)}))

(re-frame/reg-event-fx
  ::execute-pick-correct
  (fn [{:keys [db]} [_ {:keys [concept-name] :as action}]]
    "Execute `pick-correct` action - send to back that user has chosen the correct concept.

    Action params:
    :concept-name - concept name.

    Example:
    {:type         'pick-correct',
     :concept-name 'ardilla'}"
    (let [counter-value (or (vars.core/get-variable :score-correct) 0)]
      (vars.core/set-variable! :score-correct (inc counter-value))
      (vars.core/set-variable! :score-first-attempt false)
      {:dispatch-n (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-pick-wrong
  (fn [{:keys [db]} [_ {:keys [concept-name option] :as action}]]
    "Execute `pick-wrong` action - send to back that user has chosen the wrong concept.

    Action params:
    :concept-name - concept name.
    :option - additional data.

    Example:
    {:type         'pick-wrong',
     :concept-name 'ardilla'
     :option       'oso'}"
    (let [counter-incorrect (or (vars.core/get-variable :score-incorrect) 0)
          counter-mistake (or (vars.core/get-variable :score-mistake) 0)
          first-attempt? (vars.core/get-variable :score-first-attempt)]
      (when first-attempt?
        (vars.core/set-variable! :score-incorrect (inc counter-incorrect)))
      (vars.core/set-variable! :score-mistake (inc counter-mistake))
      (vars.core/set-variable! :score-first-attempt false)
      {:dispatch-n (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-set-current-concept
  (fn [{:keys [db]} [_ {:keys [value] :as action}]]
    "Execute `set-current-concept` action - set current concept.

    Action params:
    :value - concept name.

    Example:
    {:type  'set-current-concept',
     :value 'ardilla'}"
    (vars.core/set-variable! :score-first-attempt true)
    {:dispatch-n (list (ce/success-event action))}))


(def timeout-counter (atom {}))

(re-frame/reg-event-fx
  ::execute-next-timeout-counter
  (fn [{:keys [db]} [_ {:keys [id] :as main-action}]]
    (reset! timeout-counter (-> @timeout-counter
                                (assoc-in [id :last] (.now js/Date))
                                (update-in [id :counter] inc)))
    {:dispatch-n (list
                   (ce/success-event main-action))}))

(re-frame/reg-event-fx
  ::execute-set-timeout
  (fn [{:keys [db]} [_ {:keys [action interval] :as main-action}]]
    (.setTimeout js/window
                 (fn []
                   (let [scene-action (-> (ce/get-action action db)
                                          (assoc :params (:params main-action)))]
                     (re-frame/dispatch [::ce/execute-action scene-action]))
                   ) interval)
    {:dispatch-n (list (ce/success-event main-action))}))

(re-frame/reg-event-fx
  ::execute-start-timeout-counter
  (fn [{:keys [db]} [_ {:keys [id interval action autostart] :as main-action}]]
    (reset! timeout-counter (-> @timeout-counter
                                (assoc-in [id :last] (if autostart 0 nil))
                                (assoc-in [id :counter] 0)))
    (ce/remove-timer id)
    (let [interval-id (.setInterval
                        js/window
                        (fn []
                          (let [last (get-in @timeout-counter [id :last])
                                counter (get-in @timeout-counter [id :counter])
                                scene-action (-> (ce/get-action action db)
                                                 (assoc :params (:params main-action))
                                                 (assoc-in [:params :counter] counter))]
                            (if (and (< interval (- (.now js/Date) last)) (not (nil? last)))
                              (do
                                (ce/remove-timer id)
                                (vars.core/set-variable! (str id "-value") counter)
                                (re-frame/dispatch [::ce/execute-action scene-action])))))
                        interval)]
      {:dispatch-n (list [::ce/execute-register-timer {:name id
                                                       :id   interval-id
                                                       :type "interval"}]
                         (ce/success-event main-action))})))

(re-frame/reg-event-fx
  ::execute-set-interval
  (fn [{:keys [db]} [_ {:keys [id interval action] :as main-action}]]
    "Execute `set-interval` action - set a periodically repeating action.

    Action params:
    :id - id of interval.
    :action - action name to execute.
    :interval - interval in ms.

    Example:
    {:type     'set-interval'
     :id       'reminder'
     :interval 17000
     :action   'show-click-reminder'}"
    (ce/remove-timer id)
    (when (:activity-started db)
      (let [scene-action (-> (ce/get-action action db)
                             (assoc :params (:params main-action)))
            interval-id (.setInterval js/window (fn [] (re-frame/dispatch [::ce/execute-action scene-action])) interval)]
        {:dispatch-n (list [::ce/execute-register-timer {:name id
                                                         :id   interval-id
                                                         :type "interval"}]
                           (ce/success-event main-action))}))))

(re-frame/reg-event-fx
  ::execute-remove-interval
  (fn [{:keys [db]} [_ {:keys [id] :as action}]]
    "Execute `remove-interval` action - reset a periodically repeating action.

    Action params:
    :id - id of interval.

    Example:
    {:type 'remove-interval'
     :id   'reminder'}"
    {:dispatch-n (list [::ce/execute-remove-timer {:name id}]
                       (ce/success-event action))}))

(re-frame/reg-event-fx
  ::execute-set-traffic-light
  (fn [{:keys [_]} [_ {:keys [target value] :as action}]]
    "Execute `` action - .

    Action params:
    : - .

    Example:
    "
    {:dispatch-n (list [::scene/set-traffic-light (keyword target) value]
                       (ce/success-event action))}))

(re-frame/reg-event-fx
  ::close-settings
  (fn [_ _]
    {:dispatch [::overlays/hide-settings]}))

(re-frame/reg-event-fx
  ::save-settings
  (fn [{:keys [db]} _]
    (let [settings (:settings db)]
      {:db       (assoc-in db [:progress-data :settings] settings)
       :dispatch [:progress-data-changed]})))

(re-frame/reg-event-fx
  ::load-settings
  (fn [{:keys [db]} _]
    (let [{:keys [music-volume effects-volume] :as settings} (get-in db [:progress-data :settings])]
      {:db         (update db :settings merge settings)
       :dispatch-n (list [::set-music-volume music-volume]
                         [::set-effects-volume effects-volume])})))

(re-frame/reg-event-fx
  ::start-sandbox
  (fn [{:keys [db]} [_ course-id scene-id encoded-lessons]]
    (let [lessons (some-> encoded-lessons js/decodeURIComponent js/atob js/JSON.parse (js->clj :keywordize-keys true))]
      (logger/trace "sandbox started with" lessons)
      {:db         (cond-> db
                           (seq lessons) (assoc-in [:sandbox :loaded-lessons] lessons))
       :dispatch-n (list [::load-course course-id scene-id])})))

(re-frame/reg-event-fx
  ::history-back
  (fn [{:keys [_]} [_]]
    {:history-back true}))

(re-frame/reg-fx
  :history-back
  (fn [_]
    (.back (.-history js/window))))

(re-frame/reg-event-fx
  ::execute-char-movement
  (fn [{:keys [db]} [_ {:keys [action target transition-id] :as action-data}]]
    (let [character (scene/get-scene-object db (keyword transition-id))
          target (scene/get-scene-object db (keyword target))
          handle-action-finish #(ce/dispatch-success-fn action-data)]
      (movements/move action character target handle-action-finish)
      {})))

(comment
  (let [db @re-frame.db/app-db
        next-activity "letter-intro-1"
        current-scene "i-spy-1"
        scene-list (interpreter-subs/navigation-scene-list db)]
    (->> (find-path current-scene next-activity scene-list)
         (drop 1)
         (take-last 3)
         (first))))
