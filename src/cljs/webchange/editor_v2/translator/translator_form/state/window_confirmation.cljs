(ns webchange.editor-v2.translator.translator-form.state.window-confirmation
  (:require
    [re-frame.core :as re-frame]))

(def unsaved-changes-message "You have unsaved changes. Are you sure you want to leave the page?")

(re-frame/reg-fx
  :set-before-leave
  (fn [text]
    (let [event-handler (fn [event]
                          (aset event "returnValue" text)
                          text)]
      (aset js/window "onbeforeunload" event-handler))))

(re-frame/reg-fx
  :reset-before-leave
  (fn []
    (aset js/window "onbeforeunload" nil)))
