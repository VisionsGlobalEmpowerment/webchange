(ns webchange.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [webchange.core-test]
              [webchange.editor.common.action-form.action-form-test]
              [webchange.editor.common.action-form.animation-sequence-test]
              [webchange.editor.form-elements.controlled-input-test]
              [webchange.editor.form-elements.integer-test]
              [webchange.editor.form-elements.number-test]
              [webchange.interpreter.events-test]))

(doo-tests
  ;'webchange.core-test
  'webchange.editor.common.action-form.action-form-test
  ;'webchange.editor.common.action-form.animation-sequence-test
  ;'webchange.editor.form-elements.controlled-input-test
  ;'webchange.editor.form-elements.integer-test
  ;'webchange.editor.form-elements.number-test
  ;'webchange.interpreter.events-test
  )
