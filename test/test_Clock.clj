(ns test-Clock
  (:require
    [Clock :refer [Init Tick]]
    [clojure.test :refer [deftest is run-tests use-fixtures]]
    [salt]))

(use-fixtures :each (salt/namespace-fixture 'test-Clock))

(salt/evaluate {}
               '{clock 0}
               (Init))

;; => true

(salt/evaluate {}
               '{clock 1}
               (Init))
;; => true

(salt/evaluate {}
               '{clock 2}
               (Init))
;; => false

(salt/evaluate {}
               '{clock 0
                 clock' 1}
               (Tick))
;; => true

(salt/evaluate {}
               '{clock 0
                 clock' 0}
               (Tick))

;; => false

(salt/simplify "src/Clock.clj"
               {}
               '{clock 0}
               (Tick))
;; => (= clock' 1)

(salt/simplify "src/Clock.clj"
               {}
               '{clock' 0}
               (Tick))
;; => (= clock 1)

(salt/simulate "src/Clock.clj"
               {}
               '{clock 0}
               100
               (Tick))
;; => #{{clock' 1}}


(salt/transpile "src/Clock.clj")
;; =>
"---------------------------- MODULE Clock ----------------------------
VARIABLE clock

Init == clock \\in { 0, 1 }

Tick == 
    IF  (clock = 0)
    THEN (clock' = 1)
    ELSE (clock' = 0)

Spec == 
    /\\  Init
    /\\  [][Tick]_<< clock >>


=============================================================================
"

;;;;

(deftest test-tick
  (is (salt/evaluate {}
                     '{clock 0
                       clock' 1}
                     (Tick)))

  (is (not (salt/evaluate {}
                          '{clock 0
                            clock' 0}
                          (Tick)))))

(deftest test-init

  (is (salt/evaluate {}
                     '{clock 0}
                     (Init)))

  (is (salt/evaluate {}
                     '{clock 1}
                     (Init)))

  (is (not (salt/evaluate {}
                          '{clock 2}
                          (Init)))))

(comment
  (run-tests))
