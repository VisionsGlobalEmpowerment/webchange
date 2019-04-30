(ns webchange.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [webchange.core-test]
              [webchange.interpreter.events-test]
              [webchange.editor.form-elements.integer-test]
              [webchange.editor.form-elements.number-test]))

(doo-tests 'webchange.core-test
           'webchange.interpreter.events-test
           'webchange.editor.form-elements.integer-test
           'webchange.editor.form-elements.number-test)
