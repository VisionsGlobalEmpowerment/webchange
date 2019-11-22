(ns webchange.dataset.handler
  (:require [compojure.core :refer [GET POST PUT PATCH DELETE defroutes routes]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [resource-response response redirect]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.tools.logging :as log]
            [webchange.common.handler :refer [handle current-user]]
            [webchange.auth.core :as auth]
            [webchange.dataset.core :as core]))

(defn handle-create-dataset
  [request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (core/create-dataset! data)
        handle)))

(defn handle-update-dataset
  [dataset-id request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (core/update-dataset! (Integer/parseInt dataset-id) data)
        handle)))

(defn handle-create-dataset-item
  [request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (core/create-dataset-item! data)
        handle)))

(defn handle-update-dataset-item
  [id request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (core/update-dataset-item! (Integer/parseInt id) data)
        handle)))

(defn handle-patch-dataset-item
  [id request]
  (let [owner-id (current-user request)
        id (Integer/parseInt id)
        item (core/get-item id)]
    (if-not (nil? item)
      (let [field-name (-> request :body :name keyword)
            field-patch (-> request :body :data)
            field-origin-data (get-in item [:data field-name])]
        (-> (core/update-dataset-item! id (assoc-in item [:data field-name] (merge field-origin-data field-patch)))
            handle))
      (not-found "not found"))))

(defn handle-delete-dataset-item
  [id request]
  (let [owner-id (current-user request)]
    (-> (core/delete-dataset-item! (Integer/parseInt id))
        handle)))

(defn handle-create-lesson-set
  [request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (core/create-lesson-set! data)
        handle)))

(defn handle-update-lesson-set
  [id request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (core/update-lesson-set! (Integer/parseInt id) data)
        handle)))

(defn handle-delete-lesson-set
  [id request]
  (let [owner-id (current-user request)]
    (-> (core/delete-lesson-set! (Integer/parseInt id))
        handle)))

(defroutes dataset-routes
           (GET "/api/datasets/:id" [id] (-> id Integer/parseInt core/get-dataset response))
           (GET "/api/courses/:course-id/datasets" [course-id] (-> course-id core/get-course-datasets response))
           (POST "/api/datasets" request
             (handle-create-dataset request))
           (PUT "/api/datasets/:id" [id :as request]
             (handle-update-dataset id request))

           (GET "/api/datasets/:id/items" [id] (-> id Integer/parseInt core/get-dataset-items response))
           (GET "/api/dataset-items/:id" [id]
             (if-let [item (-> id Integer/parseInt core/get-item)]
               (response {:item item})
               (not-found "not found")))
           (POST "/api/dataset-items" request
             (handle-create-dataset-item request))
           (PUT "/api/dataset-items/:id" [id :as request]
             (handle-update-dataset-item id request))
           (PATCH "/api/dataset-items/:id" [id :as request]
             (handle-patch-dataset-item id request))
           (DELETE "/api/dataset-items/:id" [id :as request]
             (handle-delete-dataset-item id request))

           (GET "/api/datasets/:id/lesson-sets" [id] (-> id Integer/parseInt core/get-dataset-lessons response))
           (GET "/api/datasets/:dataset-id/lesson-sets/:name" [dataset-id name]
             (if-let [item (core/get-lesson-set-by-name (Integer/parseInt dataset-id) name)]
               (response {:lesson-set item})
               (not-found "not found")))
           (POST "/api/lesson-sets" request
             (handle-create-lesson-set request))
           (PUT "/api/lesson-sets/:id" [id :as request]
             (handle-update-lesson-set id request))
           (DELETE "/api/lesson-sets/:id" [id :as request]
             (handle-delete-lesson-set id request))

           (GET "/api/courses/:course-id/lesson-sets" [course-id] (-> course-id core/get-course-lessons response)))