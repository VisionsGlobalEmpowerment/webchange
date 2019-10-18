(ns webchange.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            ;[webchange.core-test]
            ;[webchange.editor.common.action-form.action-form-test]
            ;[webchange.editor.common.action-form.animation-sequence-test]
            ;[webchange.editor.form-elements.controlled-input-test]
            ;[webchange.editor.form-elements.integer-test]
            ;[webchange.editor.form-elements.number-test]
            ;[webchange.editor-v2.diagram.scene-data-parser.parser-test]
            [webchange.editor-v2.diagram.scene-data-parser.actions-parser-test]
            ;[webchange.interpreter.events-test]
            ;[webchange.interpreter.variables.events-test]
            ;[webchange.interpreter.utils-test]
            ;[webchange.common.svg-path.path-to-transitions-test]
            ))

(doo-tests
            ;'webchange.core-test
           ;'webchange.editor.common.action-form.action-form-test
           ;'webchange.editor.common.action-form.animation-sequence-test
           ;'webchange.editor.form-elements.controlled-input-test
           ;'webchange.editor.form-elements.integer-test
           ;'webchange.editor.form-elements.number-test
           ;'webchange.editor-v2.diagram.scene-data-parser.parser-test
           'webchange.editor-v2.diagram.scene-data-parser.actions-parser-test
           ;'webchange.interpreter.events-test
           ;'webchange.interpreter.variables.events-test
           ;'webchange.interpreter.utils-test
           ;'webchange.common.svg-path.path-to-transitions-test
           )
