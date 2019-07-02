(ns webchange.wrappers.cache)

(defn add-all
  "The addAll() method of the Cache interface takes an array of URLs,
  retrieves them, and adds the resulting response objects to the given cache.
  The request objects created during retrieval become keys to the stored response operations."
  [& {:keys [cache requests then]}]
  (-> (.addAll cache requests)
      (.then then)))

(defn delete
  "The delete() method of the Cache interface finds the Cache entry whose key is the request,
  and if found, deletes the Cache entry and returns a Promise that resolves to true.
  If no Cache entry is found, it resolves to false."
  [& {:keys [cache-name then]}]
  (-> (.delete js/caches cache-name)
      (.then then)))

(defn keys
  "The keys() method of the Cache interface returns a Promise that resolves to an array of Cache keys."
  [& {:keys [then]}]
  (-> (.keys js/caches)
      (.then then)))

(defn match
  "The match() method of the Cache interface returns a Promise
  that resolves to the Response associated with the first matching request in the Cache object.
  If no match is found, the Promise resolves to undefined."
  [& {:keys [cache request then]}]
  (-> (.match cache request)
      (.then then)))

(defn open
  "The open() method of the CacheStorage interface returns a Promise that resolves to the Cache object matching the cacheName."
  [& {:keys [cache-name then]}]
  (-> (.open js/caches cache-name)
      (.then then)))

(defn put
  "The put() method of the Cache interface allows key/value pairs to be added to the current Cache object."
  [& {:keys [cache request response then]}]
  (-> (.put cache request (.clone response))
      (.then then)))
